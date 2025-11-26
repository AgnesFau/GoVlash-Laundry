package controller;

import java.sql.SQLException;
import java.util.ArrayList;
import model.Transaction;

public class TransactionController {
	public ArrayList<Transaction> getCustomerHistory(int customerId) {
	    return Transaction.getTransactionByCustomerId(customerId);
	}
	
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
	
	public ArrayList<Transaction> getAllTransactions() {
        return Transaction.getAllTransactions();
    }
	
	public String assignStaffToTransaction(int transactionId, int staffId) {
        return Transaction.assignStaffToTransaction(transactionId, staffId);
    }
}