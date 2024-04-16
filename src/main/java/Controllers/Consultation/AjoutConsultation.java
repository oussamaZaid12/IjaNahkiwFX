package Controllers.Consultation;

import entities.Consultation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
        // Retrieve data from input fields
        String pathologie = tfpathologie.getText();
        String remarques = tfremarques.getText();
        LocalDate date = TfdatePicker.getValue();
        int hour = Integer.parseInt(tfheure.getText());
        int minute = Integer.parseInt(tfminute.getText());
        int idPatient = Integer.parseInt(Tfidpatient.getText());
        int idTherapeute = Integer.parseInt(Tftherapeute.getText());

        // Set seconds to 0
        int second = 0;

        // Create a LocalTime object with the retrieved hour, minute, and second values
        LocalTime time = LocalTime.of(hour, minute, second);

        // Create a LocalDateTime object by combining date and time
        LocalDateTime dateTime = LocalDateTime.of(date, time);

        // Create a new Consultation object with the retrieved data
        Consultation consultation = new Consultation(idPatient, idTherapeute, dateTime, pathologie, remarques, false, 0);

        // Call a service method to add the consultation to the database
        try {
            ServiceConsultation serviceConsultation = new ServiceConsultation();
            serviceConsultation.ajouter(consultation);
            System.out.println("Consultation added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding consultation: " + e.getMessage());
            // Handle any potential exceptions
        }

        // Optionally, you can perform further actions after adding the consultation, such as updating the UI or displaying a confirmation message.
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
}
