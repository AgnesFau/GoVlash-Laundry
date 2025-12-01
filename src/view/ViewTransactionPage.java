package view;

import java.util.ArrayList;

import controller.NotificationController;
import controller.TransactionController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import main.Main;
import model.Transaction;

public class ViewTransactionPage {
    Stage stage;
    TransactionController trController = new TransactionController();
    NotificationController ntfyController = new NotificationController();
    
    Label titleLbl;
    TableView<Transaction> table;
    ToggleButton filterBtn; 
    Button backBtn;
    VBox mainLayout;
    ObservableList<Transaction> masterData;
    FilteredList<Transaction> filteredData;

    public ViewTransactionPage(Stage stage) {
        this.stage = stage;
    }

    public Scene init() {
        titleLbl = new Label("All Transactions");
        titleLbl.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        filterBtn = new ToggleButton("Show Finished Only");
        filterBtn.setStyle("-fx-base: #FF9800; -fx-text-fill: white;");
        
        table = new TableView<>();
        setupTableColumns();
        loadData(); 
        VBox.setVgrow(table, Priority.ALWAYS);
        backBtn = new Button("Back to Dashboard");
        backBtn.setOnAction(e -> Main.goToManageService(stage));
        HBox topBar = new HBox(10, titleLbl, filterBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);
        
        mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.getChildren().addAll(topBar, table, backBtn);
        addFilterBehaviour();

        return new Scene(mainLayout, 1000, 600);
    }

    private void setupTableColumns() {
        TableColumn<Transaction, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<Transaction, Integer> custCol = new TableColumn<>("Cust ID");
        custCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        
        TableColumn<Transaction, Integer> serviceCol = new TableColumn<>("Service ID");
        serviceCol.setCellValueFactory(new PropertyValueFactory<>("serviceId"));

        TableColumn<Transaction, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Transaction, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        TableColumn<Transaction, Double> weightCol = new TableColumn<>("Weight");
        weightCol.setCellValueFactory(new PropertyValueFactory<>("totalWeight"));

        table.getColumns().addAll(idCol, custCol, serviceCol, dateCol, statusCol, weightCol);
        setupSendNotifColumn();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    private void setupSendNotifColumn() {
    	TableColumn<Transaction, Void> sendNotif = new TableColumn<>("Send Notification");
    	
    	sendNotif.setCellFactory(col -> new TableCell<Transaction, Void>() {
    		private final Button sendNotif = new Button("Send Notification");    		
    		{
    			sendNotif.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
    			
    			sendNotif.setOnAction(e -> {
    				Transaction tr = getTableView().getItems().get(getIndex());
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
    			});
    		}
    		
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                	Transaction tr = getTableView().getItems().get(getIndex());
                	boolean isPending = !tr.getStatus().equalsIgnoreCase("Finished");
                	boolean isNotified = tr.isNotified();
                	sendNotif.setDisable(isPending || isNotified);
                	
                	if (isNotified) {
                	    sendNotif.setText("Sent");
                	}
                	
                	if (isPending || isNotified) {
                		sendNotif.setStyle("-fx-background-color: grey; -fx-text-fill: white;");
                	} else {
                		sendNotif.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                	}
                	                	
                    HBox box = new HBox(sendNotif);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
                
            }
    		
    	});
    	
    	table.getColumns().add(sendNotif);
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
        });
    }
    
    public static Scene getScene(Stage stage) {
        return new ViewTransactionPage(stage).init();
    }
    
    private void showAlert(String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.show();
    }
}