package Controllers.Consultation;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import entities.Consultation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import services.ServiceConsultation;
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

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnModifier;

    private Consultation currentConsultation;
    private AffichageConsultation affichagePubController;
    private AffichageConsultationpatient affichagePubControllerpatient;

    public void setAffichageConsController(AffichageConsultation controller) {
        this.affichagePubController = controller;
    }
    public void setAffichageConsController(AffichageConsultationpatient controller) {
        this.affichagePubControllerpatient = controller;
    }

    public void setConsultation(Consultation consultation) {
        this.currentConsultation = consultation;
        PathologieCons.setText("Pathologie:" + consultation.getPathologie());
        dateCons.setText("Date de consultation:" +consultation.getDateC().toString());
        idPatient.setText("ID Patient:" +String.valueOf(consultation.getIdp()));
        idTherapeute.setText("ID Therapeute:" +String.valueOf(consultation.getIdt()));
        remarques.setText("Remarques:" +consultation.getRemarques());
    }

    @FXML
    private void handleEditAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/Consultation/EditConsultationDoctor.fxml"));
            Parent root = loader.load();
            EditConsultationDoctor controller = loader.getController();
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
        if (event.getClickCount() == 2) {
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

    @FXML
    private void handleDownloadPDF(ActionEvent event) {
        String path = System.getProperty("user.home") + "\\Desktop\\output.pdf";

        try {
            PdfWriter writer = new PdfWriter(path);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Adding a logo to the PDF
            InputStream logoStream = getClass().getResourceAsStream("/images/logo ff.png"); // Adjust the path to where your logo is stored
            if (logoStream != null) {
                byte[] logoData = logoStream.readAllBytes();
                ImageData logo = ImageDataFactory.create(logoData);
                com.itextpdf.layout.element.Image pdfImage = new com.itextpdf.layout.element.Image(logo);

                pdfImage.setWidth(50); // Set the width as per your requirement
                pdfImage.setHeight(50); // Set the height as per your requirement
                document.add(pdfImage);
            }

            document.add(new Paragraph("Pathologie: " + PathologieCons.getText()));
            document.add(new Paragraph("Remarque: " + remarques.getText()));
            document.add(new Paragraph("Date: " + dateCons.getText()));
            document.close();
            showAlert("Success", "PDF created successfully at " + path);
        } catch (Exception e) {
            showAlert("Error", "Failed to create PDF. " + e.getMessage());
        }

    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}