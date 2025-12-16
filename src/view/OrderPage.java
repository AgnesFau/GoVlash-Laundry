package view;

import controller.TransactionController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    
    // constructor
    public OrderPage(Stage stage, User currentUser) {
        this.stage = stage;
        this.currentUser = currentUser;
    }

    // inisiasi tampilan
    public Scene init() {
        Label titleLbl = new Label("Place New Order");
        titleLbl.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Label serviceLbl = new Label("Select Service:");
        serviceCb = new ComboBox<>();
        serviceCb.setItems(Service.getListService()); 
        serviceCb.setPromptText("Choose a service...");
        serviceCb.setPrefWidth(300);
        
        Label weightLbl = new Label("Total Weight (Kg):");
        weightTxt = new TextField();
        weightTxt.setPromptText("Min 2kg, Max 50kg");
        
        Label notesLbl = new Label("Notes:");
        notesTxt = new TextArea();
        notesTxt.setPromptText("Special instructions (optional)");
        notesTxt.setPrefHeight(100);

        submitBtn = new Button("Confirm Order");
        submitBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        submitBtn.setMinWidth(150);
        
        backBtn = new Button("Cancel");
        backBtn.setMinWidth(150);
        
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 30;");
        
        form.add(serviceLbl, 0, 0); form.add(serviceCb, 1, 0);
        form.add(weightLbl, 0, 1);  form.add(weightTxt, 1, 1);
        form.add(notesLbl, 0, 2);   form.add(notesTxt, 1, 2);
         
        VBox container = new VBox(20);
        container.setPadding(new Insets(30));
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(titleLbl, form, submitBtn, backBtn);

        addActions();

        return new Scene(container, 800, 600);
    }

    // action untuk order service
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
                alert.setContentText("Order placed successfully! Check your history.");
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

    // menampilkan alert
    private void showAlert(String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setContentText(msg);
        alert.show();
    }
}