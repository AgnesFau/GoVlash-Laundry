package controller;

import java.sql.SQLException;
import java.util.ArrayList;
import model.Transaction;

public class TransactionController {
	public ArrayList<Transaction> getCustomerHistory(int customerId) {
	    return Transaction.getTransactionByCustomerId(customerId);
	}
}