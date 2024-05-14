package Controllers.Publication;

import Controllers.User.Session;
import entities.User;
import entities.publication;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgcodecs.Imgcodecs;
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

    private static final String IMAGES_DIR = "C:\\Users\\Tifa\\Desktop\\symfonypull13.02\\public\\upload-images\\";
    @FXML private DatePicker TfDate;
    @FXML private TextField TfDescription;
    @FXML private TextField TfTitre;
    @FXML private ImageView imagePreview;

    private File imageFile;
    private VideoCapture capture;
    private Mat frame;
    private AnimationTimer timer;
    private boolean isPreviewing = false;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @FXML
    private void initialize() {
        capture = new VideoCapture();
        frame = new Mat();
        try {
            Files.createDirectories(Paths.get(IMAGES_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initialize and define the animation timer to update frames
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (capture.isOpened()) {
                    capture.read(frame);
                    Image image = Utils.mat2Image(frame);
                    imagePreview.setImage(image);
                }
            }
        };
    }

    @FXML
    private void startCamera(ActionEvent event) {
        if (!capture.isOpened()) {
            capture.open(0);
        }

        if (capture.isOpened() && !isPreviewing) {
            isPreviewing = true;
            timer.start();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'accéder à la caméra.");
        }
    }

    @FXML
    private void stopCamera(ActionEvent event) {
        timer.stop();
        isPreviewing = false;
        capture.release();
    }

    @FXML
    private void takePhoto(ActionEvent event) {
        if (capture.isOpened()) {
            String outputFileName = IMAGES_DIR + generateUniqueFileName("captured_image");
            Imgcodecs.imwrite(outputFileName, frame);
            imageFile = new File(outputFileName);
            imagePreview.setImage(new Image("file:" + outputFileName));
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'accéder à la caméra.");
        }
        stopCamera(event);
    }

    @FXML
    private void AjouterPublication(ActionEvent event) {
        if (TfTitre.getText().isEmpty() || TfDescription.getText().isEmpty() || TfDate.getValue() == null || imageFile == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs et l'image doivent être remplis.");
            return;
        }

        try {
            User currentUser = Session.getUser();
            if (currentUser == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur de session", "Aucun utilisateur connecté.");
                return;
            }

            String titre = TfTitre.getText();
            String description = TfDescription.getText();
            int idUser = currentUser.getId();
            LocalDate dateLocal = TfDate.getValue();
            java.sql.Date date = java.sql.Date.valueOf(dateLocal);

            Path destinationPath = Paths.get(IMAGES_DIR, imageFile.getName());
            Files.copy(imageFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            String imageName = destinationPath.getFileName().toString();

            publication pub = new publication(idUser, titre, description, imageName, date);
            ServicePublication servicePublication = new ServicePublication();
            servicePublication.ajouter(pub);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "La publication a été ajoutée avec succès.");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de fichier", "Un problème est survenu lors de la copie de l'image.");
            e.printStackTrace();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Un problème est survenu lors de l'ajout de la publication.");
            e.printStackTrace();
        }
    }

    private String generateUniqueFileName(String baseName) {
        return baseName + "_" + System.currentTimeMillis() + ".png";
    }
    @FXML
    private void choisirImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image pour la publication");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.jpeg", "*.bmp", "*.gif"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                Path destinationPath = Paths.get(IMAGES_DIR + file.getName());
                Files.copy(file.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                imageFile = destinationPath.toFile();
                imagePreview.setImage(new Image(file.toURI().toString()));
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur lors de la copie de l'image", "Une erreur est survenue lors de la copie du fichier.");
                e.printStackTrace();
            }
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
