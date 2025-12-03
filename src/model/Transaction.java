package model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import db.DbConnect;

public class Transaction {
	private int id;
	private int serviceId;
	private int customerId;
	private int receptionistId;
	private int laundryStaffId;
	private LocalDate date;
	private String status;
	private Double totalWeight;
	private String notes;
	private boolean isNotified = false;
	
	private ArrayList<Transaction> listTransactions = new ArrayList<Transaction>();

	public Transaction(int id, int serviceId, int customerId, int receptionistId, int laundryStaffId, LocalDate date,
            String status, Double totalWeight, String notes) {
        this.id = id;
        this.serviceId = serviceId;
        this.customerId = customerId;
        this.receptionistId = receptionistId;
        this.laundryStaffId = laundryStaffId;
        this.date = date;
        this.status = status;
        this.totalWeight = totalWeight;
        this.notes = notes;
    }
	
	public ArrayList<Transaction> getTransactionByStatus(String status){
		ArrayList<Transaction> listByStatus = new ArrayList<Transaction>();
		
		for (Transaction transaction : listTransactions) {
			if(transaction.status.equals(status)) {
				listByStatus.add(transaction);
			}
		}
		
		return listByStatus;
	}
	
	public static ArrayList<Transaction> getAllTransactions() {
        ArrayList<Transaction> trList = new ArrayList<>();
        String query = "SELECT * FROM transactions"; 
        
        try {
            PreparedStatement ps = DbConnect.getInstance().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                int id = rs.getInt("id");
                int serviceId = rs.getInt("service_id");
                int custId = rs.getInt("customer_id");
                int recepId = rs.getInt("receptionist_id");
                int staffId = rs.getInt("laundry_staff_id");
                
                java.sql.Date sqlDate = rs.getDate("date");
                LocalDate date = (sqlDate != null) ? sqlDate.toLocalDate() : null;
                
                String status = rs.getString("status");
                Double totalWeight = rs.getDouble("total_weight");
                String notes = rs.getString("notes");
                boolean isNotified = rs.getBoolean("is_notified");
                
                Transaction tr = new Transaction(id, serviceId, custId, recepId, staffId, date, status, totalWeight, notes);
                tr.setIsNotified(isNotified);

                trList.add(tr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trList;
    }
	
	public static void createTransaction(int serviceId, int customerId, double weight, String notes) throws SQLException {
		
		String q = "INSERT INTO transactions (service_id, customer_id, date, status, total_weight, notes) VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement ps = DbConnect.getInstance().prepareStatement(q);
        ps.setInt(1, serviceId);
        ps.setInt(2, customerId);
        ps.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
        ps.setString(4, "Pending");
        ps.setDouble(5, weight);
        ps.setString(6, notes);
        
        ps.executeUpdate();
	}
	public static ArrayList<Transaction> getTransactionByCustomerId(int customerId) {
        ArrayList<Transaction> trList = new ArrayList<Transaction>();
        String query = "SELECT * FROM transactions WHERE customer_id = ? ORDER BY date DESC";
        
        try {
            PreparedStatement ps = DbConnect.getInstance().prepareStatement(query);
            ps.setInt(1, customerId);
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                int id = rs.getInt("id");
                int serviceId = rs.getInt("service_id");
                int custId = rs.getInt("customer_id");
                int recepId = rs.getInt("receptionist_id");
                int staffId = rs.getInt("laundry_staff_id");
                java.sql.Date sqlDate = rs.getDate("date");
                LocalDate date = (sqlDate != null) ? sqlDate.toLocalDate() : null;
                String status = rs.getString("status");
                Double totalWeight = rs.getDouble("total_weight");
                String notes = rs.getString("notes");
                boolean isNotified = rs.getBoolean("is_notified");
                
                Transaction tr = new Transaction(id, serviceId, custId, recepId, staffId, date, status, totalWeight, notes);
                tr.setIsNotified(isNotified);
                trList.add(tr);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trList;
    }
	
	public static String assignStaffToTransaction(int transactionId, int staffId, int receptionistId) {
	    String query = "UPDATE transactions SET laundry_staff_id = ?, status = 'On Progress', receptionist_id = ? WHERE id = ?";

	    try {
	        PreparedStatement ps = DbConnect.getInstance().prepareStatement(query);

	        ps.setInt(1, staffId);
	        ps.setInt(2, receptionistId);
	        ps.setInt(3, transactionId);

	        int rows = ps.executeUpdate();

	        if (rows > 0) {
	            return null;
	        } else {
	            return "Failed to assign staff (no rows updated).";
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Database error: " + e.getMessage();
	    }
	}

	public static ArrayList<Transaction> getAssignedOrdersByLaundryStaffID(int laundryStaffID) {
	    ArrayList<Transaction> trList = new ArrayList<>();
	    String query = "SELECT * FROM transactions WHERE laundry_staff_id = ? ORDER BY date DESC";

	    try {
	        PreparedStatement ps = DbConnect.getInstance().prepareStatement(query);
	        ps.setInt(1, laundryStaffID);

	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            int id = rs.getInt("id");
	            int serviceId = rs.getInt("service_id");
	            int custId = rs.getInt("customer_id");
	            int recepId = rs.getInt("receptionist_id");
	            int staffId = rs.getInt("laundry_staff_id");

	            Date sqlDate = rs.getDate("date");
	            LocalDate date = (sqlDate != null) ? sqlDate.toLocalDate() : null;

	            String status = rs.getString("status");
	            Double totalWeight = rs.getDouble("total_weight");
	            String notes = rs.getString("notes");

	            trList.add(new Transaction(id, serviceId, custId, recepId, staffId, date, status, totalWeight, notes));
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return trList;
	}

	public static void updateTransactionStatus(int transactionId, String status) {
	    String query = "UPDATE transactions SET status = ? WHERE id = ?";

	    try {
	        PreparedStatement ps = DbConnect.getInstance().prepareStatement(query);
	        ps.setString(1, status);
	        ps.setInt(2, transactionId);

	        int rows = ps.executeUpdate();
	        if (rows > 0) return;

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void updateNotified(int transactionId, int isNotified) {
		String query = "UPDATE transactions SET is_notified = ? WHERE id = ?";
		
		try {
			PreparedStatement ps = DbConnect.getInstance().prepareStatement(query);
			ps.setInt(1, isNotified);
			ps.setInt(2, transactionId);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getReceptionistId() {
		return receptionistId;
	}

	public void setReceptionistId(int receptionistId) {
		this.receptionistId = receptionistId;
	}

	public int getLaundryStaffId() {
		return laundryStaffId;
	}

	public void setLaundryStaffId(int laundryStaffId) {
		this.laundryStaffId = laundryStaffId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public ArrayList<Transaction> getListTransactions() {
		return listTransactions;
	}

	public void setListTransactions(ArrayList<Transaction> listTransactions) {
		this.listTransactions = listTransactions;
	}
	
	public boolean isNotified() {
	    return isNotified;
	}

	public void setIsNotified(boolean isNotified) {
	    this.isNotified = isNotified;
	}
}
