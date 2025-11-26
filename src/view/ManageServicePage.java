package view;

import controller.ServiceController;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.Main;
import model.Service;
import model.User;

public class ManageServicePage {
	Stage stage;
	Scene scene;
	
	ServiceController controller = new ServiceController();
    
	TableView<Service> serviceTable;
    
    Label nameLbl, descLbl, priceLbl, durationLbl;
    TextField nameTxt, descTxt, priceTxt, durationTxt;
    Button addBtn;
    
    VBox container;
	
	private Scene init() {
		TableColumn<Service, Number> idColumn = new TableColumn<>("Id");
		idColumn.setCellValueFactory(data -> data.getValue().getId());
		TableColumn<Service, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(data -> data.getValue().getName());
		TableColumn<Service, String> descColumn = new TableColumn<>("Description");
		descColumn.setCellValueFactory(data -> data.getValue().getDescription());
		TableColumn<Service, Number> priceColumn = new TableColumn<>("Price");
		priceColumn.setCellValueFactory(data -> data.getValue().getPrice());
		TableColumn<Service, Number> durationColumn = new TableColumn<>("Duration");
		durationColumn.setCellValueFactory(data -> data.getValue().getDuration());
		
		serviceTable = new TableView<Service>();
		serviceTable.getColumns().addAll(idColumn, nameColumn, descColumn, priceColumn, durationColumn);
		serviceTable.setItems(Service.getListService());
		
		nameLbl = new Label("Service Name");
		descLbl = new Label("Description");		
		priceLbl = new Label("Price");
		durationLbl = new Label("Duration");
		
		nameTxt = new TextField();
		descTxt = new TextField();
		priceTxt = new TextField();
		durationTxt = new TextField();
		
		addBtn = new Button("Add New Service");
		
		container = new VBox();
		container.getChildren().addAll(nameLbl, nameTxt, descLbl, descTxt, priceLbl, priceTxt, durationLbl, durationTxt, addBtn, serviceTable);
		
		adddBehaviour();
		scene = new Scene(container, 1000, 800);
		
		return scene;
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
		addBtn.setOnAction(e-> {
			String error = controller.addService(nameTxt.getText(), descTxt.getText(), Double.parseDouble(priceTxt.getText()), Integer.parseInt(durationTxt.getText()));
			
			if(error != null) {
				showAlert(error, AlertType.ERROR);
			}
			else {
				showAlert("Successfully add a new service", AlertType.INFORMATION);
			}
		});
	}
	
	public static Scene getScene(Stage stage) {
	    return new ManageServicePage(stage).init();
	}

	private void showAlert(String message, AlertType type) {
	    Alert alert = new Alert(type);
	    alert.setTitle("Error");
	    alert.setContentText(message);
	    alert.show();
	}
	
	public ManageServicePage(Stage stage) {
		this.stage = stage;
	}
}
