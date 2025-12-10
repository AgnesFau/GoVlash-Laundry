package controller;

import java.sql.SQLException;
import java.util.ArrayList;
import model.Transaction;

public class TransactionController {
	
	// ambil semua data transaction berdasarkan customer id
	public ArrayList<Transaction> getCustomerHistory(int customerId) {
	    return Transaction.getTransactionByCustomerId(customerId);
	}
	
	// validasi untuk menambahkan transaction ke database
	public String createTransaction(int serviceId, int customerId, String weightStr, String notes) {
	        
        if (weightStr.isEmpty() || notes.isEmpty()) {
            return "All fields must be filled!";
        }
        double weight = 0;
        try {
            weight = Double.parseDouble(weightStr);
        } catch (NumberFormatException e) {
            return "Weight must be a valid number!";
        }
        if (weight < 2 || weight > 50) {
            return "Weight must be between 2kg and 50kg!";
        }
        if (notes.length() > 250) {
            return "Notes cannot exceed 250 characters!";
        }
        try {
            Transaction.createTransaction(serviceId, customerId, weight, notes);
            return null; 
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }
    }
	
	// ambil semua data transaction
	public ArrayList<Transaction> getAllTransactions() {
        return Transaction.getAllTransactions();
    }
	
	// assign task (transaction) ke laundry staff, untuk receptionist
	public String assignStaffToTransaction(int transactionId, int staffId, int receptionistId) {
        return Transaction.assignStaffToTransaction(transactionId, staffId, receptionistId);
    }
	
	// ambil data transaction yang ditugaskan ke laundry staff
	public ArrayList<Transaction> getAssignedOrdersByLaundryStaffID(int laundryStaffID){
		return Transaction.getAssignedOrdersByLaundryStaffID(laundryStaffID);
	}
	
	// update transaction status (pending, on progress, finished)
	public void updateTransactionStatus(int transactionID, String status) {
		Transaction.updateTransactionStatus(transactionID, status);
	}
	
	// update sudah dinotif atau belum (1 = udh di notif, 0 = blm)
	public void updateNotified(int transactionId, int isNotified) {
		Transaction.updateNotified(transactionId, isNotified);
	}
}