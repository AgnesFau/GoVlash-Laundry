package view;

import component.ServiceTableComponent;
import controller.ServiceController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import main.Main;
import model.Service;

public class ManageServicePage {
    Stage stage;
    Scene scene;
    
    ServiceController controller = new ServiceController();
    TableView<Service> serviceTable;
    
    Label titleLbl, nameLbl, descLbl, priceLbl, durationLbl;
    TextField nameTxt, descTxt, priceTxt, durationTxt;
    Button addBtn, editBtn, employeePageBtn, logoutBtn;
    
    VBox mainLayout;
    GridPane formLayout;
    
    public ManageServicePage(Stage stage) {
        this.stage = stage;
    }

    private Scene init() {
        titleLbl = new Label("Manage Services");
        titleLbl.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        nameLbl = new Label("Service Name:");
        descLbl = new Label("Description:");       
        priceLbl = new Label("Price (Rp):");
        durationLbl = new Label("Duration (Days):");
        
        nameTxt = new TextField();
        descTxt = new TextField();
        priceTxt = new TextField();
        durationTxt = new TextField();
        
        addBtn = new Button("Add New Service");
        addBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        
        editBtn = new Button("Save Changes");
        editBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");
        editBtn.setVisible(false); 
        employeePageBtn = new Button("Go to Manage Employees");
        employeePageBtn.setMinWidth(180);
        
        logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

        formLayout = new GridPane();
        formLayout.setHgap(15);
        formLayout.setVgap(15);
        formLayout.setPadding(new Insets(20));
        formLayout.setAlignment(Pos.CENTER);
        formLayout.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 20;");
        
        formLayout.add(nameLbl, 0, 0);     formLayout.add(nameTxt, 1, 0);
        formLayout.add(priceLbl, 2, 0);    formLayout.add(priceTxt, 3, 0);
        
        formLayout.add(descLbl, 0, 1);     formLayout.add(descTxt, 1, 1);
        formLayout.add(durationLbl, 2, 1); formLayout.add(durationTxt, 3, 1);
        
        HBox buttonBox = new HBox(10, addBtn, editBtn);
        formLayout.add(buttonBox, 1, 2);

        serviceTable = ServiceTableComponent.create();
        serviceTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.TOP_CENTER);
        
        HBox navBox = new HBox(15, employeePageBtn, logoutBtn);
        navBox.setAlignment(Pos.CENTER_RIGHT);
        
        VBox.setVgrow(serviceTable, Priority.ALWAYS);
        
        mainLayout.getChildren().addAll(navBox, titleLbl, formLayout, serviceTable);
        
        addBehaviour();
        scene = new Scene(mainLayout, 1000, 700);
        
        return scene;
    }
    
    private void setupActionColumn() {
        TableColumn<Service, Void> actionColumn = new TableColumn<>("Action");

        actionColumn.setCellFactory(col -> new TableCell<Service, Void>() {
            private final Button updateBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");

            {
                updateBtn.setOnAction(e -> {
                    Service s = getTableView().getItems().get(getIndex());
                    onUpdate(s); 
                });

                deleteBtn.setOnAction(e -> {
                    Service s = getTableView().getItems().get(getIndex());
                    controller.deleteService(s);
                });
                deleteBtn.setStyle("-fx-text-fill: red;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(5, updateBtn, deleteBtn);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });
        
        serviceTable.getColumns().add(actionColumn);
    }
    
    private void onUpdate(Service service) {
        nameTxt.setText(service.getName().get());
        descTxt.setText(service.getDescription().get());
        priceTxt.setText(String.valueOf(service.getPrice().get()));
        durationTxt.setText(String.valueOf(service.getDuration().get()));

        addBtn.setVisible(false);
        editBtn.setVisible(true);
        editBtn.setOnAction(e -> {
            try {
                controller.updateService(service.getId().get(), 
                        nameTxt.getText(), 
                        descTxt.getText(), 
                        Double.parseDouble(priceTxt.getText()), 
                        Integer.parseInt(durationTxt.getText()),
                        service);
                
                showAlert("Updated successfully!", AlertType.INFORMATION);
                
                clearForm();
                addBtn.setVisible(true);
                editBtn.setVisible(false);
                
            } catch (Exception ex) {
                showAlert(ex.getMessage(), AlertType.ERROR);
            }
            serviceTable.refresh();
        });
    }
    
    private void clearForm() {
        nameTxt.clear();
        descTxt.clear();
        priceTxt.clear();
        durationTxt.clear();
    }
    
    private void addBehaviour() {
        priceTxt.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) priceTxt.setText(oldValue);
        });
        durationTxt.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) durationTxt.setText(newValue.replaceAll("[^\\d]", ""));
        });
        addBtn.setOnAction(e-> {
            if(nameTxt.getText().isEmpty() || priceTxt.getText().isEmpty()) {
                showAlert("Please fill all fields", AlertType.ERROR);
                return;
            }
            
            String error = controller.addService(nameTxt.getText(), descTxt.getText(), 
                    Double.parseDouble(priceTxt.getText()), Integer.parseInt(durationTxt.getText()));
            
            if(error != null) {
                showAlert(error, AlertType.ERROR);
            } else {
                showAlert("Successfully add a new service", AlertType.INFORMATION);
                clearForm();
            }
        });
        employeePageBtn.setOnAction(e -> Main.goToManageEmployee(stage));
        logoutBtn.setOnAction(e -> Main.goToLogin(stage));
    }
    
    public static Scene getScene(Stage stage) {
        return new ManageServicePage(stage).init();
    }

    private void showAlert(String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.show();
    }
}