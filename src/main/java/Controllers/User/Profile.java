package Controllers.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import entities.Role;
import entities.User;
import services.UserService;

import java.io.*;
import java.util.Optional;

public class Profile {
    private User currentUser;
    @FXML
    private TextField nom;
    @FXML
    private TextField prenom;
    @FXML
    private TextField email;
    @FXML
    private TextField age;
    @FXML
    private Button updateButton;
    @FXML
    private ImageView profileImageView;
    @FXML
    private Button logoutButton;

    @FXML
    private Button uploadButton;

    public void initData(User usere) {
        if(usere.getRole().equals(Role.ADMIN)){
            logoutButton.setVisible(false);
        }
        //  textFiedTest.setText("testtt");
        UserService us = new UserService();

        User user =us.getUserById(usere.getId());
        this.currentUser = user;
        nom.setText(user.getNom());
        prenom.setText(user.getPrenom());
        email.setText(user.getEmail());
        System.out.println(user.getImage());

        String imageUrl = "/static/images/" + user.getImage(); // Assuming user.getImage() returns the image filename
        Image image = new Image(getClass().getResource(imageUrl).toExternalForm());
        profileImageView.setImage(image);
        System.out.println(user);
        populateProfileInformation(user);

    }
    @FXML
    private void uploadImage(ActionEvent event) throws IOException {
        UserService us = new UserService();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        // Set the initial directory to the user's home directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        // Restrict file types to images
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Show the file chooser dialog
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            // Display the selected image in the ImageView
            Image image = new Image(selectedFile.toURI().toString());
            profileImageView.setImage(image);
            saveImageAndGetUrl(selectedFile.toPath().toUri().toURL().openStream(), selectedFile.getName());
            us.updateImage(currentUser,selectedFile.getName());
            currentUser.setImage(selectedFile.getName());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Image updated successfully");

        }
    }
    private void populateProfileInformation(User user) {

    }

    private void handleEditButtonAction() {
        // Implement edit profile action
    }

    public void enabelEditing(){
        nom.setDisable(false);
        prenom.setDisable(false);
        email.setDisable(false);
        age.setDisable(false);
        profileImageView.setDisable(false);
        updateButton.setVisible(true);
    }
    public void UpdateData() {
        // Check if currentUser is null
        if (currentUser == null) {
            // Handle the case where currentUser is null
            System.err.println("currentUser is null");
            return;
        }

        // Create a new UserService instance
        UserService us = new UserService();

        // Create a new User object to hold updated data
        User u = new User();

        // Populate the User object with data
        u.setId(currentUser.getId());  // Assuming currentUser has an id property
        u.setEmail(email.getText());
        u.setRole(currentUser.getRole());
        u.setNom(nom.getText());
        u.setPrenom(prenom.getText());

        // Check if the image is not null in the currentUser before setting it in the updated user
        if (currentUser.getImage() != null) {
            u.setImage(currentUser.getImage());
        } else {
            // Handle the case where the image in the currentUser is null
            System.err.println("currentUser's image is null");
            // You may choose to set a default image or handle it in some other way
        }

        // Call the UserService to update the profile
        us.updateProfile(u);

        // Display a confirmation message
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Informations Updated");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Handle the OK button click if needed
        }
    }

    private String saveImageAndGetUrl(InputStream inputStream, String filename) throws IOException {
        // Define the directory where images will be stored
        String uploadDirectory = "src/main/resources/static/images"; // Change this to your desired directory path
        File directory = new File(uploadDirectory);
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it doesn't exist
        }

        // Save the image file to the upload directory
        String filePath = uploadDirectory + File.separator + filename;
        try (OutputStream outputStream = new FileOutputStream(new File(filePath))) {
            int read;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }

        // Return the URL of the saved image
        return  filename; // Adjust the URL format as needed
    }
    public void logout(){
        Stage stage = (Stage) updateButton.getScene().getWindow();
        stage.close();
        Stage newStage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/gui/login.fxml"));
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}