package model;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Employee extends User{
	
	private static ObservableList<User> listEmployee = FXCollections.observableArrayList(); // menampung employee

	// constructor
	public Employee(int id, String username, String email, String password, String gender, LocalDate dob, String role) {
		super(id, username, email, password, gender, dob, role);
		listEmployee.add(this);
	}

	// setter getter
	public static ObservableList<User> getListEmployee() {
		return listEmployee;
	}

	public void setListEmployee(ObservableList<User> listEmployee) {
		Employee.listEmployee = listEmployee;
	}	
}
