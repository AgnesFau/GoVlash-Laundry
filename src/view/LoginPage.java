package view;

import controller.UserController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import main.Main;
import model.User;

public class LoginPage {
	Scene scene;
	Stage stage;
	UserController userController;

	Label titleLbl, emailLbl, passwordLbl;
	TextField emailTxt;
	PasswordField passwordTxt;
	Button submitBtn, registerBtn;
	
	VBox mainLayout; 
	GridPane formLayout;
	
	private Scene init() {
		titleLbl = new Label("GoVlash Login");
		titleLbl.setFont(Font.font("Arial", FontWeight.BOLD, 24));

		emailLbl = new Label("Email:");
		passwordLbl = new Label("Password:");
		
		emailTxt = new TextField();
		emailTxt.setPromptText("example@email.com");
		
		passwordTxt = new PasswordField();
		passwordTxt.setPromptText("Enter your password");

		submitBtn = new Button("Login");
		submitBtn.setMinWidth(100);
		submitBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

		registerBtn = new Button("Don't have an account? Register here");
		registerBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: blue; -fx-underline: true;");

		formLayout = new GridPane();
		formLayout.setAlignment(Pos.CENTER);
		formLayout.setHgap(10);
		formLayout.setVgap(10);
		formLayout.setPadding(new Insets(20));

		formLayout.add(emailLbl, 0, 0); 
		formLayout.add(emailTxt, 1, 0);
		formLayout.add(passwordLbl, 0, 1);
		formLayout.add(passwordTxt, 1, 1);
		formLayout.add(submitBtn, 1, 2);

		mainLayout = new VBox(20);
		mainLayout.setAlignment(Pos.CENTER);
		mainLayout.setPadding(new Insets(30));
		mainLayout.getChildren().addAll(titleLbl, formLayout, registerBtn);

		addBehaviour();
		scene = new Scene(mainLayout, 800, 600); 
		return scene;
	}
	
	private void addBehaviour() {
		submitBtn.setOnAction(e -> {
			User user = userController.login(emailTxt.getText(), passwordTxt.getText());
			
			if(user == null) {
				showAlert("Invalid Credential");
			}
			else {
				Main.currentUser = user; 
				
				if(user.getRole().equals("Customer")) {
					CustomerHomePage homePage = new CustomerHomePage(stage, user);
					stage.setScene(homePage.init());
				}
				else if(user.getRole().equals("Admin")) {
					Main.goToManageService(stage);
				}
			}
		});
		
		registerBtn.setOnAction(e -> {
			Main.goToRegister(stage);
		});
	}
	
	private void showAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setContentText(message);
		alert.show();
	}
	
	public static Scene getScene(Stage stage) {
		return new LoginPage(stage).init();
	}

	public LoginPage(Stage stage) {
		this.stage = stage;
		userController = new UserController();
	}
}