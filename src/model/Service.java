package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import db.DbConnect;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Service {
	private IntegerProperty id;
	private StringProperty name;
	private StringProperty description;
	private DoubleProperty price;
	private IntegerProperty duration;
	
	private static ObservableList<Service> listService = FXCollections.observableArrayList(); // untuk menampung data services

	// constructor
	public Service(IntegerProperty id, StringProperty name, StringProperty description, DoubleProperty price, IntegerProperty duration) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.duration = duration;
		listService.add(this);
	}
	
	// menambahkan service ke database
	public static Service addService(String name, String description, Double price, Integer duration) throws SQLException {
	    String sql = "INSERT INTO services (name, description, price, duration) VALUES (?, ?, ?, ?)";

	    PreparedStatement stmt = DbConnect.getInstance().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	    stmt.setString(1, name);
	    stmt.setString(2, description);
	    stmt.setDouble(3, price);
	    stmt.setInt(4, duration);

	    stmt.executeUpdate();

	    ResultSet rs = stmt.getGeneratedKeys();
	    int newId = 0;
	    if (rs.next()) {
	        newId = rs.getInt(1);
	    }

	    Service s = new Service(new SimpleIntegerProperty(newId), new SimpleStringProperty(name), new SimpleStringProperty(description), new SimpleDoubleProperty(price), new SimpleIntegerProperty(duration));
	    return s; 
	}

	// edit service, untuk admin
	public static void updateService(int id, String name, String desc, Double price, Integer duration) throws SQLException {
	    String sql = "UPDATE services SET name=?, description=?, price=?, duration=? WHERE id=?";
	    
	    PreparedStatement stmt = DbConnect.getInstance().prepareStatement(sql);
	    stmt.setString(1, name);
	    stmt.setString(2, desc);
	    stmt.setDouble(3, price);
	    stmt.setInt(4, duration);
	    stmt.setInt(5, id);
	    stmt.executeUpdate();
	}
	
	// hapus service dari database
	public static void deleteService(int id) throws SQLException {
	    String sql = "DELETE FROM services WHERE id=?";
	    
	    PreparedStatement stmt = DbConnect.getInstance().prepareStatement(sql);
	    stmt.setInt(1, id);
	    stmt.executeUpdate();
	}
	
	// ambil semua data service dari database
	public static void loadServicesFromDB() throws SQLException {
	    String sql = "SELECT * FROM services";
	    PreparedStatement stmt = DbConnect.getInstance().prepareStatement(sql);
	    ResultSet rs = stmt.executeQuery();

	    listService.clear();

	    while (rs.next()) {
	        int id = rs.getInt("id");
	        String name = rs.getString("name");
	        String description = rs.getString("description");
	        Double price = Double.parseDouble(rs.getString("price"));
	        Integer duration = Integer.parseInt(rs.getString("duration"));
	        
	        new Service(new SimpleIntegerProperty(id), new SimpleStringProperty(name), new SimpleStringProperty(description), new SimpleDoubleProperty(price), new SimpleIntegerProperty(duration));
	    }
	}

	// getter setter
	public IntegerProperty getId() {
		return id;
	}

	public void setId(int id) {
		this.id = new SimpleIntegerProperty(id);
	}

	public StringProperty getName() {
		return name;
	}

	public void setName(String name) {
		this.name = new SimpleStringProperty(name);
	}

	public StringProperty getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = new SimpleStringProperty(description);
	}

	public DoubleProperty getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = new SimpleDoubleProperty(price);
	}

	public IntegerProperty getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = new SimpleIntegerProperty(duration);
	}

	public static ObservableList<Service> getListService() {
		return listService;
	}

	public static void setListService(ObservableList<Service> listService) {
		Service.listService = listService;
	}
	@Override
    public String toString() {
        return name.get();
    }
}
