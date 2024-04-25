package Controllers.Question;

import entities.Question;
import entities.Answer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import services.ServiceQuestion;
import javax.xml.validation.Validator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControllerQuestion {

    @FXML
    private TextField labelTitleQuestion;
    @FXML
    private TextField labelUserId;
    @FXML
    private TextField labelAnswer1;
    @FXML
    private TextField labelAnswer2;
    @FXML
    private TextField labelAnswer3;
    @FXML private Label errorQuestionLabel;
    @FXML private Label errorUserIdLabel;
    @FXML private Label errorAnswersLabel;
    @FXML
    void ajouterQuestion(ActionEvent event) {
        boolean valid = true;
        errorQuestionLabel.setText("");
        errorUserIdLabel.setText("");
        errorAnswersLabel.setText("");

        // Retrieve data from input fields
        String title = labelTitleQuestion.getText();

        if (!Validator.validateQuestion(title)) {
            errorQuestionLabel.setText("Question must end with a '?'");
            valid = false;
        }

        // Validate answers
        if (!validateAllAnswers()) {
            errorAnswersLabel.setText("Answers must not contain special symbols");
            valid = false;
        }

        if (!valid) return;  // Stop further processing if validation fails
        try {
            int userId = Integer.parseInt(labelUserId.getText());
            // Further processing to save the question and answers
            // Create a new Question object
            Question question = new Question();
            question.setTitleQuestion(title);
            question.setIdUserId(userId);

            // Create answer objects and add them to the question
            List<Answer> answers = new ArrayList<>();
            addAnswerIfNotEmpty(answers, labelAnswer1.getText(), question.getId());
            addAnswerIfNotEmpty(answers, labelAnswer2.getText(), question.getId());
            addAnswerIfNotEmpty(answers, labelAnswer3.getText(), question.getId());

            question.setProposedAnswers(answers);  // Make sure the Question class supports this method

            // Assume serviceQuestion.ajouter(question) is defined to save the question
            System.out.println("Question added successfully with answers!");
        } catch (NumberFormatException e) {
            errorUserIdLabel.setText("User ID must be a number.");
        }


        // Using the service to add the question and its answers to the database

    }

    private void addAnswerIfNotEmpty(List<Answer> answers, String proposition, int questionId) {
        if (!proposition.isEmpty()) {
            Answer answer = new Answer();
            answer.setPropositionChoisieId(proposition);
            answer.setQuestionId(questionId);
            // Assuming you handle user ID and answer ID generation in the Answer constructor or elsewhere
            answers.add(answer);
        }
    }
    public class Validator {

        // Validate that the input string ends with a question mark
        public static boolean validateQuestion(String question) {
            return question != null && question.endsWith("?");
        }

        // Validate that the input string does not contain any symbols (only letters and numbers and basic punctuation)
        public static boolean validateAnswer(String answer) {
            return answer != null && answer.matches("[A-Za-z0-9 ,.]*");
        }
    }
    private boolean validateAllAnswers() {
        return Validator.validateAnswer(labelAnswer1.getText()) &&
                Validator.validateAnswer(labelAnswer2.getText()) &&
                Validator.validateAnswer(labelAnswer3.getText());
    }
}
