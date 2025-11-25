package model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Employee extends User{
	
	private static ArrayList<Employee> listEmployee = new ArrayList<Employee>();

	public Employee(int id, String username, String email, String password, String gender, LocalDate dob, String role) {
		super(id, username, email, password, gender, dob, role);
		listEmployee.add(this);
	}

	public ArrayList<Employee> getListEmployee() {
		return listEmployee;
	}

	public void setListEmployee(ArrayList<Employee> listEmployee) {
		this.listEmployee = listEmployee;
	}	
}
