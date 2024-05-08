package Controllers.Consultation;

import Controllers.Notification.NotificationWindowController;
import Controllers.User.Session;
import entities.Consultation;
import entities.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.ServiceConsultation;
import services.ServiceNotification;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AffichageConsultation {

    @FXML
    private AnchorPane ConsultationPane;

    @FXML
    private FlowPane consultationscontainer;

    @FXML
    private TextField searchField;
    private final ServiceConsultation serviceConsultation = new ServiceConsultation();
    private ServiceNotification serviceNotification = new ServiceNotification();

    public void setServiceNotification(ServiceNotification serviceNotification) {
        this.serviceNotification = serviceNotification;
    }

    @FXML
    private void showNotificationWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/Consultation/NotificationWindow.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Notifications");

            NotificationWindowController notificationWindowController = loader.getController();
            notificationWindowController.setServiceNotification(serviceNotification);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @FXML
    private void initialize() {
        loadConsultations(null);
        // Add a listener to the search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            loadConsultations(newValue);
        });
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText();
    }

    private void loadConsultations(String searchTerm) {
        try {
            User currentUser = Session.getUser();
            if (currentUser == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur de session", "Aucun utilisateur connecté.");
                return;
            }
            List<Consultation> consultations = serviceConsultation.getConsultationsByTherapistId(currentUser.getId());
            if (searchTerm != null && !searchTerm.isEmpty()) {
                consultations = consultations.stream()
                        .filter(pub -> pub.getPathologie().toLowerCase().contains(searchTerm.toLowerCase()))
                        .collect(Collectors.toList());
            }

            consultationscontainer.getChildren().clear();
            for (Consultation con : consultations) {
                if(con.getFiche()!=0){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/Consultation/cardconsultation.fxml"));
                    Node card = loader.load(); // This line can throw IOException
                    Cardconsultation controller = loader.getController();
                    controller.setConsultation(con);
                    controller.setAffichageConsController(this); // Pass reference to this controller
                    consultationscontainer.getChildren().add(card);
                }
                else
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/Consultation/cardconsultationsansfiche.fxml"));
                    Node card = loader.load(); // This line can throw IOException
                    Cardconsultation controller = loader.getController();
                    controller.setConsultation(con);
                    controller.setAffichageConsController(this); // Pass reference to this controller
                    consultationscontainer.getChildren().add(card);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshConsultationsView() {
        Platform.runLater(() -> {
            loadConsultations(null); // Reload all publications
        });
    }


    public void ShowCalendar(ActionEvent actionEvent) {
        try {
            Node displayCal = FXMLLoader.load(getClass().getResource("/Front/Consultation/Calendar.fxml"));
            ConsultationPane.getChildren().setAll(displayCal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDisplayFiches(ActionEvent actionEvent) {
        try {
            Node displayAjout = FXMLLoader.load(getClass().getResource("/Front/FicheMedicale/AffichageFiche.fxml"));
            ConsultationPane.getChildren().setAll(displayAjout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showstat(ActionEvent actionEvent) {
        try {
            Node displaystat = FXMLLoader.load(getClass().getResource("/Front/Consultation/stat.fxml"));
            ConsultationPane.getChildren().setAll(displaystat);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showchatclient(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/client/client-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Client Chat");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showchatserver(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/server/server-view.fxml"));
            Parent root = loader.load();

            // Create a new stage for the chat window
            Stage chatStage = new Stage();
            chatStage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow()); // Set main window as owner
            chatStage.setScene(new Scene(root));
            chatStage.setTitle("Server Chat");

            // Set modality to NONE
            chatStage.initModality(Modality.NONE);

            chatStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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