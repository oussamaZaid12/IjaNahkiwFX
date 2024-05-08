package Controllers.Consultation;

import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import services.UserService;

import java.io.IOException;
import java.util.List;

public class Listtherapists {

    @FXML
    private AnchorPane ConsultationPane;
    @FXML
    private FlowPane usersscontainer;
    private final UserService serviceUser = new UserService();

    @FXML
    private void initialize() {
        loadusers(null); // Load all publications initially
        // Add a listener to the search field

    }


    private void loadusers(String searchTerm) {
        try {
            List<User> users = serviceUser.getTherapistUsers();
            usersscontainer.getChildren().clear();
            for (User user : users) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/Consultation/carduser.fxml"));
                Node card = loader.load(); // This line can throw IOException
                Carduser controller = loader.getController();
                controller.setUser(user);
           //     controller.setAffichageUserController(this); // Pass reference to this controller
                usersscontainer.getChildren().add(card);
            }
        } catch (Exception e) { // Catch any exception here
            e.printStackTrace();
        }
    }



    public void showdisplayconsultations(ActionEvent actionEvent) {
        try {
            Node displayAjout = FXMLLoader.load(getClass().getResource("/Front/Consultation/affichageConsultationpatient.fxml"));
            ConsultationPane.getChildren().setAll(displayAjout);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, for example, by showing an error message
        }
    }
    public void showChatbot(ActionEvent actionEvent) {
        try {
            // Load the chatbot window or overlay
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/Quiz/Chatbot.fxml")); // Adjust path as needed
            Parent chatbotRoot = loader.load();

            // Display the chatbot window as a modal or embedded
            Stage chatbotStage = new Stage();
            chatbotStage.setTitle("Chatbot");
            chatbotStage.setScene(new Scene(chatbotRoot));
            chatbotStage.initOwner(ConsultationPane.getScene().getWindow());
            chatbotStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}