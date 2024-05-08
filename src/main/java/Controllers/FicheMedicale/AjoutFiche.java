package Controllers.FicheMedicale;

import Controllers.User.Session;
import entities.FicheMedicale;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import services.ServiceFicheMedicale;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class AjoutFiche {

    @FXML
    private AnchorPane FichePane;
    @FXML
    private Button TfValider;

    @FXML
    private DatePicker tfdatecreation;

    @FXML
    private DatePicker tfdernieremaj;

    @FXML
    private TextField tfidp;

    @FXML
    private TextField tfidt;

    @FXML
    void AjouterFiche(ActionEvent event) {
        try {
            User currentUser = Session.getUser();
            if (currentUser == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur de session", "Aucun utilisateur connecté.");
                return;
            }
            int idUser = currentUser.getId();
            LocalDate localDateCreation = tfdatecreation.getValue();
            if (localDateCreation == null) {
                showAlert("Input Error", "Please enter a valid creation date.");
                return;
            }
            Date dateCreation = Date.valueOf(localDateCreation);

            LocalDate localDateDerniereMaj = tfdernieremaj.getValue();
            if (localDateDerniereMaj == null) {
                showAlert("Input Error", "Please enter a valid last update date.");
                return;
            }
            Date dateDerniereMaj = Date.valueOf(localDateDerniereMaj);

            // Check that the last update date is not before the creation date
            if (dateDerniereMaj.before(dateCreation)) {
                showAlert("Input Error", "The last update date cannot be before the creation date.");
                return;
            }

            int idPatient = Integer.parseInt(tfidp.getText());
           // int idTherapeute = Integer.parseInt(tfidt.getText());

            // Create a new FicheMedicale object
            FicheMedicale ficheMedicale = new FicheMedicale(dateCreation, dateDerniereMaj, idPatient, idUser);

            // Call a service method to add the fiche medicale to the database
            ServiceFicheMedicale serviceFicheMedicale = new ServiceFicheMedicale();
            serviceFicheMedicale.ajouter(ficheMedicale);

            // Show success message
            showAlert("Success", "Fiche medicale added successfully.");

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numeric values for ID Patient and ID Therapeute.");
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while adding fiche medicale: " + e.getMessage());
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void ReturnShowFiches(ActionEvent actionEvent) {
        try {
            Node displayCons = FXMLLoader.load(getClass().getResource("/Front/FicheMedicale/AffichageFiche.fxml"));
            FichePane.getChildren().setAll(displayCons);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, for example, by showing an error message
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
