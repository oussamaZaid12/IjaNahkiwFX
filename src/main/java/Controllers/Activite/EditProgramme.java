package Controllers.Activite ;

import entities.Programme;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import services.ServiceProgramme;

import java.sql.SQLException;

public class EditProgramme {

    @FXML
    private TextField tfActiviteId;
    @FXML
    private TextField tfExerciceId;
    @FXML
    private TextField tfLieu;
    @FXML
    private TextField tfBut;
    @FXML
    private TextField tfNomL;
    @FXML
    private TextField tfImage;
    @FXML
    private Button btnValider;

    private Programme currentProgramme; // Holds the programme being edited

    // This method is called to initialize the form with a Programme object
    public void setProgramme(Programme programme) {
        this.currentProgramme = programme;
        tfActiviteId.setText(String.valueOf(programme.getActiviteId()));
        tfExerciceId.setText(String.valueOf(programme.getExerciceId()));
        tfLieu.setText(programme.getLieu());
        tfBut.setText(programme.getBut());
        tfNomL.setText(programme.getNomL());
        tfImage.setText(programme.getImage());
    }

    @FXML
    void editProgramme(ActionEvent event) {
        // Update the currentProgramme object with new values from form
        currentProgramme.setActiviteId(Integer.parseInt(tfActiviteId.getText()));
        currentProgramme.setExerciceId(Integer.parseInt(tfExerciceId.getText()));
        currentProgramme.setLieu(tfLieu.getText());
        currentProgramme.setBut(tfBut.getText());
        currentProgramme.setNomL(tfNomL.getText());
        currentProgramme.setImage(tfImage.getText());

        // Attempt to update programme in the database
        try {
            ServiceProgramme serviceProgramme = new ServiceProgramme();
            serviceProgramme.modifier(currentProgramme);  // Assuming there's a method to update programme in your service
            System.out.println("Programme mis à jour avec succès!");
            // Optionally, close the window or clear the form here if needed
        } catch (SQLException e) {
            System.out.println("Error updating programme: " + e.getMessage());
            // Handle error, possibly show an error message to the user
        }
    }
}
