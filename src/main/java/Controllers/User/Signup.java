package Controllers.User;

import entities.Role;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.UserService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class Signup {
    @FXML
    private Button signupButton;
    @FXML
    private TextField nomTextField;
    @FXML
    private TextField prenomTextField;
    @FXML
    private TextField ageTextField;
    @FXML
    private TextField emailTextfield;
    @FXML
    private TextField emailTextfield1;
    @FXML
    private PasswordField passwordTextfield;
    @FXML
    private PasswordField ConfirmpasswordTextfield;
    @FXML
    private Label invalidText;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    Label captchaCode;
    @FXML
    TextField captchaInput;

    @FXML
    private Button uploadProfileImageButton;

    private String profileImagePath;
    private String captcha;
    public void initialize() {
        // Générer et afficher le captcha au chargement de la page
        generateCaptcha();
    }

    // Méthode pour générer et afficher le captcha
    private void generateCaptcha() {
        Random rand = new Random();
        StringBuilder captchaBuilder = new StringBuilder();
        // Générer un captcha de longueur 6
        for (int i = 0; i < 6; i++) {
            // Pour chaque itération, décidez aléatoirement d'ajouter un caractère ou un chiffre
            if (rand.nextBoolean()) {
                // Ajouter un chiffre aléatoire entre 0 et 9
                int randomNumber = rand.nextInt(10);
                captchaBuilder.append(randomNumber);
            } else {
                // Ajouter un caractère aléatoire de 'A' à 'Z'
                char randomChar = (char) ('A' + rand.nextInt(26));
                captchaBuilder.append(randomChar);
            }
        }
        // Combinez les nombres et les caractères aléatoires pour former le captcha
        captcha = captchaBuilder.toString();
        captchaCode.setText(captcha);
    }
    public void signupButtonOnAction(ActionEvent event) throws IOException {

        // Vérifie si tous les champs sont remplis
        if (emailTextfield.getText().isEmpty() || emailTextfield1.getText().isEmpty() || passwordTextfield.getText().isEmpty() || ConfirmpasswordTextfield.getText().isEmpty()|| roleComboBox.getValue() == null) {
            invalidText.setText("Please fill in all fields");
            invalidText.setVisible(true);
            return;
        }
        // Vérifie si les emails correspondent
        if(!emailTextfield.getText().equals(emailTextfield1.getText())){
            invalidText.setText("Emails do not match");
            invalidText.setVisible(true);
            return;
        }
        // Vérifie si les mots de passe correspondent
        if(!passwordTextfield.getText().equals(ConfirmpasswordTextfield.getText())){
            invalidText.setText("Passwords do not match");
            invalidText.setVisible(true);
            return;
        }
        // Vérifie le format de l'email
        if (!emailTextfield.getText().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            invalidText.setText("Invalid email format");
            invalidText.setVisible(true);
            return;
        }
        // Vérifie la longueur du mot de passe
        if (passwordTextfield.getText().length() < 8) {
            invalidText.setText("Password must be at least 8 characters long");
            invalidText.setVisible(true);
            return;
        }
        // Vérifie si l'âge est un nombre
        if (!ageTextField.getText().matches("\\d+")) {
            invalidText.setText("Age must be a numeric value");
            invalidText.setVisible(true);
            return;
        }
        if (!captchaInput.getText().equals(captcha)) {
            showAlert("Captcha does not match");
            return;
        }

        // Crée un objet UserService pour gérer les utilisateurs
        UserService us = new UserService();
        // Vérifie si l'utilisateur existe déjà avec cet email
        if(us.getUserByEmail(emailTextfield.getText()) == null){
            // Génère un nom de fichier aléatoire pour l'image de profil
            String profileImageName = generateRandomFileName();
            // Crée un nouvel utilisateur avec les informations fournies
            User u = new User(emailTextfield.getText(), passwordTextfield.getText(), profileImageName, Role.valueOf(roleComboBox.getValue()), true, nomTextField.getText(), prenomTextField.getText(), Integer.parseInt(ageTextField.getText()));
            // Ajoute l'utilisateur à la base de données
            us.ajouterUser(u);
            // Télécharge l'image de profil si un chemin d'accès a été fourni
            if (profileImagePath != null) {
                saveProfileImage(profileImagePath, profileImageName);
            }
        }else{
            invalidText.setText("User already exists");
            invalidText.setVisible(true);
            return;
        }
    }

    // Méthode pour générer un nom de fichier aléatoire pour l'image de profil
    private String generateRandomFileName() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10; // Longueur du nom de fichier
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString() + ".png"; // Extension de fichier
    }

    /// Méthode pour enregistrer l'image de profil dans le répertoire approprié
    private void saveProfileImage(String imagePath, String imageName) {
        File source = new File(imagePath);
        File destination = new File("./images" + imageName); // Spécifiez le chemin du dossier de destination

        try {
            // Créez un flux d'entrée pour l'image source
            FileInputStream fis = new FileInputStream(source);
            // Créez un flux de sortie pour l'image de destination
            FileOutputStream fos = new FileOutputStream(destination);

            // Copiez les données de l'image source vers l'image de destination
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }

            // Fermez les flux
            fis.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Méthode pour gérer le clic sur le bouton de téléchargement d'image de profil
    @FXML
    public void uploadProfileImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        Stage stage = (Stage) signupButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            // Obtient le chemin d'accès de l'image sélectionnée
            profileImagePath = selectedFile.getAbsolutePath();
        }
    }

    public void goBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/login.fxml"));
        Parent profileInterface = loader.load();
        Scene profileScene = new Scene(profileInterface);
        Stage profileStage = new Stage();
        profileStage.setScene(profileScene);

        // Close the current stage (assuming loginButton is accessible from here)
        Stage currentStage = (Stage) signupButton.getScene().getWindow();
        currentStage.close();

        // Show the profile stage
        profileStage.show();
    }

    public void goToProfile(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/profile.fxml"));
        Parent profileInterface = loader.load();

        // Get the controller instance
        Profile profileController = loader.getController();

        // Initialize data using the controller's method
        profileController.initData(user);

        Scene profileScene = new Scene(profileInterface);
        Stage profileStage = new Stage();
        profileStage.setScene(profileScene);

        // Close the current stage (assuming loginButton is accessible from here)
        Stage currentStage = (Stage) signupButton.getScene().getWindow();
        currentStage.close();

        // Show the profile stage
        profileStage.show();
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
