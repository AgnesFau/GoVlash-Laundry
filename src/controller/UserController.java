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

	    if (!password.equals(confirmPassword)) {
	        return "Password and confirm password do not match!";
	    }

	    if(name.isEmpty() || email.isEmpty() || password.isEmpty() || gender == null || dob == null){
	        return "All fields must be filled!";
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
