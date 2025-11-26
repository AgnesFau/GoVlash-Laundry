package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;

public class CustomerHomePage {
    Stage stage;
    Scene scene;
    User currentUser;
    Label welcomeLbl;
    Button orderBtn, historyBtn, logoutBtn;
    VBox container;

    public CustomerHomePage(Stage stage, User currentUser) {
        this.stage = stage;
        this.currentUser = currentUser;
    }

    public Scene init() {
        welcomeLbl = new Label("Welcome, " + currentUser.getUsername());
        welcomeLbl.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        orderBtn = new Button("Order Laundry Service");
        historyBtn = new Button("View Transaction History");
        logoutBtn = new Button("Logout");
        orderBtn.setMinWidth(200);
        historyBtn.setMinWidth(200);
        logoutBtn.setMinWidth(200);
        container = new VBox(20);
        container.setAlignment(Pos.CENTER); 
        container.getChildren().addAll(welcomeLbl, orderBtn, historyBtn, logoutBtn);
        addActions();

        scene = new Scene(container, 800, 600);
        return scene;
    }

    private void addActions() {
    	orderBtn.setOnAction(e -> {
            OrderPage orderPage = new OrderPage(stage, currentUser);
            stage.setScene(orderPage.init());
        });
        historyBtn.setOnAction(e -> {
            HistoryPage historyPage = new HistoryPage(stage, currentUser);
            stage.setScene(historyPage.init());
        });
        logoutBtn.setOnAction(e -> {
            LoginPage loginPage = new LoginPage(stage);
            stage.setScene(loginPage.getScene(stage)); 
        });
    }
}