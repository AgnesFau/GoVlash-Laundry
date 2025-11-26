package view;

import controller.TransactionController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Service;
import model.User;

public class OrderPage {
    Stage stage;
    User currentUser;
    TransactionController trController = new TransactionController();
    ComboBox<Service> serviceCb; 
    TextField weightTxt;
    TextArea notesTxt;
    Button submitBtn, backBtn;
    
    public OrderPage(Stage stage, User currentUser) {
        this.stage = stage;
        this.currentUser = currentUser;
    }

    public Scene init() {
        Label titleLbl = new Label("Order Laundry");
        titleLbl.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Label serviceLbl = new Label("Select Service:");
        serviceCb = new ComboBox<>();
        serviceCb.setItems(Service.getListService());
        serviceCb.setPromptText("Choose a service...");
        
        Label weightLbl = new Label("Total Weight (Kg):");
        weightTxt = new TextField();
        weightTxt.setPromptText("e.g. 5.5");
        
        Label notesLbl = new Label("Notes:");
        notesTxt = new TextArea();
        notesTxt.setPromptText("Any special request?");
        notesTxt.setPrefHeight(100);

        submitBtn = new Button("Place Order");
        submitBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        
        backBtn = new Button("Back to Home");
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setAlignment(Pos.CENTER);
        
        form.add(serviceLbl, 0, 0);
        form.add(serviceCb, 1, 0);
        
        form.add(weightLbl, 0, 1);
        form.add(weightTxt, 1, 1);
        
        form.add(notesLbl, 0, 2);
        form.add(notesTxt, 1, 2);
        
        form.add(submitBtn, 1, 3);

        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(titleLbl, form, backBtn);

        // Actions
        addActions();

        return new Scene(container, 800, 600);
    }

    private void addActions() {
        submitBtn.setOnAction(e -> {
            Service selectedService = serviceCb.getValue();
            String weightStr = weightTxt.getText();
            String notes = notesTxt.getText();
            if (selectedService == null) {
                showAlert("Please select a service!");
                return;
            }
            String result = trController.createTransaction(
                selectedService.getId().get(), 
                currentUser.getId(), 
                weightStr, 
                notes
            );

            if (result == null) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Order Created Successfully!");
                alert.showAndWait();
                HistoryPage history = new HistoryPage(stage, currentUser);
                stage.setScene(history.init());
            } else {
                showAlert(result);
            }
        });

        backBtn.setOnAction(e -> {
            CustomerHomePage home = new CustomerHomePage(stage, currentUser);
            stage.setScene(home.init());
        });
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(msg);
        alert.show();
    }
}