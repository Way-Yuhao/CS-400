/**
 * Filename:   FoodData.java
 * Project:    P5 Meal Planner
 * Version:    1.0
 * User:       unkown
 * Date:       Dec 12, 2018
 * Authors:    Debra Deppeler
 *
 * Semester:   Fall 2018
 * Course:     CS400
 * Instructor: Deppeler (deppeler@cs.wisc.edu)
 * Credits:    Junheng Wang, Ruijian Huang, Huifeng Su, Yuhao Liu, Jiasheng Zhang
 * Bugs:       no known bugs
 *
 */

package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * This class represents the backend for managing all 
 * the operations associated with FoodItems
 * 
 * @author sapan (sapan@cs.wisc.edu)
 */
public class FoodData implements FoodDataADT<FoodItem> {
    // List of all the food items.
    private List<FoodItem> foodItemList;

    // Map of nutrients and their corresponding index
    private HashMap<String, BPTree<Double, Integer>> indexes;
    
    
    /**
     * Public constructor of the class. The method also instantiates a HashMap to store
     * nutrition data.
     */
    public FoodData() {
    	
        this.foodItemList = new LinkedList<FoodItem>();
        this.indexes = new HashMap<String, BPTree<Double, Integer>>();	
        this.indexes.put("calories", new BPTree<Double, Integer>(3));
        this.indexes.put("fat", new BPTree<Double, Integer>(3));
        this.indexes.put("carbohydrate", new BPTree<Double, Integer>(3));
        this.indexes.put("fiber", new BPTree<Double, Integer>(3));
        this.indexes.put("protein", new BPTree<Double, Integer>(3));
    }
    
    
    /**
     * Loads the data in the .csv file
     * 
     * file format:
     * <id1>,<name>,<nutrient1>,<value1>,<nutrient2>,<value2>,...
     * <id2>,<name>,<nutrient1>,<value1>,<nutrient2>,<value2>,...
     * 
     * Example:
     * 556540ff5d613c9d5f5935a9,Stewarts_PremiumDarkChocolatewithMintCookieCrunch,calories,280,fat,18,carbohydrate,34,fiber,3,protein,3
     * 
     * Note:
     *     1. All the rows are in valid format.
     *  2. All IDs are unique.
     *  3. Names can be duplicate.
     *  4. All columns are strictly alphanumeric (a-zA-Z0-9_).
     *  5. All food items will strictly contain 5 nutrients in the given order:    
     *     calories,fat,carbohydrate,fiber,protein
     *  6. Nutrients are CASE-INSENSITIVE. 
     * 
     * @param filePath path of the food item data file 
     *        (e.g. folder1/subfolder1/.../foodItems.csv) 
     */
    @Override
    public void loadFoodItems(String filePath) {
    	//create a File object for the file to be read
        File inputFile = new File(filePath);
        try {
            String fileExtension = filePath.substring(filePath.length() - 3, filePath.length());
            //if the file does type is not .csv
            if (!fileExtension.equals("csv")) {
            	//throw exception
                throw new IllegalArgumentException();
            }
            //scanner for to read from the file
        	Scanner sc = new Scanner(inputFile);
        	//clear food list before each read attempt
        	foodItemList.clear();
        	/* reading from the file */
        	while (sc.hasNextLine()) {
        		int listIndex = 0;
        		//split the data by comma for each line
        		String[] rawDataSet = sc.nextLine().split(",");
        		if (rawDataSet.length == 0) break; //skip empty lines
        		FoodItem food = new FoodItem(rawDataSet[0], rawDataSet[1]);
        		int i = 2;
        		//add nutrition data
        		while (i <= 11) {
        			food.addNutrient(rawDataSet[i++], Double.parseDouble(rawDataSet[i++]));
        		}
        		//add the new FoodItem to foodItemList with the assigned index
        		insertionSort(food); //insert the FoodItem per ascending order
        	}
        	/* after reading from the file */
        	globalUpdateIndex(); //update all indexes in the foodItemList
        	addAllToIndexes(); //add all FoodItems to indexes
        } catch (IllegalArgumentException e) {
            System.err.println("Input file is not a csv file.");
        } catch(FileNotFoundException e) {
        	System.err.println("Input file not found.");
        }
    }
    
    

