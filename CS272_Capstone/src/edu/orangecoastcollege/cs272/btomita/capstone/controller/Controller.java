package edu.orangecoastcollege.cs272.btomita.capstone.controller;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

import edu.orangecoastcollege.cs272.btomita.capstone.model.DBModel;
import edu.orangecoastcollege.cs272.btomita.capstone.model.Restaurant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

public final class Controller
{
	private static Controller theOne;

    private static final String DB_NAME = "cs272.db";
    private static final String TABLE_NAME = "oc";
    private static final String[] FIELD_NAMES = { "id", "name", "price", "reviews", "category", "street", "city", "phone", "site" };
    private static final String [] FIELD_TYPES = { "INTEGER PRIMARY KEY", "TEXT", "TEXT", "INTEGER", "TEXT", "TEXT", "TEXT", "TEXT", "TEXT" };
    private static final String DATA_FILE = "food1.csv";

    private ObservableList<Restaurant> mAllRestaurantsList;
//    private ObservableList<Restaurant> hbRestaurantsList;
//    private ObservableList<Restaurant> cmRestaurantsList;
//    private ObservableList<Restaurant> fvRestaurantsList;
//    private ObservableList<Restaurant> irvRestaurantsList;
//    private ObservableList<Restaurant> anaRestaurantsList;
    private DBModel mDB;

    private Controller(){};

