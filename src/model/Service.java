package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import db.DbConnect;

public class Service {
	private int id;
	private String name;
	private String description;
	private Double price;
	private Integer duration;
	
	private static ArrayList<Service> listService = new ArrayList<Service>();

	public Service(int id, String name, String description, Double price, Integer duration) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.duration = duration;
		listService.add(this);
	}
	
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

	    Service s = new Service(newId, name, description, price, duration);
	    return s; 
	}

	
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
	        
	        new Service(id, name, description, price, duration);
	    }
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public static ArrayList<Service> getListService() {
		return listService;
	}

	public static void setListService(ArrayList<Service> listService) {
		Service.listService = listService;
	}
}
