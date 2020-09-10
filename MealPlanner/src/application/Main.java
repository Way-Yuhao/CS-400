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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * This is a GUI class for the purpose of establishing a bridge between users and the final program.
 * It contains all functions required to utilize the function, with information of available foods,
 * current meal selected, and current query applied given to users.
 *
 * @author Ruijian Huang, Yuhao Liu, Huifeng Su, Junheng Wang, Leon Zhang
 */
public class Main extends Application {
    public int width = 1700; // Scene's initial width
    public int height = 800; // Scene's initial height

    /**
     * This method contains all the settings of GUI's layout, with only one primaryStage passed
     * into.
     *
     * @param primaryStage
     */

    FoodData foodData = new FoodData();     // Instance of FoodData class
    MealList mealList = new MealList();     // Instance of MealList class

    // Following labels give user an idea where to enter
    // different kinds of information for the food
    Label labAddFoodId = new Label();

    Label labAddFoodName = new Label();
    Label labAddFoodCal = new Label();
    Label labAddFoodFat = new Label();
    Label labAddFoodCarb = new Label();
    Label labAddFoodFib = new Label();
    Label labAddFoodPro = new Label();

    // Following TextField variables are the place in the UI where users enter
    // different kind of nutrition info
    TextField txtAddFoodId = new TextField();
    TextField txtAddFoodName = new TextField();
    TextField txtAddFoodCal = new TextField();
    TextField txtAddFoodFat = new TextField();
    TextField txtAddFoodCarb = new TextField();
    TextField txtAddFoodFib = new TextField();
    TextField txtAddFoodPro = new TextField();

    // Following Strings hold the ACTUAL INPUT from the user
    String addFoodProteinHolder = "";
    String addFoodFiberHolder = "";
    String addFoodCarboHydrateHolder = "";
    String addFoodCaloriesHolder = "";
    String addFoodFatHolder = "";
    String addFoodNameHolder = "";
    String addFoodIdHolder = "";

    // Following Objects stores the input from the user that has been FORMATTED
    Double addFoodCal;
    Double addFoodFat;
    Double addFoodCarb;
    Double addFoodFib;
    Double addFoodPro;
    String addFoodName;
    String addFoodId;

    // Used by query dialog to temporarily contain the string in the TextFields
    String calLowerLimHolder = "";  //saved previous info
    String calUpperLimHolder = "";  //saved previous info
    String fatLowerLimHolder = "";  //saved previous info
    String fatUpperLimHolder = "";  //saved previous info
    String carboLowerLimHolder = "";  //saved previous info
    String carboUpperLimHolder = "";  //saved previous info
    String fiberLowerLimHolder = "";  //saved previous info
    String fiberUpperLimHolder = "";  //saved previous info
    String proteinLowerLimHolder = "";  //saved previous info
    String proteinUpperLimHolder = "";  //saved previous info

    // Used by query to temporarily contain the parsed double value from query "holders" above
    Double calLower;  //saved previous info
    Double calUpper;  //saved previous info
    Double fatLower;  //saved previous info
    Double fatUpper;  //saved previous info
    Double carboLower;  //saved previous info
    Double carboUpper;  //saved previous info
    Double fiberLower;  //saved previous info
    Double fiberUpper;  //saved previous info
    Double proteinLower;  //saved previous info
    Double proteinUpper;  //saved previous info

    // Initialization of a bunch of TextFields used by query dialog, to get user input
    TextField caloriesLowerLim = new TextField();
    TextField caloriesUpperLim = new TextField();
    TextField fatLowerLim = new TextField();
    TextField fatUpperLim = new TextField();
    TextField carboLowerLim = new TextField();
    TextField carboUpperLim = new TextField();
    TextField fiberLowerLim = new TextField();
    TextField fiberUpperLim = new TextField();
    TextField proteinLowerLim = new TextField();
    TextField proteinUpperLim = new TextField();

    GridPane filterGrid = new GridPane();   // GridPane for viewing query list [upper left]
    ArrayList<FilterEntry> filterEntryList = new ArrayList<>();     // ArrayList to organize query information
    // Another ArrayList to process information needed by FoodData class to apply queries
    ArrayList<String> queryStringList = new ArrayList<>();

    // Some Config Values
    final static String LARGER_EQUAL = ">=";
    final static String EQUAL = "==";
    final static String SMALLER_EQUAL = "<=";
    final static Double LOWER_LIM = 0.0;        // default lower limit of a nutrient
    final static Double UPPER_LIM = Double.MAX_VALUE;       // default upper limit of a nutrient

    // Title for query ScrollPane on the upper left
    Text filterTableHeader1 = new Text("      Nutrient Type : ");
    Text filterTableHeader2 = new Text("      Comaprison Type : ");
    Text filterTableHeader3 = new Text("      Threshhold amount : ");

    // A bunch of ScrollPanes for GUI
    ScrollPane foodSP = new ScrollPane();
    ScrollPane querySP = new ScrollPane();
    ScrollPane mealSP = new ScrollPane();
    ScrollPane sumSP = new ScrollPane();
    // The font size of titles of the four ScrollPane
    int fontTitleSize = 18;

    // 2 invisible buttons triggered multiple times inside other buttons to refresh visual information
    Button btnInvisibleFoodListRefresher = new Button();
    Button btnInvisibleMealListRefresher = new Button();
    Button btnResetFilter = new Button("Reset Query");     //event handler located in filter method

    GridPane mealListGrid = new GridPane(); // GridPane for viewing foodList
    String nameFilter = "";
    List<FoodItem> listAllFoodItem = new ArrayList<>();
    List<FoodItem> listAllMealItem = new ArrayList<>();
    ArrayList<Boolean> foodListSelectedList = new ArrayList<Boolean>(); // Checkboxes for foodList
    ArrayList<Boolean> mealListSelectedList = new ArrayList<Boolean>(); // Checkboxex for mealList


    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The methods setup everything in GUI and utilizes all functionalities of other classes
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            BorderPane root = new BorderPane();
            Scene scene = new Scene(root, width, height);

            HBox biggestBox = new HBox(); // Biggest box to carry vBox1 and vBox2
            VBox vBox1 = new VBox(); // Left side Box containing all elements
            VBox vBox2 = new VBox(); // Right side Box containing all elements

            // Menu bar - setup
            MenuBar menuBar = new MenuBar();
            Menu FileMenu = new Menu("File");
            Menu HelpMenu = new Menu("Help");
            MenuItem filemenu1 = new MenuItem("Load");
            MenuItem filemenu2 = new MenuItem("Save");
            MenuItem instruction = new MenuItem("User Manual");

