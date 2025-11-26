package controller;

import java.sql.SQLException;
import model.Service;

public class ServiceController {
	public String addService(String name, String description, Double price, Integer duration) {
	    if (name.isEmpty() || description.isEmpty()) {
	        return "All fields must be filled!";
	    }
	    
	    if(name.length()>50) {
	    	return "Service name must be less than or equal to 50 charactes";
	    }
	    else if(description.length()>250) {
	    	return "Service description must be less than or equal to 250 characters";
	    }
	    else if (price <= 0) {
			return "Price must be greater than 0";
		}
	    else if(duration < 1 || duration > 30) {
	    	return "Duration must be between 1 and 30 (inclusive)";
	    }

	    try {
	        Service.addService(name, description, price, duration);
	        return null;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Database error occurred!";
	    }
	}
}
