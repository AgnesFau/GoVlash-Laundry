package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import db.DbConnect;

public abstract class User {
	private int id;
	private String username;
	private String email;
	private String password;
	private String gender;
	private LocalDate dob;
	private String role;
	private static ArrayList<User> userList = new ArrayList<User>();
	
	public User(int id, String username, String email, String password, String gender, LocalDate dob, String role) {
		super();
		this.id = userList.size()+1;
		this.username = username;
		this.email = email;
		this.password = password;
		this.gender = gender;
		this.dob = dob;
		this.role = role;
		userList.add(this);
	}
	
	public static User login(String email, String password) {
		for (User user : userList) {
			if(user.email.equals(email) && user.password.equals(password)) {
				return user;
			}
		}
		
		return null;
	}
	
	public ArrayList<User> getUserByRole(String role){
		ArrayList<User> userByRole = new ArrayList<User>();
		for (User user : userList) {
			if(user.role.equals(role)) {
				userByRole.add(user);
			}
		}
		
		return userByRole;
	}
	
	public User getUserByEmail(String email){
		for (User user : userList) {
			if(user.email.equals(email)) {
				return user;
			}
		}
		
		return null;
	}
	
	public User getUserByName(String name) {
		for (User user : userList) {
			if(user.username.equals(name)) {
				return user;
			}
		}
		
		return null;
	}

	public static void addUser(User user) throws SQLException {
	    String sql = "INSERT INTO users (username, email, password, gender, dob, role) VALUES (?, ?, ?, ?, ?, ?)";
	    
	    PreparedStatement stmt = DbConnect.getInstance().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	    stmt.setString(1, user.getUsername());
	    stmt.setString(2, user.getEmail());
	    stmt.setString(3, user.getPassword());
	    stmt.setString(4, user.getGender());
	    stmt.setDate(5, java.sql.Date.valueOf(user.getDob()));
	    stmt.setString(6, user.getRole());
	    
	    stmt.executeUpdate();
	}
	
	public static void loadUsersFromDB() throws SQLException {
	    String sql = "SELECT * FROM users";
	    PreparedStatement stmt = DbConnect.getInstance().prepareStatement(sql);
	    ResultSet rs = stmt.executeQuery();

	    userList.clear();

	    while (rs.next()) {
	        int id = rs.getInt("id");
	        String username = rs.getString("username");
	        String email = rs.getString("email");
	        String password = rs.getString("password");
	        String gender = rs.getString("gender");
	        LocalDate dob = rs.getDate("dob").toLocalDate();
	        String role = rs.getString("role");
	        
	        if(role.equals("Customer")) {
				new Customer(id, username, email, password, gender, dob, role);
			}
			else if (role.equals("Laundry Staff")) {
				new LaundryStaff(id, username, email, password, gender, dob, role);
			}
			else if (role.equals("Receptionist")) {
				new Receptionist(id, username, email, password, gender, dob, role);
			}
			else if (role.equals("Admin")) {
				new Admin(id, username, email, password, gender, dob, role);
			}
	    }
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public static ArrayList<User> getUserList() {
		return userList;
	}

	public static void setUserList(ArrayList<User> userList) {
		User.userList = userList;
	}
}
