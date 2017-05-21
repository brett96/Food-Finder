package edu.orangecoastcollege.cs272.btomita.capstone.view;
import java.util.Random;

import edu.orangecoastcollege.cs272.btomita.capstone.controller.Controller;

import edu.orangecoastcollege.cs272.btomita.capstone.model.Restaurant;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
public class ViewFX extends Application 
{
	Scene mainScene;
	ComboBox<String> categoriesCB;
	ComboBox<String> cityCB;
	Slider minPriceSlider = new Slider(1, 5, 0);
	Slider maxPriceSlider = new Slider(1, 5, 0);
	Slider reviewsSlider = new Slider(0, 1000, 0);
	ListView<Restaurant> restaurantsLV = new ListView<>();
	
	final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
	
	Stage mainStage;
	Restaurant selectedRestaurant;
	ObservableList<Restaurant> restaurantsList;
	ObservableList<String> categoriesList;
	ObservableList<String> priceList;
	ObservableList<String> cityList;
	Controller controller;// = Controller.getInstance();
	Button pickButton = new Button("Pick Restaurant");
	Button resetButton = new Button("Reset");
	
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
//		// TODO Auto-generated method stub
		//mainStage = primaryStage;
//		
//		//Setup for all Restaurants:
		
//		controller = Controller.getInstance();
//		restaurantsList = controller.getAllRestaurants();
//		categoriesList = controller.getDistinctCategories();
//		priceList = controller.getDistinctPrices();
//		
//		cityList = controller.getDistinctCities();
//		//nextButton.setDisable(true);
//		
//		mainScene = createMainScene();
//		mainStage.setTitle("Food Finder");
//		mainStage.setScene(mainScene);
//		mainStage.show();

