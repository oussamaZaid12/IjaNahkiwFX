package test;

import Controllers.User.Login;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.prefs.Preferences;

public class MainFX extends Application {

    private static AnchorPane mainLayout;
    private MediaPlayer backgroundMusic;

    @Override
    public void start(Stage stage) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/login.fxml"));
            mainLayout = loader.load();
            Login loginController = loader.getController();
            loadSavedLoginInfo(loginController);

            // Create the scene
            Scene scene = new Scene(mainLayout);
            stage.setScene(scene);
            stage.setTitle("Ija Nahkiw");

            // Load and play background music using a relative path
            String musicFile = getClass().getResource("/relaxmusic.mp3").toExternalForm();
            Media media = new Media(musicFile);
            backgroundMusic = new MediaPlayer(media);
            backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE); // Loop indefinitely
            backgroundMusic.play();

            // Show the stage
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }

    public static void setCenterView(Parent node) {
        mainLayout.getChildren().setAll(node);
    }

    private void loadSavedLoginInfo(Login loginController) {
        System.out.println("Loading saved login info...");

        // Get user preferences
        Preferences prefs = Preferences.userNodeForPackage(Login.class);

        // Retrieve the saved email and password
        String savedEmail = prefs.get("email", null);
        String savedPassword = prefs.get("password", null);

        // Check if any saved login info is found
        if (savedEmail != null && savedPassword != null) {
            loginController.setEmail(savedEmail);
            loginController.setPassword(savedPassword);
            System.out.println("Saved login info loaded successfully.");
        } else {
            System.out.println("No saved login info found.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
