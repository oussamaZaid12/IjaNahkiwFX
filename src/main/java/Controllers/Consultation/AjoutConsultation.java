package Controllers.Consultation;

import entities.Consultation;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import services.ServiceConsultation;
import Controllers.User.Session;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AjoutConsultation {

    @FXML
    private AnchorPane ConsultationPane;

    @FXML
    private Button TfValider;

    @FXML
    private DatePicker TfdatePicker;

    @FXML
    private TextField Tfidpatient;

    @FXML
    private TextField Tftherapeute;

    @FXML
    private TextField tfheure;

    @FXML
    private TextField tfminute;

    @FXML
    private TextField tfpathologie;

    @FXML
    private TextField tfremarques;

    @FXML
    void AjouterConsultation(ActionEvent event) {

        try {
            User currentUser = Session.getUser();
            if (currentUser == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur de session", "Aucun utilisateur connecté.");
                return;
            }

            LocalDate date = TfdatePicker.getValue();
            if (date == null) {
                showAlert(Alert.AlertType.INFORMATION, "Input Error", "Please enter a valid date.");
                return;
            }
            if (date.isBefore(LocalDate.now())) {
                showAlert(Alert.AlertType.INFORMATION, "Input Error", "The date of the consultation cannot be in the past.");
                return;
            }

            int hour, minute, idPatient, idTherapeute;
            try {
                hour = Integer.parseInt(tfheure.getText());
                minute = Integer.parseInt(tfminute.getText());
                idPatient = Integer.parseInt(Tfidpatient.getText());
                idTherapeute = Integer.parseInt(Tftherapeute.getText());
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.INFORMATION, "Input Error", "Please ensure that hour, minute, patient ID, and therapist ID are numeric.");
                return;
            }

            if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
                showAlert(Alert.AlertType.INFORMATION, "Input Error", "Please enter a valid hour (0-23) and minute (0-59).");
                return;
            }

            String pathologie = tfpathologie.getText();
            if (pathologie.trim().isEmpty() || pathologie.length() < 3) {
                showAlert(Alert.AlertType.INFORMATION, "Input Error", "Pathology must have at least 3 characters.");
                return;
            }

            String remarques = tfremarques.getText();
            LocalTime time = LocalTime.of(hour, minute);
            LocalDateTime dateTime = LocalDateTime.of(date, time);

            Consultation consultation = new Consultation(idPatient, idTherapeute, dateTime, pathologie, remarques, false, 0);
            ServiceConsultation serviceConsultation = new ServiceConsultation();
            serviceConsultation.ajouter(consultation);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Consultation added successfully!");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error adding consultation: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void ReturnShowConsultations(ActionEvent actionEvent) {
        try {
            Node displayCons = FXMLLoader.load(getClass().getResource("/Front/Consultation/affichageConsultation.fxml"));
            ConsultationPane.getChildren().setAll(displayCons);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