            FileMenu.getItems().addAll(filemenu1, filemenu2);
            HelpMenu.getItems().addAll(instruction);
            menuBar.getMenus().addAll(FileMenu, HelpMenu);

            // Menu Bar Functionality Setup
            // Event: Load
            filemenu1.setOnAction(event -> {
                // We may just use a pop up window with a textbox asking for
                FileChooser chooser = new FileChooser();
                String currentDir = System.getProperty("user.dir");
                File startPlace = new File(currentDir);
                chooser.setInitialDirectory(startPlace);
                chooser.setTitle("Choose File");
                // Get the file from filechooser
                File file = chooser.showOpenDialog(primaryStage);
                // Pass the filename to foodData so that it can load information
                if (file != null) {
                    this.foodData.loadFoodItems(file.getAbsolutePath());
                    listAllFoodItem = foodData.getAllFoodItems();
                    btnInvisibleFoodListRefresher.fire();
                }
            });
            // Event: Save
            filemenu2.setOnAction(event -> {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Save File");
                File file = chooser.showSaveDialog(primaryStage);
                if (file != null) {
                    this.foodData.saveFoodItems(file.getAbsolutePath());
                }
            });
            // Event: get User Manual
            instruction.setOnAction(event -> {
                Dialog dialogManual = new Dialog();
                showManual(dialogManual);
            });

            // GUI Structure Setup
            // Setting up each part of the vBox at left: HBox for Query + GridPane for Query within ScrollPane
            //                                         + HBox for FoodList + GridPane for Foodlist within ScrollPane

