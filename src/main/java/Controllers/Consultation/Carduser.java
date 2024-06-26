package Controllers.Consultation;

import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

import java.io.InputStream;


public class Carduser {

    @FXML
    private Label email;

    @FXML
    private ImageView imageuser;

    @FXML
    private Label nom;

    @FXML
    private Label prenom;

    @FXML
    private Button reserverConsultation;
    private User currenttherapist;
    private AjoutConsultation AjoutController;

    public void setAjoutController(AjoutConsultation controller) {
        this.AjoutController = controller;
    }

    @FXML
    private void handleReserverConsultation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/Consultation/AjoutConsultation.fxml"));
            Parent root = loader.load();
            AjoutConsultation controller = loader.getController();
            controller.setidtherapist(this.currenttherapist.getId());
            //  MainFX.setCenterView(root);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUser(User user) {
        this.currenttherapist = user;
        nom.setText(user.getNom()); // Set the nom value
        prenom.setText(user.getPrenom()); // Set the prenom value
        email.setText(user.getEmail()); // Set the email value
        String imagePath = "/images/"+ user.getImage();
        InputStream imageStream = getClass().getResourceAsStream(imagePath);

        // Check if the input stream is null and assign a default image if necessary
        if (imageStream != null) {
            Image image = new Image(imageStream);
            imageuser.setImage(image);
        } else {
            // Use a default image for the publication if the specified image is not found
            imageuser.setImage(new Image(getClass().getResourceAsStream("/images/therapeut.png")));
        }
    }
}