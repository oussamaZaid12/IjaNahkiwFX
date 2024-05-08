package Controllers.Activite;


import entities.Exercice;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import services.ServiceExercice;

import java.sql.SQLException;

public class EditExercice {

    @FXML
    private TextField tfNomCoach;
    @FXML
    private TextField tfDuree;
    @FXML
    private Button btnValider;

    private Exercice currentExercice; // Holds the exercice being edited

    // This method is called to initialize the form with an Exercice object
    public void setExercice(Exercice exercice) {
        this.currentExercice = exercice;
        tfNomCoach.setText(exercice.getNomCoach());
        tfDuree.setText(exercice.getDuree());
    }

    @FXML
    void editExercice(ActionEvent event) {
        // Update the currentExercice object with new values from form
        currentExercice.setNomCoach(tfNomCoach.getText());
        currentExercice.setDuree(tfDuree.getText());

        // Attempt to update exercice in the database
        try {
            ServiceExercice serviceExercice = new ServiceExercice();
            serviceExercice.modifier(currentExercice);
            System.out.println("Exercice updated successfully!");
            // Optionally, close the window or clear the form here if needed
        } catch (SQLException e) {
            System.out.println("Error updating exercice: " + e.getMessage());
            // Handle error, possibly show an error message to the user
        }
    }
}
