package Controllers.Consultation;

import Controllers.User.Session;
import entities.Consultation;
import entities.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import services.ServiceConsultation;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AffichageConsultationpatient {

    @FXML
    private AnchorPane ConsultationPane;

    @FXML
    private FlowPane consultationscontainer;

    @FXML
    private TextField searchField;
    private final ServiceConsultation serviceConsultation = new ServiceConsultation();


    @FXML
    private void initialize() {
        loadConsultations(null); // Load all publications initially
        // Add a listener to the search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            loadConsultations(newValue); // Load publications with the search term
        });
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText();
        // Perform search and update the view...
    }
    private void loadConsultations(String searchTerm) {
        try {
            User currentUser = Session.getUser();
            if (currentUser == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur de session", "Aucun utilisateur connecté.");
                return;
            }
            List<Consultation> consultations = serviceConsultation.afficher();
            if (searchTerm != null && !searchTerm.isEmpty()) {
                consultations = consultations.stream()
                        .filter(pub -> pub.getPathologie().toLowerCase().contains(searchTerm.toLowerCase()))
                        .collect(Collectors.toList());
            }

            consultationscontainer.getChildren().clear();
            for (Consultation con :consultations) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/Consultation/cardconsultationpatient.fxml"));
                Node card = loader.load(); // This line can throw IOException
                CardconsultationPatient controller = loader.getController();
                controller.setConsultation(con);
                controller.setAffichageConsController(this); // Pass reference to this controller
                consultationscontainer.getChildren().add(card);
            }
        } catch (Exception e) { // Catch any exception here
            e.printStackTrace();
        }
    }



    public void refreshConsultationsView() {
        Platform.runLater(() -> {
            loadConsultations(null); // Reload all publications
        });
    }


    public void ajoutcon(ActionEvent actionEvent) {
        try {
            Node displayAjout = FXMLLoader.load(getClass().getResource("/Front/Consultation/AjoutConsultation.fxml"));
            ConsultationPane.getChildren().setAll(displayAjout);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
