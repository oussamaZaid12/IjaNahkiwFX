package entities;

import java.time.LocalDateTime;

public class Notification {
    private int id;
    private int userId; // New user_id field
    private String title;
    private String message;
    private LocalDateTime timestamp;
    private boolean isread;

    public Notification(int id, int userId, String title, String message, LocalDateTime timestamp, boolean read) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
        this.isread = read;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return isread;
    }

    public void setRead(boolean read) {
        this.isread = read;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", read=" + isread +
                '}';
    }
}