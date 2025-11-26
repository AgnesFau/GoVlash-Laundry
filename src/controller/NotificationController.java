package controller;

import java.util.ArrayList;
import model.Notification;

public class NotificationController {
    
    public ArrayList<Notification> getAllNotifications(int userId) {
        return Notification.getUserNotifications(userId);
    }
    
    public void markAsRead(int notificationId) {
        Notification.markAsRead(notificationId);
    }
    
    public void deleteNotification(int notificationId) {
        Notification.delete(notificationId);
    }
}