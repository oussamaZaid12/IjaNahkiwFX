package Controllers.server;
/**
 * Server Main Class
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Front/client/server-view.fxml"));
        primaryStage.setTitle("Server!");
        primaryStage.setScene(new Scene(root, 480, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}