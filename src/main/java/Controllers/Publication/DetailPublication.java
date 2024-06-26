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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.ServiceCommentaire;
import services.ServiceLike;
import services.UserService;

import java.io.File;
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
    private static final String IMAGES_DIR = "C:\\Users\\Tifa\\Desktop\\symfonypull13.02\\public\\upload-images\\";



    public void setPublication(publication pub) {
        currentPublication = pub;
        titrePubDetails.setText(pub.getTitreP());
        descriptionPubDetails.setText(pub.getDescriptionP());
        datePubDetails.setText(pub.getDateP().toString());

        // Correct way to load images from a filesystem in JavaFX
        String imagePath = "file:/" + IMAGES_DIR + pub.getImageP().replace("\\", "/");
        Image image = new Image(imagePath);
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
        String[] profanities = {"fuck", "bitch","zebi","fuckk","ass"}; // Define your list of bad words
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

            // Assuming currentUser is the currently logged-in user
            User currentUser = Session.getUser();

            for (Commentaire commentaire : commentaires) {
                HBox commentBox = new HBox();
                commentBox.setSpacing(10); // Space between elements in the hbox

                // Retrieve the user who made the comment
                UserService serviceUser = new UserService();
                User userWhoCommented = serviceUser.getUserById(commentaire.getId_user());

                // Check if the user is an admin or a regular user and add respective icons
                String iconPath = userWhoCommented != null && userWhoCommented.getRole() == Role.ROLE_ADMIN
                        ? "/images/doctoricon.png"
                        : "/images/patienticon.png";

                // Load the appropriate icon
                Image userIcon = new Image(getClass().getResourceAsStream(iconPath));
                ImageView userIconView = new ImageView(userIcon);
                userIconView.setFitHeight(20);
                userIconView.setFitWidth(20);
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

                // Add delete button only if the current user is an admin or the one who wrote the comment
                if (currentUser != null && (currentUser.getRole() == Role.ROLE_ADMIN || currentUser.getId() == commentaire.getId_user())) {
                    ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/166475.png")));
                    deleteIcon.setFitHeight(20);
                    deleteIcon.setFitWidth(20);
                    Button deleteButton = new Button();
                    deleteButton.setGraphic(deleteIcon);
                    deleteButton.setOnAction(event -> handleDeleteComment(commentaire));
                    deleteButton.setStyle("-fx-background-color: transparent; -fx-border: none;");
                    commentBox.getChildren().add(deleteButton);
                }

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
            try {
                String logoPath = "C:\\Users\\Tifa\\Desktop\\symfonypull13.02\\public\\upload-images\\logo ff.png"; // Adjust the path to where your logo is stored
                File logoFile = new File(logoPath);
                ImageData logo = ImageDataFactory.create(logoFile.toURI().toString());
                com.itextpdf.layout.element.Image pdfImage = new com.itextpdf.layout.element.Image(logo);

                pdfImage.setWidth(50); // Set the width as per your requirement
                pdfImage.setHeight(50); // Set the height as per your requirement
                document.add(pdfImage);
            } catch (Exception e) {
                System.out.println("Error loading logo image: " + e.getMessage());
            }

            document.add(new Paragraph("Title: " + titrePubDetails.getText()));

            try {
                String imagePath = "C:\\Users\\Tifa\\Desktop\\symfonypull13.02\\public\\upload-images\\" + currentPublication.getImageP();
                File imageFile = new File(imagePath);
                ImageData data = ImageDataFactory.create(imageFile.toURI().toString());
                com.itextpdf.layout.element.Image pdfImage = new com.itextpdf.layout.element.Image(data);
                document.add(pdfImage); // Add the image to the document
            } catch (Exception e) {
                System.out.println("Error loading publication image: " + e.getMessage());
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
            String imagePath = "C:\\Users\\Tifa\\Desktop\\symfonypull13.02\\public\\upload-images\\" + currentPublication.getImageP();
            File file = new File(imagePath);
            Image image = new Image(file.toURI().toString());
            imagePubDetails.setImage(image);
        } catch (Exception e) {
            System.out.println("Error loading image from filesystem: " + e.getMessage());
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



    @FXML
    public void ReturnShowPublications() {
        try {
            // Get the current user
            User currentUser = Session.getUser();

            // Determine the path based on the user's role
            String resourcePath;
            if (currentUser != null && currentUser.getRole() == Role.ROLE_ADMIN) {
                resourcePath = "/Back/Publication/affichagePub.fxml";
            } else {
                // If the role is not "ROLE_USER" or the user is not authenticated, load the default front view
                resourcePath = "/Front/Publication/affichagePub.fxml";
            }

            // Load the corresponding FXML file
            Node displayPubs = FXMLLoader.load(getClass().getResource(resourcePath));
            detailsPubPane.getChildren().setAll(displayPubs);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Cannot load the view: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showChatbot(ActionEvent actionEvent) {
        try {
            // Load the chatbot window or overlay
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/Quiz/Chatbot.fxml")); // Adjust path as needed
            Parent chatbotRoot = loader.load();

            // Display the chatbot window as a modal or embedded
            Stage chatbotStage = new Stage();
            chatbotStage.setTitle("Chatbot");
            chatbotStage.setScene(new Scene(chatbotRoot));
            chatbotStage.initOwner(detailsPubPane.getScene().getWindow());
            chatbotStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}