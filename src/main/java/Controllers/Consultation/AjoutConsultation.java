package Controllers.Consultation;

import entities.Consultation;
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
            // Retrieve and validate data from input fields
            LocalDate date = TfdatePicker.getValue();
            if (date == null) {
                showAlert("Input Error", "Please enter a valid date.");
                return;
            }
            if (date.isBefore(LocalDate.now())) {
                showAlert("Input Error", "The date of the consultation cannot be in the past.");
                return;
            }
            int hour, minute, idPatient, idTherapeute;
            try {
                hour = Integer.parseInt(tfheure.getText());
                minute = Integer.parseInt(tfminute.getText());
                idPatient = Integer.parseInt(Tfidpatient.getText());
                idTherapeute = Integer.parseInt(Tftherapeute.getText());
            } catch (NumberFormatException e) {
                showAlert("Input Error", "Please ensure that hour, minute, patient ID, and therapist ID are numeric.");
                return;
            }

            if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
                showAlert("Input Error", "Please enter a valid hour (0-23) and minute (0-59).");
                return;
            }

            String pathologie = tfpathologie.getText();
            if (pathologie.trim().length() < 3 || pathologie.isEmpty()) {
                showAlert("Input Error", "Pathology must have at least 3 characters.");
                return;
            }

            String remarques = tfremarques.getText();

            // Set seconds to 0
            int second = 0;
            LocalTime time = LocalTime.of(hour, minute, second);
            LocalDateTime dateTime = LocalDateTime.of(date, time);

            Consultation consultation = new Consultation(idPatient, idTherapeute, dateTime, pathologie, remarques, false, 0);

            ServiceConsultation serviceConsultation = new ServiceConsultation();
            serviceConsultation.ajouter(consultation);
            showAlert("Success", "Consultation added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding consultation: " + e.getMessage());
            showAlert("Database Error", "Error adding consultation: " + e.getMessage());
        }
    }

    @FXML
    public void ReturnShowConsultations(ActionEvent actionEvent) {
        try {
            Node displayCons = FXMLLoader.load(getClass().getResource("/Front/Consultation/affichageConsultation.fxml"));
            ConsultationPane.getChildren().setAll(displayCons);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, for example, by showing an error message
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
