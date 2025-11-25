package view;

import java.time.LocalDate;

import controller.UserController;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.Main;
import model.User;

public class RegisterPage {
	Stage stage;
	Scene scene;
	
	Label usernameLbl, emailLbl, passwordLbl, genderLbl, dobLbl, confirmLbl;
	TextField usernameTxt, emailTxt;
	PasswordField passwordTxt, confirmTxt;
	RadioButton femaleRb, maleRb;
	DatePicker dobPck;
	ToggleGroup genderTg;
	
	Button submitBtn, loginBtn;
	
	VBox container;
	
	private Scene init() {
		usernameLbl = new Label("Username");
		emailLbl = new Label("Email");
		passwordLbl = new Label("Password");
		confirmLbl = new Label("Confirm Password");
		genderLbl = new Label("Gender");
		dobLbl = new Label("Date of birth");
		
		usernameTxt = new TextField();
		emailTxt = new TextField();
		passwordTxt = new PasswordField();
		confirmTxt = new PasswordField();
		genderTg = new ToggleGroup();
		maleRb = new RadioButton("Male");
		femaleRb = new RadioButton("Female");
		dobPck = new DatePicker();
		
		submitBtn = new Button("Submit");
		loginBtn = new Button("Login here");
		
		container = new VBox();
		
		femaleRb.setToggleGroup(genderTg);
		maleRb.setToggleGroup(genderTg);
		
		container = new VBox();
		container.getChildren().addAll(usernameLbl, usernameTxt, passwordLbl, passwordTxt, confirmLbl, confirmTxt, emailLbl, emailTxt, genderLbl, femaleRb, maleRb, dobLbl, dobPck, submitBtn, loginBtn);
		addBehaviour();
		
		scene = new Scene(container, 1000, 800);
		
		return scene;
	}
	
	private void addBehaviour() {
	    submitBtn.setOnAction(e -> {

	        RadioButton selectedGender = (RadioButton) genderTg.getSelectedToggle();
	        String gender = selectedGender == null ? null : selectedGender.getText();

	        UserController controller = new UserController();

	        String error = controller.addUser(
	                usernameTxt.getText(),
	                emailTxt.getText(),
	                passwordTxt.getText(),
	                confirmTxt.getText(),
	                gender,
	                dobPck.getValue(),
	                "Customer"
	        );

	        if (error != null) {
	            showAlert(error); 
	        }
	        else {
	            Alert success = new Alert(Alert.AlertType.INFORMATION);
	            success.setTitle("Success");
	            success.setContentText("Registration successful! Please login.");
	            success.showAndWait();

	            return;
	        }
	    });

	    loginBtn.setOnAction(e -> {
	    	System.out.println("Go to login page");
	        Main.goToLogin(stage);
	    });
	}
	
	public static Scene getScene(Stage stage) {
	    return new RegisterPage(stage).init();
	}

	
	private void showAlert(String message) {
	    Alert alert = new Alert(Alert.AlertType.ERROR);
	    alert.setTitle("Error");
	    alert.setContentText(message);
	    alert.show();
	}
	
	public RegisterPage(Stage stage) {
		this.stage = stage;
	}
}
