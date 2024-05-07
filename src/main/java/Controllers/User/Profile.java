package Controllers.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import entities.User;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.UserService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

public class Profile {
    @FXML
    private TextField nom;
    @FXML
    private TextField prenom;
    @FXML
    private TextField email;

    @FXML
    private TextField age;
    @FXML
    private ImageView profileImageView;
    @FXML
    private Button editButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button uploadButton;
    @FXML
    private Button logoutButton;

    private User currentUser;
    private UserService userService = new UserService();

    public void initData(User user) {
        currentUser = user;
        if (currentUser != null) {
            populateProfileInformation(currentUser);
        }
    }

    private void populateProfileInformation(User user) {
        if (user != null) {
            // Affichage du nom et du prénom
            nom.setText(user.getNom());
            prenom.setText(user.getPrenom());

// Affichage de l'email
            email.setText(user.getEmail());

// Affichage de l'âge
            int userAge = user.getAge();
            age.setText(String.valueOf(userAge));
            // Chargement et affichage de l'image du profil
            if (user.getImage() != null && !user.getImage().isEmpty()) {
                String imagePath = "C:\\Users\\eya\\Desktop\\ekher pull\\src\\main\\resources\\static\\" + user.getImage();
                File file = new File(imagePath);
                if (file.exists()) {
                    try {
                        Image image = new Image(file.toURI().toString());
                        profileImageView.setImage(image);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("L'image n'existe pas à l'emplacement spécifié : " + imagePath);
                }
            }
        }
    }





    @FXML
    private void enabelEditing() {
        nom.setDisable(false);
        prenom.setDisable(false);
        email.setDisable(false);
        age.setDisable(false);
        updateButton.setVisible(true);
    }

    @FXML
    private void UpdateData() {
        if (currentUser == null) {
            System.out.println("Erreur : currentUser est null.");
            return;
        }

        try {
            currentUser.setNom(nom.getText());
            currentUser.setPrenom(prenom.getText());
            currentUser.setEmail(email.getText());
            currentUser.setAge(Integer.parseInt(age.getText()));
            userService.updateProfile(currentUser);

            // Afficher un message de succès
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Profil mis à jour", "Votre profil a été mis à jour avec succès.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la mise à jour du profil", "Une erreur s'est produite lors de la mise à jour de votre profil : " + e.getMessage());
        }
    }


    @FXML
    private void uploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image de profil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers d'image", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        Stage stage = (Stage) uploadButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            // Obtenez le chemin absolu de l'image sélectionnée
            String imagePath = selectedFile.getAbsolutePath();

            // Mettez à jour l'image de profil dans l'interface utilisateur
            try {
                Image image = new Image(new FileInputStream(selectedFile));
                profileImageView.setImage(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // Mettez à jour le chemin de l'image dans l'objet User
            if (currentUser != null) {
                currentUser.setImage(imagePath);
                // Mettez à jour l'utilisateur dans la base de données si nécessaire
                userService.updateProfile(currentUser);
                // Afficher un message de succès
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Image de profil mise à jour", "Votre image de profil a été mise à jour avec succès.");
            }
        }
    }


    @FXML
    private void logout(ActionEvent event) {
        Node node = (Node) event.getSource(); // Récupérer le nœud source de l'événement
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();

        try {
            Stage newStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/gui/login.fxml"));
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
