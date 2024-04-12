package Controllers.FicheMedicale;

import entities.FicheMedicale;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import services.ServiceFicheMedicale;

import java.util.List;
import java.util.stream.Collectors;

public class AffichageFiche {

    @FXML
    private FlowPane fichescontainer;

    @FXML
    private TextField searchField;
    private final ServiceFicheMedicale serviceFicheMedicale = new ServiceFicheMedicale();


    @FXML
    private void initialize() {
        loadFiches(null); // Load all fiches initially
        // Add a listener to the search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            loadFiches(newValue); // Load fiches with the search term
        });
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText();
        // Perform search and update the view...
    }

    private void loadFiches(String searchTerm) {
        try {
            List<FicheMedicale> fiches = serviceFicheMedicale.afficher();
            if (searchTerm != null && !searchTerm.isEmpty()) {
                fiches = fiches.stream()
                        .filter(fiche -> fiche.getDateCreation().toString().toLowerCase().contains(searchTerm.toLowerCase()))
                        .collect(Collectors.toList());
            }
            fichescontainer.getChildren().clear();
            for (FicheMedicale fiche : fiches) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/FicheMedicale/cardfiche.fxml"));
                Node card = loader.load(); // This line can throw IOException
                Cardfiche controller = loader.getController();
                controller.setFiche(fiche);
                // Set any other necessary properties or references in the card controller
                fichescontainer.getChildren().add(card);
            }
        } catch (Exception e) { // Catch any exception here
            e.printStackTrace();
        }
    }

    public void refreshFichesView() {
        Platform.runLater(() -> {
            loadFiches(null); // Reload all fiches
        });
    }
}
