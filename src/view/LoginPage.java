package view;

import controller.UserController;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.Main;
import model.User;

public class LoginPage {
	Scene scene;
	
	Label emailLbl, passwordLbl;
	TextField emailTxt;
	PasswordField passwordTxt;
	Button submitBtn, registerBtn;
	VBox container;
	
	Stage stage;
	
	UserController userController;
	
	private Scene init() {
		emailLbl = new Label("Email");
		passwordLbl = new Label("Password");
		
		emailTxt = new TextField();
		passwordTxt = new PasswordField();
		
		submitBtn = new Button("Submit");
		registerBtn = new Button("Register here!");
		
		container = new VBox();
		container.getChildren().addAll(emailLbl, emailTxt, passwordLbl, passwordTxt, submitBtn, registerBtn);

		addBehaviour();
		scene = new Scene(container, 1000, 500);
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
				if(Main.currentUser.getRole().equals("Admin")) {
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
