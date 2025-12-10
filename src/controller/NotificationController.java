package controller;

import java.sql.SQLException;
import java.util.ArrayList;
import model.Notification;
import model.User;

public class NotificationController {
    
	// ambil data notifications berdasarkan user id tertentu
    public ArrayList<Notification> getAllNotifications(int userId) {
        return Notification.getUserNotifications(userId);
    }
    
    // validasi ketika notifikasi akan ditambahkan ke database
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
    
    // update status notification jadi read
    public void markAsRead(int notificationId) {
        Notification.markAsRead(notificationId);
    }
    
    // hapus notification
    public void deleteNotification(int notificationId) {
        Notification.delete(notificationId);
    }
}