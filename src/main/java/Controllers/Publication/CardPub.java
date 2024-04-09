package Controllers.Publication;

import entities.publication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import services.ServicePublication;
import test.MainFX;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class CardPub {
    @FXML
    private Button btnDelete;
    @FXML
    private AnchorPane cardPane;
    @FXML
    private ImageView imagePub;
    @FXML
    private Label titrePub;
    @FXML
    private Label descriptionPub;
    @FXML
    private Label datePub;
    @FXML
    private Label idUserPub;

    private publication currentPublication;
    private AffichagePub affichagePubController;

    public void setAffichagePubController(AffichagePub controller) {
        this.affichagePubController = controller;
    }

    public void setPublication(publication publication) {
        this.currentPublication = publication;
        titrePub.setText(publication.getTitreP());
        descriptionPub.setText(publication.getDescriptionP());
        idUserPub.setText(String.valueOf(publication.getIdUserId()));
        datePub.setText(publication.getDateP().toString());

        // Ajuster la visibilité des boutons en fonction de l'ID de l'utilisateur
        boolean isUserOne = publication.getIdUserId() != 1;
        btnDelete.setVisible(isUserOne);

        String imagePath = "/images/" + publication.getImageP();
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        if (imageStream != null) {
            Image image = new Image(imageStream);
            imagePub.setImage(image);
        } else {
            // Set a default image or leave it blank
        }
    }
    @FXML
    private void handleEditAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/Publication/editPub.fxml"));
            Parent root = loader.load();
            editPub controller = loader.getController();
            controller.setPublication(this.currentPublication);
            MainFX.setCenterView(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @FXML
    private void handleDeleteAction(ActionEvent event) {
        try {
            ServicePublication servicePublication = new ServicePublication();
            servicePublication.supprimer(currentPublication); // Delete the publication
            System.out.println("Publication deleted successfully");

            // Refresh the publications view
            if (affichagePubController != null) {
                affichagePubController.refreshPublicationsView();
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
        if (event.getClickCount() == 2) { // pour un double-clic, par exemple
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/Publication/DetailPublication.fxml"));
                Parent detailView = loader.load();

                DetailPublication controller = loader.getController();
                controller.setPublication(this.currentPublication);

                // Supposons que vous avez une méthode statique dans MainFX pour changer la vue au centre
                MainFX.setCenterView(detailView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
