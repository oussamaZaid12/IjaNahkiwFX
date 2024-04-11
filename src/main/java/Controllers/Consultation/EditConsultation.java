package Controllers.Consultation;

import entities.Consultation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import services.ServiceConsultation;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class EditConsultation {

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
    private Consultation currentConsultation;
    private ServiceConsultation serviceConsultation = new ServiceConsultation();

    public void setConsultation(Consultation consultation) {
        this.currentConsultation = consultation;

        tfpathologie.setText(consultation.getPathologie());
        tfremarques.setText(consultation.getRemarques());
        Tfidpatient.setText(String.valueOf(consultation.getIdp()));
        Tftherapeute.setText(String.valueOf(consultation.getIdt()));
        tffiche.setText(String.valueOf(consultation.getFiche()));

        // Extract hour and minute from LocalDateTime
        LocalDateTime dateTime = consultation.getDateC();
        int hour = dateTime.getHour();
        int minute = dateTime.getMinute();

        // Set the hour and minute text fields
        tfheure.setText(String.valueOf(hour));
        tfminute.setText(String.valueOf(minute));

        // Convert java.sql.Timestamp to java.time.LocalDate
        LocalDate dateLocal = dateTime.toLocalDate();
        TfdatePicker.setValue(dateLocal);
    }



    @FXML
    void ModifierConsultation(ActionEvent event) {
        try {
            String pathologie = tfpathologie.getText();
            String remarques = tfremarques.getText();
            int idPatient = Integer.parseInt(Tfidpatient.getText());
            int idTherapeute = Integer.parseInt(Tftherapeute.getText());
            int heure = Integer.parseInt(tfheure.getText());
            int minute = Integer.parseInt(tfminute.getText());
            LocalDate dateLocal = TfdatePicker.getValue();
            int fiche = Integer.parseInt(tffiche.getText());
            LocalDateTime dateTime = LocalDateTime.of(dateLocal, LocalTime.of(heure, minute));

            currentConsultation.setPathologie(pathologie);
            currentConsultation.setRemarques(remarques);
            currentConsultation.setIdp(idPatient);
            currentConsultation.setIdt(idTherapeute);
            currentConsultation.setDateC(Timestamp.valueOf(dateTime).toLocalDateTime());
            currentConsultation.setFiche(fiche);
            serviceConsultation.modifier(currentConsultation);

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Consultation has been updated successfully.");
            alert.showAndWait();

        } catch (NumberFormatException | SQLException e) {
            // Show error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred: " + e.getMessage());
            alert.showAndWait();
        }

    }
}
