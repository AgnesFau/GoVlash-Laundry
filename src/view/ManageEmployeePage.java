package view;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

import controller.UserController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import main.Main;
import model.Employee;

public class ManageEmployeePage {
    Stage stage;
    Scene scene;
    
    // Components
    Label titleLbl, usernameLbl, emailLbl, passwordLbl, confirmLbl, genderLbl, dobLbl, roleLbl;
    TextField usernameTxt, emailTxt;
    PasswordField passwordTxt, confirmTxt;
    RadioButton femaleRb, maleRb;
    DatePicker dobPck;
    ToggleGroup genderTg;
    ComboBox<String> roleCb;
    Button addBtn, backBtn;
    TableView<Employee> employeeTable;
    
    // Layouts
    VBox mainLayout;
    GridPane formLayout;
    
    public ManageEmployeePage(Stage stage) {
        this.stage = stage;
    }

    private Scene init() {
        titleLbl = new Label("Manage Employees");
        titleLbl.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        usernameLbl = new Label("Username:");
        emailLbl = new Label("Email:");
        passwordLbl = new Label("Password:");
        confirmLbl = new Label("Confirm Password:");
        genderLbl = new Label("Gender:");
        dobLbl = new Label("Date of Birth:");
        roleLbl = new Label("Role:");
        
        usernameTxt = new TextField();
        emailTxt = new TextField();
        passwordTxt = new PasswordField();
        confirmTxt = new PasswordField();
        
        genderTg = new ToggleGroup();
        maleRb = new RadioButton("Male");
        femaleRb = new RadioButton("Female");
        maleRb.setToggleGroup(genderTg);
        femaleRb.setToggleGroup(genderTg);
        HBox genderBox = new HBox(15, maleRb, femaleRb);
        
        dobPck = new DatePicker();
        
        roleCb = new ComboBox<>(FXCollections.observableArrayList("Laundry Staff", "Admin", "Receptionist"));
        roleCb.setPromptText("Select Role");
        
        addBtn = new Button("Hire Employee");
        addBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addBtn.setMinWidth(150);
        
        backBtn = new Button("Back / Logout");
        formLayout = new GridPane();
        formLayout.setHgap(15);
        formLayout.setVgap(15);
        formLayout.setPadding(new Insets(20));
        formLayout.setAlignment(Pos.CENTER);
        formLayout.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 20;");
        formLayout.add(usernameLbl, 0, 0); formLayout.add(usernameTxt, 1, 0);
        formLayout.add(emailLbl, 2, 0);    formLayout.add(emailTxt, 3, 0);
        
        formLayout.add(passwordLbl, 0, 1); formLayout.add(passwordTxt, 1, 1);
        formLayout.add(confirmLbl, 2, 1);  formLayout.add(confirmTxt, 3, 1);
        
        formLayout.add(genderLbl, 0, 2);   formLayout.add(genderBox, 1, 2);
        formLayout.add(dobLbl, 2, 2);      formLayout.add(dobPck, 3, 2);
        
        formLayout.add(roleLbl, 0, 3);     formLayout.add(roleCb, 1, 3);
        formLayout.add(addBtn, 3, 3);      

        initTable();
        
        mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(employeeTable, Priority.ALWAYS);
        
        mainLayout.getChildren().addAll(titleLbl, formLayout, employeeTable, backBtn);
        
        addBehaviour();
        
        scene = new Scene(mainLayout, 1000, 700);
        return scene;
    }
    
    private void addBehaviour() {
        addBtn.setOnAction(e -> {
            RadioButton selectedGender = (RadioButton) genderTg.getSelectedToggle();
            String gender = selectedGender == null ? null : selectedGender.getText();

            UserController controller = new UserController();
            LocalDate dob = (dobPck.getValue() != null) ? dobPck.getValue() : null;

            String error = controller.addUser(
                    usernameTxt.getText(),
                    emailTxt.getText(),
                    passwordTxt.getText(),
                    confirmTxt.getText(),
                    gender,
                    dob,
                    roleCb.getValue()
            );
            
            if(error != null) {
                showAlert(error, AlertType.ERROR);
            } else {
                showAlert("Employee hired successfully!", AlertType.INFORMATION);
            }
        });
        
        backBtn.setOnAction(e -> {
            Main.goToManageService(stage);
        });
    }
    
    private void initTable() {
        employeeTable = new TableView<>();
        
        TableColumn<Employee, Number> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()));
        
        TableColumn<Employee, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));
        
        TableColumn<Employee, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        
        TableColumn<Employee, String> genderColumn = new TableColumn<>("Gender");
        genderColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGender()));
        
        TableColumn<Employee, String> dobColumn = new TableColumn<>("DOB");
        dobColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDob().toString()));
        
        TableColumn<Employee, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRole()));

        employeeTable.getColumns().addAll(idColumn, usernameColumn, emailColumn, genderColumn, dobColumn, roleColumn);
        
        employeeTable.setItems(Employee.getListEmployee());
        employeeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); 
    }
    
    public static Scene getScene(Stage stage) {
        return new ManageEmployeePage(stage).init();
    }

    private void showAlert(String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.show();
    }
}