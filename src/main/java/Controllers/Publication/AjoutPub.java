package Controllers.Publication;

import entities.publication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import services.ServicePublication;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;


public class AjoutPub {

            // Constants
            private static final String IMAGES_DIR = "src/main/resources/images/";

            @FXML private DatePicker TfDate;
            @FXML private TextField TfDescription;
            @FXML private TextField TfIdUser;
            @FXML private TextField TfTitre;
            @FXML private Button TfValider;
            @FXML private ImageView imagePreview; // Pour afficher l'aperçu de l'image

            private File imageFile; // Pour stocker le fichier image sélectionné
       /*     private void switchToDisplayPublicationsScene() {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichagePub.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) TfValider.getScene().getWindow();
                    stage.setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/


            @FXML
            private void AjouterPublication(ActionEvent event) {
                try {
                    String titre = TfTitre.getText();
                    String description = TfDescription.getText();
                    int idUser = Integer.parseInt(TfIdUser.getText());
                    LocalDate dateLocal = TfDate.getValue();
                    java.sql.Date date = java.sql.Date.valueOf(dateLocal);

                    if (titre.isEmpty() || description.isEmpty() || dateLocal == null || imageFile == null) {
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs et l'image doivent être remplis.");
                        return;
                    }

                    // Create the directory if it doesn't exist
                    Files.createDirectories(Paths.get(IMAGES_DIR));

                    // Copy the image to the images directory and get the filename
                    Path sourcePath = imageFile.toPath();
                    Path destinationPath = Paths.get(IMAGES_DIR + imageFile.getName());
                    Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    String imageName = destinationPath.getFileName().toString();

                    publication pub = new publication(idUser, titre, description, imageName, date);
                    ServicePublication servicePublication = new ServicePublication();
                    servicePublication.ajouter(pub);

                    showAlert(Alert.AlertType.INFORMATION, "Succès", "La publication a été ajoutée avec succès.");
                } catch (IOException e) {
                    e.printStackTrace(); // Print the stack trace to the console
                    showAlert(Alert.AlertType.ERROR, "Erreur de fichier", "Un problème est survenu lors de la copie de l'image : " + e.getMessage());
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez saisir un ID utilisateur valide (nombre entier).");
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Un problème est survenu lors de l'ajout de la publication : " + e.getMessage());
                } catch (NullPointerException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Champ manquant : veuillez remplir tous les champs.");
                }
               // switchToDisplayPublicationsScene();
            }

            @FXML
            private void choisirImage(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choisir une image pour la publication");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.jpeg", "*.bmp", "*.gif"));
                File file = fileChooser.showOpenDialog(null);

                if (file != null) {
                    imageFile = file;
                    Image image = new Image(file.toURI().toString());
                    imagePreview.setImage(image);
                }
            }

            private void showAlert(Alert.AlertType alertType, String title, String content) {
                Alert alert = new Alert(alertType);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(content);
                alert.showAndWait();
            }
        }
