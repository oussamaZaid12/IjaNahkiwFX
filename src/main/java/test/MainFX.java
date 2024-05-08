package test;

import Controllers.User.Login;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.prefs.Preferences;

public class MainFX extends Application {

    private static AnchorPane mainLayout;

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/login.fxml"));
            mainLayout = loader.load();
            Login loginController = loader.getController();
            loadSavedLoginInfo(loginController);
            Scene scene = new Scene(mainLayout);
            stage.setScene(scene);
            stage.setTitle("Ija Nahkiw");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }

    public static void setCenterView(Parent node) {
        mainLayout.getChildren().setAll(node);

    }
    private void loadSavedLoginInfo(Login loginController) {
        System.out.println("Loading saved login info..."); // Instruction de débogage

        // Obtient les préférences utilisateur
        Preferences prefs = Preferences.userNodeForPackage(Login.class);

        // Récupère l'email et le mot de passe sauvegardés
        String savedEmail = prefs.get("email", null);
        String savedPassword = prefs.get("password", null);

        // Vérifie si des informations de connexion sauvegardées ont été trouvées
        if (savedEmail != null && savedPassword != null) {
            // Pré-remplit les champs d'email et de mot de passe dans le contrôleur Login
            loginController.setEmail(savedEmail);
            loginController.setPassword(savedPassword);
            System.out.println("Saved login info loaded successfully."); // Instruction de débogage
        } else {
            System.out.println("No saved login info found."); // Instruction de débogage
        }
    }
    public static void main(String[] args) {
        launch(args);
    }

}
