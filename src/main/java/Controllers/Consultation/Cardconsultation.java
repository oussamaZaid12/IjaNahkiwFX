package Controllers.Consultation;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import entities.Consultation;
import entities.FicheMedicale;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import services.ServiceConsultation;
import services.ServiceFicheMedicale;
import services.UserService;
import test.MainFX;

import java.io.IOException;
import java.io.InputStream;
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
    private Consultation currentConsultation;
    private AffichageConsultation affichagePubController;
    private AffichageConsultationpatient affichagePubControllerpatient;

    public void setAffichageConsController(AffichageConsultation controller) {
        this.affichagePubController = controller;
    }

    public void setConsultation(Consultation consultation) {
        this.currentConsultation = consultation;
        PathologieCons.setText("Pathologie:" + consultation.getPathologie());
        dateCons.setText("Date de consultation:" +consultation.getDateC().toString());
        UserService serv = new UserService();
        String email = serv.getUserById(consultation.getIdp()).getEmail();
        idPatient.setText("ID Patient:" +email);
        //idTherapeute.setText("ID Therapeute:" +String.valueOf(consultation.getIdt()));
        remarques.setText("Remarques:" +consultation.getRemarques());
    }

    @FXML
    private void handleEditAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/Consultation/EditConsultationDoctor.fxml"));
            Parent root = loader.load();
            EditConsultationDoctor controller = loader.getController();
            controller.setConsultation(this.currentConsultation);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
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



    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void AttribuerFiche(ActionEvent actionEvent) {
        try {
            ServiceFicheMedicale serviceFiche = new ServiceFicheMedicale();
            int idp = currentConsultation.getIdp();
            int idt = currentConsultation.getIdt();
            FicheMedicale fiche = serviceFiche.getFicheByTherapistAndPatientId(idp, idt);
            System.out.println("La fiche trouvée dans la base: " + fiche);
            if(fiche.getId()==0){
                System.out.println("pas de fiche trouvée");
                serviceFiche.createFicheByTherapistAndPatientId(idp, idt);
                System.out.println("Fiche créée dans la base: ");}
            ServiceConsultation serviceConsultation = new ServiceConsultation();
            serviceConsultation.modifyFicheMedicaleId(currentConsultation.getId(), fiche.getId());
            System.out.println("Consultation modifiée dans la base");
            showAlert("Succès", "Fiche attribuée");
            if (affichagePubController != null) {
                affichagePubController.refreshConsultationsView();
                System.out.println("Refresh method called");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'accès à la base de données:");
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de l'accès à la base de données.");
        }
    }
}