package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

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
     * Public constructor
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
        File inputFile = new File(filePath);
        try {
            String fileExtension = filePath.substring(filePath.length() - 3, filePath.length());
            if (!fileExtension.equals("csv")) {
                throw new IllegalArgumentException();
            }
        	Scanner sc = new Scanner(inputFile);
        	foodItemList.clear();
        	while (sc.hasNextLine()) {
        		int listIndex = 0;
        		String[] rawDataSet = sc.nextLine().split(",");
        		if (rawDataSet.length == 0) break;
        		FoodItem food = new FoodItem(rawDataSet[0], rawDataSet[1]);
        		int i = 2;
        		//add nutrition data
        		while (i <= 11) {
        			food.addNutrient(rawDataSet[i++], Double.parseDouble(rawDataSet[i++]));
        		}
        		//add the new FoodItem to foodItemList with the assigned index
        		insertionSort(food);
        	}
        	globalUpdateIndex();
        	addAllToIndexes();
        } catch (IllegalArgumentException e) {
            System.err.println("Input file is not a csv file.");
        } catch(FileNotFoundException e) {
        	System.err.println("Input file not found.");
        }
    }
    
    private void insertionSort(FoodItem newFood) {
    	if (foodItemList.size() == 0) 
    		foodItemList.add(newFood);
    	else {
    		int listIndex = 0;
	    	for (FoodItem food: foodItemList) {
	    		if (newFood.getName().compareTo(food.getName()) <= 0) {
	    			break;
	    		} else {
	    			listIndex++;
	    		}
	    	}
	    	foodItemList.add(listIndex, newFood);
	    	return;
    	}
    }
    
    private void addAllToIndexes() {
    	String[] nutritions= {"calories", "carbohydrate", "fat", "fiber", "protein"};
    	for (String nutrition: nutritions) {
    		for (FoodItem food: foodItemList) {
    			this.indexes.get(nutrition).insert(food.getNutrientValue(nutrition), food.getIndex());
        	}
    	}
    }
    
    private void globalUpdateIndex() {
    	int listIndex = 0;
    	for (FoodItem food: foodItemList) {
    		food.setIndex(listIndex++);
    	}
    }
    
    public List<FoodItem> globalFilter(String substring, List<String> rules) {
        if ((substring == null || substring.equals("")) && (rules == null || rules.size() == 0))
            return this.foodItemList;
    	if (substring == null || substring.equals("")) 
    		return filterByNutrients(rules);
    	else if (rules == null || rules.size() == 0)
    		return filterByName(substring);
    	else {
    		List<FoodItem> list1 = filterByNutrients(rules);
    		List<FoodItem> list2 = filterByName(substring);
    		Iterator<FoodItem> it = list1.iterator();
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
        	List<FoodItem> matches = new ArrayList<FoodItem>();
        	for (FoodItem food: foodItemList) {
        		if (food.getName().contains(substring))
        			matches.add(food);
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
    	//decoding rules
    	int numOfRules = rules.size();
    	String[] nuts = new String[numOfRules];
    	String[][] rulesArr = new String[numOfRules][3];
    	int i = 0;
    	for (String rule: rules) {
    		String[] temp = rules.get(i).split(" ");
    		rulesArr[i] = new String[] {temp[0], temp[1], temp[2]};
    		i++;
    	}
    	for (int j = 0; j < numOfRules; j++) {
    		nuts[j] = rulesArr[j][0];
    	} 
    	
    	ArrayList<List<Integer>> sets = new ArrayList<List<Integer>>();
    	i = 0;
    	for (String[] singleRuleArr: rulesArr) {
    		sets.add(singleNutritionFilter(singleRuleArr[0], singleRuleArr[1], singleRuleArr[2]));
    	}
    	
    	HashSet<Integer> intersectionSet = new HashSet<>(sets.get(0));
    	for (int k = 1; k < sets.size(); k++) {
    		HashSet<Integer> set = new HashSet<>(sets.get(k));
    		intersectionSet.retainAll(set);
    	}
    	List<Integer> sortedIndexes = new ArrayList<Integer>();
    	for (Integer link: intersectionSet) {
    		sortedIndexes.add(link);
    	}
    	
    	List<FoodItem> matches = new ArrayList<FoodItem>();
    	Collections.sort(sortedIndexes);
    	for (Integer link: sortedIndexes) {
    		matches.add(this.foodItemList.get(link));
    	}
    	return matches;
    }
    
    private List<Integer> singleNutritionFilter(String nutrition, String comparator, String value) {
    	return this.indexes.get(nutrition).rangeSearch(Double.parseDouble(value), comparator);
    }

    /**
     * Adds a food item to the loaded data.
     * @param foodItem the food item instance to be added
     */
    @Override
    public void addFoodItem(FoodItem foodItem) {
    	//TODO: duplicate id's?
    	//TODO: is there a need for randomly generating id
    	foodItem.setIndex(this.foodItemList.size());
    	insertionSort(foodItem);
        globalUpdateIndex();
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
		File file = new File(filename);
		if (file.exists())
			System.err.println("File already exists. Please change the file name.");
		else {
			try {
				PrintWriter output = new PrintWriter(file);
				for (FoodItem food: foodItemList) {
					output.println(food.mutatedToString());
				}
				output.close();
			} catch (FileNotFoundException e) {
				// TODO why this exception?
				e.printStackTrace();
			}
		}
		
	}
	
	protected boolean isEmpty() { return this.foodItemList.isEmpty();}
	
	///////////////////FOR TESTING PURPOSES//////////////////////
	public static void main(String[] args) {
		FoodData fd = new FoodData();
		fd.loadFoodItems("foodItems.csv");
		System.out.println("");
		List<String> rules = new ArrayList<String>();
		//rules.add("calories >= 400.0");
		rules.add("fat >= 19.0");
		rules.add("fat <= 19.0");
		//System.out.println(fd.filterByNutrients(rules).toString());
		System.out.println(fd.globalFilter("DingDongs", rules));
	}
}
