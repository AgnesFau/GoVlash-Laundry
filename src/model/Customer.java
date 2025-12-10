package model;

import java.time.LocalDate;

public class Customer extends User{

	// constructor
	public Customer(int id, String username, String email, String password, String gender, LocalDate dob, String role) {
		super(id, username, email, password, gender, dob, role);
	}

}
