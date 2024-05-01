package Controllers.Question;

import entities.Proposition;
import entities.Question;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import services.ServiceQuestion;

import java.sql.SQLException;

public class ControllerQuestion {

    @FXML
    private TextField labelTitleQuestion;
    @FXML
    private TextField labelUserId;
    @FXML
    private TextField labelQuestionnaireId;
    @FXML
    private TextField labelAnswer1;

    @FXML
    private TextField labelAnswer2;

    @FXML
    private TextField labelAnswer3;
    @FXML
    private ChoiceBox<Integer> labelScore1;
    @FXML
    private ChoiceBox<Integer> labelScore2;
    @FXML
    private ChoiceBox<Integer> labelScore3;
    @FXML
    private Label errorQuestionLabel;
    @FXML
    private Label errorUserIdLabel;
    @FXML
    private Label errorAnswersLabel;

    private ServiceQuestion serviceQuestion;

    public void initialize() {
        labelScore1.setItems(FXCollections.observableArrayList(-1, 0, 1));
        labelScore2.setItems(FXCollections.observableArrayList(-1, 0, 1));
        labelScore3.setItems(FXCollections.observableArrayList(-1, 0, 1));
    }
    public ControllerQuestion() {
        serviceQuestion = new ServiceQuestion();  // Create an instance of ServiceQuestion
    }

    @FXML
    void addQuestion(ActionEvent event) {
        try {
            int userId = Integer.parseInt(labelUserId.getText());
            int questionnaireId = Integer.parseInt(labelQuestionnaireId.getText());
            Question question = new Question();
            question.setTitleQuestion(labelTitleQuestion.getText());
            question.setIdUserId(userId);
            question.setQuestionnaireId(questionnaireId);

            addPropositionIfNotEmpty(question, labelAnswer1.getText(), labelScore1.getValue());
            addPropositionIfNotEmpty(question, labelAnswer2.getText(), labelScore2.getValue());
            addPropositionIfNotEmpty(question, labelAnswer3.getText(), labelScore3.getValue());

            serviceQuestion.addQuestionWithPropositions(question);
            System.out.println("Question and propositions added successfully!");
        } catch (NumberFormatException e) {
            errorUserIdLabel.setText("User ID and Questionnaire ID must be numbers.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error adding question and propositions.");
        }
    }

    private void addPropositionIfNotEmpty(Question question, String title, Integer score) {
        if (title != null && !title.isEmpty() && score != null) {
            Proposition proposition = new Proposition();
            proposition.setTitleProposition(title);
            proposition.setScore(score);
            question.addProposition(proposition);
        }
    }

    private boolean validateInputs() {
        boolean valid = true;
        if (labelTitleQuestion.getText().isEmpty() || !labelTitleQuestion.getText().endsWith("?")) {
            errorQuestionLabel.setText("Question must end with a '?'");
            valid = false;
        }

        if (labelUserId.getText().isEmpty()) {
            errorUserIdLabel.setText("User ID is required.");
            valid = false;
        }

        // Example of additional validation for answer texts and scores
        if (labelAnswer1.getText().isEmpty() ) {
            errorAnswersLabel.setText("All answers must be provided.");
            valid = false;
        }

        return valid;
    }
}
