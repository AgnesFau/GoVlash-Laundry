package view;

import java.time.LocalDate;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Employee;
import model.Service;

public class ManageEmployeePage {
	Stage stage;
	Scene scene;
	
	Label usernameLbl, emailLbl, passwordLbl, genderLbl, dobLbl, confirmLbl;
	TextField usernameTxt, emailTxt;
	PasswordField passwordTxt, confirmTxt;
	RadioButton femaleRb, maleRb;
	DatePicker dobPck;
	ToggleGroup genderTg;
	
	Button addBtn;
	
	TableView<Employee> employeeTable;
	
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
		
		addBtn = new Button("Hire Employee");
		femaleRb.setToggleGroup(genderTg);
		maleRb.setToggleGroup(genderTg);
		
		initTable();
		
		container = new VBox();
		container.getChildren().addAll(usernameLbl, usernameTxt, passwordLbl, passwordTxt, confirmLbl, confirmTxt, emailLbl, emailTxt, genderLbl, femaleRb, maleRb, dobLbl, dobPck, addBtn, employeeTable);
		
		
		scene = new Scene(container, 1000, 500);
		
		return scene;
	}
	
	private void initTable() {
		employeeTable = new TableView<Employee>();
		
        TableColumn<Employee, Number> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()));
        TableColumn<Employee, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()) );
        TableColumn<Employee, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        TableColumn<Employee, String> genderColumn = new TableColumn<>("Gender");
        genderColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGender()));
        TableColumn<Employee, String> dobColumn = new TableColumn<>("Gender");
        genderColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDob().toString()));
        TableColumn<Employee, String> roleColumn = new TableColumn<>("Role");
        genderColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRole()));

        employeeTable.getColumns().addAll(idColumn, usernameColumn, emailColumn, genderColumn, dobColumn, roleColumn);
		employeeTable.setItems(Employee.getListEmployee());
	}
	
	public static Scene getScene(Stage stage) {
	    return new ManageEmployeePage(stage).init();
	}

	private void showAlert(String message, AlertType type) {
	    Alert alert = new Alert(type);
	    alert.setTitle("Error");
	    alert.setContentText(message);
	    alert.show();
	}
	
	public ManageEmployeePage(Stage stage) {
		this.stage = stage;
	}
}
