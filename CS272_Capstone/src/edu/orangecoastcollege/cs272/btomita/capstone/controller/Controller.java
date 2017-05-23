package edu.orangecoastcollege.cs272.btomita.capstone.controller;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import edu.orangecoastcollege.cs272.btomita.capstone.model.DBModel;
import edu.orangecoastcollege.cs272.btomita.capstone.model.Restaurant;
import edu.orangecoastcollege.cs272.btomita.capstone.model.User;
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
    
    private static final String USER_TABLE_NAME = "users";
    private static final String[] USER_FIELD_NAMES = {"id", "name", "email", "password"};
    private static final String[] USER_FIELD_TYPES = {"INTEGER PRIMARY KEY", "TEXT", "TEXT", "TEXT"};
    
    private static final String USER_VISITED_TABLE_NAME = "user_visited";
    private static final String[] USER_VISITED_FIELD_NAMES = { "user_id", "restaurant_id"};
    private static final String[] USER_VISITED_FIELD_TYPES = {"INTEGER", "INTEGER"};
    
    private static final String USER_RESTAURANTS_TABLE_NAME = "user_restaurants";
    private static final String[] USER_RESTAURANTS_FIELD_NAMES = {"user_id", "restaurant_id"};
    private static final String[] USER_RESTAURANTS_FIELD_TYPES = {"INTEGER", "INTEGER"};
    
    private static final String USER_LIKED_RESTAURANTS_TABLE_NAME = "user_liked_restaurants";
    private static final String[] USER_LIKED_RESTAURANTS_FIELD_NAMES = {"user_id", "restaurant_id"};
    private static final String[] USER_LIKED_RESTAURANTS_FIELD_TYPES = {"INTEGER", "INTEGER"};
    
    private static final String USER_DISLIKED_RESTAURANTS_TABLE_NAME = "user_disliked_restaurants";
    private static final String[] USER_DISLIKED_RESTAURANTS_FIELD_NAMES = {"user_id", "restaurant_id"};
    private static final String[] USER_DISLIKED_RESTAURANTS_FIELD_TYPES = {"INTEGER", "INTEGER"};
    
    private static final String DATA_FILE = "food1.csv";

    private ObservableList<Restaurant> mAllRestaurantsList;
    private ObservableList<User> mAllUsersList;
    
