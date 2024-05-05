package Controllers.Publication;

import Controllers.User.Session;
import entities.Role;
import entities.User;
import entities.publication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import services.ServicePublication;

import java.util.List;
import java.util.stream.Collectors;

public class AffichagePub {
    @FXML
    private FlowPane publicationsContainer;

    @FXML
    private TextField searchField;
    private final ServicePublication servicePublication = new ServicePublication();

    @FXML
    private void initialize() {
        loadPublications(null); // Load all publications initially

        // Add a listener to the search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            loadPublications(newValue); // Load publications with the search term
        });

    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText();
        // Perform search and update the view...
    }
    private void loadPublications(String searchTerm) {
        try {
            List<publication> publications = servicePublication.afficher();
            if (searchTerm != null && !searchTerm.isEmpty()) {
                publications = publications.stream()
                        .filter(pub -> pub.getTitreP().toLowerCase().contains(searchTerm.toLowerCase()))
                        .collect(Collectors.toList());
            }

            publicationsContainer.getChildren().clear();
            for (publication pub : publications) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/Publication/cardPub.fxml"));
                Node card = loader.load(); // This line can throw IOException
                CardPub controller = loader.getController();
                controller.setPublication(pub);
                controller.setAffichagePubController(this); // Pass reference to this controller
                publicationsContainer.getChildren().add(card);
            }
        } catch (Exception e) { // Catch any exception here
            e.printStackTrace();
        }
    }


    public void refreshPublicationsView() {
        Platform.runLater(() -> {
            loadPublications(null); // Reload all publications
        });
    }





}
