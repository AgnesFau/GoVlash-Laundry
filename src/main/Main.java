package main;

import java.sql.SQLException;

import controller.UserController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Service;
import model.User;
import view.LoginPage;
import view.ManageEmployeePage;
import view.ManageServicePage;
import view.RegisterPage;

public class Main extends Application{
	
	public static User currentUser;
	
	UserController user = new UserController();
	public static Scene loginScene;
    public static Scene registerScene;
    public static Scene manageServiceScene;
    public static Scene manageEmployeeScene;
	
	public static void goToLogin(Stage stage) {
        stage.setScene(loginScene);
    }

    public static void goToRegister(Stage stage) {
        stage.setScene(registerScene);
    }
    
    public static void goToManageService(Stage stage) {
    	stage.setScene(manageServiceScene);
    }
	
	public static void main(String[] args) {
		try {
			User.loadUsersFromDB();
			Service.loadServicesFromDB();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
        loginScene = LoginPage.getScene(stage);
        registerScene = RegisterPage.getScene(stage);
        manageServiceScene = ManageServicePage.getScene(stage);
        manageEmployeeScene = ManageEmployeePage.getScene(stage);

        stage.setScene(manageEmployeeScene);
        stage.show();
	}

}