            // Query - HBox setup
            HBox queryBox = new HBox();     // HBox containing name of Querypane and a add query button
            Text queryBoxName = new Text("  Current Query");
            queryBoxName.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, fontTitleSize));
            Button btnEditQuery = new Button("Edit Query");
            HBox queryButtonContainer = new HBox();
            queryButtonContainer.getChildren().addAll(btnEditQuery, btnResetFilter);
            queryButtonContainer.setSpacing(7);
            queryBox.getChildren().addAll(queryBoxName, queryButtonContainer);
            queryBox.setSpacing(470); // Manually place this button to the right of HBox

            // Button 'Add to Meal List' Setup [middle left]
            Button btnAddToMeal = new Button("Add to Meal List");
            btnAddToMeal.setOnAction(event -> {
                // Renewing T/F values for foodList
                this.foodListSelectedList = new ArrayList<>();
                for (int i = 0; i < listAllFoodItem.size(); i++) {
                    // add food to mealList based on checkBox values in foodList
                    foodListSelectedList.add(listAllFoodItem.get(i).getCheckBoxValue());
                }
                // Filter out all the ones to add to mealList
                for (int i = 0; i < foodListSelectedList.size(); i++) {
                    if (foodListSelectedList.get(i) == true) {
                        FoodItem temp = new FoodItem(listAllFoodItem.get(i).getID(),
                                listAllFoodItem.get(i).getNutrients(), listAllFoodItem.get(i).getName(),
                                listAllFoodItem.get(i).getIndex());
                        mealList.addItem(temp);
                    }
                }
                this.listAllMealItem = this.mealList.getMealList();
                btnInvisibleMealListRefresher.fire();       // Update mealList on GUI [upper right]
            });

            // Foodlist - HBox setup
            HBox foodListBox = new HBox();   // HBox containing name of FoodListPane and add food button
            Text foodListName = new Text("  Food List");
            foodListName.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, fontTitleSize));
            Button addFoodButton = new Button();
            addFoodButton.setText("Add Food");

            Dialog dialogAddFood = new Dialog();       // Dialog for 'Add Food' Button
            addFood(dialogAddFood);                    // method call to complete food adding
            addFoodButton.setOnAction(event -> dialogAddFood.show());

            TextField searchContent = new TextField();    // Search Bar Setup
            Button nameFilterButton = new Button();  // Search Bar Button
            nameFilterButton.setText("Search");
            nameFilterButton.setOnAction(event -> {
                try {
                    if (foodData.isEmpty()) throw new IllegalArgumentException();      // Check if a file is loaded
                    nameFilter = searchContent.getText();
                    //use a golbalFilter in foodData to combine all queries and the name substring as filters
                    listAllFoodItem = foodData.globalFilter(nameFilter, queryStringList);
                    btnInvisibleFoodListRefresher.fire();       // Update foodList on GUI [lower left]
                } catch (IllegalArgumentException i) {
                    emptyFoodListAlert();       // An alert would show if file has not been loaded
                }
            });
            HBox searchBarContainer = new HBox();
            searchBarContainer.getChildren().addAll(searchContent, nameFilterButton);
            searchBarContainer.setSpacing(5);
            HBox searchBarBiggerContainer = new HBox();
            searchBarBiggerContainer.getChildren().addAll(searchBarContainer, addFoodButton, btnAddToMeal);
            searchBarBiggerContainer.setSpacing(7);

            // Foodlist - GridPane setup
            GridPane foodListGrid = new GridPane(); // GridPane for viewing foodList
            foodListGrid.setHgap(10);
            int foodListHeaderSize = 18;
            Text foodName = new Text(" Food Item Name      ");
            foodName.setFont(Font.font("Arial", foodListHeaderSize));
            Text calories = new Text(" Calories      ");
            calories.setFont(Font.font("Arial", foodListHeaderSize));
            Text fat = new Text(" Fat      ");
            fat.setFont(Font.font("Arial", foodListHeaderSize));
            Text carbohydrate = new Text(" Carbohydrate      ");
            carbohydrate.setFont(Font.font("Arial", foodListHeaderSize));
            Text fiber = new Text(" Fiber      ");
            fiber.setFont(Font.font("Arial", foodListHeaderSize));
            Text protein = new Text(" Protein      ");
            protein.setFont(Font.font("Arial", foodListHeaderSize));
            foodListGrid.setGridLinesVisible(true);

            // Lower left GUI Integration
            foodListBox.getChildren().addAll(foodListName, searchBarBiggerContainer);
            foodListBox.setSpacing(300); // Manually place this button to the right of HBox

            // Query list - GridPane setup [upper left]
            double queryFontontSize = 16;
            filterTableHeader1.setFont(Font.font("Arial", queryFontontSize));
            filterTableHeader2.setFont(Font.font("Arial", queryFontontSize));
            filterTableHeader3.setFont(Font.font("Arial", queryFontontSize));

            filterGrid.add(filterTableHeader1, 1, 0);
            filterGrid.add(filterTableHeader2, 2, 0);
            filterGrid.add(filterTableHeader3, 3, 0);
            filterGrid.setHgap(20);


            // Right GUI Setup
            // Including HBox for Query, GridPane for Query within ScrollPane, HBox for FoodList,
            // GridPane for Foodlist within ScrollPane
            // Meal List - HBox setup
            HBox upperRight = new HBox();
            Text mealLTitle = new Text("  Meal List");
            mealLTitle.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, fontTitleSize));
            Button clearBtn = new Button("Clear All");
            clearBtn.setOnAction(event -> {
                mealList.clearAll();
                this.listAllMealItem.clear();
                btnInvisibleMealListRefresher.fire();
            });
            Button btnDeleteFromMeal = new Button("Delete Food");
            btnDeleteFromMeal.setOnAction(event -> {
                // Renewing T/F values for foodList
                this.mealListSelectedList = new ArrayList<Boolean>();
                for (int i = 0; i < listAllMealItem.size(); i++) {
                    mealListSelectedList.add(listAllMealItem.get(i).getMealCheckBoxValue());
                }
                // Filter out all the ones to remove from mealList
                ArrayList<Integer> removeMealIndex = new ArrayList<Integer>();
                for (int i = 0; i < mealListSelectedList.size(); i++) {
                    if (mealListSelectedList.get(i)) {
                        removeMealIndex.add(i);
                    }
                }
                for (int j = removeMealIndex.size() - 1; j >= 0; j--) {
                    int index = removeMealIndex.get(j);
                    listAllMealItem.remove(index);
                }
                btnInvisibleMealListRefresher.fire();
            });
            upperRight.setSpacing(480);
            HBox upperRightButtonContainer = new HBox();
            upperRightButtonContainer.getChildren().addAll(btnDeleteFromMeal, clearBtn);
            upperRightButtonContainer.setSpacing(10);
            upperRight.getChildren().addAll(mealLTitle, upperRightButtonContainer);

            // Meal List - GridPane setup
            mealListGrid.setHgap(10);
            int mealListHeaderSize = 18;
            Text mealFoodName = new Text(" Food Item Name      ");
            mealFoodName.setFont(Font.font("Arial", mealListHeaderSize));
            Text mealCalories = new Text(" Calories      ");
            mealCalories.setFont(Font.font("Arial", mealListHeaderSize));
            Text mealFat = new Text(" Fat      ");
            mealFat.setFont(Font.font("Arial", mealListHeaderSize));
            Text mealCarbohydrate = new Text(" Carbohydrate      ");
            mealCarbohydrate.setFont(Font.font("Arial", mealListHeaderSize));
            Text mealFiber = new Text(" Fiber      ");
            mealFiber.setFont(Font.font("Arial", mealListHeaderSize));
            Text mealProtein = new Text(" Protein      ");
            mealProtein.setFont(Font.font("Arial", mealListHeaderSize));

            //Meal Summary - HBox setup
            HBox sumTitleBox = new HBox();
            Text sumTitle = new Text("  Meal Summary");
            sumTitle.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, fontTitleSize));
            Button sumBtn = new Button("Generate Summary");
            sumTitleBox.getChildren().addAll(sumTitle, sumBtn);
            sumTitleBox.setSpacing(570);

            // Following are wrapping up all components and put them together
            // Left side - wrap up
            vBox1.getChildren().addAll(queryBox, querySP, foodListBox, foodSP);
            vBox1.setPrefWidth(scene.getWidth() / 2);
            vBox1.setVgrow(foodSP, Priority.ALWAYS); // only expanding size of FoodList

            // Right side - wrap up
            vBox2.getChildren().addAll(upperRight, mealSP, sumTitleBox, sumSP);
            vBox2.setPrefWidth(scene.getWidth() / 2);

            // ScrollPanes - policies
            // ScrollBar policies
            querySP.setVbarPolicy(ScrollBarPolicy.ALWAYS);
            querySP.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
            foodSP.setVbarPolicy(ScrollBarPolicy.ALWAYS);
            foodSP.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
            sumSP.setVbarPolicy(ScrollBarPolicy.ALWAYS);
            sumSP.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
            // Size policies
            querySP.setPrefHeight(150);
            querySP.setMaxHeight(height / 3);
            querySP.setMinHeight(150);
            sumSP.setPrefSize(width / 2, height / 2);
            mealSP.setPrefSize(width / 2, height / 1.5);
            // Putting GridPanes into ScrollPanes
            foodSP.setContent(foodListGrid); // insert GridPane into FoodList scrollpane
            querySP.setContent(filterGrid); // insert GridPane into Query scrollpane
            mealSP.setContent(mealListGrid); // insert GridPane into Meal scrollPane
            foodListGrid.setGridLinesVisible(true);
            mealListGrid.setGridLinesVisible(true);// Showing Grid Lines

            // Scene - setup
            root.setTop(menuBar); // put the menuBar at top
            biggestBox.getChildren().addAll(vBox1, vBox2); // wrapping rest elements into one box
            root.setLeft(biggestBox); // Putting the box into BorderPane

            //scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Meal Planner");
            primaryStage.show();


            // Additional functionalities Setup
            instruction.fire();     // Popup window and help window trigger

            // Hidden Button used to update foodList on GUI
            btnInvisibleFoodListRefresher.setOnAction(event -> {
                try {
                    foodListGrid.getChildren().clear();
                    foodListGrid.add(foodName, 1, 0, 1, 1);
                    foodListGrid.add(calories, 2, 0, 1, 1);
                    foodListGrid.add(fat, 3, 0, 1, 1);
                    foodListGrid.add(carbohydrate, 4, 0, 1, 1);
                    foodListGrid.add(fiber, 5, 0, 1, 1);
                    foodListGrid.add(protein, 6, 0, 1, 1);

                    for (int i = 0; i < listAllFoodItem.size(); i++) {
                        foodListGrid.add(listAllFoodItem.get(i).getCheckBox(), 0, i + 1, 1, 1);
                        foodListGrid.add(new Text(listAllFoodItem.get(i).getName()), 1, i + 1, 1, 1);
                        foodListGrid.add(new Text("   " + listAllFoodItem.get(i).getNutrientValue("calories")), 2, i + 1, 1, 1);
                        foodListGrid.add(new Text("   " + listAllFoodItem.get(i).getNutrientValue("fat")), 3, i + 1, 1, 1);
                        foodListGrid.add(new Text("   " + listAllFoodItem.get(i).getNutrientValue("carbohydrate")), 4, i + 1, 1, 1);
                        foodListGrid.add(new Text("   " + listAllFoodItem.get(i).getNutrientValue("fiber")), 5, i + 1, 1, 1);
                        foodListGrid.add(new Text("   " + listAllFoodItem.get(i).getNutrientValue("protein")), 6, i + 1, 1, 1);
                    }
                } catch (Exception e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
            });
            // Hidden Button used to update mealLiat on GUI
            btnInvisibleMealListRefresher.setOnAction(event -> {
                try {
                    mealListGrid.getChildren().clear();
                    mealListGrid.add(mealFoodName, 1, 0, 1, 1);
                    mealListGrid.add(mealCalories, 2, 0, 1, 1);
                    mealListGrid.add(mealFat, 3, 0, 1, 1);
                    mealListGrid.add(mealCarbohydrate, 4, 0, 1, 1);
                    mealListGrid.add(mealFiber, 5, 0, 1, 1);
                    mealListGrid.add(mealProtein, 6, 0, 1, 1);

                    for (int i = 0; i < listAllMealItem.size(); i++) {
                        mealListGrid.add(listAllMealItem.get(i).getMealCheckBox(), 0, i + 1, 1, 1);
                        mealListGrid.add(new Text(listAllMealItem.get(i).getName()), 1, i + 1, 1, 1);
                        mealListGrid.add(new Text("   " + listAllMealItem.get(i).getNutrientValue("calories")), 2, i + 1, 1, 1);
                        mealListGrid.add(new Text("   " + listAllMealItem.get(i).getNutrientValue("fat")), 3, i + 1, 1, 1);
                        mealListGrid.add(new Text("   " + listAllMealItem.get(i).getNutrientValue("carbohydrate")), 4, i + 1, 1, 1);
                        mealListGrid.add(new Text("   " + listAllMealItem.get(i).getNutrientValue("fiber")), 5, i + 1, 1, 1);
                        mealListGrid.add(new Text("   " + listAllMealItem.get(i).getNutrientValue("protein")), 6, i + 1, 1, 1);
                    }
                } catch (Exception e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
            });

            //Query Dialog Setup [filter]
            Dialog dialogFilter = new Dialog();
            dialogFilter.initOwner(primaryStage);
            filter(dialogFilter);
            btnEditQuery.setOnAction((event) -> dialogFilter.show());

            //Generate Summary button
            sumBtn.setOnAction(event -> {
                GridPane summaryGP = new GridPane();
                HashMap<String, Double> nutritionSummary = mealList.getNutritionSummary();

                summaryGP.setHgap(70.0);
                summaryGP.setVgap(20.0);
                Text sum = new Text("    Summary");
                Text sumCal = new Text(" Total Calories");
                Text sumFat = new Text(" Total Fat");
                Text sumCar = new Text(" Total Carbohydrate");
                Text sumFiber = new Text(" Total Fiber");
                Text sumPro = new Text(" Total Protein");

                sum.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 23));
                sumCal.setFont(Font.font("Arial", foodListHeaderSize));
                sumFat.setFont(Font.font("Arial", foodListHeaderSize));
                sumCar.setFont(Font.font("Arial", foodListHeaderSize));
                sumFiber.setFont(Font.font("Arial", foodListHeaderSize));
                sumPro.setFont(Font.font("Arial", foodListHeaderSize));

                summaryGP.add(sum, 1, 0, 2, 1);
                summaryGP.add(sumCal, 0, 1);
                summaryGP.add(sumFat, 0, 2);
                summaryGP.add(sumCar, 0, 3);
                summaryGP.add(sumFiber, 0, 4);
                summaryGP.add(sumPro, 0, 5);

                mealList.analyzeMeal();
                Text digitCal = new Text("          " + nutritionSummary.get("calories").toString());
                Text digitFat = new Text("          " + nutritionSummary.get("fat").toString());
                Text digitCar = new Text("          " + nutritionSummary.get("carbohydrate").toString());
                Text digitFiber = new Text("          " + nutritionSummary.get("fiber").toString());
                Text digitPro = new Text("          " + nutritionSummary.get("protein").toString());

                digitCal.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, foodListHeaderSize));
                digitFat.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, foodListHeaderSize));
                digitCar.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, foodListHeaderSize));
                digitFiber.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, foodListHeaderSize));
                digitPro.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, foodListHeaderSize));

                summaryGP.add(digitCal, 1, 1);
                summaryGP.add(digitFat, 1, 2);
                summaryGP.add(digitCar, 1, 3);
                summaryGP.add(digitFiber, 1, 4);
                summaryGP.add(digitPro, 1, 5);

                sumSP.setContent(summaryGP);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * This method setup the 'instruction' or 'user manual' dialog windows, which contains basic info needed for users
     * This method is called when the program first start and when the 'User Manual' button in menu's help is pressed
     *
     * @param dialogManual user manual dialog to be setup here
     */
    private void showManual(Dialog dialogManual) {
        dialogManual.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeManualButton = dialogManual.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeManualButton.managedProperty().bind(closeManualButton.visibleProperty());
        closeManualButton.setVisible(false);

        GridPane instructionGrid = new GridPane();
        instructionGrid.setHgap(10);
        instructionGrid.setVgap(10);
        Button proceed = new Button("Proceed");
        proceed.setOnAction(event -> dialogManual.close());
        Text instructionLabel1 = new Text("        > Query: ");
        Text instructionLabel2 = new Text("        > Search Bar");
        Text instructionLabel3 = new Text("        > Add Food");
        Text instructionLabel4 = new Text("        > Add Food to Meal");
        Text instructionLabel5 = new Text("        > Delete from Meal");
        Text instructionLabel6 = new Text("        > Generate Summary");
        double fontSize = 15;
        instructionLabel1.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
        instructionLabel2.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
        instructionLabel3.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
        instructionLabel4.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
        instructionLabel5.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
        instructionLabel6.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
        Text courtesy = new Text("          ENJOY THE MEAL !!!");
        courtesy.setFont(Font.font("Thomas", FontWeight.EXTRA_BOLD, FontPosture.ITALIC, 30));

        instructionGrid.add(new Text("To use the program, a formatted .csv file is required"), 0, 0, 2, 1);
        instructionGrid.add(instructionLabel1, 0, 1, 1, 1);
        instructionGrid.add(new Text("  Food queries can be edited by pressing the 'Edit Filter' button;"), 1, 1, 2, 1);
        instructionGrid.add(instructionLabel2, 0, 2, 1, 1);
        instructionGrid.add(new Text("  Search bar can be used to search food given specific names;"), 1, 2, 1, 1);
        instructionGrid.add(instructionLabel3, 0, 3, 1, 1);
        instructionGrid.add(new Text("  Additional food can be added by pressing 'Add Food' button;"), 1, 3, 2, 1);
        instructionGrid.add(instructionLabel4, 0, 4, 1, 1);
        instructionGrid.add(new Text("  To add food to meal List, first select check boxes, then press 'Add To Meal List' button;"), 1, 4, 2, 1);

        instructionGrid.add(instructionLabel5, 0, 5, 1, 1);
        instructionGrid.add(new Text("  To remove item from meal list, first select check boxes, then press 'Delete Food' button;"), 1, 5, 2, 1);
        instructionGrid.add(instructionLabel6, 0, 6, 1, 1);
        instructionGrid.add(new Text("  Display the sum of each type of nutrients of foods in the current meal list."), 1, 6, 2, 1);
        //instructionGrid.add(new HBox(), 0, 7, 1, 1);
        instructionGrid.add(courtesy, 0, 8, 2, 1);
        instructionGrid.add(new Text("* This manual can be accessed from menu -> Help"), 0, 7, 2, 1);
        instructionGrid.add(proceed, 2, 8, 2, 1);
        dialogManual.getDialogPane().setContent(instructionGrid);

        dialogManual.setTitle("Manual");
        dialogManual.show();
    }

    /**
     * This method will be called after user click the add food button and will pop
     * up a new dialog enabling users to enter the name, id, and nutrition
     * information about the food they want to add to the food list. A save button
     * and cancel button in dialog will trigger further event handler connecting
     * with food list and closing window
     *
     * @param dialogAddFood
     */
    private void addFood(Dialog dialogAddFood) {

        // Initiate the pop up window for user to enter information and save/cancel
        dialogAddFood.initModality(Modality.APPLICATION_MODAL);
        dialogAddFood.setTitle("Add Food");
        GridPane gridDialogAddFood = new GridPane();
        DialogPane dialogPaneAddFood = new DialogPane();
        dialogPaneAddFood.setContent(gridDialogAddFood);
        dialogAddFood.setDialogPane(dialogPaneAddFood);
        gridDialogAddFood.setHgap(10);
        gridDialogAddFood.setVgap(10);
        HBox cancelAndSave = new HBox();
        cancelAndSave.setSpacing(10);

        // Label before each text field to let user know where to enter information
        labAddFoodName.setText("Enter food name: ");
        labAddFoodCal.setText("Enter calories: ");
        labAddFoodFat.setText("Enter fat: ");
        labAddFoodFib.setText("Enter fiber: ");
        labAddFoodPro.setText("Enter protein: ");
        labAddFoodCarb.setText("Enter CarboHydrate: ");
        labAddFoodId.setText("Enter food id: ");

        // Put the different labels and text fields in to the grid pane which will be
        // visible on the pop up window
        gridDialogAddFood.add(labAddFoodId, 2, 0);
        gridDialogAddFood.add(txtAddFoodId, 3, 0);
        gridDialogAddFood.add(labAddFoodName, 2, 1);
        gridDialogAddFood.add(txtAddFoodName, 3, 1);
        gridDialogAddFood.add(labAddFoodCal, 2, 2);
        gridDialogAddFood.add(txtAddFoodCal, 3, 2);
        gridDialogAddFood.add(labAddFoodFat, 2, 3);
        gridDialogAddFood.add(txtAddFoodFat, 3, 3);
        gridDialogAddFood.add(labAddFoodCarb, 2, 4);
        gridDialogAddFood.add(txtAddFoodCarb, 3, 4);
        gridDialogAddFood.add(labAddFoodFib, 2, 5);
        gridDialogAddFood.add(txtAddFoodFib, 3, 5);
        gridDialogAddFood.add(labAddFoodPro, 2, 6);
        gridDialogAddFood.add(txtAddFoodPro, 3, 6);

        // Set the button that enable users to close the window
        dialogAddFood.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = dialogAddFood.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
        closeButton.setVisible(false);

        // Set the button that enable users to ignore the input and close the window
        Button btnCancelAddFood = new Button("Cancel");
        btnCancelAddFood.setOnAction(event -> {
            // close the window
            dialogAddFood.close();
        });

        // Save Button enable users to save the food they wanted to add if they didn't
        // leave any of the food information blank
        Button btnSaveAddFood = new Button("Save");
        cancelAndSave.getChildren().addAll(btnCancelAddFood, btnSaveAddFood);
        gridDialogAddFood.add(cancelAndSave, 3, 8);

        // Save Button setup
        btnSaveAddFood.setOnAction(event -> {
            //Entering nothing will not be allowed when adding food
            try {
                //formatting food's information corresponding type
                fromTextFieldToDesiredFormat();

                //Not entering anything will not be allowed and the food will not be able to added to the list
                if (addFoodCal.equals("") || addFoodFat.equals("") || addFoodCarb.equals("") || addFoodFib.equals("") || addFoodPro.equals("")
                        || addFoodName.equals("") || addFoodId.equals("") || addFoodCal == null || addFoodFat == null
                        || addFoodCarb == null || addFoodFib == null || addFoodName == null || addFoodId == null || addFoodPro == null || 
                        addFoodCal.compareTo(0.0) < 0 || addFoodFat.compareTo(0.0) < 0 ||addFoodCarb.compareTo(0.0) < 0 ||
                        addFoodFib.compareTo(0.0) < 0 ||addFoodPro.compareTo(0.0) < 0) {
                    throw new IllegalArgumentException();
                }

                // adding the new food the food list
                FoodItem newFoodAddedByUser = new FoodItem(addFoodId, addFoodName);
                //finishing the hash map containing all kinds of nutrition information
                newFoodAddedByUser.addNutrient("calories", addFoodCal);
                newFoodAddedByUser.addNutrient("fiber", addFoodFib);
                newFoodAddedByUser.addNutrient("protein", addFoodPro);
                newFoodAddedByUser.addNutrient("carbohydrate", addFoodCarb);
                newFoodAddedByUser.addNutrient("fat", addFoodFat);
            

                //showing the new food on the food list on screen and close the window
                foodData.addFoodItem(newFoodAddedByUser);
                listAllFoodItem = foodData.getAllFoodItems();
                dialogAddFood.close();

            } catch (NumberFormatException i) { // give user alert if entered nothing for any of the text field
                Alert numberFormatFilter = new Alert(Alert.AlertType.INFORMATION);
                numberFormatFilter.setTitle("ERROR");
                numberFormatFilter.setHeaderText("Illegal Filter Entry");
                numberFormatFilter.setContentText(
                        "Please enter legal filter entries. \n  " + "Integer or decimal larger or equal to 0 ");
                numberFormatFilter.showAndWait();

            } catch (IllegalArgumentException e) {//give user alert if entered undesired data type into the text Field
                Alert numberFormatFilter = new Alert(Alert.AlertType.INFORMATION);
                numberFormatFilter.setTitle("ERROR");
                numberFormatFilter.setHeaderText("Illegal Filter Entry");
                numberFormatFilter.setContentText(
                        "Please enter legal filter entries. \n  " + "Integer or decimal larger or equal to 0 ");
                numberFormatFilter.showAndWait();
            }
            //update the new added food's information to different sections
            btnInvisibleFoodListRefresher.fire();
        });

    }

    /**
     * The helper method to setup query on GUI and invoke functionalities in foodData and others
     * @param dialogFilter query dialog to be setup
     */
    private void filter(Dialog dialogFilter) {

        setDefaultFilter();     // call another helper method to return all queries to default values
        // dialog setup
        dialogFilter.initModality(Modality.APPLICATION_MODAL);
        dialogFilter.setTitle("Edit Filter");

        // GridPane & DialogPane setup for query dialog
        GridPane gridDialogFilter = new GridPane();
        DialogPane dialogPaneFilter = new DialogPane();
        dialogPaneFilter.setContent(gridDialogFilter);
        dialogFilter.setDialogPane(dialogPaneFilter);
        gridDialogFilter.setHgap(10);
        gridDialogFilter.setVgap(10);

        //Line Setup 1: calories
        Text textCaloriesFilter = new Text(" <=   Calories            <=");
        caloriesLowerLim.setText(calLowerLimHolder);    //load saved previous info to TextField
        caloriesUpperLim.setText(calUpperLimHolder);    //load saved previous info to TextField

        //Line Setup 2: fat
        Text textFatFilter = new Text(" <=   Fat                    <=");
        caloriesLowerLim.setText(fatLowerLimHolder);    //load saved previous info to TextField
        caloriesUpperLim.setText(fatUpperLimHolder);    //load saved previous info to TextField

        //Line Setup 3: Carbohydrate
        Text textCarboFilter = new Text(" <=   Carbohydrate   <=");
        caloriesLowerLim.setText(carboLowerLimHolder);    //load saved previous info to TextField
        caloriesUpperLim.setText(carboUpperLimHolder);    //load saved previous info to TextField

        //Line Setup 4: Fiber
        Text textFiberFilter = new Text(" <=   Fiber                 <=");
        caloriesLowerLim.setText(fiberLowerLimHolder);    //load saved previous info to TextField
        caloriesUpperLim.setText(fiberUpperLimHolder);    //load saved previous info to TextField

        //Line Setup 5: Protein
        Text textProteinFilter = new Text(" <=   Protein             <=");
        caloriesLowerLim.setText(proteinLowerLimHolder);    //load saved previous info to TextField
        caloriesUpperLim.setText(proteinUpperLimHolder);    //load saved previous info to TextField

        //BUTTON setup
        // "x" button setup
        dialogFilter.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = dialogFilter.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
        closeButton.setVisible(false);

        // cancel button setup
        Button btnCancelFilter = new Button("Cancel");
        btnCancelFilter.setOnAction(event -> {
            dialogFilter.close();
        });

        // reset button to clean the data
        Button btnResetFilterDialog = new Button("Reset Filter");
        btnResetFilterDialog.setOnAction(event -> {
            setDefaultFilter();     // reset everything used by filter to default values
        });

        // Save Button setup
        Button btnFilterSave = new Button("Save");
        HBox hBoxBtnFilterSave = new HBox();
        Text space = new Text("                        ");
        hBoxBtnFilterSave.getChildren().addAll(space, btnFilterSave);
        btnFilterSave.setAlignment(Pos.TOP_RIGHT);
        btnFilterSave.setOnAction(event -> {
            try {
                fromTextFieldToDouble();    // save text input to usable double value, illegal format would invoke alert
                // test logic of queries input by user
                if (calLower.compareTo(calUpper) > 0 || carboLower.compareTo(carboUpper) > 0 ||
                        fatLower.compareTo(fatUpper) > 0 || fiberLower.compareTo(fiberUpper) > 0 ||
                        proteinLower.compareTo(proteinUpper) > 0 || calLower.compareTo(LOWER_LIM) < 0 ||
                        carboLower.compareTo(LOWER_LIM) < 0 || fatLower.compareTo(LOWER_LIM) < 0 ||
                        fiberLower.compareTo(LOWER_LIM) < 0 || proteinLower.compareTo(LOWER_LIM) < 0)
                    throw new IllegalArgumentException();   // Exception would trigger Alert and reset all filters

                filterEntryList = new ArrayList<>();
                inputFilterEntryList();      // save all formatted double queries info to an intermediary list
                updateFilterTable();        // update GUI filter table [upper left]

                // Convert info from filterEntryList to a formatted String list, usable by foodData
                queryStringList = new ArrayList<>();
                for (FilterEntry element : filterEntryList) {
                    queryStringList.add(element.getNutrient() + " " + element.getFilterType() + " "
                            + element.getLimit());
                }
                if (foodData.isEmpty()) throw new IllegalAccessException();
                listAllFoodItem = foodData.globalFilter(nameFilter, queryStringList);   // combine with searchBar info to filter
                btnInvisibleFoodListRefresher.fire();       // update GUI
                dialogFilter.close();

            } catch (IllegalAccessException e) {
                emptyFoodListAlert();
            } catch (NumberFormatException n) {
                Alert numberFormatFilter = new Alert(Alert.AlertType.INFORMATION);
                numberFormatFilter.setTitle("ERROR");
                numberFormatFilter.setHeaderText("Entry Format Error");
                numberFormatFilter.setContentText("Filter entered cannot be processed.");
                numberFormatFilter.showAndWait();
                btnResetFilter.fire();
            } catch (IllegalArgumentException i) {
                Alert numberFormatFilter = new Alert(Alert.AlertType.INFORMATION);
                numberFormatFilter.setTitle("ERROR");
                numberFormatFilter.setHeaderText("Illegal Filter Entry");
                numberFormatFilter.setContentText("Please enter legal filter entries. \n  " +
                        "1. Integer or decimal larger or equal to 0 \n  2. For [Entry1] <= Nutrient <= [Entry2]" +
                        "\n     Entry1 should be smaller than Entry2.");
                numberFormatFilter.showAndWait();
                btnResetFilter.fire();
            }
        });

        // Reset Button on the primaryStage
        btnResetFilter.setOnAction(event -> {
            btnResetFilterDialog.fire();
            btnFilterSave.fire();
        });

        // filter prompt
        Text promptFilter1 = new Text("Entry is optional.");
        Text promptFilter2 = new Text("Please enter legal integer or decimal number(s).");
        //fiter gridPane integration
        int filTableLine = 2;
        gridDialogFilter.add(promptFilter1, 0, 0, 3, 1);
        gridDialogFilter.add(promptFilter2, 0, 1, 3, 1);

        gridDialogFilter.add(hBoxBtnFilterSave, 2, filTableLine + 5, 1, 1);
        gridDialogFilter.add(btnResetFilterDialog, 1, filTableLine + 5, 1, 1);
        gridDialogFilter.add(btnCancelFilter, 0, filTableLine + 5, 1, 1);

        gridDialogFilter.add(caloriesLowerLim, 0, filTableLine, 1, 1);
        gridDialogFilter.add(textCaloriesFilter, 1, filTableLine, 1, 1);
        gridDialogFilter.add(caloriesUpperLim, 2, filTableLine, 1, 1);

        gridDialogFilter.add(fatLowerLim, 0, filTableLine + 1, 1, 1);
        gridDialogFilter.add(textFatFilter, 1, filTableLine + 1, 1, 1);
        gridDialogFilter.add(fatUpperLim, 2, filTableLine + 1, 1, 1);

        gridDialogFilter.add(carboLowerLim, 0, filTableLine + 2, 1, 1);
        gridDialogFilter.add(textCarboFilter, 1, filTableLine + 2, 1, 1);
        gridDialogFilter.add(carboUpperLim, 2, filTableLine + 2, 1, 1);

        gridDialogFilter.add(fiberLowerLim, 0, filTableLine + 3, 1, 1);
        gridDialogFilter.add(textFiberFilter, 1, filTableLine + 3, 1, 1);
        gridDialogFilter.add(fiberUpperLim, 2, filTableLine + 3, 1, 1);

        gridDialogFilter.add(proteinLowerLim, 0, filTableLine + 4, 1, 1);
        gridDialogFilter.add(textProteinFilter, 1, filTableLine + 4, 1, 1);
        gridDialogFilter.add(proteinUpperLim, 2, filTableLine + 4, 1, 1);
    }

    /**
     * A helper method for filtering, specifically reset information of all filter-related fields
     */
    private void setDefaultFilter() {
        calLower = LOWER_LIM;
        calUpper = UPPER_LIM;
        fatLower = LOWER_LIM;
        fatUpper = UPPER_LIM;
        carboLower = LOWER_LIM;
        carboUpper = UPPER_LIM;
        fiberLower = LOWER_LIM;
        fiberUpper = UPPER_LIM;
        proteinLower = LOWER_LIM;
        proteinUpper = UPPER_LIM;

        calLowerLimHolder = "";
        calUpperLimHolder = "";
        carboLowerLimHolder = "";
        carboUpperLimHolder = "";
        fatLowerLimHolder = "";
        fatUpperLimHolder = "";
        fiberLowerLimHolder = "";
        fiberUpperLimHolder = "";
        proteinLowerLimHolder = "";
        proteinUpperLimHolder = "";

        caloriesLowerLim.setText("");
        caloriesUpperLim.setText("");
        carboLowerLim.setText("");
        carboUpperLim.setText("");
        fatLowerLim.setText("");
        fatUpperLim.setText("");
        fiberLowerLim.setText("");
        fiberUpperLim.setText("");
        proteinLowerLim.setText("");
        proteinUpperLim.setText("");
    }

    /**
     * This method transfer user input that is not empty to appropriate data type
     * (Double for numbers, String for name and id)
     *
     * @return void
     */
    private void fromTextFieldToDesiredFormat() {
        // Store ACTUAL user inputs of different nutrition information to corresponding
        // holder String
        addFoodCaloriesHolder = txtAddFoodCal.getText();
        addFoodProteinHolder = txtAddFoodPro.getText();
        addFoodCarboHydrateHolder = txtAddFoodCarb.getText();
        addFoodFatHolder = txtAddFoodFat.getText();
        addFoodFiberHolder = txtAddFoodFib.getText();
        addFoodNameHolder = txtAddFoodName.getText();
        addFoodIdHolder = txtAddFoodId.getText();

        // If user didn't leave the text field empty, transfer quantified informaiton
        // entered to Double
        if (!addFoodIdHolder.equals(""))
            addFoodId = addFoodIdHolder;
        if (!addFoodCaloriesHolder.equals(""))
            addFoodCal = Double.parseDouble(addFoodCaloriesHolder);
        if (!addFoodFiberHolder.equals(""))
            addFoodFib = Double.parseDouble(addFoodFiberHolder);
        if (!addFoodFatHolder.equals(""))
            addFoodFat = Double.parseDouble(addFoodFatHolder);
        if (!addFoodCarboHydrateHolder.equals(""))
            addFoodCarb = Double.parseDouble(addFoodCarboHydrateHolder);

        // If user didn't leave the text field empty, transfer textual information
        // entered to String
        if (!addFoodProteinHolder.equals(""))
            addFoodPro = Double.parseDouble(addFoodProteinHolder);
        if (!addFoodNameHolder.equals(""))
            addFoodName = addFoodNameHolder;
    }

    /**
     * A helper method for filter, getting String from TextField and convert to usable double
     * @throws NumberFormatException would be thrown when user input is inconvertible
     */
    private void fromTextFieldToDouble() throws NumberFormatException{
        calLowerLimHolder = caloriesLowerLim.getText();
        calUpperLimHolder = caloriesUpperLim.getText();
        carboLowerLimHolder = carboLowerLim.getText();
        carboUpperLimHolder = carboUpperLim.getText();
        fatLowerLimHolder = fatLowerLim.getText();
        fatUpperLimHolder = fatUpperLim.getText();
        fiberLowerLimHolder = fiberLowerLim.getText();
        fiberUpperLimHolder = fiberUpperLim.getText();
        proteinLowerLimHolder = proteinLowerLim.getText();
        proteinUpperLimHolder = proteinUpperLim.getText();

        if (!calLowerLimHolder.equals("")) calLower = Double.parseDouble(calLowerLimHolder);
        else calLower = LOWER_LIM;
        if (!calUpperLimHolder.equals("")) calUpper = Double.parseDouble(calUpperLimHolder);
        else calUpper = UPPER_LIM;
        if (!carboLowerLimHolder.equals("")) carboLower = Double.parseDouble(carboLowerLimHolder);
        else carboLower = LOWER_LIM;
        if (!carboUpperLimHolder.equals("")) carboUpper = Double.parseDouble(carboUpperLimHolder);
        else carboUpper = UPPER_LIM;
        if (!fatLowerLimHolder.equals("")) fatLower = Double.parseDouble(fatLowerLimHolder);
        else fatLower = LOWER_LIM;
        if (!fatUpperLimHolder.equals("")) fatUpper = Double.parseDouble(fatUpperLimHolder);
        else fatUpper = UPPER_LIM;
        if (!fiberLowerLimHolder.equals("")) fiberLower = Double.parseDouble(fiberLowerLimHolder);
        else fiberLower = LOWER_LIM;
        if (!fiberUpperLimHolder.equals("")) fiberUpper = Double.parseDouble(fiberUpperLimHolder);
        else fiberUpper = UPPER_LIM;
        if (!proteinLowerLimHolder.equals("")) proteinLower = Double.parseDouble(proteinLowerLimHolder);
        else proteinLower = LOWER_LIM;
        if (!proteinUpperLimHolder.equals("")) proteinUpper = Double.parseDouble(proteinUpperLimHolder);
        else proteinUpper = UPPER_LIM;
    }

    /**
     * Another helper method used by filter() to add entry information into filterEntryList for better organization
     */
    private void inputFilterEntryList() {

        if (calLower.equals(calUpper)) {
            filterEntryList.add(new FilterEntry("calories", EQUAL, calUpper));
        } else {
            if (!calLower.equals(LOWER_LIM))
                filterEntryList.add(new FilterEntry("calories", LARGER_EQUAL, calLower));
            if (!calUpper.equals(UPPER_LIM))
                filterEntryList.add(new FilterEntry("calories", SMALLER_EQUAL, calUpper));
        }

        if (carboLower.equals(carboUpper)) {
            filterEntryList.add(new FilterEntry("carbohydrate", EQUAL, carboUpper));
        } else {
            if (!carboLower.equals(LOWER_LIM))
                filterEntryList.add(new FilterEntry("carbohydrate", LARGER_EQUAL, carboLower));
            if (!carboUpper.equals(UPPER_LIM))
                filterEntryList.add(new FilterEntry("carbohydrate", SMALLER_EQUAL, carboUpper));
        }

        if (fatLower.equals(fatUpper)) {
            filterEntryList.add(new FilterEntry("fat", EQUAL, fatUpper));
        } else {
            if (!fatLower.equals(LOWER_LIM))
                filterEntryList.add(new FilterEntry("fat", LARGER_EQUAL, fatLower));
            if (!fatUpper.equals(UPPER_LIM))
                filterEntryList.add(new FilterEntry("fat", SMALLER_EQUAL, fatUpper));
        }

        if (fiberLower.equals(fiberUpper)) {
            filterEntryList.add(new FilterEntry("fiber", EQUAL, fiberUpper));
        } else {
            if (!fiberLower.equals(LOWER_LIM))
                filterEntryList.add(new FilterEntry("fiber", LARGER_EQUAL, fiberLower));
            if (!fiberUpper.equals(UPPER_LIM))
                filterEntryList.add(new FilterEntry("fiber", SMALLER_EQUAL, fiberUpper));
        }

        if (proteinLower.equals(proteinUpper)) {
            filterEntryList.add(new FilterEntry("protein", EQUAL, proteinUpper));
        } else {
            if (!proteinLower.equals(LOWER_LIM))
                filterEntryList.add(new FilterEntry("protein", LARGER_EQUAL, proteinLower));
            if (!proteinUpper.equals(UPPER_LIM))
                filterEntryList.add(new FilterEntry("protein", SMALLER_EQUAL, proteinUpper));
        }
    }

    /**
     * Another helper method used by filter() to update filter on GUI after all changes
     */
    private void updateFilterTable() {
        try {
            filterGrid.getChildren().clear();
            filterGrid.add(filterTableHeader1, 1, 0);
            filterGrid.add(filterTableHeader2, 2, 0);
            filterGrid.add(filterTableHeader3, 3, 0);
            for (int i = 0; i < filterEntryList.size(); i++) {
                filterGrid.add(new Text("  >"), 0, i + 1, 1, 1);
                filterGrid.add(new Text("           " + filterEntryList.get(i).getNutrient()), 1, i + 1, 1, 1);
                filterGrid.add(new Text("           " + filterEntryList.get(i).getFilterType()), 2, i + 1, 1, 1);
                filterGrid.add(new Text("           " + filterEntryList.get(i).getLimit().toString()), 3, i + 1, 1, 1);
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    /**
     * A helper method to setup an Alert that would be called when no file is loaded
     */
    private void emptyFoodListAlert() {
        Alert emptyFoodList = new Alert(Alert.AlertType.INFORMATION);
        emptyFoodList.setTitle("ERROR");
        emptyFoodList.setHeaderText("ERROR: Empty Food List");
        emptyFoodList.setContentText("Food data has not yet loaded from file.");
        emptyFoodList.showAndWait();
    }

    /**
     * An inner class to better organize all information of filters/ queries
     */
    class FilterEntry {
        private String nutrient;
        private String filterType;
        private Double limit;

        FilterEntry() {
            System.out.println("Initialization problem");
        }

        FilterEntry(String nutrient, String filterType, Double limit) {
            this.nutrient = nutrient;
            this.filterType = filterType;
            this.limit = limit;
        }

        public String getNutrient() {
            return nutrient;
        }

        public String getFilterType() {
            return filterType;
        }

        public Double getLimit() {
            return limit;
        }
    }
}


