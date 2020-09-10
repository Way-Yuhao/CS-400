/**
 * Filename:   FoodItem.java
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

import javafx.scene.control.CheckBox;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents a food item with all its properties.
 *
 * @author Ruijian Huang
 */
public class FoodItem {
    // The name of the food item.
    private String name;

    // The id of the food item.
    private String id;

    // Map of nutrients and value.
    private HashMap<String, Double> nutrients;

    // location in foodItemList, for later relocate item
    private Integer index;

    private CheckBox foodBox;
    private CheckBox mealBox;

    /**
     * Constructor
     *
     * @param name name of the food item
     * @param id unique id of the food item
     */
    public FoodItem(String id, String name) {
        this.id = id;
        this.name = name;
        this.index = null;
        this.nutrients = new HashMap<String, Double>();
        this.foodBox = new CheckBox();
        this.mealBox = new CheckBox();
    }

    /**
     * Constructor for making an copy
     *
     * @param name name of the food item
     * @param id unique id of the food item
     */
    protected FoodItem(String id, HashMap<String, Double> nutrients, String name, int index) {
        this.id = id;
        this.name = name;
        this.index = index;
        this.nutrients = nutrients;
        this.foodBox = new CheckBox();
        this.mealBox = new CheckBox();
        // checkBox.
    }


    /**
     * Gets the name of the food item
     *
     * @return name of the food item
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the unique id of the food item
     *
     * @return id of the food item
     */
    public String getID() {
        return id;
    }

    /**
     * Gets the nutrients of the food item
     *
     * @return nutrients of the food item
     */
    public HashMap<String, Double> getNutrients() {
        return nutrients;
    }

    /**
     * Adds a nutrient and its value to this food. If nutrient already exists, updates its value.
     */
    public void addNutrient(String name, Double value) {
        nutrients.put(name, value);
    }

    /**
     * Returns the value of the given nutrient for this food item. If not present, then returns 0.
     */
    public double getNutrientValue(String name) {
        return nutrients.get(name);
    }

    /**
     * Set index according to location in foodItemList passed in
     *
     * @param index index in foodItemList ArrayList
     */
    protected void setIndex(int index) {
        this.index = index;
    }

    /**
     * return index for relocating foodItem
     *
     * @return index of the foodItem
     */
    protected Integer getIndex() {
        return index;
    }

    /**
     * This method gets all information from this class' fields and rearrange them into a single
     * String containing all nutrients information.     * 
     * @return information of all nutrients
     */
    protected String mutatedToString() {
        return id + "," + name + "," + "calories" + "," + nutrients.get("calories").toString() + ","
            + "fat" + "," + nutrients.get("fat").toString() + "," + "carbohydrate" + ","
            + nutrients.get("carbohydrate").toString() + "," + "fiber" + ","
            + nutrients.get("fiber").toString() + "," + "protein" + ","
            + nutrients.get("protein").toString();
    }

    /**
     * This method returns the checkBox reference for selecting food in foodList
     * @return CheckBox
     */
    protected CheckBox getCheckBox() {
        return foodBox;
    }
    
    /**
     * This method returns the boolean value of if the checkBox for item in foodList is selected
     * @return true if selected
     */
    protected boolean getCheckBoxValue() {
        return foodBox.isSelected();
    }
    /**
     * This method returns the checkBox reference for selecting food in mealList
     * @return CheckBox
     */
    protected CheckBox getMealCheckBox() {
        return mealBox;
    }
    /**
     * This method returns the boolean value of if the checkBox for item in mealList is selected
     * @return true if selected
     */
    protected boolean getMealCheckBoxValue() {
        return mealBox.isSelected();
    }
}
