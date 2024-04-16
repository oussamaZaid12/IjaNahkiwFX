package Controllers.FicheMedicale;

import entities.FicheMedicale;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import services.ServiceFicheMedicale;
import test.MainFX;

import java.io.IOException;
import java.sql.SQLException;

public class Cardfiche {
    @FXML
    private AnchorPane FichePane;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnModifier;
    @FXML
    private Label tfdatecreation;
    @FXML
    private Label tfdatemiseajour;
    @FXML
    private Label tfid;
    @FXML
    private Label tfidp;
    @FXML
    private Label tfidt;
    private FicheMedicale currentFiche;
    private AffichageFiche affichageFicheController;

    public void setAffichageFicheController(AffichageFiche controller) {
        this.affichageFicheController = controller;
    }

    public void setFiche(FicheMedicale fiche) {
        this.currentFiche = fiche;
        tfdatecreation.setText(currentFiche.getDateCreation().toString());
        tfdatemiseajour.setText(currentFiche.getDerniereMaj().toString());
        tfid.setText(String.valueOf(currentFiche.getId()));
        tfidp.setText(String.valueOf(currentFiche.getIdp()));
        tfidt.setText(String.valueOf(currentFiche.getIdt())); // Update tfidt with idt value
    }

    @FXML
    private void handleEditAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/FicheMedicale/EditFiche.fxml"));
            Parent root = loader.load();
            EditFiche controller = loader.getController();
            controller.setFiche(this.currentFiche);
            MainFX.setCenterView(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteAction(ActionEvent event) {
        try {
            ServiceFicheMedicale serviceFicheMedicale = new ServiceFicheMedicale();
            serviceFicheMedicale.supprimer(currentFiche); // Delete the fiche medicale
            System.out.println("Fiche medicale deleted successfully");

            // Refresh the fiche medicale view
            if (affichageFicheController != null) {
                affichageFicheController.refreshFichesView();
                System.out.println("Refresh method called");
            } else {
                System.out.println("affichageFicheController is null");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception, for example, by showing an error message
        }
    }

    @FXML
    public void generateqrcode(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/FicheMedicale/qrcode.fxml"));
            Parent root = loader.load();
            Qrcode controller = loader.getController();
            controller.setFiche(this.currentFiche); // Pass the current fiche to the Qrcode controller
            MainFX.setCenterView(root);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, for example, by showing an error message
        }
    }
}
