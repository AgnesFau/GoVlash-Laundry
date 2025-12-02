package view;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import controller.NotificationController;
import controller.TransactionController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import javafx.scene.control.ToggleButton;
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

public class ViewTransactionPage {
    Stage stage;
    TransactionController trController = new TransactionController();
    NotificationController ntfyController = new NotificationController();
    
    Label titleLbl;
    TableView<Transaction> table;
    ToggleButton filterBtn; 
    Button backBtn, refreshBtn;
    VBox mainLayout;
    
    ObservableList<Transaction> masterData;
    FilteredList<Transaction> filteredData;

    public ViewTransactionPage(Stage stage) {
        this.stage = stage;
    }

    public Scene init() {
        titleLbl = new Label("All Transactions Monitoring");
        titleLbl.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        filterBtn = new ToggleButton("Filter: Finished Only");
        filterBtn.setStyle("-fx-base: #FF9800; -fx-text-fill: white; -fx-font-weight: bold;");
        filterBtn.setMinWidth(150);
        
        backBtn = new Button("Back to Dashboard");
        backBtn.setMinWidth(150);
        backBtn.setOnAction(e -> Main.goToManageService(stage));
        
        table = new TableView<>();
        setupTableColumns();
        loadData(); 
        VBox.setVgrow(table, Priority.ALWAYS);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        HBox topBar = new HBox(15, titleLbl, spacer, backBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);
        
        HBox toolBar = new HBox(10, new Label("Filter View: "), filterBtn);
        toolBar.setAlignment(Pos.CENTER_LEFT);
        
        mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(25));
        mainLayout.getChildren().addAll(topBar, toolBar, table);
        
        addFilterBehaviour();

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
        
        TableColumn<Transaction, Double> weightCol = new TableColumn<>("Weight (Kg)");
        weightCol.setCellValueFactory(new PropertyValueFactory<>("totalWeight"));

        table.getColumns().addAll(custCol, serviceCol, dateCol, statusCol, weightCol);
        
        setupSendNotifColumn();
        
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    private void setupSendNotifColumn() {
        TableColumn<Transaction, Void> notifCol = new TableColumn<>("Customer Notification");
        
        notifCol.setCellFactory(col -> new TableCell<Transaction, Void>() {
            private final Button sendBtn = new Button("Send Notification");         
            {
                sendBtn.setOnAction(e -> {
                    Transaction tr = getTableView().getItems().get(getIndex());
                    handleSendNotification(tr);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Transaction tr = getTableView().getItems().get(getIndex());
                    boolean isFinished = tr.getStatus().equalsIgnoreCase("Finished");
                    boolean isNotified = tr.isNotified();
                    
                    if (isNotified) {
                        sendBtn.setText("Sent \u2713"); 
                        sendBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: green; -fx-font-weight: bold; -fx-border-color: green; -fx-border-radius: 5;");
                        sendBtn.setDisable(true);
                    } 
                    else if (!isFinished) {
                        sendBtn.setText("Wait Finish");
                        sendBtn.setStyle("-fx-background-color: #e0e0e0; -fx-text-fill: grey;");
                        sendBtn.setDisable(true);
                    } 
                    else {
                        sendBtn.setText("Send Notification");
                        sendBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                        sendBtn.setDisable(false);
                    }
                                        
                    HBox box = new HBox(sendBtn);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });
        
        table.getColumns().add(notifCol);
    }
    
    private void handleSendNotification(Transaction tr) {
        int recipientId = tr.getCustomerId();
        int transactionId = tr.getId();
        
        if(!tr.getStatus().equalsIgnoreCase("Finished")) {
            showAlert("Transaction is not finished!", AlertType.ERROR);
            return;
        }
        
        String err = ntfyController.addNotification(recipientId);
        
        if(err != null) {
            showAlert(err, AlertType.ERROR);
            return;
        }
        
        trController.updateNotified(transactionId, 1);
        
        tr.setIsNotified(true);
        table.refresh(); 
        
        showAlert("Notification sent to Customer!", AlertType.INFORMATION);
    }
    
    private void loadData() {
        ArrayList<Transaction> rawData = trController.getAllTransactions();
        masterData = FXCollections.observableArrayList(rawData);
        filteredData = new FilteredList<>(masterData, p -> true); 
        table.setItems(filteredData);
    }
    
    private void addFilterBehaviour() {
        filterBtn.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            filteredData.setPredicate(transaction -> {
                if (!isNowSelected) {
                    return true; 
                }
                return "Finished".equalsIgnoreCase(transaction.getStatus());
            });
            
            if(isNowSelected) {
                filterBtn.setText("Show All");
                filterBtn.setStyle("-fx-base: #2196F3; -fx-text-fill: white;");
            } else {
                filterBtn.setText("Filter: Finished Only");
                filterBtn.setStyle("-fx-base: #FF9800; -fx-text-fill: white;");
            }
        });
    }
    
    private void showAlert(String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.show();
    }
    
    public static Scene getScene(Stage stage) {
        return new ViewTransactionPage(stage).init();
    }
}