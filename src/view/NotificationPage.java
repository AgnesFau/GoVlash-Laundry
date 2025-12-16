package view;

import controller.NotificationController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Notification;
import model.User;

public class NotificationPage {
    Stage stage;
    User currentUser;
    NotificationController notifController = new NotificationController();
    
    TableView<Notification> table;
    Button backBtn;
    
    // constructor
    public NotificationPage(Stage stage, User currentUser) {
        this.stage = stage;
        this.currentUser = currentUser;
    }

    // inisiasi
    public Scene init() {
        Label titleLbl = new Label("My Notifications");
        titleLbl.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        table = new TableView<>();
        setupColumns();
        loadData();
        
        VBox.setVgrow(table, Priority.ALWAYS);
        
        table.setOnMouseClicked(e -> {
            Notification selected = table.getSelectionModel().getSelectedItem();
            if(selected != null) {
                NotificationDetailPage detailPage = new NotificationDetailPage(stage, currentUser, selected);
                stage.setScene(detailPage.init());
            }
        });

        backBtn = new Button("Back to Home");
        backBtn.setOnAction(e -> {
            CustomerHomePage home = new CustomerHomePage(stage, currentUser);
            stage.setScene(home.init());
        });

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getChildren().addAll(titleLbl, new Label("(Click a row to view details)"), table, backBtn);
        
        return new Scene(layout, 800, 600);
    }
    
    // kolom table untuk menampilkan data
    private void setupColumns() {
        TableColumn<Notification, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cellData -> cellData.getValue().createdAtProperty());
        dateCol.setPrefWidth(150);
        
        TableColumn<Notification, String> msgCol = new TableColumn<>("Message");
        msgCol.setCellValueFactory(cellData -> cellData.getValue().messageProperty());
        msgCol.setPrefWidth(400); 
        
        TableColumn<Notification, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        
        table.getColumns().addAll(dateCol, msgCol, statusCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    // ambil data dari database
    private void loadData() {
        ObservableList<Notification> data = FXCollections.observableArrayList(
            notifController.getAllNotifications(currentUser.getId())
        );
        table.setItems(data);
    }
}