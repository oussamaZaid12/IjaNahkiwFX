package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainFX extends Application {

    private static BorderPane mainLayout;

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/NavBar.fxml"));
            mainLayout = loader.load();
            Scene scene = new Scene(mainLayout);
            stage.setScene(scene);
            stage.setTitle("Ija Nahkiw");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }

    public static void setCenterView(Parent node) {
        mainLayout.setCenter(node);
    }

    public static void main(String[] args) {
        launch(args);
    }
}