//    private ObservableList<Restaurant> hbRestaurantsList;
//    private ObservableList<Restaurant> cmRestaurantsList;
//    private ObservableList<Restaurant> fvRestaurantsList;
//    private ObservableList<Restaurant> irvRestaurantsList;
//    private ObservableList<Restaurant> anaRestaurantsList;
    private User mCurrentUser;
    //private int mCurrentUserID;
    private DBModel mDB;
    private DBModel mUsersDB;
    private DBModel mUserVisitedDB;
    private DBModel mUserFavoriteRestaurantsDB;
    private DBModel mUserDislikedRestaurantsDB;


    private Controller(){};

    public static Controller getInstance()
    {
        if(theOne == null)
        {
            theOne = new Controller();
            theOne.mAllRestaurantsList = FXCollections.observableArrayList();
            theOne.mAllUsersList = FXCollections.observableArrayList();

            try
            {
            	theOne.setUsersDB(new DBModel(DB_NAME, USER_TABLE_NAME, USER_FIELD_NAMES, USER_FIELD_TYPES));
            	ArrayList<ArrayList<String>> resultsList = theOne.getUsersDB().getAllRecords();
            	for(ArrayList<String> values : resultsList)
            	{
            		int id = Integer.parseInt(values.get(0));
            		String name = values.get(1);
            		String email = values.get(2);
            		theOne.mAllUsersList.add(new User(id, name, email));
            	}
            	
                theOne.mDB = new DBModel(DB_NAME, TABLE_NAME, FIELD_NAMES, FIELD_TYPES);
                theOne.initializeDBFromFile();
                resultsList = theOne.mDB.getAllRecords();
                for(ArrayList<String> values : resultsList)
                {
                	int id = Integer.parseInt(values.get(0));
                	String name = values.get(1);
            		String price = values.get(2);
            		int reviews = Integer.parseInt(values.get(3));
            		String category = values.get(4);
            		String street = values.get(5);
            		String city = values.get(6);
            		String phone = values.get(7);
            		String site = values.get(8);
            		theOne.mAllRestaurantsList.add(new Restaurant(id, name, price, reviews, category, street, city, phone, site));
                }
                
                theOne.mUserVisitedDB = new DBModel(DB_NAME, USER_VISITED_TABLE_NAME, USER_VISITED_FIELD_NAMES, USER_VISITED_FIELD_TYPES);
                
//                ResultSet rs = theOne.mDB.getAllRecords();
//                if(rs != null)
//                {
//                	while(rs.next())
//                	{
//                		int id = rs.getInt(FIELD_NAMES[0]);
//                		String name = rs.getString(FIELD_NAMES[1]);
//                		String price = rs.getString(FIELD_NAMES[2]);
//                		int reviews = rs.getInt(FIELD_NAMES[3]);
//                		String category = rs.getString(FIELD_NAMES[4]);
//                		String street = rs.getString(FIELD_NAMES[5]);
//                		String city = rs.getString(FIELD_NAMES[6]);
//                		String phone = rs.getString(FIELD_NAMES[7]);
//                		String site = rs.getString(FIELD_NAMES[8]);
//                		
//                		theOne.mAllRestaurantsList.add(new Restaurant(id, name, price, reviews, category, street, city, phone, site));
//                	}
//                }
                theOne.mUserFavoriteRestaurantsDB = new DBModel(DB_NAME, USER_RESTAURANTS_TABLE_NAME, 
                		USER_RESTAURANTS_FIELD_NAMES, USER_RESTAURANTS_FIELD_TYPES);
                theOne.mUserDislikedRestaurantsDB = new DBModel(DB_NAME, USER_DISLIKED_RESTAURANTS_TABLE_NAME, 
                		USER_DISLIKED_RESTAURANTS_FIELD_NAMES, USER_DISLIKED_RESTAURANTS_FIELD_TYPES);

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
    
    public int getCurrentUserID()
    {
    	return this.mCurrentUser.getId();
    }
    
    public User getCurrentUser()
    {
    	return this.mCurrentUser;
    }
    
    public void setUser(User user)
    {
    	this.mCurrentUser = user;
    }
    
    public User signIn(String email, String password)
    {
    	for (User u : theOne.mAllUsersList)
			if (u.getEmail().equalsIgnoreCase(email))
			{
				try 
				{
					ArrayList<ArrayList<String>> resultsList = theOne.getUsersDB().getRecord(String.valueOf(u.getId()));
					String storedPassword = resultsList.get(0).get(3);
					if (password.equals(storedPassword))
					{
						this.mCurrentUser = u;
						return u;
						
					}
						
						
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
    	return null;
    }
    
    public String signInUser(String email, String password) {
		for (User u : theOne.mAllUsersList)
			if (u.getEmail().equalsIgnoreCase(email))
			{
				try 
				{
					ArrayList<ArrayList<String>> resultsList = theOne.getUsersDB().getRecord(String.valueOf(u.getId()));
					String storedPassword = resultsList.get(0).get(3);
					if (password.equals(storedPassword))
					{
						this.mCurrentUser = u;
						return "SUCCESS";
					}
						
						
				} 
				catch (Exception e) {return "Error";}
				return "Incorrect password.  Please try again.";		
			}		
		return "Email address not found.  Please try again.";
	}
    
    public String signUpUser(String name, String email, String password)
    {
    	for(User user : theOne.mAllUsersList)
    		if(user.getEmail().equalsIgnoreCase(email))
    			return "Email already exists";
    	// Add user to database
    	String[] values = {name, email, password};
    	try
    	{
    		int id = theOne.getUsersDB().createRecord(Arrays.copyOfRange
    				(USER_FIELD_NAMES, 1, USER_FIELD_NAMES.length), values);
    		User newUser = new User(id, name, email);
    		theOne.mAllUsersList.add(newUser);
    		theOne.mCurrentUser = newUser;
    		return "SUCCESS";
    	}
    	catch(SQLException e)
    	{
    		return "Account not created.  Please try again.";
    	}
    }
    
    public ObservableList<User> getAllUsers()
    {
    	return theOne.mAllUsersList;
    }
    
    public ObservableList<Restaurant> getRestaurantsForCurrentUser()
    {
    	ObservableList<Restaurant> userRestaurantsList = FXCollections.observableArrayList();
    	if(mCurrentUser != null)
    	{
    		try
    		{
    			ArrayList<ArrayList<String>> resultsList = theOne.mUserVisitedDB.getRecord(String.valueOf(mCurrentUser.getId()));
    			for(ArrayList<String> values : resultsList)
    			{
    				int restaurantId = Integer.parseInt(values.get(1));
    				for(Restaurant r : theOne.mAllRestaurantsList)
    					if(r.getId() == restaurantId) userRestaurantsList.add(r);
    			}
    		}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
    	}
    	return userRestaurantsList;
    }
    
    public ObservableList<String> getDistinctCities()
    {
    	ObservableList<String> cities = FXCollections.observableArrayList();
    	cities.add("");
    	for(Restaurant r : theOne.mAllRestaurantsList)
    		if(!cities.contains(r.getCity())) cities.add(r.getCity());
    	FXCollections.sort(cities);
    	return cities;
    }
    
    public ObservableList<String> getDistinctPrices()
    {
    	ObservableList<String> prices = FXCollections.observableArrayList();
    	prices.add("");
    	for(Restaurant r : theOne.mAllRestaurantsList)
    		if(!prices.contains(r.getPrice())) prices.add(r.getPrice());
    	FXCollections.sort(prices);
    	return prices;
    }
    
//   
    
    public ObservableList<String> getDistinctCategories()
    {
    	ObservableList<String>categories = FXCollections.observableArrayList();
    	categories.add("");
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
    
    public boolean addRestaurantToVisited(Restaurant r)
    {
    	ObservableList<Restaurant> userVisitedList = theOne.getRestaurantsForCurrentUser();
    	if(userVisitedList.contains(r)) return false;
    	String[] values = {String.valueOf(mCurrentUser.getId()), String.valueOf(r.getId())};
    	try
    	{
    		this.mUserVisitedDB.createRecord(USER_VISITED_FIELD_NAMES, values);
    	}
    	catch (SQLException e)
    	{
    		e.printStackTrace();
    		return false;
    	}
    	return true;
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
    		if(((price >= minPrice && price <= maxPrice)) && (r.getReviews() >= reviews) && 
    				(city == null || r.getCity().contains(city)) && 
    				(category == null || r.getCategories().contains(category)))
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
    		theOne.mDB.deleteRecord(String.valueOf(r.getId()));
    	}
    	catch (SQLException e)
    	{
    		return false;
    	}
    	return true;
    }
    
    public ObservableList<Restaurant> getFavoriteRestaurantsForCurrentUser()
    {
        ObservableList<Restaurant> userFavoriteRestaurantsList = FXCollections.observableArrayList();
        if (mCurrentUser != null)
        {
            try {
                ArrayList<ArrayList<String>> resultsList = theOne.mUserFavoriteRestaurantsDB.getRecord(String.valueOf(mCurrentUser.getId()));
                for (ArrayList<String> values : resultsList)
                {
                    int restaurantId = Integer.parseInt(values.get(1));
                    for (Restaurant r : theOne.mAllRestaurantsList)
                        if (r.getId() == restaurantId)
                            userFavoriteRestaurantsList.add(r);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else
        	System.out.println("Current user is set to null");
        return userFavoriteRestaurantsList;
    }
    
    public ObservableList<Restaurant> getVisitedRestaurantsForCurrentUser()
    {
    	ObservableList<Restaurant> userVisitedRestaurantsList = FXCollections.observableArrayList();
        if (mCurrentUser != null)
        {
            try {
                ArrayList<ArrayList<String>> resultsList = theOne.mUserVisitedDB.getRecord(String.valueOf(mCurrentUser.getId()));
                for (ArrayList<String> values : resultsList)
                {
                    int restaurantId = Integer.parseInt(values.get(1));
                    for (Restaurant r : theOne.mAllRestaurantsList)
                        if (r.getId() == restaurantId)
                            userVisitedRestaurantsList.add(r);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else
        	System.out.println("Current user is set to null");
        return userVisitedRestaurantsList;
    }
    
    public boolean addFavoriteRestaurant(Restaurant selectedRestaurant)  {
        ObservableList<Restaurant> userRestaurantsList = theOne.getFavoriteRestaurantsForCurrentUser();
        if (userRestaurantsList.contains(selectedRestaurant))
            return false;
        String userID = String.valueOf(theOne.mCurrentUser.getId());
        if(selectedRestaurant == null) return false;
        String restaurantID = String.valueOf(selectedRestaurant.getId());
        String[] values = {userID, restaurantID};
        //String[] values = {String.valueOf(theOne.mCurrentUser.getId()), String.valueOf(selectedRestaurant.getId())};
        try {
            this.mUserFavoriteRestaurantsDB.createRecord(USER_LIKED_RESTAURANTS_FIELD_NAMES, values);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean removeFavoriteRestaurant(Restaurant selectedRestaurant)
    {
    	ObservableList<Restaurant> userRestaurantsList = theOne.getFavoriteRestaurantsForCurrentUser();
        if (userRestaurantsList.contains(selectedRestaurant))
        {
            try {
				theOne.mUserFavoriteRestaurantsDB.removeRestaurantFromFavorite((String.valueOf(selectedRestaurant.getId())));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return true;
        }
        return false;
    }
    
    public ObservableList<Restaurant> getDislikedRestaurantsForCurrentUser()
    {
        ObservableList<Restaurant> userDislikedRestaurantsList = FXCollections.observableArrayList();
        if (mCurrentUser != null)
        {
            try {
                ArrayList<ArrayList<String>> resultsList = theOne.mUserDislikedRestaurantsDB.getRecord(String.valueOf(mCurrentUser.getId()));
                for (ArrayList<String> values : resultsList)
                {
                    int restaurantId = Integer.parseInt(values.get(1));
                    for (Restaurant r : theOne.mAllRestaurantsList)
                        if (r.getId() == restaurantId)
                            userDislikedRestaurantsList.add(r);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userDislikedRestaurantsList;
    }
    
    public boolean addDislikedRestaurant(Restaurant selectedRestaurant)  {
        ObservableList<Restaurant> userRestaurantsList = theOne.getDislikedRestaurantsForCurrentUser();
        if (userRestaurantsList.contains(selectedRestaurant))
            return false;
        if(selectedRestaurant == null) return false;
        String[] values = {String.valueOf(theOne.mCurrentUser.getId()), String.valueOf(selectedRestaurant.getId())};
        try {
            this.mUserDislikedRestaurantsDB.createRecord(USER_DISLIKED_RESTAURANTS_FIELD_NAMES, values);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean removeDislikedRestaurant(Restaurant selectedRestaurant)
    {
    	ObservableList<Restaurant> userRestaurantsList = theOne.getDislikedRestaurantsForCurrentUser();
        if (userRestaurantsList.contains(selectedRestaurant))
        {
            try {
				theOne.mUserDislikedRestaurantsDB.removeRestaurantFromDisliked(String.valueOf(selectedRestaurant.getId()));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return true;
        }
        return false;
    }

	public DBModel getUsersDB() {
		return mUsersDB;
	}

	public void setUsersDB(DBModel usersDB) {
		mUsersDB = usersDB;
	}
}
