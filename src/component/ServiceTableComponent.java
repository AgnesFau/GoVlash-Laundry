package component;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Service;

public class ServiceTableComponent { // table untuk service, dipake di ManageServicePage
	public static TableView<Service> create() {

        TableView<Service> table = new TableView<>();
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
        table.getColumns().addAll(idColumn, nameColumn, descColumn, priceColumn, durationColumn);
        table.setItems(Service.getListService());

        return table;
    }
}
