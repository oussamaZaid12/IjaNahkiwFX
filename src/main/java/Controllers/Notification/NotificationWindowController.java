package Controllers.Notification;

import Controllers.Consultation.Cardconsultation;
import entities.Consultation;
import entities.Notification;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import services.ServiceNotification;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class NotificationWindowController {
    @FXML
    private FlowPane notificationscontainer;
    private ServiceNotification serviceNotification =new ServiceNotification();

    public void setServiceNotification(ServiceNotification serviceNotification) {
        this.serviceNotification = serviceNotification;
    }

    private void loadNotifications() {
        try {
            // Clear existing notifications before loading new ones
            notificationscontainer.getChildren().clear();

            List<Notification> notifications = serviceNotification.afficher();
            for (Notification con : notifications) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/Consultation/cardnotification.fxml"));
                    Node card = loader.load();
                    Cardnotification controller = loader.getController();
                    controller.setNotification(con); // Pass the notification to the controller
                    controller.setAffichageNotifController(this); // Pass reference to this controller
                    notificationscontainer.getChildren().add(card);
                } catch (IOException e) {
                    System.out.println("Error loading notification card: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving notifications: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void initialize() {
        loadNotifications();}

    public void refreshNotficationsView() {

            Platform.runLater(() -> {
                loadNotifications(); // Reload all publications
            });

    }
}