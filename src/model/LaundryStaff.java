	package model;

import java.time.LocalDate;

public class LaundryStaff extends Employee{

	// constructor
	public LaundryStaff(int id, String username, String email, String password, String gender, LocalDate dob, String role) {
		super(id, username, email, password, gender, dob, role);
	}

}
