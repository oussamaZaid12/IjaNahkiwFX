package Controllers.FicheMedicale;

import Controllers.User.Session;
import entities.FicheMedicale;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import services.ServiceFicheMedicale;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;

public class EditFiche {
    @FXML
    private TextField tfid;

    @FXML
    private TextField tfidp;

    @FXML
    private TextField tfidt;

    @FXML
    private DatePicker tfdatedecreation;

    @FXML
    private DatePicker tfdatemiseajour;

    private FicheMedicale currentFiche;
    private ServiceFicheMedicale serviceFicheMedicale = new ServiceFicheMedicale();

    public void setFiche(FicheMedicale currentFiche) {
        this.currentFiche = currentFiche;
        tfidp.setText(String.valueOf(currentFiche.getIdp()));
        tfidt.setText(String.valueOf(currentFiche.getIdt()));
        LocalDate localDateCreation =currentFiche.getDateCreation().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDateMiseAjout = currentFiche.getDateCreation().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        // Set the values of the DatePicker components
        tfdatedecreation.setValue(localDateCreation);
        tfdatemiseajour.setValue(localDateMiseAjout);
    }

    @FXML
    public void ModifierFiche(ActionEvent actionEvent) {
        try {
            User currentUser = Session.getUser();
            if (currentUser == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur de session", "Aucun utilisateur connecté.");
                return;
            }
            int idUser = currentUser.getId();
            // Parse IDs from text fields
            int id = Integer.parseInt(tfid.getText());
            int idp = Integer.parseInt(tfidp.getText());
            //  int idt = Integer.parseInt(tfidt.getText());

            // Retrieve and validate dates
            LocalDate dateCreation = tfdatedecreation.getValue();
            if (dateCreation == null) {
                showAlert("Input Error", "Please enter a valid creation date.");
                return;
            }

            LocalDate dateMiseAjout = tfdatemiseajour.getValue();
            if (dateMiseAjout == null) {
                showAlert("Input Error", "Please enter a valid update date.");
                return;
            }

            // Check date consistency
            if (dateMiseAjout.isBefore(dateCreation)) {
                showAlert("Input Error", "The last update date cannot be before the creation date.");
                return;
            }

            // Set updated values to currentFiche
            currentFiche.setId(id);
            currentFiche.setIdp(idp);
            currentFiche.setIdt(idUser);
            currentFiche.setDateCreation(Date.valueOf(dateCreation));
            currentFiche.setDerniereMaj(Date.valueOf(dateMiseAjout));

            // Update fiche in database
            serviceFicheMedicale.modifier(currentFiche);

            // Show success message
            showAlert("Success", "Fiche medicale has been updated successfully.");
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numeric values for IDs.");
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while updating fiche medicale: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
