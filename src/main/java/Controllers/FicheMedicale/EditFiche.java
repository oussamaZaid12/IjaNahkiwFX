package Controllers.FicheMedicale;

import entities.FicheMedicale;
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
            int id = Integer.parseInt(tfid.getText());
            int idp = Integer.parseInt(tfidp.getText());
            int idt = Integer.parseInt(tfidt.getText());
            LocalDate dateCreation = tfdatedecreation.getValue();
            LocalDate dateMiseAjout = tfdatemiseajour.getValue();

            currentFiche.setId(id);
            currentFiche.setIdp(idp);
            currentFiche.setIdt(idt);
            currentFiche.setDateCreation(Date.valueOf(dateCreation));
            currentFiche.setDerniereMaj(Date.valueOf(dateMiseAjout));

            serviceFicheMedicale.modifier(currentFiche);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Fiche medicale has been updated successfully.");
            alert.showAndWait();
        } catch (NumberFormatException | SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
