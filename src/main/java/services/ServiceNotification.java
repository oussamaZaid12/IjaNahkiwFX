package services;

import entities.Notification;
import utils.MyDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceNotification implements IService<Notification> {
    private Connection con;

    public ServiceNotification() {
        con = MyDB.getInstance().getConnection();
    }

    @Override
    public void ajouter(Notification notification) throws SQLException {
        String req = "INSERT INTO notification (title, message, timestamp, isread, user_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setString(1, notification.getTitle());
        pre.setString(2, notification.getMessage());
        pre.setObject(3, notification.getTimestamp());
        pre.setBoolean(4, notification.isRead());
        pre.setInt(5, notification.getUserId());
        pre.executeUpdate();
    }

    @Override
    public void modifier(Notification notification) throws SQLException {
        String req = "UPDATE notification SET title=?, message=?, timestamp=?, isread=?, user_id=? WHERE id=?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setString(1, notification.getTitle());
        pre.setString(2, notification.getMessage());
        pre.setObject(3, notification.getTimestamp());
        pre.setBoolean(4, notification.isRead());
        pre.setInt(5, notification.getUserId());
        pre.setInt(6, notification.getId());
        pre.executeUpdate();
    }

    @Override
    public void supprimer(Notification notification) throws SQLException {
        PreparedStatement pre = con.prepareStatement("DELETE FROM notification WHERE id=?");
        pre.setInt(1, notification.getId());
        pre.executeUpdate();
    }

    @Override
    public List<Notification> afficher() throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String req = "SELECT * FROM notification";
        Statement ste = con.createStatement();
        ResultSet res = ste.executeQuery(req);
        while (res.next()) {
            Notification notification = new Notification(
                    res.getInt("id"),
                    res.getInt("user_id"),
                    res.getString("title"),
                    res.getString("message"),
                    res.getObject("timestamp", LocalDateTime.class),
                    res.getBoolean("isread")
            );
            notifications.add(notification);
        }
        return notifications;
    }

    public List<Notification> getNotificationsByUserId(int userId) throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String req = "SELECT * FROM notification WHERE user_id = ?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setInt(1, userId);
        ResultSet res = pre.executeQuery();
        while (res.next()) {
            Notification notification = new Notification(
                    res.getInt("id"),
                    res.getInt("user_id"),
                    res.getString("title"),
                    res.getString("message"),
                    res.getObject("timestamp", LocalDateTime.class),
                    res.getBoolean("isread")
            );
            notifications.add(notification);
        }
        return notifications;
    }
}
