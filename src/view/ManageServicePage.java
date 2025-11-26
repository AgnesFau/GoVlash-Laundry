package view;

import component.ServiceTableComponent;
import controller.ServiceController;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Service;

public class ManageServicePage {
    Stage stage;
    Scene scene;

    ServiceController controller = new ServiceController();
    TableView<Service> serviceTable;

    Label nameLbl, descLbl, priceLbl, durationLbl, titleLbl;
    TextField nameTxt, descTxt, priceTxt, durationTxt;
    Button addBtn, editBtn;

    VBox container, outerBox;

    private Scene init() {

        // ===== Page Title =====
        titleLbl = new Label("Manage Services");
        titleLbl.setFont(Font.font("Inter", 28));
        titleLbl.setTextFill(Color.web("#2d6cdf"));

        // ===== Table =====
        serviceTable = ServiceTableComponent.create();
        styleTable();

        // ===== Labels =====
        nameLbl = new Label("Service Name");
        descLbl = new Label("Description");
        priceLbl = new Label("Price");
        durationLbl = new Label("Duration");

        styleLabel(nameLbl);
        styleLabel(descLbl);
        styleLabel(priceLbl);
        styleLabel(durationLbl);

        // ===== Input Fields =====
        nameTxt = new TextField();
        descTxt = new TextField();
        priceTxt = new TextField();
        durationTxt = new TextField();

        styleInput(nameTxt);
        styleInput(descTxt);
        styleInput(priceTxt);
        styleInput(durationTxt);

        // ===== Buttons =====
        addBtn = new Button("Add New Service");
        editBtn = new Button("Save Changes");
        editBtn.setVisible(false);

        stylePrimaryButton(addBtn);
        styleSecondaryButton(editBtn);

        // ===== Form Layout =====
        VBox formBox = new VBox(10);
        formBox.getChildren().addAll(
            nameLbl, nameTxt,
            descLbl, descTxt,
            priceLbl, priceTxt,
            durationLbl, durationTxt,
            addBtn, editBtn
        );

        formBox.setPadding(new Insets(20));
        formBox.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: #e5e5e5;" +
            "-fx-border-radius: 12;" +
            "-fx-border-width: 1;"
        );

        // ===== Action Column =====
        TableColumn<Service, Void> actionColumn = new TableColumn<>("Action");

        actionColumn.setCellFactory(col -> new TableCell<Service, Void>() {

            private final Button updateBtn = new Button("Update");
            private final Button deleteBtn = new Button("Delete");

            {
                styleSmallButton(updateBtn, "#2d6cdf", "white");
                styleSmallButton(deleteBtn, "#f44336", "white");

                updateBtn.setOnAction(e -> {
                    Service s = getTableView().getItems().get(getIndex());
                    onUpdate(s);
                });

                deleteBtn.setOnAction(e -> {
                    Service s = getTableView().getItems().get(getIndex());
                    controller.deleteService(s);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(10, updateBtn, deleteBtn);
                    setGraphic(box);
                }
            }
        });

        serviceTable.getColumns().add(actionColumn);

        // ===== Layout =====
        container = new VBox(25);
        container.setPadding(new Insets(25));
        container.getChildren().addAll(titleLbl, formBox, serviceTable);

        // ===== Background =====
        outerBox = new VBox();
        outerBox.setAlignment(Pos.TOP_CENTER);
        outerBox.setPadding(new Insets(20));
        outerBox.setStyle("-fx-background-color: #f7f8fa;");

        outerBox.getChildren().add(container);
        VBox.setVgrow(serviceTable, Priority.ALWAYS);

        adddBehaviour();
        scene = new Scene(outerBox, 1000, 600);

        return scene;
    }

    // ========== Style Helpers ==========

    private void styleLabel(Label lbl) {
        lbl.setFont(Font.font("Inter", 14));
        lbl.setTextFill(Color.web("#444"));
    }

    private void styleInput(TextField tf) {
        tf.setPrefWidth(350);
        tf.setFont(Font.font("Inter", 14));
        tf.setStyle(
            "-fx-background-color: #fdfdfd;" +
            "-fx-border-color: #dcdcdc;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 8 10;"
        );
    }

    private void stylePrimaryButton(Button btn) {
        btn.setFont(Font.font("Inter", 15));
        btn.setPrefWidth(350);
        btn.setStyle(
            "-fx-background-color: #2d6cdf;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 10 0;"
        );
    }

    private void styleSecondaryButton(Button btn) {
        btn.setFont(Font.font("Inter", 15));
        btn.setPrefWidth(350);
        btn.setStyle(
            "-fx-background-color: #ececec;" +
            "-fx-text-fill: #222;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 10 0;"
        );
    }

    private void styleSmallButton(Button btn, String bg, String fg) {
        btn.setFont(Font.font("Inter", 12));
        btn.setStyle(
            "-fx-background-color: " + bg + ";" +
            "-fx-text-fill: " + fg + ";" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 5 12;"
        );
    }

    private void styleTable() {
        serviceTable.setStyle(
            "-fx-background-color: white;" +
            "-fx-table-cell-border-color: #e1e1e1;"
        );
    }

    // ========== LOGIC (TIDAK DIRUBAH!) ==========

    private void onUpdate(Service service) {
        nameTxt.setText(service.getName().get());
        descTxt.setText(service.getDescription().get());
        priceTxt.setText(String.valueOf(service.getPrice().get()));
        durationTxt.setText(String.valueOf(service.getDuration().get()));

        addBtn.setVisible(false);
        editBtn.setVisible(true);

        editBtn.setOnAction(e -> {
            try {
                controller.updateService(
                    service.getId().get(),
                    nameTxt.getText(),
                    descTxt.getText(),
                    Double.parseDouble(priceTxt.getText()),
                    Integer.parseInt(durationTxt.getText()),
                    service
                );
                showAlert("Updated successfully!", AlertType.INFORMATION);

                addBtn.setVisible(true);
                editBtn.setVisible(false);
            } catch (Exception ex) {
                showAlert(ex.getMessage(), AlertType.ERROR);
            }
            serviceTable.refresh();
        });
    }

    private void adddBehaviour() {
        priceTxt.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                priceTxt.setText(oldValue);
            }
        });

        durationTxt.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                durationTxt.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        addBtn.setOnAction(e -> {
            String error = controller.addService(
                nameTxt.getText(),
                descTxt.getText(),
                Double.parseDouble(priceTxt.getText()),
                Integer.parseInt(durationTxt.getText())
            );

            if (error != null) {
                showAlert(error, AlertType.ERROR);
            } else {
                showAlert("Successfully add a new service", AlertType.INFORMATION);
            }
        });
    }

    public static Scene getScene(Stage stage) {
        return new ManageServicePage(stage).init();
    }

    private void showAlert(String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Message");
        alert.setContentText(message);
        alert.show();
    }

    public ManageServicePage(Stage stage) {
        this.stage = stage;
    }
}
