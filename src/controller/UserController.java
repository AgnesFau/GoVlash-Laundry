package controller;

import java.sql.SQLException;
import java.time.LocalDate;

import model.Admin;
import model.Customer;
import model.LaundryStaff;
import model.Receptionist;
import model.User;

public class UserController {
	public String addUser(String name, String email, String password, String confirmPassword, String gender, LocalDate dob, String role) {
		if(name.isEmpty() || email.isEmpty() || password.isEmpty() || gender == null || dob == null){
			return "All fields must be filled!";
		}
	    if (!password.equals(confirmPassword)) {
	        return "Password and confirm password do not match!";
	    }
	    if(role.equals("Customer")) {
	    	if(!email.endsWith("@email.com")){
	    		return "Email must ends with @email.com";	    		
	    	}
	    	else if(LocalDate.now().getYear()-dob.getYear() <12) {
	    		return "You must be at least 12 years old";
	    	}
	    } else if(role.equals("Laundry Staff") || role.equals("Admin") || role.equals("Receptionist")) { 
	    	if (!email.endsWith("@govlash.com")) {
	    		return "Email must ends with @goovlash.com";
	    	}
	    	else if(LocalDate.now().getYear()-dob.getYear() <17) {
		    	return "You must be at least 17 years old";
		    }
	    }
	    else {
	    	return "Role is unknown";
	    }
	    
	    if(password.length()<6) {
	    	return "Password must be at leatst 6 characters";
	    }
	    if(!gender.equals("Female") && !gender.equals("Male")) {
	    	return "Gender must be Male or Female";
	    }
	    
	    User user = null;
	    int id = User.getUserList().size() + 1;

	    switch (role) {
	        case "Customer":
	            user = new Customer(id, name, email, password, gender, dob, role);
	            break;
	        case "Laundry Staff":
	            user = new LaundryStaff(id, name, email, password, gender, dob, role);
	            break;
	        case "Receptionist":
	            user = new Receptionist(id, name, email, password, gender, dob, role);
	            break;
	        case "Admin":
	            user = new Admin(id, name, email, password, gender, dob, role);
	            break;
	    }

	    try {
	        User.addUser(user);
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Database error occurred!";
	    }

	    return null;  	
	}

	
	public User login(String email, String password) {
		if(email.isEmpty() || password.isEmpty()) {
			return null;
		}
		
		return User.login(email, password);
	}
}
