package model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Notification {
	private int id;
	private int recipientId;
	private String message;
	private LocalDateTime createdAt;
	private Boolean isRead;
	
	private ArrayList<Notification> listNotification = new ArrayList<Notification>();
	
	public Notification(int recipientId, String message, LocalDateTime createdAt, Boolean isRead) {
		super();
		this.id = listNotification.size()+1;
		this.recipientId = recipientId;
		this.message = message;
		this.createdAt = createdAt;
		this.isRead = isRead;
		listNotification.add(this);
	}
	
	public ArrayList<Notification> getNotificationByRecipientId(int id){
		ArrayList<Notification> listByRecipientId = new ArrayList<Notification>();
		
		for (Notification notification : listNotification) {
			if(this.recipientId == id) {
				listByRecipientId.add(notification);
			}
		}
		
		return listByRecipientId;
	}
	
	public Notification getNotificationById(int id) {
		for (Notification notification : listNotification) {
			if(notification.id == id) {
				return notification;
			}
		}
		
		return null;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRecipientId() {
		return recipientId;
	}
	public void setRecipientId(int recipientId) {
		this.recipientId = recipientId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public Boolean getIsRead() {
		return isRead;
	}
	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}
}
