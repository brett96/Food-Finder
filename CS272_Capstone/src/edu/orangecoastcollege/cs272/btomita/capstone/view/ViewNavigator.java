package edu.orangecoastcollege.cs272.btomita.capstone.view;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewNavigator {
	public static final String SIGN_UP_SCENE = "SignUpScene.fxml";
	public static final String SIGN_IN_SCENE = "SignInScene.fxml";
	public static final String RESTAURANT_LIST_SCENE = "RestaurantsListScene.fxml";
	public static final String USER_VISITED_SCENE = "ViewVisitedScene.fxml";


	public static Stage mainStage;

	public static void setStage(Stage stage) {
		mainStage = stage;
	}
	
	public static void loadScene(String title, Scene scene)
	{
		mainStage.setTitle(title);
		mainStage.setScene(scene);
		mainStage.show();
	}

	public static void loadFXMLScene(String title, String sceneFXML) {

		try {
			mainStage.setTitle(title);
			Scene scene = new Scene(FXMLLoader.load(ViewNavigator.class.getResource(sceneFXML)));
			mainStage.setScene(scene);
			mainStage.show();
		} catch (IOException e) {
			System.err.println("Error loading: " + sceneFXML + "\n" + e.getMessage());
			e.printStackTrace();
		}
	}

}
