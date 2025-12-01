package main;

import java.sql.SQLException;

import controller.UserController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Service;
import model.User;
import view.LaundryStaffPage;
import view.LoginPage;
import view.ManageEmployeePage;
import view.ManagePendingTransaction;
import view.ManageServicePage;
import view.RegisterPage;
import view.ViewTransactionPage;

public class Main extends Application {
	
	public static User currentUser;
	
	UserController user = new UserController();
	public static Scene loginScene;
	public static Scene registerScene;
    public static Scene manageServiceScene;
    public static Scene manageEmployeeScene;
    public static Scene viewTransactionScene; 
    public static Scene managePendingTransaction;
    
	public static void goToLogin(Stage stage) {
		stage.setScene(loginScene);
	}
	public static void goToRegister(Stage stage) {
		stage.setScene(registerScene);
	}
    
    public static void goToManageService(Stage stage) {
    	stage.setScene(manageServiceScene);
    }
    
    public static void goToManageEmployee(Stage stage) {
        stage.setScene(manageEmployeeScene);
    }
    public static void goToViewTransactions(Stage stage) {
        stage.setScene(ViewTransactionPage.getScene(stage));
    }
    public static void goToLaundryStaffPage(Stage stage) {
    	stage.setScene(LaundryStaffPage.getScene(stage, currentUser.getId()));
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
	public static void goToManagePendingTransaction(Stage stage) {
        stage.setScene(ManagePendingTransaction.getScene(stage));
    }

	@Override
	public void start(Stage stage) throws Exception {
		loginScene = LoginPage.getScene(stage);
		registerScene = RegisterPage.getScene(stage);
        manageServiceScene = ManageServicePage.getScene(stage);
        manageEmployeeScene = ManageEmployeePage.getScene(stage);
        viewTransactionScene = ViewTransactionPage.getScene(stage); 
        managePendingTransaction = ManagePendingTransaction.getScene(stage);
        
		stage.setScene(loginScene);
		stage.show();
	}
}