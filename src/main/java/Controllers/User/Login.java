package Controllers.User;

import entities.Role;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import services.UserService;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

public class Login {

    @FXML
    private Button loginButton;
    @FXML
    private TextField emailTextfield;
    @FXML
    private PasswordField passwordTextfield;
    @FXML
    private Hyperlink signupLink;
    @FXML
    private Label invalidText;
    @FXML
    private CheckBox rememberMeCheckbox;
    private User connectedUser;

    public void setEmail(String email) {
        emailTextfield.setText(email);
    }
    public void setPassword(String password) {
        passwordTextfield.setText(password);
    }
    public void loginButtonOnAction(ActionEvent e) throws IOException {
        Logger logger = Logger.getLogger(Login.class.getName());
        if (emailTextfield.getText().isEmpty() || passwordTextfield.getText().isEmpty()) {
            invalidText.setText("Veuillez remplir tous les champs");
            return;
        }

        UserService us = new UserService();
        User u = us.getUserByEmail(emailTextfield.getText());
        if (u == null) {
            invalidText.setText("Utilisateur introuvable");
            return;
        }
        if (u.getBanned()) {
            invalidText.setText("Compte inactif veuillez contacter l'administrateur");
            return;
        }
        String storedPassword = u.getPassword();

        // Check if the entered password matches the stored password hash
        boolean passwordMatch = BCrypt.checkpw(passwordTextfield.getText(), storedPassword);
        logger.info("Password entered by user: " + passwordTextfield.getText());
        logger.info("Password stored in database: " + storedPassword);
        logger.info("Password match: " + passwordMatch);
        if (passwordMatch) {
            this.connectedUser = u;
            if (rememberMeCheckbox.isSelected()) {
                // Sauvegarder les informations de connexion si l'option "Se souvenir de moi" est sélectionnée
                saveLoginInfo(emailTextfield.getText(), passwordTextfield.getText());
                logger.info("Login info saved: Email - " + emailTextfield.getText() + ", Password - " + passwordTextfield.getText());
            } else {
                // Effacer les informations de connexion sauvegardées si l'option "Se souvenir de moi" n'est pas sélectionnée
                clearSavedLoginInfo();
                logger.info("Login info cleared");
            }

            User ui = us.getUserById(u.getId());
            invalidText.setText("");
            if (u.getRole() == Role.ROLE_ADMIN) {
                navigateTo("/Back/Dashboard.fxml");
            } else if (u.getRole() == Role.ROLE_THERAPEUTE) {
                navigateTo("/Front/NavBar.fxml");
            } else {
                navigateTo("/Front/NavBar.fxml");
            }
        } else {
            invalidText.setText("Mot de passe incorrect");
        }
    }
    private void clearSavedLoginInfo() {
        // Obtient les préférences utilisateur
        Preferences prefs = Preferences.userNodeForPackage(Login.class);

        // Supprime les clés "email" et "password" des préférences
        prefs.remove("email");
        prefs.remove("password");
    }

    private void saveLoginInfo(String email, String password) {
        if (rememberMeCheckbox.isSelected()) {
            // Obtient les préférences utilisateur
            Preferences prefs = Preferences.userNodeForPackage(Login.class);

            // Enregistre l'email et le mot de passe dans les préférences
            prefs.put("email", email);
            prefs.put("password", password);
        }
    }
    private void navigateTo(String fxmlPath) throws IOException {
        System.out.println("Navigating to " + fxmlPath.substring(fxmlPath.lastIndexOf("/") + 1).replace(".fxml", "").toUpperCase());
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent navBar = loader.load();
        Scene navBarScene = new Scene(navBar);
        Stage navBarStage = new Stage();
        navBarStage.setScene(navBarScene);

        // Close the current window
        Stage currentStage = (Stage) loginButton.getScene().getWindow();
        currentStage.close();

        // Show the new window
        navBarStage.show();
    }




    /*
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
        Stage currentStage = (Stage) loginButton.getScene().getWindow();
        currentStage.close();

        // Show the profile stage
        profileStage.show();
    }
*/
    @FXML
    private void forgetPassword(ActionEvent event){
        UserService userService = new UserService();
        Stage resetPasswordStage = new Stage();
        Parent resetPasswordInterface;
        try {
            resetPasswordInterface = FXMLLoader.load(getClass().getResource("/gui/forgetPassword.fxml"));
            Scene resetPasswordScene = new Scene(resetPasswordInterface);
            resetPasswordStage.setScene(resetPasswordScene);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Show the UserInterface stage
            resetPasswordStage.show();

        } catch (IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public void goToSignup() throws IOException {
        Stage signupStage = new Stage();
        Parent signupInterface = FXMLLoader.load(getClass().getResource("/gui/signup.fxml"));
        Scene signupScene = new Scene(signupInterface);
        signupStage.setScene(signupScene);
        Stage currentStage = (Stage) loginButton.getScene().getWindow();
        currentStage.close();
        signupStage.show();
    }

}