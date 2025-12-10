package view;

import java.util.ArrayList;

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
import javafx.stage.Stage;
import main.Main;
import model.Service;
import model.Transaction;
import model.User;

public class LaundryStaffPage {

	Stage stage;
    int staffId;

    TransactionController trController = new TransactionController();

    Label titleLbl;
    Button logoutBtn;
    TableView<Transaction> table;
    VBox mainLayout;
    ObservableList<Transaction> assignedData;
    
    // constructor
    public LaundryStaffPage(Stage stage, int staffId) {
        this.stage = stage;
        this.staffId = staffId;
    }

    // inisiasi tampilan
    public Scene init() {
        titleLbl = new Label("My Assigned Tasks");
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
        mainLayout.getChildren().addAll(topBar, new Label("List of laundry orders assigned to you:"), table);

        return new Scene(mainLayout, 1000, 600);
    }

    // table column untuk menampilkan data
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

        TableColumn<Transaction, Void> finishCol = new TableColumn<>("Action");
        finishCol.setCellFactory(col -> new TableCell<Transaction, Void>() {
            private final Button finishBtn = new Button("Mark as Finished");

            {
                finishBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                finishBtn.setOnAction(e -> {
                    Transaction selected = getTableView().getItems().get(getIndex());
                    handleMarkAsFinished(selected);
                    finishBtn.setDisable(true);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    return;
                }

                Transaction transaction = getTableView().getItems().get(getIndex());

                if (transaction.getStatus().equalsIgnoreCase("Finished")) {
                    finishBtn.setDisable(true);
                    finishBtn.setStyle("-fx-background-color: grey; -fx-text-fill: white;");
                } else {
                    finishBtn.setDisable(false);
                    finishBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                }

                setGraphic(finishBtn);
            }

        });

        table.getColumns().addAll(custCol, serviceCol, dateCol, statusCol, weightCol, finishCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    // ambil data dari database
    private void loadData() {
        ArrayList<Transaction> rawData = trController.getAssignedOrdersByLaundryStaffID(staffId);
        assignedData = FXCollections.observableArrayList(rawData);
        table.setItems(assignedData);
    }

    // update status transaction jadi finish
    private void handleMarkAsFinished(Transaction transaction) {
        trController.updateTransactionStatus(transaction.getId(), "Finished");

        showAlert("Order marked as Finished!", AlertType.INFORMATION);
        loadData();
    }

    // alert card
    private void showAlert(String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.show();
    }

    // scene untuk staff laundry
    public static Scene getScene(Stage stage, int staffId) {
        return new LaundryStaffPage(stage, staffId).init();
    }
}
