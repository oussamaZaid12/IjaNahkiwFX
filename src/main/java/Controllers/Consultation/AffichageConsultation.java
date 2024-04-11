package Controllers.Consultation;

import entities.Consultation;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import services.ServiceConsultation;

import java.util.List;
import java.util.stream.Collectors;

public class AffichageConsultation {

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
            List<Consultation> consultations = serviceConsultation.afficher();
            if (searchTerm != null && !searchTerm.isEmpty()) {
                consultations = consultations.stream()
                        .filter(pub -> pub.getPathologie().toLowerCase().contains(searchTerm.toLowerCase()))
                        .collect(Collectors.toList());
            }

            consultationscontainer.getChildren().clear();
            for (Consultation con :consultations) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/Consultation/cardconsultation.fxml"));
                Node card = loader.load(); // This line can throw IOException
                Cardconsultation controller = loader.getController();
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


}
