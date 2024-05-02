package Controllers.User;

import Controllers.Dashboard;
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
    private User connectedUser;
    public void loginButtonOnAction(ActionEvent e) throws IOException {
        if (emailTextfield.getText().isEmpty() || passwordTextfield.getText().isEmpty()) {
            invalidText.setText("Veuillez remplir tous les champs");
            return;
        }

        UserService us = new UserService();
        User u = us.getUserByEmail(emailTextfield.getText());
        if(u == null) {
            invalidText.setText("Utilisateur introuvable");
            return;
        }

        boolean passwordMatch = BCrypt.checkpw(passwordTextfield.getText(), u.getPassword());
        if (passwordMatch) {
            Session.setUser(u);  // Set user in session
            switch (u.getRole()) {
                case ADMIN:
                case ROLE_ADMIN:
                    goToUserList();
                    break;
                case ROLE_THERAPEUTE:
                    changeScene("/Front/NavBar.fxml", e);
                    break;
                default:
                    changeScene("/Front/NavBar.fxml", e);
                    break;
            }
        } else {
            invalidText.setText("Mot de passe incorrect");
        }
    }

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
    private void changeScene(String fxmlPath, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
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
    public void goToUserList() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/Dashboard.fxml"));
        Parent profileInterface = loader.load();
        Dashboard dashboardController = loader.getController();

        Scene profileScene = new Scene(profileInterface);
        Stage profileStage = new Stage();
        profileStage.setScene(profileScene);

        Stage currentStage = (Stage) loginButton.getScene().getWindow();
        currentStage.close();

        profileStage.show();
    }

}
