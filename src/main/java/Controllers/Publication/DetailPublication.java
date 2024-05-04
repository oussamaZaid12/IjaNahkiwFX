package Controllers.Publication;

import Controllers.User.Session;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import entities.Commentaire;
import entities.Role;
import entities.User;
import entities.publication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import services.ServiceCommentaire;
import services.ServiceLike;
import services.UserService;

import java.io.IOException;
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
    @FXML
    private AnchorPane detailsPubPane;
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
        User currentUser = Session.getUser(); // Retrieve the current user from the session
        if (currentUser == null) {
            showAlert("Erreur", "Aucun utilisateur connecté. Veuillez vous connecter pour ajouter des commentaires.");
            return; // Stop the method if no user is logged in
        }

        try {
            String contenu = tfAddComment.getText();
            String filteredComment = filterProfanity(contenu); // Filter the comment for profanity
            int idUser = currentUser.getId(); // Use the user ID from the session
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
    private boolean containsAsterisks(String content) {
        return content.contains("*");
    }

    private void displayComments() {
        try {
            List<Commentaire> commentaires = serviceCommentaire.getCommentairesByPublication(currentPublication.getId());
            commentsContainer.getChildren().clear();
            for (Commentaire commentaire : commentaires) {
                HBox commentBox = new HBox();
                commentBox.setSpacing(10); // Space between elements in the hbox

                // Check if the user is an admin or a regular user and add respective icons
                // Assuming ServiceUser is the service used to fetch user information
                UserService serviceUser = new UserService();
                User userWhoCommented = serviceUser.getUserById(commentaire.getId_user());

// Check if the user is an admin
                String iconPath = userWhoCommented != null && userWhoCommented.getRole() == Role.ROLE_ADMIN
                        ? "/images/doctoricon.png"
                        : "/images/patienticon.png";

// Load the appropriate icon
                Image userIcon = new Image(getClass().getResourceAsStream(iconPath));
                ImageView userIconView = new ImageView(userIcon);
                userIconView.setFitHeight(20);
                userIconView.setFitWidth(20);

// Add the icon to the comment box
                commentBox.getChildren().add(userIconView);


                // Add comment label
                Label commentLabel = new Label(userWhoCommented.getNom() + commentaire.getId_user() + " : " + commentaire.getContenu_c());
                commentBox.getChildren().add(commentLabel);

                // Check for asterisks and add a warning icon if found
                if (containsAsterisks(commentaire.getContenu_c())) {
                    ImageView warningIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/notificon.png")));
                    warningIcon.setFitHeight(20);
                    warningIcon.setFitWidth(20);
                    commentBox.getChildren().add(warningIcon);
                }

                // Add delete button with icon
                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/166475.png")));
                deleteIcon.setFitHeight(20);
                deleteIcon.setFitWidth(20);
                Button deleteButton = new Button();
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setOnAction(event -> handleDeleteComment(commentaire));
                deleteButton.setStyle("-fx-background-color: transparent; -fx-border: none;");
                commentBox.getChildren().add(deleteButton);

                commentsContainer.getChildren().add(commentBox);
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
        User currentUser = Session.getUser();  // Get the currently logged-in user from the session
        if (currentUser == null) {
            showAlert("Error", "No user logged in. Please log in to like publications.");
            return;  // Stop further execution if no user is logged in
        }

        int userId = currentUser.getId();  // Get the user ID from the session

        try {
            serviceLike.addOrUpdateLike(userId, currentPublication.getId(), true);
            updateLikeDislikeCounts();
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while updating the like status: " + e.getMessage());
            e.printStackTrace();  // Print stack trace for debugging
        }
    }


    @FXML
    private void handleDislikeAction(ActionEvent event) {
        User currentUser = Session.getUser();  // Retrieve the current user from the session
        if (currentUser == null) {
            showAlert("Error", "No user logged in. Please log in to dislike publications.");
            return;  // Stop further execution if no user is logged in
        }

        int userId = currentUser.getId();  // Get the user ID from the session

        try {
            serviceLike.addOrUpdateLike(userId, currentPublication.getId(), false);  // false indicates a dislike
            updateLikeDislikeCounts();
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while updating the dislike status: " + e.getMessage());
            e.printStackTrace();  // Print stack trace for debugging
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
    @FXML
    public void ReturnShowPublications() {
        try {
            Node displayPubs = FXMLLoader.load(getClass().getResource("/Front/Publication/affichagePub.fxml"));
            detailsPubPane.getChildren().setAll(displayPubs);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, for example, by showing an error message
        }
    }


}