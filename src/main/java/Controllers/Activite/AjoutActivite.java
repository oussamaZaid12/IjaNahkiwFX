package Controllers.Activite;

import entities.Activite;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import services.ServiceActivite;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AjoutActivite {

    @FXML
    private Button TfValider;
    @FXML
    private TextField TfNom;
    @FXML
    private TextField TfGenre;
    @FXML
    private AnchorPane addprog;

    @FXML
    void AjouterActivite(ActionEvent event) {
        String nom = TfNom.getText();
        String genre = TfGenre.getText();
        LocalDateTime dateTime = LocalDateTime.now();
        Activite activite = new Activite(0, nom, dateTime, genre);

        try {
            ServiceActivite serviceActivite = new ServiceActivite();
            serviceActivite.ajouter(activite);
            System.out.println("Activity added successfully!");
            TfNom.clear();
            TfGenre.clear();
        } catch (SQLException e) {
            System.out.println("Error adding activity: " + e.getMessage());
            showAlert("Database Error", "Error adding activity to the database.");
        }
    }

    @FXML
    void handleAddExercice(ActionEvent event) {
        loadView("/Back/Activite/AjoutExercice.fxml");
    }

    @FXML
    void handleAddProgramme(ActionEvent event) {
        loadView("/Back/Activite/AjouProgramme.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            Node view = FXMLLoader.load(getClass().getResource(fxmlPath));
            addprog.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Loading Error", "Failed to load the view from " + fxmlPath);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}