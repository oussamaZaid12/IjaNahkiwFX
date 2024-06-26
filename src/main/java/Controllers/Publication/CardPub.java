package Controllers.Publication;

import Controllers.User.Session;
import entities.Commentaire;
import entities.Role;
import entities.User;
import entities.publication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import services.ServiceCommentaire;
import services.ServicePublication;
import services.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputFilter;
import java.sql.SQLException;
import java.util.List;

public class CardPub {
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnModifier;
    @FXML
    private ImageView photoprofil;
    @FXML
    private AnchorPane affichagefrontContainer;
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
    @FXML
    private ImageView warningIcon;
    private publication currentPublication;
    private AffichagePub affichagePubController;
    private ServiceCommentaire serviceCommentaire = new ServiceCommentaire();
    private static final String IMAGES_DIR = "C:\\Users\\Tifa\\Desktop\\symfonypull13.02\\public\\upload-images\\";



    public void setAffichagePubController(AffichagePub controller) {
        this.affichagePubController = controller;
    }
    public void setAffichagefrontContainer(AnchorPane affichagefrontContainer) {
        this.affichagefrontContainer = affichagefrontContainer;
    }
    @FXML
    public void initialize() {

        warningIcon.setVisible(true); // Temporarily force visibility
    }


    public void setPublication(publication publication) {
        this.currentPublication = publication;
        titrePub.setText(publication.getTitreP());
        descriptionPub.setText(publication.getDescriptionP());
        datePub.setText(publication.getDateP().toString());

        // Retrieve user information based on the user ID in the publication
        UserService serviceUser = new UserService();
        User author = serviceUser.getUserById(publication.getIdUserId());

        if (author != null) {
            // Display the author's name and surname
            idUserPub.setText(author.getNom() + " " + author.getPrenom());

            // Load the author's profile picture
            String profileImagePath = "file:/" + IMAGES_DIR + author.getImage();
            Image profileImage = new Image(profileImagePath);
            photoprofil.setImage(profileImage);
        } else {
            // Handle the case where the user cannot be found
            idUserPub.setText("Auteur inconnu");
            // Use a default profile image when the specified image is not found
            photoprofil.setImage(new Image("file:/" + IMAGES_DIR + "default_user.png"));
        }

        // Load publication image
        String publicationImagePath = "file:/" + IMAGES_DIR + publication.getImageP();
        Image publicationImage = new Image(publicationImagePath);
        imagePub.setImage(publicationImage);
        updateButtonVisibility();
        updateWarningIconVisibility();
    }




    private void updateButtonVisibility() {
        User currentUser = Session.getUser();
        // Show delete button only if the current user is the poster or an admin
        boolean canEditDelete = currentUser != null &&
                (currentUser.getRole() == Role.ROLE_ADMIN);
        btnDelete.setVisible(canEditDelete);
        btnModifier.setVisible(canEditDelete);
    }

    @FXML
    private void handleEditAction(ActionEvent event) {


        try {


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/Publication/editPub.fxml"));
            System.out.println("try to edit");
            Parent root = loader.load();
            editPub controller = loader.getController();
            controller.setPublication(this.currentPublication);
            Stage stage=new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            //MainFX.setCenterView(root);
        } catch (IOException e) {
            System.out.printf(e.getMessage());;
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
        if (event.getClickCount() == 2) {
            FXMLLoader loader = new FXMLLoader();
            try {
                User currentUser = Session.getUser();
                String resourcePath = currentUser != null && currentUser.getRole() == Role.ROLE_ADMIN
                        ? "/Back/Publication/DetailPublication.fxml"
                        : "/Front/Publication/DetailPublication.fxml";
                loader.setLocation(getClass().getResource(resourcePath));
                Parent detailView = loader.load();

                // Retrieve the controller to set the publication data
                DetailPublication controller = loader.getController();
                controller.setPublication(this.currentPublication);

                // Clear and then add the detail view within affichagefrontContainer
                affichagefrontContainer.getChildren().clear();
                affichagefrontContainer.getChildren().add(detailView);

            } catch (IOException e) {
                System.err.println("Failed to load detail view: " + e.getMessage());
                showAlert("Error", "Cannot load the detail view: " + e.getMessage());
            }
        }
    }








    private void updateWarningIconVisibility() {
        try {
            boolean hasProfanity = checkForProfanity(currentPublication);
            System.out.println("Updating warning icon visibility. Profanity found: " + hasProfanity);
            warningIcon.setVisible(hasProfanity);}
        catch (SQLException e) {
            e.printStackTrace();
            warningIcon.setVisible(false);
        }

    }


    private boolean checkForProfanity(publication pub) throws SQLException {
        List<Commentaire> comments = serviceCommentaire.getCommentairesByPublication(pub.getId());
        for (Commentaire comment : comments) {
            System.out.println("Comment Content: " + comment.getContenu_c());  // Debugging line to print comment content
            if (containsProfanity(comment.getContenu_c())) {
                return true;
            }
        }
        return false;
    }


    private boolean containsProfanity(String content) {
        return content.contains("*");
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Utilisez AlertType.ERROR pour les erreurs
        alert.setTitle(title);
        alert.setHeaderText(null); // Pas de texte d'en-tête
        alert.setContentText(content);
        alert.showAndWait(); // Affiche l'alerte et attend que l'utilisateur la ferme
    }



}