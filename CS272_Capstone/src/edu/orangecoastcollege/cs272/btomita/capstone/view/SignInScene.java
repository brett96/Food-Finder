package edu.orangecoastcollege.cs272.btomita.capstone.view;

import edu.orangecoastcollege.cs272.btomita.capstone.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SignInScene {

	private static Controller controller = Controller.getInstance();

	@FXML
	private TextField emailAddressTF;
	@FXML
	private TextField passwordTF;
	@FXML
	private Label emailErrorLabel;
	@FXML
	private Label passwordErrorLabel;
	@FXML
	private Label signInErrorLabel;

	
	@FXML
	public Object signIn() {
		String email = emailAddressTF.getText();
		String password = passwordTF.getText();

		emailErrorLabel.setVisible(email.isEmpty());
		passwordErrorLabel.setVisible(password.isEmpty());

		if (emailErrorLabel.isVisible() || passwordErrorLabel.isVisible())
			return null;

		String result = controller.signInUser(email, password);
		if (result.equalsIgnoreCase("SUCCESS")) {
			signInErrorLabel.setVisible(false);
			// Go to next scene
			ViewNavigator.loadScene("Food Finder", new ViewFX().createMainScene());
			return this;
		}
		signInErrorLabel.setText(result);
		signInErrorLabel.setVisible(true);
		return null;
	}
	
	@FXML
	public Object loadSignUp()
	{
		ViewNavigator.loadFXMLScene("Sign Up", ViewNavigator.SIGN_UP_SCENE);
		return this;
	}

}
