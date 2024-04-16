package Controllers.FicheMedicale;

import entities.FicheMedicale;
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
            // Retrieve data from input fields
            LocalDate localDateCreation = tfdatecreation.getValue();
            Date dateCreation = Date.valueOf(localDateCreation);

            LocalDate localDateDerniereMaj = tfdernieremaj.getValue();
            Date dateDerniereMaj = Date.valueOf(localDateDerniereMaj);
            int idPatient = Integer.parseInt(tfidp.getText());
            int idTherapeute = Integer.parseInt(tfidt.getText());

            // Create a new FicheMedicale object with the retrieved data
            FicheMedicale ficheMedicale = new FicheMedicale(dateCreation, dateDerniereMaj, idPatient, idTherapeute);

            // Call a service method to add the fiche medicale to the database
            ServiceFicheMedicale serviceFicheMedicale = new ServiceFicheMedicale();
            serviceFicheMedicale.ajouter(ficheMedicale);

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Fiche medicale added successfully.");
            alert.showAndWait();

        } catch (NumberFormatException e) {
            // Handle NumberFormatException
            showAlert("Error", "Please enter valid numeric values for ID Patient and ID Therapeute.");
        } catch (SQLException e) {
            // Handle SQLException
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
}
