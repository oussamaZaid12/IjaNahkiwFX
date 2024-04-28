package Controllers.Consultation;

import entities.Consultation;
import javafx.scene.control.Alert;
import services.ServiceConsultation;

import java.sql.SQLException;
import java.util.List;

public class UpcomingConsultationChecker {
    private final ServiceConsultation serviceConsultation;

    public UpcomingConsultationChecker(ServiceConsultation serviceConsultation) {
        this.serviceConsultation = serviceConsultation;
    }

    public void checkUpcomingConsultations() {
        try {
            List<Consultation> upcomingConsultations = serviceConsultation.getUpcomingConsultations();
            if (!upcomingConsultations.isEmpty()) {
                System.out.println(upcomingConsultations);
                showAlert(upcomingConsultations);
            }
            else
                System.out.println("nothing found");

        } catch (SQLException e) {
        }
    }

    private void showAlert(List<Consultation> upcomingConsultations) {
        // Create and show an alert to the user
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Upcoming Consultations");
        alert.setHeaderText("You have upcoming consultations in less than 24 hours:");
        StringBuilder message = new StringBuilder();
        for (Consultation consultation : upcomingConsultations) {
            message.append(consultation.toString()).append("\n");
        }
        alert.setContentText(message.toString());
        alert.showAndWait();
    }


}