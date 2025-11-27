package controller;

import java.sql.SQLException;
import java.util.ArrayList;
import model.Notification;
import model.User;

public class NotificationController {
    
    public ArrayList<Notification> getAllNotifications(int userId) {
        return Notification.getUserNotifications(userId);
    }
    
    public String addNotification(int recipientId) {
    	if(User.getUserById(recipientId) == null) {
    		return "User ID not found!";
    	}
    	
    	try {
			Notification.addNotification(recipientId);
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Database error: " + e.getMessage();
		}
    }
    
    public void markAsRead(int notificationId) {
        Notification.markAsRead(notificationId);
    }
    
    public void deleteNotification(int notificationId) {
        Notification.delete(notificationId);
    }
}