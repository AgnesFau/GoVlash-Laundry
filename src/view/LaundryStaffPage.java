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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import main.Main;
import model.Transaction;
import model.User;

public class LaundryStaffPage {

    Stage stage;
    int staffId;

    TransactionController trController = new TransactionController();
    UserController userController = new UserController();

    Label titleLbl;
    TableView<Transaction> table;
    VBox mainLayout;
    ObservableList<Transaction> assignedData;

    public LaundryStaffPage(Stage stage, int staffId) {
        this.stage = stage;
        this.staffId = staffId;
    }

    public Scene init() {
        titleLbl = new Label("Assigned Order");
        titleLbl.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        table = new TableView<>();
        setupTableColumns();
        loadData();
        VBox.setVgrow(table, Priority.ALWAYS);

        HBox topBar = new HBox(10, titleLbl);
        topBar.setAlignment(Pos.CENTER_LEFT);

        mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.getChildren().addAll(topBar, table);

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

        table.getColumns().addAll(idCol, custCol, serviceCol, dateCol, statusCol, weightCol, finishCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    private void loadData() {
        ArrayList<Transaction> rawData = trController.getAssignedOrdersByLaundryStaffID(staffId);
        assignedData = FXCollections.observableArrayList(rawData);
        table.setItems(assignedData);
    }

    private void handleMarkAsFinished(Transaction transaction) {
        trController.updateTransactionStatus(transaction.getId(), "Finished");

        showAlert("Order marked as Finished!", AlertType.INFORMATION);
        loadData();
    }

    private void showAlert(String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.show();
    }

    public static Scene getScene(Stage stage, int staffId) {
        return new LaundryStaffPage(stage, staffId).init();
    }
}
