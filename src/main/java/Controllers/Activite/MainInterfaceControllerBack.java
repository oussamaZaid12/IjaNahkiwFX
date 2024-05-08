package Controllers.Activite;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainInterfaceControllerBack {

    @FXML
    private void openActivites(ActionEvent event) {
        loadScene("/Back/Activite/affichageActivite.fxml");
    }

    @FXML
    private void openProgrammes(ActionEvent event) {
        loadScene("/Back/Activite/AffichageProgramme.fxml");
    }

    @FXML
    private void openExercices(ActionEvent event) {
        loadScene("/Back/Activite/affichageExercice.fxml");
    }

    private void loadScene(String fxmlPath) {
        try {
            Node content = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setScene(new Scene((Parent) content));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();  // Handle the exception appropriately
        }
    }
}

