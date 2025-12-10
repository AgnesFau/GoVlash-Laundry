package controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import model.Admin;
import model.Customer;
import model.Employee;
import model.LaundryStaff;
import model.Receptionist;
import model.User;

public class UserController {
	
	// validasi untuk menambahkan user ke database
	public String addUser(String name, String email, String password, String confirmPassword, String gender, LocalDate dob, String role) {
		User user = null;
	    int id = User.getUserList().size() + 1;
	    user = new Customer(id, name, email, password, gender, dob, role);

	    try {
	        User.addUser(user);
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Database error occurred!";
	    }

	    return null;  	
	}
	
	// validasi field ketika login
	public User login(String email, String password) {
		if(email.isEmpty() || password.isEmpty()) {
			return null;
		}
		
		return User.login(email, password);
	}
	
	// menambahkan employee ke database
	public void addEmployee(String name, String email, String password, String confirmPassword, String gender, LocalDate dob, String role) {
		User user = null;
	    int id = User.getUserList().size() + 1;

	    switch (role) {
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
	        return;
	    }
	}
	
	// validasi untuk menambahkan employee
	public String validateAddEmployee(String name, String email, String password, String confirmPassword, String gender, LocalDate dob, String role) {
		if(name.isEmpty() || email.isEmpty() || password.isEmpty() || gender == null || dob == null){
			return "All fields must be filled!";
		}
	    if (!password.equals(confirmPassword)) {
	        return "Password and confirm password do not match!";
	    }
	    if(getUserByEmail(email) != null) {
	    	return "Email is used!";
	    }
	    if(password.length()<6) {
	    	return "Password must be at leatst 6 characters";
	    }
	    if(!gender.equals("Female") && !gender.equals("Male")) {
	    	return "Gender must be Male or Female";
	    }
		if(role.equals("Laundry Staff") || role.equals("Admin") || role.equals("Receptionist")) { 
	    	if (!email.endsWith("@govlash.com")) {
	    		return "Email must ends with @govlash.com";
	    	}
	    	else if(LocalDate.now().getYear()-dob.getYear() <17) {
		    	return "You must be at least 17 years old";
		    }
	    }
	    else {
	    	return "Role is unknown";
	    }
	    return null;
	}

	// validasi untuk registrasi
	public String validateAddCustomer(String name, String email, String password, String confirmPassword, String gender, LocalDate dob) {
		if(name.isEmpty() || email.isEmpty() || password.isEmpty() || gender == null || dob == null){
			return "All fields must be filled!";
		}
	    if (!password.equals(confirmPassword)) {
	        return "Password and confirm password do not match!";
	    }
	    if(!email.endsWith("@email.com")){
	    	return "Email must ends with @email.com";	    		
	    }
	    else if(LocalDate.now().getYear()-dob.getYear() <12) {
	    	return "You must be at least 12 years old";
	    }	    
	    if(getUserByEmail(email) != null) {
	    	return "Email is used!";
	    }
	    if(password.length()<6) {
	    	return "Password must be at leatst 6 characters";
	    }
	    if(!gender.equals("Female") && !gender.equals("Male")) {
	    	return "Gender must be Male or Female";
	    }
	    return null;
	}
	
	// ambil data user berdasarkan role mereka
	public ArrayList<User> getUserByRole(String role){
		if(!role.equals("Customer") && !role.equals("Laundry Staff") && !role.equals("Admin") && !role.equals("Receptionist")) {
			return null;
		}
		
		return User.getUserByRole(role);
	}
	
	// ambil data user berdasarkan email
	private User getUserByEmail(String Email) {
		return User.getUserByEmail(Email);
	}
	
	// ambil data user berdasarkan nama
	private User getUserByName(String name) {
		return User.getUserByName(name);
	}
	
	// ambil seluruh data pegawai
	public ObservableList<User> getAllEmployees(){
		return Employee.getListEmployee();
	}
}
