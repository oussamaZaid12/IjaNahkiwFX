package Controllers.Activite;

import entities.Activite;
import entities.Exercice;
import entities.Programme;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import services.ServiceActivite;
import services.ServiceExercice;
import services.ServiceProgramme;

import java.sql.SQLException;
import java.util.List;

public class DisplayAllActiviteController {

    @FXML
    private VBox vBoxContainer;

    @FXML
    public void initialize() {
        loadData();
    }

    // Encapsulated method for loading all data
    private void loadData() {
        // Clear any previous data before adding new cards
        vBoxContainer.getChildren().clear();
        displayActivites();
        displayProgrammes();
        displayExercices();
    }

    private void displayActivites() {
        ServiceActivite serviceActivite = new ServiceActivite();
        try {
            List<Activite> activites = serviceActivite.afficher();
            for (Activite activite : activites) {
                VBox card = createCard("Activite: " + activite.getNom(), "activite-label");
                vBoxContainer.getChildren().add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayProgrammes() {
        ServiceProgramme serviceProgramme = new ServiceProgramme();
        try {
            List<Programme> programmes = serviceProgramme.afficher();
            for (Programme programme : programmes) {
                VBox card = createCard("Programme: " + programme.getNomL(), "programme-label");
                vBoxContainer.getChildren().add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayExercices() {
        ServiceExercice serviceExercice = new ServiceExercice();
        try {
            List<Exercice> exercices = serviceExercice.afficher();
            for (Exercice exercice : exercices) {
                VBox card = createCard("Exercice: " + exercice.getNomCoach(), "exercice-label");
                vBoxContainer.getChildren().add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to create a styled card for each item
    private VBox createCard(String text, String styleClass) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(10));
        card.getStyleClass().add("card");

        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        card.getChildren().add(label);

        return card;
    }
}
