package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class HomeView {

    @FXML
    private AnchorPane mainHome;

    @FXML
    public void showDisplayPublications() {
        try {
            Node displayPubs = FXMLLoader.load(getClass().getResource("/Front/Publication/affichagePub.fxml"));
            mainHome.getChildren().setAll(displayPubs);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, for example, by showing an error message
        }
    }

    @FXML
    public void DisplayQuiz() {
        try {
            Node displayPubs = FXMLLoader.load(getClass().getResource("/Back/Question/CardQuestion.fxml"));
            mainHome.getChildren().setAll(displayPubs);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, for example, by showing an error message
        }
    }

}
