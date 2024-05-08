package Controllers.Activite;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class allAct {

    @FXML
    private SplitPane mainSplitPanee;
    @FXML
    private AnchorPane leftPane;
    @FXML
    private AnchorPane rightPane;

    public void initialize() {
        loadViewInPane("/Back/Activite/affichageActivite.fxml", leftPane);
        loadViewInPane("/Front/Activite/ActiviteDisplay.fxml", rightPane);

    }

    private void loadViewInPane(String fxmlPath, AnchorPane pane) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            pane.getChildren().setAll((AnchorPane) loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
