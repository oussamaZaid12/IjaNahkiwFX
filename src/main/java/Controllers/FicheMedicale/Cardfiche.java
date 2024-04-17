package Controllers.FicheMedicale;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import entities.FicheMedicale;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import services.ServiceFicheMedicale;
import test.MainFX;

import java.io.IOException;
import java.io.InputStream;
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
        tfidt.setText(String.valueOf(currentFiche.getIdt()));
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
            showAlert("Error", "Failed to load edit fiche view: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteAction(ActionEvent event) {
        try {
            ServiceFicheMedicale serviceFicheMedicale = new ServiceFicheMedicale();
            serviceFicheMedicale.supprimer(currentFiche);
            showAlert("Success", "Fiche médicale deleted successfully.");

            if (affichageFicheController != null) {
                affichageFicheController.refreshFichesView();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete fiche médicale: " + e.getMessage());
        }
    }

    @FXML
    public void generateqrcode(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/FicheMedicale/qrcode.fxml"));
            Parent root = loader.load();
            Qrcode controller = loader.getController();
            controller.setFiche(this.currentFiche);
            MainFX.setCenterView(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load QR code generation view: " + e.getMessage());
        }
    }

    @FXML
    private void handleDownloadPDF(ActionEvent event) {
        String path = System.getProperty("user.home") + "\\Desktop\\output.pdf";

        try {
            PdfWriter writer = new PdfWriter(path);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            InputStream logoStream = getClass().getResourceAsStream("/images/logo ff.png");
            if (logoStream != null) {
                byte[] logoData = logoStream.readAllBytes();
                ImageData logo = ImageDataFactory.create(logoData);
                com.itextpdf.layout.element.Image pdfImage = new com.itextpdf.layout.element.Image(logo);
                pdfImage.setWidth(50);
                pdfImage.setHeight(50);
                document.add(pdfImage);
            }

            document.add(new Paragraph("Patient id: " + tfidp.getText()));
            document.add(new Paragraph("Therapeute id: " + tfidt.getText()));
            document.add(new Paragraph("Date de creation: " + tfdatecreation.getText()));
            document.close();
            showAlert("Success", "PDF created successfully at " + path);
        } catch (Exception e) {
            showAlert("Error", "Failed to create PDF: " + e.getMessage());
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
