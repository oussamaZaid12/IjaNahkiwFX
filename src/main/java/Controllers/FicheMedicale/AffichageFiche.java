package Controllers.FicheMedicale;

import entities.FicheMedicale;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import services.ServiceFicheMedicale;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AffichageFiche {

    @FXML
    private AnchorPane FichePane;

    @FXML
    private FlowPane fichescontainer;

    @FXML
    private TextField searchField;
    private final ServiceFicheMedicale serviceFicheMedicale = new ServiceFicheMedicale();

    @FXML
    private void initialize() {
        loadFiches(null);
        // Add a listener to the search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            loadFiches(newValue);
        });
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText();
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

    public void ajoutf(ActionEvent actionEvent) {
        try {
            Node displayAjout = FXMLLoader.load(getClass().getResource("/Front/FicheMedicale/AjoutFiche.fxml"));
            FichePane.getChildren().setAll(displayAjout);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, for example, by showing an error message
        }
    }
}