    /**
     * Gets all the food items that have name containing the substring.
     * 
     * Example:
     *     All FoodItem
     *         51c38f5d97c3e6d3d972f08a,Similac_FormulaSoyforDiarrheaReadytoFeed,calories,100,fat,0,carbohydrate,0,fiber,0,protein,3
     *         556540ff5d613c9d5f5935a9,Stewarts_PremiumDarkChocolatewithMintCookieCrunch,calories,280,fat,18,carbohydrate,34,fiber,3,protein,3
     *     Substring: soy
     *     Filtered FoodItem
     *         51c38f5d97c3e6d3d972f08a,Similac_FormulaSoyforDiarrheaReadytoFeed,calories,100,fat,0,carbohydrate,0,fiber,0,protein,3
     * 
     * Note:
     *     1. Matching should be CASE-INSENSITIVE.
     *     2. The whole substring should be present in the name of FoodItem object.
     *     3. substring will be strictly alphanumeric (a-zA-Z0-9_)
     * 
     * @param substring substring to be searched
     * @return list of filtered food items; if no food item matched, return empty list
     */
    @Override
    public List<FoodItem> filterByName(String substring) {
        if (substring == null) {
        	return null;
        } else {
        	// list of FoodItems to be returned
        	List<FoodItem> matches = new ArrayList<FoodItem>(); 
        	for (FoodItem food: foodItemList) { //for each FoodItem
        		if (food.getName().contains(substring)) //if it contains the substring
        			matches.add(food); //add it to the list
        	}
        	return matches;
        }
    }

    /**
     * Gets all the food items that fulfill ALL the provided rules
     *
     * Format of a rule:
     *     "<nutrient> <comparator> <value>"
     * 
     * Definition of a rule:
     *     A rule is a string which has three parts separated by a space:
     *         1. <nutrient>: Name of one of the 5 nutrients [CASE-INSENSITIVE]
     *         2. <comparator>: One of the following comparison operators: <=, >=, ==
     *         3. <value>: a double value
     * 
     * Note:
     *     1. Multiple rules can contain the same nutrient.
     *         E.g. ["calories >= 50.0", "calories <= 200.0", "fiber == 2.5"]
     *     2. A FoodItemADT object MUST satisfy ALL the provided rules i
     *        to be returned in the filtered list.
     *
     * @param rules list of rules
     * @return list of filtered food items; if no food item matched, return empty list
     */
    @Override
    public List<FoodItem> filterByNutrients(List<String> rules) {
    	/* decoding rules */
    	int numOfRules = rules.size();
    	//stores the name of nutrients present in the list of rules
    	String[] nuts = new String[numOfRules]; 
    	//2-D array stores the decoded rule data
    	String[][] rulesArr = new String[numOfRules][3];
    	int i = 0; //loop index
    	for (String rule: rules) {
    		String[] temp = rules.get(i).split(" "); //split the data by spaces
    		//store the data in ruleArr, following the order nutrient, comparator, value
    		rulesArr[i] = new String[] {temp[0], temp[1], temp[2]};
    		i++;
    	}
    	for (int j = 0; j < numOfRules; j++) {
    		nuts[j] = rulesArr[j][0]; //copy nutrient name to the nut array
    	} 
    	
    	/* solving the intersection of set of indexes under each rule */
    	ArrayList<List<Integer>> sets = new ArrayList<List<Integer>>();
    	i = 0; //reset loop index
    	for (String[] singleRuleArr: rulesArr) {
    		sets.add(singleNutritionFilter(singleRuleArr[0], singleRuleArr[1], singleRuleArr[2]));
    	}
    	// a set of FoodItems that passes the first rule
    	HashSet<Integer> intersectionSet = new HashSet<>(sets.get(0)); 
    	for (int k = 1; k < sets.size(); k++) {
    		// for each addition set
    		HashSet<Integer> set = new HashSet<>(sets.get(k));
    		intersectionSet.retainAll(set); //only keep the intersection with the first set
    	}
    	
    	/* sorting the list in ascending order */
    	// add all elements in the intersection set to a list
    	List<Integer> sortedIndexes = new ArrayList<Integer>();
    	for (Integer link: intersectionSet) {
    		sortedIndexes.add(link);
    	}
    	// a list of matches to be returned
    	List<FoodItem> matches = new ArrayList<FoodItem>();
    	Collections.sort(sortedIndexes); //sort the list in ascending order
    	for (Integer link: sortedIndexes) { //get each index from each element
    		//add the corresponding FoodData to each index
    		matches.add(this.foodItemList.get(link));
    	}
    	return matches;
    }
    

