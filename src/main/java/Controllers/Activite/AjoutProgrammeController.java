package Controllers.Activite;

import entities.Activite;
import entities.Exercice;
import entities.Programme;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
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

    private ServiceProgramme serviceProgramme = new ServiceProgramme();
    private ServiceActivite serviceActivite = new ServiceActivite();
    private ServiceExercice serviceExercice = new ServiceExercice();

    @FXML
    public void initialize() {
        loadActivites();
        loadExercices();
    }

    private void loadActivites() {
        try {
            List<Activite> activites = serviceActivite.afficher();
            cbActiviteId.getItems().setAll(activites);
            cbActiviteId.setConverter(new StringConverter<Activite>() {
                @Override
                public String toString(Activite object) {
                    return object != null ? object.getNom() + " (ID: " + object.getId() + ")" : null;
                }

                @Override
                public Activite fromString(String string) {
                    return null; // No conversion from string needed
                }
            });
        } catch (SQLException e) {
            showError("Database Error", "Failed to load activities from the database.");
        }
    }

    private void loadExercices() {
        try {
            List<Exercice> exercices = serviceExercice.afficher();
            cbExerciceId.getItems().setAll(exercices);
            cbExerciceId.setConverter(new StringConverter<Exercice>() {
                @Override
                public String toString(Exercice object) {
                    return object != null ? object.getNomCoach() + " (ID: " + object.getId() + ")" : null;
                }

                @Override
                public Exercice fromString(String string) {
                    return null; // No conversion from string needed
                }
            });
        } catch (SQLException e) {
            showError("Database Error", "Failed to load exercices from the database.");
        }
    }

    @FXML
    private void handleAddProgramme() {
        try {
            Activite selectedActivite = cbActiviteId.getSelectionModel().getSelectedItem();
            Exercice selectedExercice = cbExerciceId.getSelectionModel().getSelectedItem();

            if (selectedActivite == null || selectedExercice == null) {
                showError("Input Error", "You must select both an activity and an exercise.");
                return;
            }

            Programme newProgramme = new Programme(0, selectedActivite.getId(), selectedExercice.getId(),
                    tfLieu.getText(), tfBut.getText(), tfNomL.getText(), tfImage.getText());
            serviceProgramme.ajouter(newProgramme);
            showConfirmation("Success", "Programme added successfully!");
        } catch (SQLException e) {
            showError("Database Error", "Failed to add programme: " + e.getMessage());
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showConfirmation(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
