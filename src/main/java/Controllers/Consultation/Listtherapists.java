package Controllers.Consultation;

import entities.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import services.ServiceNotification;
import services.UserService;

import java.util.List;

public class Listtherapists {

    @FXML
    private AnchorPane ConsultationPane;

    @FXML
    private FlowPane usersscontainer;


    private final UserService serviceUser = new UserService();

    @FXML
    private Button notificationsButton;

    private ServiceNotification serviceNotification = new ServiceNotification();

    public void setServiceNotification(ServiceNotification serviceNotification) {
        this.serviceNotification = serviceNotification;
    }

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

    public void refreshConsultationsView() {
        Platform.runLater(() -> {
            loadusers(null); // Reload all publications
        });
    }



}