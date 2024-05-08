package Controllers.Activite;

import entities.Activite;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import services.ServiceActivite;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class    editActivite {

    @FXML
    private TextField TfNom;

    @FXML
    private TextField TfGenre;

    @FXML
    private DatePicker tfdate;

    @FXML
    private Button TfValider;

    private Activite currentActivite; // Holds the activite being edited

    // This method is called to initialize the form with an Activite object
    public void setActivite(Activite activite) {
        this.currentActivite = activite;
        TfNom.setText(activite.getNom());
        TfGenre.setText(activite.getGenre());
        tfdate.setValue(activite.getDate().toLocalDate()); // Assuming the getDate() returns a LocalDateTime
    }

    @FXML
    void editActivite(ActionEvent event) {
        // Update the currentActivite object with new values from form
        currentActivite.setNom(TfNom.getText());
        currentActivite.setGenre(TfGenre.getText());
        currentActivite.setDate(LocalDateTime.of(tfdate.getValue(), LocalTime.MIDNIGHT)); // Set time to midnight

        // Attempt to update activite in the database
        try {
            ServiceActivite serviceActivite = new ServiceActivite();
            serviceActivite.modifier(currentActivite);  // Assuming there's a method to update activite in your service
            System.out.println("Activité mise à jour avec succès!");
            // Optionally, close the window or clear the form here if needed
        } catch (SQLException e) {
            System.out.println("Error updating activity: " + e.getMessage());
            // Handle error, possibly show an error message to the user
        }
    }
}
