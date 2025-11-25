package model;

import java.time.LocalDate;
import java.util.ArrayList;

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
	
	private ArrayList<Transaction> listTransactions = new ArrayList<Transaction>();

	public Transaction(int serviceId, int customerId, int receptionistId, int laundryStaffId, LocalDate date,
			String status, Double totalWeight, String notes, ArrayList<Transaction> listTransactions) {
		super();
		this.id = listTransactions.size()+1;
		this.serviceId = serviceId;
		this.customerId = customerId;
		this.receptionistId = receptionistId;
		this.laundryStaffId = laundryStaffId;
		this.date = date;
		this.status = status;
		this.totalWeight = totalWeight;
		this.notes = notes;
		this.listTransactions = listTransactions;
		listTransactions.add(this);
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
}
