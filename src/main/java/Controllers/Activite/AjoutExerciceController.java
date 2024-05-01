package Controllers.Activite;

import entities.Exercice;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import services.ServiceExercice;

import java.sql.SQLException;

public class AjoutExerciceController {
    @FXML
    private TextField tfNomCoach;
    @FXML
    private TextField tfDuree;

    @FXML
    private void handleAddExercice() {
        String nomCoach = tfNomCoach.getText();
        String duree = tfDuree.getText();

        Exercice exercice = new Exercice(0, nomCoach, duree); // Assuming ID is auto-generated
        ServiceExercice serviceExercice = new ServiceExercice();
        try {
            serviceExercice.ajouter(exercice);
            clearFields();
            System.out.println("Exercice added successfully!");
        } catch (SQLException e) {
            System.out.println("Failed to add exercice: " + e.getMessage());
        }
    }

    private void clearFields() {
        tfNomCoach.clear();
        tfDuree.clear();
    }
}