    public static Controller getInstance()
    {
    	theOne = null;
        if(theOne == null)
        {
            theOne = new Controller();
            theOne.mAllRestaurantsList = FXCollections.observableArrayList();

            try
            {
                theOne.mDB = new DBModel(DB_NAME, TABLE_NAME, FIELD_NAMES, FIELD_TYPES);
                theOne.initializeDBFromFile();
                
                ResultSet rs = theOne.mDB.getAllRecords();
                if(rs != null)
                {
                	while(rs.next())
                	{
                		int id = rs.getInt(FIELD_NAMES[0]);
                		String name = rs.getString(FIELD_NAMES[1]);
                		String price = rs.getString(FIELD_NAMES[2]);
                		int reviews = rs.getInt(FIELD_NAMES[3]);
                		String category = rs.getString(FIELD_NAMES[4]);
                		String street = rs.getString(FIELD_NAMES[5]);
                		String city = rs.getString(FIELD_NAMES[6]);
                		String phone = rs.getString(FIELD_NAMES[7]);
                		String site = rs.getString(FIELD_NAMES[8]);
                		
                		theOne.mAllRestaurantsList.add(new Restaurant(id, name, price, reviews, category, street, city, phone, site));
                	}
                }

            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return theOne;
    }
    
    public ObservableList<Restaurant> getAllRestaurants()
    {
    	return theOne.mAllRestaurantsList;
    }
    
    public ObservableList<String> getDistinctCities()
    {
    	ObservableList<String> cities = FXCollections.observableArrayList();
    	//cities.add(" ");
    	for(Restaurant r : theOne.mAllRestaurantsList)
    		if(!cities.contains(r.getCity())) cities.add(r.getCity());
    	FXCollections.sort(cities);
    	return cities;
    }
    
    public ObservableList<String> getDistinctPrices()
    {
    	ObservableList<String> prices = FXCollections.observableArrayList();
    	prices.add(" ");
    	for(Restaurant r : theOne.mAllRestaurantsList)
    		if(!prices.contains(r.getPrice())) prices.add(r.getPrice());
    	FXCollections.sort(prices);
    	return prices;
    }
    
//   
    
    public ObservableList<String> getDistinctCategories()
    {
    	ObservableList<String>categories = FXCollections.observableArrayList();
    	categories.add(" ");
    	for(Restaurant r : theOne.mAllRestaurantsList)
    	{
//    		String[] categoriesList = r.getCategories().split("-");
//    		if(!categories.contains(categoriesList[0])) categories.add(categoriesList[0]);
    		
    		String category = r.getCategories();
    		if(category.contains(","))
    			category = category.substring(0, category.indexOf(","));
    		if(!categories.contains(category)) categories.add(category);
//    		for(int i = 0; i < categoriesList.length; i++)
//    		{
//    			if(!categories.contains(categoriesList[i])) categories.add(categoriesList[i]);
//    		}
    	}
    	FXCollections.sort(categories);
    	return categories;
    }
    
    public int convertPriceToInt(String price)
    {
//    	if(price.equals("$")) return 1;
//    	if(price.equals("$$")) return 2;
//    	if(price.equals("$$$")) return 3;
//    	if(price.equals("$$$$")) return 4;
//    	if(price.equals("$$$$$")) return 5;
//    	return 0;
    	return price.length();
    }
    
    public ObservableList<Restaurant> filter(int minPrice, int maxPrice, int reviews, String city, String category)
    {
    	ObservableList<Restaurant> filteredRestaurantsList = FXCollections.observableArrayList();
    	for(Restaurant r : theOne.mAllRestaurantsList)
    	{
    		int price = convertPriceToInt(r.getPrice());
//    		int lowPrice = convertPriceToInt(minPrice);
//    		int highPrice = convertPriceToInt(maxPrice);
    		if(((price >= minPrice && price <= maxPrice)) && (reviews <= r.getReviews()) && (city == null || r.getCity().equals(city)) && (category == null || r.getCategories().contains(category)))
    		{
    			filteredRestaurantsList.add(r);
    		}
    	}
    	return filteredRestaurantsList;
    }

    private int initializeDBFromFile() throws SQLException	//Loads all restaurants in the database
    {
        int recordsCreated = 0;

        if(theOne.mDB.getRecordCount() > 0)
            return recordsCreated;

        try
        {
            Scanner fileScanner = new Scanner(new File(DATA_FILE));
            fileScanner.nextLine();

            while(fileScanner.hasNextLine())
            {
            	String food = fileScanner.nextLine();
            	if(food.isEmpty()) continue;
                //String[] data = fileScanner.nextLine().split(",");
            	String[] data = food.split(",");
                String[] values = new String[FIELD_NAMES.length - 1];
                // "name", "price", "reviews", "category", "street", "city", "phone", "site"
//                values[0] = data[5].replaceAll("'", " ''");
//                values[1] = data[8];
//                values[2] = data[7];
//                values[3] = data[9].replaceAll("-", ",");
//                values[4] = data[13];
//                values[5] = data[14].replaceAll("\"", "");
//                values[6] = data[15];
//                values[7] = data[6];
                
                values[0] = data[0].replaceAll("'", " ''");
                values[1] = data[3];
                values[2] = data[2];
                values[3] = data[4].replaceAll("-", ",");
                values[4] = data[5].replaceAll("''", "");
                values[5] = data[6].replaceAll("''", "");
                values[6] = data[7];
                values[7] = data[1];
                
                theOne.mDB.createRecord(Arrays.copyOfRange(FIELD_NAMES, 1, FIELD_NAMES.length), values);
                recordsCreated++;
            }
            fileScanner.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return recordsCreated;
    }
    
    public boolean addRestaurant(String name, String price, int reviews, String category, String street, String city, String phone, String site)
    {
    	String[] values = {name, price, String.valueOf(reviews), category, street, city, phone};
    	
    	try
    	{
    		int id = theOne.mDB.createRecord(Arrays.copyOfRange(FIELD_NAMES, 1, FIELD_NAMES.length), values);
    		theOne.mAllRestaurantsList.add(new Restaurant(id, name, price, reviews, category, street, city, phone, site));
    	}
    	catch (SQLException e)
    	{
    		return false;
    	}
    	return true;
    }
    
    public boolean deleteRestaurant(Restaurant r)
    {
    	if(r == null)
    	{
    		return false;
    	}
    	
    	theOne.mAllRestaurantsList.remove(r);
    	
    	try
    	{
    		theOne.mDB.deleteRecord(String.valueOf(r.getID()));
    	}
    	catch (SQLException e)
    	{
    		return false;
    	}
    	return true;
    }
}
