package view;

import controller.ServiceController;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
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
	
    private SimpleStringProperty title;
    private SimpleStringProperty actor;
    private SimpleDoubleProperty price;
    private SimpleIntegerProperty rating;
    
    Label nameLbl, descLbl, priceLbl, durationLbl;
    TextField nameTxt, descTxt, priceTxt, durationTxt;
    Button addBtn;
    
    VBox container;
	
	private Scene init() {
		TableColumn<Service, String> titleColumn = new TableColumn<>("Id");
		TableColumn<Service, String> actorColumn = new TableColumn<>("Name");
		TableColumn<Service, Number> priceColumn = new TableColumn<>("Price");
		TableColumn<Service, Number> ratingColumn = new TableColumn<>("IMDB Rating");
		
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
		container.getChildren().addAll(nameLbl, nameTxt, descLbl, descTxt, priceLbl, priceTxt, durationLbl, durationTxt, addBtn);
		
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
				showAlert(error);
			}
			else {
				System.out.println("Successfully add a new service");
				for (Service service : Service.getListService()) {
					System.out.println(service.getId());
					System.out.println(service.getName());
					System.out.println(service.getDescription());
					System.out.println(service.getPrice());
					System.out.println(service.getDuration());
				}
			}
		});
	}
	
	public static Scene getScene(Stage stage) {
	    return new ManageServicePage(stage).init();
	}

	private void showAlert(String message) {
	    Alert alert = new Alert(Alert.AlertType.ERROR);
	    alert.setTitle("Error");
	    alert.setContentText(message);
	    alert.show();
	}
	
	public ManageServicePage(Stage stage) {
		this.stage = stage;
	}
}
