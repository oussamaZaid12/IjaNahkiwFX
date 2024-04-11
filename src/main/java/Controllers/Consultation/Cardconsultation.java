package Controllers.Consultation;

import entities.Consultation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import services.ServiceConsultation;
import test.MainFX;

import java.io.IOException;
import java.sql.SQLException;

public class Cardconsultation {

    @FXML
    private Label PathologieCons;

    @FXML
    private Label dateCons;

    @FXML
    private Label idPatient;

    @FXML
    private Label idTherapeute;

    @FXML
    private Label remarques;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnModifier;
    private Consultation currentConsultation;
    private AffichageConsultation affichagePubController;

    public void setAffichageConsController(AffichageConsultation controller) {
        this.affichagePubController = controller;
    }

    public void setConsultation(Consultation consultation) {
        this.currentConsultation = consultation;
        PathologieCons.setText(consultation.getPathologie());
        dateCons.setText(consultation.getDateC().toString());
        idPatient.setText(String.valueOf(consultation.getIdp()));
        idTherapeute.setText(String.valueOf(consultation.getIdt()));
        remarques.setText(consultation.getRemarques());
    }

    @FXML
    private void handleEditAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/Consultation/EditConsultation.fxml"));
            Parent root = loader.load();
            EditConsultation controller = loader.getController();
            controller.setConsultation(this.currentConsultation);
            MainFX.setCenterView(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteAction(ActionEvent event) {
        try {
            ServiceConsultation serviceConsultation = new ServiceConsultation();
            serviceConsultation.supprimer(currentConsultation); // Delete the consultation
            System.out.println("Consultation deleted successfully");

            // Refresh the consultations view
            if (affichagePubController != null) {
                affichagePubController.refreshConsultationsView();
                System.out.println("Refresh method called");
            } else {
                System.out.println("affichagePubController is null");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception, for example, by showing an error message
        }
    }

    @FXML
    private void handleCardClick(MouseEvent event) {
        if (event.getClickCount() == 2) { // for example, for a double-click
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/Consultation/DetailConsultation.fxml"));
                Parent detailView = loader.load();

                DetailConsultation controller = loader.getController();
                controller.setConsultation(this.currentConsultation);

                // Assume you have a static method in MainFX to change the view at the center
                MainFX.setCenterView(detailView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