		ViewNavigator.setStage(primaryStage);
		ViewNavigator.loadFXMLScene("Welcome to Food Finder", ViewNavigator.SIGN_IN_SCENE);
	}
	
	//privae StringConverter<Double> dollars
	
	public Scene createMainScene()
	{
		controller = Controller.getInstance();
		restaurantsList = controller.getAllRestaurants();
		categoriesList = controller.getDistinctCategories();
		priceList = controller.getDistinctPrices();
		
		cityList = controller.getDistinctCities();
		
		restaurantsLV.setItems(restaurantsList);
		restaurantsLV.setPrefWidth(1000);
		restaurantsLV.setOnMouseClicked(e -> selectRestaurant());
		
		categoriesCB = new ComboBox<>(categoriesList);
		categoriesCB.setOnAction(e -> returnRestaurant());
		cityCB = new ComboBox<>(cityList);
		//cityCB = new ComboBox<>();
		cityCB.setOnAction(e -> returnRestaurant());
		
		minPriceSlider.setShowTickMarks(true);
		minPriceSlider.setShowTickLabels(true);
		minPriceSlider.setSnapToTicks(true);
		minPriceSlider.setLabelFormatter(new StringConverter<Double>(){
			@Override
			public String toString(Double n){
				if(n == 1) return "$";
				if(n == 2) return "$$";
				if(n == 3) return "$$$";
				if(n == 4) return "$$$$";
				return "$$$$$";
			}

			@Override
			public Double fromString(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		minPriceSlider.setMajorTickUnit(1.0);
		minPriceSlider.setBlockIncrement(1.0);
		minPriceSlider.setMinorTickCount(0);
		minPriceSlider.setOnMouseDragged(e -> returnRestaurant());
		minPriceSlider.setOnMouseClicked(e -> returnRestaurant());
		
		maxPriceSlider.setShowTickMarks(true);
		maxPriceSlider.setShowTickLabels(true);
		maxPriceSlider.setValue(5);
		maxPriceSlider.setMajorTickUnit(1.0);
		maxPriceSlider.setBlockIncrement(1.0);
		maxPriceSlider.setSnapToTicks(true);
		maxPriceSlider.setMinorTickCount(0);
		maxPriceSlider.setOnMouseDragged(e -> returnRestaurant());
		maxPriceSlider.setOnMouseClicked(e -> returnRestaurant());
		maxPriceSlider.setLabelFormatter(new StringConverter<Double>(){
			@Override
			public String toString(Double n){
				if(n < 1) return "";
				return "$" + toString(n-1);
			}

			@Override
			public Double fromString(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		
		reviewsSlider.setShowTickMarks(true);
		reviewsSlider.setShowTickLabels(true);
		reviewsSlider.setMajorTickUnit(100.0);
		reviewsSlider.setBlockIncrement(10.0);
		reviewsSlider.setOnMouseDragged(e -> returnRestaurant());
		reviewsSlider.setOnMouseClicked(e -> returnRestaurant());
		
		pickButton.setOnAction(e -> viewYelp());
		resetButton.setOnAction(e -> reset());
		
		GridPane pane = new GridPane();
		pane.setVgap(10);
		pane.setPadding(new Insets(10, 10, 10, 10));
		pane.add(new Label("Filters:"),  0, 0);
		pane.add(new Label("Categories:"), 0, 1);
		pane.add(categoriesCB,  1, 1);
		
		pane.add(new Label("City:"), 0, 2);
		pane.add(cityCB, 1, 2);
		
		pane.add(new Label("Min Price:"), 0, 3);
		pane.add(minPriceSlider, 1, 3);
		
		pane.add(new Label("Max Price:"), 0, 4);
		pane.add(maxPriceSlider, 1, 4);
		
		pane.add(new Label("Minimum Reviews: "), 0, 5);
		pane.add(reviewsSlider, 1, 5);
		
		pane.add(restaurantsLV, 0, 6, 2, 1);
		pane.add(pickButton, 0, 7);
		pane.add(resetButton, 1, 7);
		
		return new Scene(pane, 1050, 600);
	}
	
//	private Scene createYelpScene()
//	{
//		Browser browser = new Browser();
//		//webEngine.load("www.yelp.com");
//		Scene webScene = new Scene(browser, 1000, 800, Color.web("#666970"));
//		return webScene;
//	}
	
	private void reset()
	{
		categoriesCB.getSelectionModel().select(-1);
		cityCB.getSelectionModel().select(-1);
		minPriceSlider.setValue(1);
		maxPriceSlider.setValue(5);
		reviewsSlider.setValue(1);
		restaurantsLV.getSelectionModel().clearSelection();
		
	}
	
	public void viewYelp()
	{
		Button backButton = new Button("Back");
		Button directionsButton = new Button("Get Directions");
		backButton.setOnAction(e -> back());
		directionsButton.setOnAction(e -> getDirections());
		WebView browser = new WebView();
		WebEngine webEngine = browser.getEngine();
		if(restaurantsLV.getSelectionModel().isEmpty()) 
		{
			selectedRestaurant = returnRestaurant();
			if (selectedRestaurant == null) {
				// Display alert to user (no restaurants found)
				return;
			}
		}
		else
			selectedRestaurant = restaurantsLV.getSelectionModel().getSelectedItem();
		webEngine.load(selectedRestaurant.getSite());
		
		//Scene yelpScene = new Scene(webEngine, 1000, 800, Color.web("#666970"));//createYelpScene();
		GridPane pane = new GridPane();
		browser.setPrefSize(1100, 700);
		pane.add(browser, 0, 1);
		pane.setVgap(10);
//		pane.setHgap(10);
		pane.setPadding(new Insets(10, 10, 10, 10));
		HBox box = new HBox();
		box.getChildren().add(backButton);
		box.getChildren().add(directionsButton);
		box.setSpacing(5);
		pane.add(box, 0, 2);
		//pane.add(directionsButton, 1, 2);
		//pane.setAlignment(Pos.BASELINE_CENTER);
		Scene yelpScene = new Scene(pane, 1100, 750, Color.web("#666960"));
//		mainStage.setTitle("Yelp");
//		mainStage.setScene(yelpScene);
		//mainStage.setTitle("Yelp");
		//mainStage.show();
		//ViewNavigator.setStage(mainStage);
		ViewNavigator.loadScene("Yelp", yelpScene);
	}
	
	public void getDirections()
	{
		//Button directionsButton = new Button("Get Directions");
		Button backButton = new Button("Back");
		backButton.setOnAction(e -> back());
		WebView browser = new WebView();
		WebEngine webEngine = browser.getEngine();
		String[] splitName = selectedRestaurant.getName().split(" ");
		String[] splitCity = selectedRestaurant.getCity().split(" ");
		//String url = "http://www.google.com/search?q=directions+to";
		String url = "https://www.google.com/maps/place/";
		url += splitName[0];
		for(int i = 1; i < splitName.length; i++)
		{
			url += "+" + splitName[i] + "+";
		}
		//url += splitCity[splitCity.length - 1];
		
		for(int i = 0; i < splitCity.length-1; i++)
		{
			url += splitCity[i] + "+";
		}
		url += splitCity[splitCity.length - 1];
		
		webEngine.load(url);
		
		GridPane pane = new GridPane();
		browser.setPrefSize(1100, 700);
		pane.add(browser, 0, 1);
		pane.add(backButton, 0, 2);
		pane.setVgap(10);
//		pane.setHgap(10);
		pane.setPadding(new Insets(10, 10, 10, 10));
		//pane.add(directionsButton, 0, 2);
		//pane.setAlignment(Pos.BASELINE_CENTER);
		Scene directionsScene = new Scene(pane, 1100, 750, Color.web("#666960"));
//		mainStage.setScene(directionsScene);
//		mainStage.setTitle("Directions to " + selectedRestaurant.getName());
//		mainStage.show();
		//ViewNavigator.setStage(mainStage);
		ViewNavigator.loadScene("Directions to " + selectedRestaurant.getName(), directionsScene);
			
	}
	
	private void back()
	{
//		mainStage.setScene(mainScene);
//		mainStage.setTitle("Food Finder");
		ViewNavigator.loadScene("Food Finder", createMainScene());
	}
	
	public void selectRestaurant()
	{
		selectedRestaurant = restaurantsLV.getSelectionModel().getSelectedItem();
		pickButton.setDisable(false);
	}
	
	private Restaurant returnRestaurant()
	{
		restaurantsList = controller.filter((int) minPriceSlider.getValue(), (int) maxPriceSlider.getValue(), (int) reviewsSlider.getValue(), 
				cityCB.getSelectionModel().getSelectedItem(), categoriesCB.getSelectionModel().getSelectedItem());
		restaurantsLV.setItems(restaurantsList);
		
		int length = restaurantsList.size();
		if (length == 0)
			return null;
		Random rng = new Random();
		int randPlace = rng.nextInt(length);
		
		return restaurantsList.get(randPlace);
	}
	
	public static void main(String[] args)
	{
		Application.launch(args);
	}

}


class Browser extends StackPane {
 
	 
    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
     
    public Browser() {
        //apply the styles
        getStyleClass().add("browser");
        // load the web page
        webEngine.load("www.yelp.com");
        //add the web view to the scene
        getChildren().add(browser);
 
    }
    private Node createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }
 
    @Override protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
    }
 
    @Override protected double computePrefWidth(double height) {
        return 750;
    }
 
    @Override protected double computePrefHeight(double width) {
        return 500;
    }
}
