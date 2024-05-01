package Controllers.Quiz;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ResultController {

    @FXML
    private TextArea resultArea;

    public void setResults(String results) {
        resultArea.setText(results);
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) resultArea.getScene().getWindow();
        stage.close();
    }
}
