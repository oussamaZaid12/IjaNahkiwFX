package Controllers.Question;

import entities.Proposition;
import entities.Question;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import services.ServiceQuestion;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EditQuestionController {

    @FXML
    private TextField labelTitleQuestion;

    @FXML
    private TextField proposition1Column;

    @FXML
    private TextField proposition2Column;

    @FXML
    private TextField proposition3Column;
    @FXML
    private ChoiceBox<Integer> labelScore1;
    @FXML
    private ChoiceBox<Integer> labelScore2;
    @FXML
    private ChoiceBox<Integer> labelScore3;
    private ServiceQuestion serviceQuestion;
    private Question currentQuestion;

    public void setCurrentQuestion(Question question) {
        this.currentQuestion = question;
        displayQuestionData();
    }

    public void setServiceQuestion(ServiceQuestion serviceQuestion) {
        this.serviceQuestion = serviceQuestion;
    }

    private void displayQuestionData() {
        labelTitleQuestion.setText(currentQuestion.getTitleQuestion());

        // Set propositions if available
        if (currentQuestion.getPropositions().size() >= 1) {
            proposition1Column.setText(currentQuestion.getPropositions().get(0).getTitleProposition());
        }
        if (currentQuestion.getPropositions().size() >= 2) {
            proposition2Column.setText(currentQuestion.getPropositions().get(1).getTitleProposition());
        }
        if (currentQuestion.getPropositions().size() >= 3) {
            proposition3Column.setText(currentQuestion.getPropositions().get(2).getTitleProposition());
        }
    }

    @FXML
    void handleSaveAction() {
        if (!validateInputs()) {
            return;
        }

        try {
            // Update the current question object with new data from the form
            currentQuestion.setTitleQuestion(labelTitleQuestion.getText());

            // Update propositions if available
            updatePropositionIfNotEmpty(currentQuestion, 0, proposition1Column.getText(), labelScore1.getValue());
            updatePropositionIfNotEmpty(currentQuestion, 1, proposition2Column.getText(), labelScore2.getValue());
            updatePropositionIfNotEmpty(currentQuestion, 2, proposition3Column.getText(), labelScore3.getValue());

            // Save the updated question
            serviceQuestion.modifier(currentQuestion);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Question updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update question.");
        }
    }

    private boolean validateInputs() {
        // Validate input fields here
        return true;
    }

    private void updatePropositionIfNotEmpty(Question question, int index, String propositionText, Integer score) {
        // Update proposition at specified index if the input is not empty
        if (!propositionText.isEmpty() && index < question.getPropositions().size()) {
            Proposition proposition = question.getPropositions().get(index);
            proposition.setTitleProposition(propositionText);
            proposition.setScore(score); // Update score
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