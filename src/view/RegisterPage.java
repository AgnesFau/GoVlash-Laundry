package view;

import controller.UserController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import main.Main;

public class RegisterPage {
	Stage stage;
	Scene scene;
	
	Label titleLbl, usernameLbl, emailLbl, passwordLbl, confirmLbl, genderLbl, dobLbl;
	TextField usernameTxt, emailTxt;
	PasswordField passwordTxt, confirmTxt;
	RadioButton femaleRb, maleRb;
	DatePicker dobPck;
	ToggleGroup genderTg;
	Button submitBtn, loginBtn;
	
	GridPane formLayout;
	VBox mainLayout;
	
	UserController userController = new UserController();
	
	private Scene init() {
		titleLbl = new Label("Register New Account");
		titleLbl.setFont(Font.font("Arial", FontWeight.BOLD, 24));

		usernameLbl = new Label("Username:");
		emailLbl = new Label("Email:");
		passwordLbl = new Label("Password:");
		confirmLbl = new Label("Confirm Pass:");
		genderLbl = new Label("Gender:");
		dobLbl = new Label("Date of Birth:");
		
		usernameTxt = new TextField();
		emailTxt = new TextField();
		emailTxt.setPromptText("ends with @email.com");
		
		passwordTxt = new PasswordField();
		confirmTxt = new PasswordField();
		
		genderTg = new ToggleGroup();
		maleRb = new RadioButton("Male");
		femaleRb = new RadioButton("Female");
		maleRb.setToggleGroup(genderTg);
		femaleRb.setToggleGroup(genderTg);
		
		HBox genderBox = new HBox(15);
		genderBox.getChildren().addAll(maleRb, femaleRb);

		dobPck = new DatePicker();
		dobPck.setPromptText("Select date");
		
		submitBtn = new Button("Register Now");
		submitBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
		submitBtn.setMinWidth(150);

		loginBtn = new Button("Already have an account? Login");
		loginBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: blue; -fx-underline: true;");

		formLayout = new GridPane();
		formLayout.setAlignment(Pos.CENTER);
		formLayout.setHgap(10);
		formLayout.setVgap(15);
		formLayout.setPadding(new Insets(20));

		formLayout.add(usernameLbl, 0, 0);
		formLayout.add(usernameTxt, 1, 0);
		
		formLayout.add(emailLbl, 0, 1);
		formLayout.add(emailTxt, 1, 1);
		
		formLayout.add(passwordLbl, 0, 2);
		formLayout.add(passwordTxt, 1, 2);
		
		formLayout.add(confirmLbl, 0, 3);
		formLayout.add(confirmTxt, 1, 3);
		
		formLayout.add(genderLbl, 0, 4);
		formLayout.add(genderBox, 1, 4);
		
		formLayout.add(dobLbl, 0, 5);
		formLayout.add(dobPck, 1, 5);
		
		formLayout.add(submitBtn, 1, 6);

		mainLayout = new VBox(20);
		mainLayout.setAlignment(Pos.CENTER);
		mainLayout.getChildren().addAll(titleLbl, formLayout, loginBtn);
		
		addBehaviour();
		scene = new Scene(mainLayout, 800, 600);
		return scene;
	}
	
	private void addBehaviour() {
		submitBtn.setOnAction(e -> {
			RadioButton selectedGender = (RadioButton) genderTg.getSelectedToggle();
			String gender = selectedGender == null ? null : selectedGender.getText();
			String error = userController.validateAddCustomer(
					usernameTxt.getText(),
					emailTxt.getText(),
					passwordTxt.getText(),
					confirmTxt.getText(),
					gender,
					dobPck.getValue()
			);

			if (error != null) {
				showAlert(error); 
			}
			else {
				userController.addUser(
						usernameTxt.getText(),
						emailTxt.getText(),
						passwordTxt.getText(),
						confirmTxt.getText(),
						gender,
						dobPck.getValue(),
						"Customer"
				);
				Alert success = new Alert(Alert.AlertType.INFORMATION);
				success.setTitle("Success");
				success.setContentText("Registration successful! Please login.");
				success.showAndWait();

				Main.goToLogin(stage);
			}
		});

		loginBtn.setOnAction(e -> {
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