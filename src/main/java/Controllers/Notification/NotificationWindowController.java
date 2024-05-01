package Controllers.Notification;

import entities.Notification;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import services.ServiceNotification;

import java.util.List;

public class NotificationWindowController {
    @FXML
    private ListView<Notification> notificationListView;

    private ServiceNotification serviceNotification;

    public void setServiceNotification(ServiceNotification serviceNotification) {
        this.serviceNotification = serviceNotification;
        loadNotifications();
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) notificationListView.getScene().getWindow();
        stage.close();
    }

    private void loadNotifications() {
        try {
            List<Notification> notifications = serviceNotification.afficher();
            notificationListView.getItems().setAll(notifications);
        } catch (Exception e) {
            // Handle the exception, e.g., log the error
        }
    }
}