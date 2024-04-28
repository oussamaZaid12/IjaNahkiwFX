package Controllers.Consultation;

import entities.Consultation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import services.ServiceConsultation;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class EditConsultationDoctor {

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
    private TextField tffiche;

    @FXML
    private CheckBox confirmationCheckBox;

    private Consultation currentConsultation;
    private ServiceConsultation serviceConsultation = new ServiceConsultation();



    public void setConsultation(Consultation consultation) {
        this.currentConsultation = consultation;

        tfpathologie.setText(consultation.getPathologie());
        tfpathologie.setEditable(false);

        tfremarques.setText(consultation.getRemarques());
        tfremarques.setEditable(false);

        Tfidpatient.setText(String.valueOf(consultation.getIdp()));
        Tfidpatient.setEditable(false);

        Tftherapeute.setText(String.valueOf(consultation.getIdt()));
        Tftherapeute.setEditable(false);

        tffiche.setText(String.valueOf(consultation.getFiche()));
        tffiche.setEditable(false);

        confirmationCheckBox.setSelected(consultation.isConfirmation());

        // Extract hour and minute from LocalDateTime
        LocalDateTime dateTime = consultation.getDateC();
        int hour = dateTime.getHour();
        int minute = dateTime.getMinute();

        // Set the hour and minute text fields
        tfheure.setText(String.valueOf(hour));
        tfheure.setEditable(false);

        tfminute.setText(String.valueOf(minute));
        tfminute.setEditable(false);

        // Convert java.sql.Timestamp to java.time.LocalDate
        LocalDate dateLocal = dateTime.toLocalDate();
        TfdatePicker.setValue(dateLocal);
    }

    @FXML
    void ModifierConsultation(ActionEvent event) {
        try {
            // Validate the date
            LocalDate dateLocal = TfdatePicker.getValue();
            if (dateLocal == null) {
                showAlert("Input Error", "Please enter a valid date.");
                return;
            }
            if (dateLocal.isBefore(LocalDate.now())) {
                showAlert("Input Error", "The date of the consultation cannot be in the past.");
                return;
            }

            int heure, minute, idPatient, idTherapeute, fiche;
            try {
                heure = Integer.parseInt(tfheure.getText());
                minute = Integer.parseInt(tfminute.getText());
                idPatient = Integer.parseInt(Tfidpatient.getText());
                idTherapeute = Integer.parseInt(Tftherapeute.getText());
                fiche = Integer.parseInt(tffiche.getText());
            } catch (NumberFormatException e) {
                showAlert("Input Error", "Please ensure that all inputs are numeric.");
                return;
            }

            if (heure < 0 || heure > 23 || minute < 0 || minute > 59) {
                showAlert("Input Error", "Please enter a valid hour (0-23) and minute (0-59).");
                return;
            }

            String pathologie = tfpathologie.getText();
            if (pathologie.trim().length() < 3) {
                showAlert("Input Error", "Pathology must have at least 3 characters.");
                return;
            }

            String remarques = tfremarques.getText();

            LocalDateTime dateTime = LocalDateTime.of(dateLocal, LocalTime.of(heure, minute));
            currentConsultation.setPathologie(pathologie);
            currentConsultation.setRemarques(remarques);
            currentConsultation.setIdp(idPatient);
            currentConsultation.setIdt(idTherapeute);
            currentConsultation.setDateC(dateTime);
            currentConsultation.setFiche(fiche);
            currentConsultation.setConfirmation(confirmationCheckBox.isSelected()); // Set the confirmation status

            serviceConsultation.modifier(currentConsultation);
            showAlert("Success", "Consultation has been updated successfully.");

        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while updating the consultation: " + e.getMessage());
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