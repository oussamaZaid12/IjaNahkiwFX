package Controllers.Publication;

import entities.Commentaire;
import entities.publication;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import services.ServiceCommentaire;

import java.sql.SQLException;
import java.util.List;

public class DetailPublication {

    @FXML
    private TextField TfIdUserC;
    @FXML
    private Button btnAddComment;
    @FXML
    private Label datePubDetails;
    @FXML
    private Label descriptionPubDetails;
    @FXML
    private ImageView imagePubDetails;
    @FXML
    private TextField tfAddComment;
    @FXML
    private Label titrePubDetails;
    @FXML
    private VBox commentsContainer;

    private publication currentPublication;
    private ServiceCommentaire serviceCommentaire = new ServiceCommentaire();

    public void setPublication(publication pub) {
        currentPublication = pub;
        titrePubDetails.setText(pub.getTitreP());
        descriptionPubDetails.setText(pub.getDescriptionP());
        datePubDetails.setText(pub.getDateP().toString());
        Image image = new Image(getClass().getResourceAsStream("/images/" + pub.getImageP()));
        imagePubDetails.setImage(image);
        displayComments();
    }

    @FXML
    void handleAddComment(ActionEvent event) {
        try {
            String contenu = tfAddComment.getText();
            int idUser = Integer.parseInt(TfIdUserC.getText());
            int publicationId = currentPublication.getId();
            Commentaire nouveauCommentaire = new Commentaire(publicationId, contenu, idUser);
            serviceCommentaire.ajouter(nouveauCommentaire);
            displayComments(); // Refresh comments display
            tfAddComment.clear(); // Clear the comment input field
            showAlert("Succès", "Commentaire ajouté avec succès !");
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur est survenue lors de l'ajout du commentaire.");
            e.printStackTrace();
        }
    }
    private void handleDeleteComment(Commentaire commentaire) {
        try {
            serviceCommentaire.supprimer(commentaire);
            displayComments(); // Refresh comments display
            showAlert("Succès", "Commentaire supprimé avec succès !");
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur est survenue lors de la suppression du commentaire.");
            e.printStackTrace();
        }
    }

    private void displayComments() {
        try {
            List<Commentaire> commentaires = serviceCommentaire.getCommentairesByPublication(currentPublication.getId());
            commentsContainer.getChildren().clear();
            for (Commentaire commentaire : commentaires) {
                Label commentLabel = new Label("Utilisateur " + commentaire.getId_user() + " : " + commentaire.getContenu_c());
                Button deleteButton = new Button("Supprimer");
                deleteButton.setOnAction(event -> handleDeleteComment(commentaire));
                commentsContainer.getChildren().addAll(commentLabel, deleteButton);
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur est survenue lors de l'affichage des commentaires.");
            e.printStackTrace();
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
