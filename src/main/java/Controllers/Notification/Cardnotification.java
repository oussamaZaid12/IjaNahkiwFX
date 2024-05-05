package Controllers.Notification;

import Controllers.Notification.NotificationWindowController;
import entities.Consultation;
import entities.Notification;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import services.ServiceConsultation;
import services.ServiceNotification;

import java.sql.SQLException;

public class Cardnotification {
    @FXML
    private Label idnotif;
    private NotificationWindowController affichagenotifController;
    private Notification currentnotification;
    public void handleDeleteAction(ActionEvent actionEvent) {
        try {
            ServiceNotification serviceNotification = new ServiceNotification();
            serviceNotification.supprimer(currentnotification); // Delete the consultation
            System.out.println("Consultation deleted successfully");
            if (affichagenotifController != null) {
                affichagenotifController.refreshNotficationsView();
                System.out.println("Refresh method called");
            } else {
                System.out.println("affichagePubController is null");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void setAffichageNotifController(NotificationWindowController notificationWindowController) {
        this.affichagenotifController= notificationWindowController;
    }

    public void setNotification(Notification con) {
        this.currentnotification=con;
        idnotif.setText(con.getMessage());
    }
}
