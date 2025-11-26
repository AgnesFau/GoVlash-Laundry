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
	
	public String updateService(int id, String name, String description, double price, int duration, Service service) {
	    try {
	        Service.updateService(id, name, description, price, duration);

	        service.setName(name);
	        service.setDescription(description);
	        service.setPrice(price);
	        service.setDuration(duration);
	        return null;
	    } catch (Exception e) {
	        return e.getMessage();
	    }
	}

	public String deleteService(Service service) {
	    try {
	        Service.deleteService(service.getId().get());
            Service.getListService().remove(service);
	        return null;
	    } catch (Exception e) {
	        return e.getMessage();
	    }
	}
}
