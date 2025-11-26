package model;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Employee extends User{
	
	private static ObservableList<Employee> listEmployee = FXCollections.observableArrayList();

	public Employee(int id, String username, String email, String password, String gender, LocalDate dob, String role) {
		super(id, username, email, password, gender, dob, role);
		listEmployee.add(this);
	}

	public static ObservableList<Employee> getListEmployee() {
		return listEmployee;
	}

	public void setListEmployee(ObservableList<Employee> listEmployee) {
		Employee.listEmployee = listEmployee;
	}	
}
