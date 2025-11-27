package model;

import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import db.DbConnect;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Notification {
    private IntegerProperty id;
    private IntegerProperty recipientId;
    private StringProperty message;
    private StringProperty createdAt;
    private StringProperty status;

    public Notification(int id, int recipientId, String message, LocalDateTime createdAt, boolean isRead) {
        this.id = new SimpleIntegerProperty(id);
        this.recipientId = new SimpleIntegerProperty(recipientId);
        this.message = new SimpleStringProperty(message);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.createdAt = new SimpleStringProperty(createdAt.format(formatter));
        
        this.status = new SimpleStringProperty(isRead ? "Read" : "Unread");
    }
    
    public static void addNotification(int recipientId) throws SQLException {
    	String sql = "INSERT INTO notifications (recipient_id, message, created_at, is_read)"
    			+ " VALUES (?, ?, ?, ?)";
    	
    	PreparedStatement ps = DbConnect.getInstance().prepareStatement(sql);
    	ps.setInt(1, recipientId);
    	ps.setString(2, "Your order is finished and ready for pickup. Thank you for choosing our service!");
    	ps.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
    	ps.setBoolean(4, false);
    	
    	ps.executeUpdate();
    }
    
    public static ArrayList<Notification> getUserNotifications(int userId) {
        ArrayList<Notification> list = new ArrayList<>();
        String query = "SELECT * FROM notifications WHERE recipient_id = ? ORDER BY created_at DESC";
        
        try {
            PreparedStatement ps = DbConnect.getInstance().prepareStatement(query);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                list.add(new Notification(
                    rs.getInt("id"),
                    rs.getInt("recipient_id"),
                    rs.getString("message"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getBoolean("is_read")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static void markAsRead(int notifId) {
        String query = "UPDATE notifications SET is_read = 1 WHERE id = ?";
        try {
            PreparedStatement ps = DbConnect.getInstance().prepareStatement(query);
            ps.setInt(1, notifId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(int notifId) {
        String query = "DELETE FROM notifications WHERE id = ?";
        try {
            PreparedStatement ps = DbConnect.getInstance().prepareStatement(query);
            ps.setInt(1, notifId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public IntegerProperty getId() { 
    	return id; 
    }
    public StringProperty getMessage() {
    	return message; 
    }
    public StringProperty getCreatedAt() { 
    	return createdAt; 
    }
    public StringProperty getStatus() { 
    	return status; 
    }
    public StringProperty messageProperty() { 
    	return message; 
    }
    public StringProperty createdAtProperty() { 
    	return createdAt; 
    }
    public StringProperty statusProperty() { 
    	return status; 
    }
}