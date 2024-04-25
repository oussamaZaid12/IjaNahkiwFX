package Controllers.Question;

import entities.Question;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import services.ServiceQuestion;
import java.sql.SQLException;

public class EditQuestionController {

    @FXML
    private TextField questionTitleField;

    @FXML
    private Button saveButton;

    private ServiceQuestion serviceQuestion;
    private Question currentQuestion;

    public EditQuestionController() {
        serviceQuestion = new ServiceQuestion();
    }

    @FXML
    public void initialize() {
        // Initialization logic, if necessary
    }

    // Call this method when loading the controller to edit a specific question
    public void setQuestion(Question question) {
        currentQuestion = question;
        questionTitleField.setText(question.getTitleQuestion());
        // Set other fields as necessary
    }

    @FXML
    protected void handleSaveAction() {
        // Update the currentQuestion object with the new data from the form
        currentQuestion.setTitleQuestion(questionTitleField.getText());
        // Update other properties as necessary

        try {
            // Save the updated question
            serviceQuestion.modifier(currentQuestion);
            // Optionally, close the editor window and refresh the list in the main interface
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception, such as showing an error message to the user
        }
    }

    // Other methods as necessary, e.g., for canceling the edit
}
