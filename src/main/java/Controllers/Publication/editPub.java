package Controllers.Publication;

import Controllers.User.Session;
import entities.User;
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
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;

public class editPub {

    @FXML
    private DatePicker TfDateEdit;

    @FXML
    private TextField TfDescriptionEdit;

    @FXML
    private Button TfEdit;

    @FXML
    private TextField TfIdUserEdit;

    @FXML
    private TextField TfTitreEdit;

    @FXML
    private Button btnImageEdit;

    @FXML
    private ImageView imagePreview;

    private File imageFile; // For storing the selected image file
    private publication currentPublication; // The publication being edited
    private ServicePublication servicePublication = new ServicePublication();

    public void setPublication(publication pub) {
        currentPublication = pub;
        TfTitreEdit.setText(pub.getTitreP());
        TfDescriptionEdit.setText(pub.getDescriptionP());

        LocalDate dateLocal = new java.util.Date(pub.getDateP().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        TfDateEdit.setValue(dateLocal);

        // Updated image path with renamed file and consistent accessing
        String imageName = "Capture_decran_2024_04_11_072440.png"; // Updated filename
        String imagePath = "/images/" + imageName + "?time=" + System.currentTimeMillis();
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        if (imageStream != null) {
            Image image = new Image(imageStream);
            imagePreview.setImage(image);
        } else {
            System.out.println("Image not found: " + imagePath);
            // Optionally set a default image or handle the error
        }
    }






    @FXML
    void EditImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an image for the publication");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.jpeg", "*.bmp", "*.gif"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            imageFile = file;
            Image image = new Image(file.toURI().toString());
            imagePreview.setImage(image);
        }
    }

    @FXML
    void ModifierPublication(ActionEvent event) {
        try {
            User currentUser = Session.getUser();  // Get the logged-in user
            if (currentUser == null) {
                showAlert("Error", "No user logged in.");
                return;
            }

            String titre = TfTitreEdit.getText();
            String description = TfDescriptionEdit.getText();
            LocalDate dateLocal = TfDateEdit.getValue();
            java.sql.Date date = java.sql.Date.valueOf(dateLocal);

            // Set only the fields that can be updated
            currentPublication.setTitreP(titre);
            currentPublication.setDescriptionP(description);
            currentPublication.setDateP(date);

            // Update the image if a new one was selected
            if (imageFile != null) {
                String imagesDir = "src/main/resources/images/";
                Files.createDirectories(Paths.get(imagesDir));
                Path sourcePath = imageFile.toPath();
                Path destinationPath = Paths.get(imagesDir + imageFile.getName());
                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                String imageName = destinationPath.getFileName().toString();
                currentPublication.setImageP(imageName);
            }

            servicePublication.modifier(currentPublication);

            // Show success message
            showAlert("Success", "Publication has been updated successfully.");
        } catch (IOException | SQLException e) {
            showAlert("Error", "An error occurred: " + e.getMessage());
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
