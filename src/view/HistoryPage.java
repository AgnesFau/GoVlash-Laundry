package view;

import java.util.ArrayList;

import controller.TransactionController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Transaction;
import model.User;
import javafx.scene.control.Button;

public class HistoryPage {
    Stage stage;
    Scene scene;
    User currentUser; 
    TransactionController trController = new TransactionController();
    VBox container;
    Label titleLbl;
    TableView<Transaction> table;
    Button backBtn;
    public HistoryPage(Stage stage, User currentUser) {
        this.stage = stage;
        this.currentUser = currentUser;
    }

    public Scene init() {
        titleLbl = new Label("Transaction History for " + currentUser.getUsername());
        titleLbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        table = new TableView<>();
        TableColumn<Transaction, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Transaction, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<Transaction, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        TableColumn<Transaction, Double> weightCol = new TableColumn<>("Weight (Kg)");
        weightCol.setCellValueFactory(new PropertyValueFactory<>("totalWeight"));
        TableColumn<Transaction, String> notesCol = new TableColumn<>("Notes");
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));
        table.getColumns().addAll(idCol, dateCol, statusCol, weightCol, notesCol);
        refreshTable();
        container = new VBox(10);
        container.setPadding(new Insets(15));
        backBtn = new Button("Back to Home");
        container.getChildren().addAll(titleLbl, table, backBtn); 
        backBtn.setOnAction(e -> {
            CustomerHomePage home = new CustomerHomePage(stage, currentUser);
            stage.setScene(home.init());
        });
        scene = new Scene(container, 800, 600);
        return scene;
    }

    private void refreshTable() {
        ArrayList<Transaction> data = trController.getCustomerHistory(currentUser.getId());
        ObservableList<Transaction> observableData = FXCollections.observableArrayList(data);
        table.setItems(observableData);
        System.out.println("Jumlah data yang diambil: " + data.size());
    }
}