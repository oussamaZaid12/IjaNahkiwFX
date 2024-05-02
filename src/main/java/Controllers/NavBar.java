package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class NavBar {

    @FXML
    private BorderPane mainContainer;


    @FXML
    public void showDisplayPublications() {
        try {
            Node displayPubs = FXMLLoader.load(getClass().getResource("/Front/Publication/affichagePub.fxml"));
            mainContainer.setCenter(displayPubs);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, for example, by showing an error message
        }
    }


    @FXML
    public void showDisplayConsultations() {
        try {
            Node displayCons = FXMLLoader.load(getClass().getResource("/Front/Consultation/affichageConsultation.fxml"));
            mainContainer.setCenter(displayCons);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, for example, by showing an error message
        }
    }

    @FXML
    public void showDisplayFiches() {
        try {
            Node displayFiches = FXMLLoader.load(getClass().getResource("/Front/FicheMedicale/AffichageFiche.fxml"));
            mainContainer.setCenter(displayFiches);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, for example, by showing an error message
        }
    }

    @FXML
    public void showHome() {
        try {
            Node home = FXMLLoader.load(getClass().getResource("/Front/HomeView.fxml"));
            mainContainer.setCenter(home);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, for example, by showing an error message
        }
    }


    public void DisplayQuiz() {
        try {
            Node quiz = FXMLLoader.load(getClass().getResource("/Back/Question/CardQuestion.fxml"));
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

