package Controllers.Activite;

import entities.Activite;
import entities.Exercice;
import entities.Programme;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import services.ServiceActivite;
import services.ServiceExercice;
import services.ServiceProgramme;

import java.sql.SQLException;
import java.util.List;

public class AjoutProgrammeController {
    @FXML
    private ComboBox<Activite> cbActiviteId;
    @FXML
    private ComboBox<Exercice> cbExerciceId;
    @FXML
    private TextField tfLieu;
    @FXML
    private TextField tfBut;
    @FXML
    private TextField tfNomL;
    @FXML
    private TextField tfImage;

    @FXML
    private void initialize() {
        loadActivites();
        loadExercices();
    }

    private void loadActivites() {
        ServiceActivite serviceActivite = new ServiceActivite();
        try {
            List<Activite> activites = serviceActivite.afficher();
            cbActiviteId.getItems().setAll(activites);
            cbActiviteId.setConverter(new StringConverter<Activite>() {
                @Override
                public String toString(Activite object) {

                    return object != null ? object.getNom() + " (" + object.getId() + ")" : "";
                }
                @Override
                public Activite fromString(String string) {
                    return null; // No conversion from string back to Activite needed here.
                }
            });
        } catch (SQLException e) {
            e.printStackTrace(); // Handle this properly in a production environment
        }
    }


    @FXML
    private void loadExercices() {
        ServiceExercice serviceExercice = new ServiceExercice();
        try {
            List<Exercice> exercices = serviceExercice.afficher();
            cbExerciceId.setItems(FXCollections.observableArrayList(exercices));
            cbExerciceId.setConverter(new StringConverter<Exercice>() {
                @Override
                public String toString(Exercice exercice) {
                    return exercice != null ? exercice.getNomCoach() + " (" + exercice.getId() + ")" : "";
                }

                @Override
                public Exercice fromString(String string) {
                    return null; // Not needed here
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    @FXML
    private void handleAddProgramme() {
        try {
            Activite selectedActivite = cbActiviteId.getSelectionModel().getSelectedItem();
            Exercice selectedExercice = cbExerciceId.getSelectionModel().getSelectedItem();

            if (selectedActivite == null||selectedExercice == null) {
                System.out.println("No Activite ou exercice selected");
                return;
            }
            int activiteId = selectedActivite.getId();
            int exerciceId = selectedExercice.getId();
            String lieu = tfLieu.getText();
            String but = tfBut.getText();
            String nomL = tfNomL.getText();
            String image = tfImage.getText();

            Programme programme = new Programme(0, activiteId, exerciceId, lieu, but, nomL, image);
            ServiceProgramme serviceProgramme = new ServiceProgramme();
            serviceProgramme.ajouter(programme);

            // Clear fields after adding
            clearFields();
            System.out.println("Programme added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding programme: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format: " + e.getMessage());
        }
    }

    private void clearFields() {
        cbActiviteId.getSelectionModel().clearSelection();
        cbExerciceId.getSelectionModel().clearSelection();
        tfLieu.clear();
        tfBut.clear();
        tfNomL.clear();
        tfImage.clear();
    }

}
