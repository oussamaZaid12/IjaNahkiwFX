package Controllers.Publication;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import entities.Commentaire;
import entities.publication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import services.ServiceCommentaire;
import services.ServiceLike;

import java.io.InputStream;
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
    private Label descriptionPubDetails, likeCountLabel, dislikeCountLabel;;
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
    private ServiceLike serviceLike = new ServiceLike();


    public void setPublication(publication pub) {
        currentPublication = pub;
        titrePubDetails.setText(pub.getTitreP());
        descriptionPubDetails.setText(pub.getDescriptionP());
        datePubDetails.setText(pub.getDateP().toString());
        Image image = new Image(getClass().getResourceAsStream("/images/" + pub.getImageP()));
        imagePubDetails.setImage(image);
        displayComments();
        updateView();
    }

    @FXML
    void handleAddComment(ActionEvent event) {
        try {
            String contenu = tfAddComment.getText();
            String filteredComment = filterProfanity(contenu); // Filter the comment for profanity
            int idUser = Integer.parseInt(TfIdUserC.getText());
            int publicationId = currentPublication.getId();

            // Ensure that the filtered comment is used when creating the new Commentaire object
            Commentaire nouveauCommentaire = new Commentaire(publicationId, filteredComment, idUser);

            serviceCommentaire.ajouter(nouveauCommentaire);
            displayComments(); // Refresh comments display
            tfAddComment.clear(); // Clear the comment input field
            showAlert("Succès", "Commentaire ajouté avec succès !");
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur est survenue lors de l'ajout du commentaire.");
            e.printStackTrace();
        }
    }
    // Implement a simple profanity filter
    private String filterProfanity(String text) {
        String[] profanities = {"fuck", "bitch"}; // Define your list of bad words
        for (String word : profanities) {
            text = text.replaceAll("(?i)" + word, "*".repeat(word.length())); // Replace with asterisks
        }
        return text;
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

            document.add(new Paragraph("Title: " + titrePubDetails.getText()));


            InputStream imageStream = getClass().getResourceAsStream("/images/" + currentPublication.getImageP());
            if (imageStream != null) {
                byte[] imageData = imageStream.readAllBytes();
                ImageData data = ImageDataFactory.create(imageData);
                // Use fully qualified name for iText Image
                com.itextpdf.layout.element.Image pdfImage = new com.itextpdf.layout.element.Image(data);
                document.add(pdfImage); // Add the image to the document
            }
            document.add(new Paragraph("Description: " + descriptionPubDetails.getText()));
            document.add(new Paragraph("Date: " + datePubDetails.getText()));
            document.close();
            showAlert("Success", "PDF created successfully at " + path);
        } catch (Exception e) {
            showAlert("Error", "Failed to create PDF. " + e.getMessage());
        }

    }


    private void updateView() {
        // Update the text fields with the publication data
        titrePubDetails.setText(currentPublication.getTitreP());
        descriptionPubDetails.setText(currentPublication.getDescriptionP());
        datePubDetails.setText(currentPublication.getDateP().toString());

        // Load the image for the publication
        try {
            Image image = new Image(getClass().getResourceAsStream("/images/" + currentPublication.getImageP()));
            imagePubDetails.setImage(image);
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
            // Set a default image or handle the error
            imagePubDetails.setImage(new Image(getClass().getResourceAsStream("/images/default.png")));
        }

        // Display comments related to the publication
        displayComments();

        // Update like and dislike counts on the UI
        updateLikeDislikeCounts();
    }


    @FXML
    private void handleLikeAction(ActionEvent event) {
        int userId = 88; // bach ntasty
        try {
            serviceLike.addOrUpdateLike(userId, currentPublication.getId(), true);
            updateLikeDislikeCounts();
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while updating the like status.");
        }
    }

    @FXML
    private void handleDislikeAction(ActionEvent event) {
        int userId = 88; // tesssst
        try {
            serviceLike.addOrUpdateLike(userId, currentPublication.getId(), false);
            updateLikeDislikeCounts();
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while updating the dislike status.");
        }
    }

    private void updateLikeDislikeCounts() {
        try {
            int likes = serviceLike.countLikes(currentPublication.getId());
            int dislikes = serviceLike.countDislikes(currentPublication.getId());
            likeCountLabel.setText(String.valueOf(likes));
            dislikeCountLabel.setText(String.valueOf(dislikes));
        } catch (SQLException e) {
            showAlert("Error", "Failed s: " + e.getMessage());
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
