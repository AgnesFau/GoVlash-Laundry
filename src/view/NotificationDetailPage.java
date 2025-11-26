package view;

import controller.NotificationController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.Notification;
import model.User;

public class NotificationDetailPage {
    Stage stage;
    User currentUser;
    Notification notification;
    NotificationController controller = new NotificationController();
    
    public NotificationDetailPage(Stage stage, User currentUser, Notification notification) {
        this.stage = stage;
        this.currentUser = currentUser;
        this.notification = notification;
        controller.markAsRead(notification.getId().get());
    }

    public Scene init() {
        Label titleLbl = new Label("Notification Detail");
        titleLbl.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        Label dateLbl = new Label("Received on: " + notification.getCreatedAt().get());
        dateLbl.setStyle("-fx-text-fill: grey;");
        
        Text messageText = new Text(notification.getMessage().get());
        messageText.setWrappingWidth(600);
        messageText.setTextAlignment(TextAlignment.CENTER);
        messageText.setFont(Font.font("Arial", 16));
        
        Button deleteBtn = new Button("Delete Notification");
        deleteBtn.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");
        
        Button backBtn = new Button("Back to List");
        
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(30));
        container.setStyle("-fx-border-color: #ccc; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-color: #f9f9f9;");
        container.setMaxWidth(700);
        
        container.getChildren().addAll(titleLbl, dateLbl, messageText, deleteBtn, backBtn);
        
        VBox root = new VBox(container);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        deleteBtn.setOnAction(e -> {
            controller.deleteNotification(notification.getId().get());
            
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("Notification deleted.");
            alert.showAndWait();
            goBack();
        });
        
        backBtn.setOnAction(e -> goBack());
        
        return new Scene(root, 800, 600);
    }
    
    private void goBack() {
        NotificationPage listPage = new NotificationPage(stage, currentUser);
        stage.setScene(listPage.init());
    }
}