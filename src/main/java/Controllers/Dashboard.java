package Controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class Dashboard {

    @FXML
    private BorderPane mainContainer;

    @FXML
    public void showAddPublication() {
        try {
            Node addPub = FXMLLoader.load(getClass().getResource("/Back/Publication/ajoutPub.fxml"));
            mainContainer.setCenter(addPub);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, for example, by showing an error message
        }
    }

    @FXML
    public void showDisplayPublications() {
        try {
            Node displayPubs = FXMLLoader.load(getClass().getResource("/Back/Publication/affichagePub.fxml"));
            mainContainer.setCenter(displayPubs);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, for example, by showing an error message
        }
    }

    @FXML
    public void showHome() {
        try {
            Node home = FXMLLoader.load(getClass().getResource("/Back/HomeView.fxml"));
            mainContainer.setCenter(home);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, for example, by showing an error message
        }
    }
    @FXML
    public void DisplayQuiz() {
        try {
            Node quiz = FXMLLoader.load(getClass().getResource("/Back/Quiz/affichageQuestionnaire.fxml"));
            mainContainer.setCenter(quiz);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, for example, by showing an error message
        }
    }


    @FXML
    private void initialize() {
        showHome();
    }
}
