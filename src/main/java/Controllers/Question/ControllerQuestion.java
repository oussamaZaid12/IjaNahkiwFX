package Controllers.Question;

import Controllers.User.Session;
import entities.Proposition;
import entities.Question;
import entities.Questionnaire;
import entities.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
    @FXML
    private Label errorQuestionLabel;
    @FXML
    private Label errorUserIdLabel;
    @FXML
    private Label errorAnswersLabel;

    private ServiceQuestion serviceQuestion;
    private Questionnaire currentQuestionnaire;

    public void initialize() {
        labelScore1.setItems(FXCollections.observableArrayList(-1, 0, 1));
        labelScore2.setItems(FXCollections.observableArrayList(-1, 0, 1));
        labelScore3.setItems(FXCollections.observableArrayList(-1, 0, 1));
    }
    public ControllerQuestion() {
        serviceQuestion = new ServiceQuestion();  // Create an instance of ServiceQuestion
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.currentQuestionnaire = questionnaire;
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
        StringBuilder errorMessage = new StringBuilder();

        // Validate question title input
        if (labelTitleQuestion.getText().isEmpty() || !labelTitleQuestion.getText().endsWith("?")) {
            errorMessage.append("Question must end with a '?' and should not be empty.\n");
            valid = false;
        }

        // Validate proposition 1
        if (proposition1Column.getText().isEmpty() || proposition2Column.getText().isEmpty() || proposition3Column.getText().isEmpty() ) {
            errorMessage.append("Propositions shouldn't be empty.\n");
            valid = false;
        }

        // Validate proposition 2
        if ( labelScore1.getValue() == null || labelScore2.getValue() == null || labelScore3.getValue() == null) {
            errorMessage.append("Propositions must be provided with a valid score.\n");
            valid = false;
        }

        // Validate proposition 3

        // If there are errors, display them in the appropriate label or alert
        if (!valid) {
            errorAnswersLabel.setText(errorMessage.toString().trim());
        } else {
            errorAnswersLabel.setText(""); // Clear any previous error messages if validation passes
        }

        return valid;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    void addQuestion(ActionEvent event) {
        if (!validateInputs()) {
            return; // Stop execution if validation fails
        }

        try {
            // Get the current user
            User currentUser = Session.getUser();
            if (currentUser == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur de session", "Aucun utilisateur connecté.");
                return;
            }

            // Ensure a questionnaire is selected
            if (currentQuestionnaire == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun questionnaire sélectionné.");
                return;
            }

            // Retrieve user and questionnaire IDs
            int idUser = currentUser.getId();
            int questionnaireId = currentQuestionnaire.getId();

            // Create and populate the new question object
            Question question = new Question();
            question.setQuestionnaireId(questionnaireId);
            question.setTitleQuestion(labelTitleQuestion.getText());
            question.setIdUserId(idUser);

            // Add propositions if not empty
            addPropositionIfNotEmpty(question, proposition1Column.getText(), labelScore1.getValue());
            addPropositionIfNotEmpty(question, proposition2Column.getText(), labelScore2.getValue());
            addPropositionIfNotEmpty(question, proposition3Column.getText(), labelScore3.getValue());

            // Insert the question and associated propositions
            serviceQuestion.addQuestionWithPropositionss(question);
            System.out.println("Question and propositions added successfully!");

        } catch (NumberFormatException e) {
            errorUserIdLabel.setText("User ID and Questionnaire ID must be numbers.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error adding question and propositions.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur inattendue est survenue.");
            e.printStackTrace();
        }
    }

}