    /**
     * Adds a food item to the loaded data.
     * @param foodItem the food item instance to be added
     */
    @Override
    public void addFoodItem(FoodItem foodItem) {
    	insertionSort(foodItem); //insert the foodItem at the correct position
        globalUpdateIndex(); //update all indexes
        String[] nutrients = {"calories", "fat", "carbohydrate", "fiber", "protein"};
        for (String nutrient: nutrients) {
    		this.indexes.get(nutrient).insert(foodItem.getNutrientValue(nutrient), foodItem.getIndex());
        }
    }

    /**
     * Gets the list of all food items.
     * @return list of FoodItem
     */
    @Override
    public List<FoodItem> getAllFoodItems() {
        return this.foodItemList;
    }

    /**
     * Save the list of food items in ascending order by name
     * 
     * @param filename name of the file where the data needs to be saved 
     */
	@Override
	public void saveFoodItems(String filename) {
		File file = new File(filename); // create a new File object for the file to be written
		if (file.exists()) //ensure that the file hasn't already been created
			System.err.println("File already exists. Please change the file name.");
		else {
			try {
				PrintWriter output = new PrintWriter(file);
				// print out each FoodData as a new line
				for (FoodItem food: foodItemList) {
					output.println(food.mutatedToString());
				}
				output.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Check if the foodItemList is empty
	 * @return true if the foodItemList is empty
	 */
	protected boolean isEmpty() { return this.foodItemList.isEmpty();}
	
	/**
	 * Helper method that applies both the name and nutrient filter in a single method.
	 * If either filter is not applied, set the corresponding parameter as null
	 * @param substring
	 * @param rules
	 * @return the list of FoodItems satisfying all filters
	 */
    protected List<FoodItem> globalFilter(String substring, List<String> rules) {
    	// case neither filter is applied
        if ((substring == null || substring.equals("")) && (rules == null || rules.size() == 0))
            return this.foodItemList; //return the full list
        // case that the only the nutrient filter is applied
    	if (substring == null || substring.equals("")) 
    		return filterByNutrients(rules);
    	//case that only the name filter is applied
    	else if (rules == null || rules.size() == 0)
    		return filterByName(substring);
    	else { //when both filters are applied
    		List<FoodItem> list1 = filterByNutrients(rules); //list satisfying nutrient filter
    		List<FoodItem> list2 = filterByName(substring); //list satisfying name filter
    		Iterator<FoodItem> it = list1.iterator();
    		//return the intersection of the two sets
    		while (it.hasNext()) {
    			FoodItem food = it.next();
    			if (!list2.contains(food)) {
    				it.remove();
    			}
    		}
    		return list1;
    	}	
    }
	
    /**
     * Helper method that iterates through the list from the head until encounters a FoodItem
     * whose name is larger than the name of the newFood, then insert the newFood at that position.
     * @param newFood
     */
	private void insertionSort(FoodItem newFood) {
    	if (foodItemList.size() == 0) 
    		foodItemList.add(newFood);
    	else {
    		int listIndex = 0; //list index
    		//iterate through the list
	    	for (FoodItem food: foodItemList) {
	    		//stop when found a FoodItem whose name is larger
	    		if (newFood.getName().compareTo(food.getName()) <= 0) {
	    			break;
	    		} else {
	    			listIndex++;
	    		}
	    	}
	    	//add the new food at such position
	    	foodItemList.add(listIndex, newFood);
	    	return;
    	}
    }
    
	/**
	 * Add each foodItem to all BPTrees in the HashMap.
	 */
    private void addAllToIndexes() {
    	String[] nutritions= {"calories", "carbohydrate", "fat", "fiber", "protein"};
    	for (String nutrition: nutritions) {
    		for (FoodItem food: foodItemList) {
    			this.indexes.get(nutrition).insert(food.getNutrientValue(nutrition), food.getIndex());
        	}
    	}
    }
    
    /**
     * Updates the indexes of all FoodItems in the foodItemList.
     */
    private void globalUpdateIndex() {
    	int listIndex = 0;
    	for (FoodItem food: foodItemList) {
    		food.setIndex(listIndex++);
    	}
    }
    
    /**
     * Return the list of values whose keys are within a certain range in a certain BPTree of the indexes HashMap.
     * @param nutrition
     * @param comparator
     * @param value
     * @return a list of values satisfying the filter
     */
    private List<Integer> singleNutritionFilter(String nutrition, String comparator, String value) {
    	//call the functionality of rangeSearch of the BPTree
    	return this.indexes.get(nutrition).rangeSearch(Double.parseDouble(value), comparator);
    }
}
