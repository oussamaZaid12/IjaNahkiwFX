package Controllers.Consultation;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Popup {

    // Method to close the notification popup
    public void close(ActionEvent actionEvent) {
        // Get the source node (the close button)
        Node source = (Node) actionEvent.getSource();
        // Get the stage (window) that contains the source node
        Stage stage = (Stage) source.getScene().getWindow();
        // Close the stage
        stage.close();
    }
}
