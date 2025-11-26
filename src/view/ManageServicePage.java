package view;

import component.ServiceTableComponent;
import controller.ServiceController;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Service;

public class ManageServicePage {
	Stage stage;
	Scene scene;
	
	ServiceController controller = new ServiceController();
	TableView<Service> serviceTable;
    
    Label nameLbl, descLbl, priceLbl, durationLbl;
    TextField nameTxt, descTxt, priceTxt, durationTxt;
    Button addBtn, editBtn;
    
    VBox container;
	
	private Scene init() {
		
		serviceTable = ServiceTableComponent.create();
		
		nameLbl = new Label("Service Name");
		descLbl = new Label("Description");		
		priceLbl = new Label("Price");
		durationLbl = new Label("Duration");
		
		nameTxt = new TextField();
		descTxt = new TextField();
		priceTxt = new TextField();
		durationTxt = new TextField();
		
		addBtn = new Button("Add New Service");
		editBtn = new Button("Save Changes");
		editBtn.setVisible(false);
		
		TableColumn<Service, Void> actionColumn = new TableColumn<>("Action");

		actionColumn.setCellFactory(col -> new TableCell<Service, Void>() {

		    private final Button updateBtn = new Button("Update");
		    private final Button deleteBtn = new Button("Delete");

		    {
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
		
		container = new VBox();
		container.getChildren().addAll(nameLbl, nameTxt, descLbl, descTxt, priceLbl, priceTxt, durationLbl, durationTxt, addBtn, editBtn, serviceTable);
		
		adddBehaviour();
		scene = new Scene(container, 1000, 500);
		
		return scene;
	}
	
	private void onUpdate(Service service) {
		System.out.println("asjdnlasdna");
	    nameTxt.setText(service.getName().get());
	    descTxt.setText(service.getDescription().get());
	    priceTxt.setText(String.valueOf(service.getPrice().get()));
	    durationTxt.setText(String.valueOf(service.getDuration().get()));

	    addBtn.setVisible(false);
	    editBtn.setVisible(true);

	    editBtn.setOnAction(e -> {
	        try {
	            controller.updateService(service.getId().get(), 
		    			nameTxt.getText(), 
		    			descTxt.getText(), 
		    			Double.parseDouble(priceTxt.getText()), 
		    			Integer.parseInt(durationTxt.getText()),
		    			service);
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