package view;

import java.util.ArrayList;
import java.util.stream.Collectors;

import controller.TransactionController;
import controller.UserController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;
import model.Service;
import model.Transaction;
import model.User;

public class ManagePendingTransaction {
	Stage stage;
    TransactionController trController = new TransactionController();
    UserController userController = new UserController();
    
    Label titleLbl;
    TableView<Transaction> table;
    Button logoutBtn;
    VBox mainLayout;
    ObservableList<Transaction> pendingData;

    public ManagePendingTransaction(Stage stage) {
        this.stage = stage;
    }

    public Scene init() {
        titleLbl = new Label("Pending Transactions Queue");
        titleLbl.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;"); 
        logoutBtn.setMinWidth(100);
        
        logoutBtn.setOnAction(e -> Main.goToLogin(stage));

        table = new TableView<>();
        setupTableColumns();
        loadData(); 
        
        VBox.setVgrow(table, Priority.ALWAYS);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        HBox topBar = new HBox(15, titleLbl, spacer, logoutBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);

        mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(25));
        mainLayout.getChildren().addAll(topBar, new Label("Assign a laundry staff to process these orders."), table);

        return new Scene(mainLayout, 1000, 600);
    }

    private void setupTableColumns() {
    	TableColumn<Transaction, String> custCol = new TableColumn<>("Customer Name");
        custCol.setCellValueFactory(cellData -> {
            int customerId = cellData.getValue().getCustomerId();
            User user = User.getUserById(customerId);
            return new SimpleStringProperty(user != null ? user.getUsername() : "Unknown ID: " + customerId);
        });
        
        TableColumn<Transaction, String> serviceCol = new TableColumn<>("Service Name");
        serviceCol.setCellValueFactory(cellData -> {
            int serviceId = cellData.getValue().getServiceId();
            String serviceName = "Unknown";
            for (Service s : Service.getListService()) {
                if (s.getId().get() == serviceId) {
                    serviceName = s.getName().get();
                    break;
                }
            }
            return new SimpleStringProperty(serviceName);
        });

        TableColumn<Transaction, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Transaction, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Transaction, Double> weightCol = new TableColumn<>("Weight");
        weightCol.setCellValueFactory(new PropertyValueFactory<>("totalWeight"));

        TableColumn<Transaction, Void> assignCol = new TableColumn<>("Assign Staff");
        assignCol.setCellFactory(col -> new TableCell<Transaction, Void>() {
            private final Button assignBtn = new Button("Assign");

            {
                assignBtn.setOnAction(e -> {
                    Transaction selected = getTableView().getItems().get(getIndex());
                    if (selected != null) {
                        showAssignPopup(selected);
                    }
                });
                assignBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(assignBtn);
                }
            }
        });

        table.getColumns().addAll(custCol, serviceCol, dateCol, statusCol, weightCol, assignCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void loadData() {
        ArrayList<Transaction> rawData = trController.getAllTransactions();

        ArrayList<Transaction> pendingOnly = (ArrayList<Transaction>) rawData.stream()
                .filter(t -> "Pending".equalsIgnoreCase(t.getStatus()))
                .collect(Collectors.toList());

        pendingData = FXCollections.observableArrayList(pendingOnly);
        table.setItems(pendingData);
    }

    private void showAssignPopup(Transaction transaction) {
        Stage popup = new Stage();
        popup.initOwner(stage);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Assign Laundry Staff - Transaction ID: " + transaction.getId());

        Label header = new Label("Choose Laundry Staff to assign");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        TableView<User> staffTable = new TableView<>();
        staffTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<User, Number> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()));
        
        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));
        
        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        
        TableColumn<User, String> genderColumn = new TableColumn<>("Gender");
        genderColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGender()));
        
        TableColumn<User, String> dobColumn = new TableColumn<>("DOB");
        dobColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDob().toString()));
        
        TableColumn<User, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRole()));

        staffTable.getColumns().addAll(idColumn, usernameColumn, emailColumn, genderColumn, dobColumn, roleColumn);

        ArrayList<User> staffList = userController.getUserByRole("Laundry Staff");
        ObservableList<User> staffObs = FXCollections.observableArrayList(staffList);
        staffTable.setItems(staffObs);

        Button assignSelectedBtn = new Button("Assign Selected");
        Button cancelBtn = new Button("Cancel");

        assignSelectedBtn.setOnAction(e -> {
        	User selectedStaff = staffTable.getSelectionModel().getSelectedItem();
            if (selectedStaff != null) {
                String error = trController.assignStaffToTransaction(transaction.getId(), selectedStaff.getId());
                if (error.isEmpty()) {
                    popup.close();
                    loadData();
                } else {
                    showAlert(error, AlertType.ERROR);
                }
            } else {
                showAlert("Please select a laundry staff first.", AlertType.ERROR);
            }
        });

        cancelBtn.setOnAction(e -> popup.close());

        HBox btnBox = new HBox(10, assignSelectedBtn, cancelBtn);
        btnBox.setAlignment(Pos.CENTER_RIGHT);

        VBox layout = new VBox(10, header, staffTable, btnBox);
        layout.setPadding(new Insets(15));
        layout.setPrefSize(450, 500);

        Scene scene = new Scene(layout);
        popup.setScene(scene);
        popup.showAndWait();
    }
    
    private void showAlert(String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.show();
    }

    public static Scene getScene(Stage stage) {
        return new ManagePendingTransaction(stage).init();
    }
}
