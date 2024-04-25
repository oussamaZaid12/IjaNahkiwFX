package Controllers.Quiz;

import Controllers.Question.ControllerQuestion;
import entities.Questionnaire;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import services.ServiceQuestionnaire;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AjoutQuestionnaire {

    @FXML
    private Button LabelAjoutQuestionnaire;

    @FXML
    private DatePicker LabelDateQuestionnaire;

    @FXML
    private TextField LabelDescrpQuestionnaire;

    @FXML
    private TextField LabelTitreQuestionnaire;

    @FXML
    private TextField LabelUserQuestionnaire;
    @FXML
    private Label errorQuestionLabel;


    @FXML
    public void initialize() {
        LabelDateQuestionnaire.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) < 0);
            }
        });
    }

    @FXML
    void AjouterQuestionnaire(ActionEvent event) {
        if (!validateTitle(LabelTitreQuestionnaire.getText())) {
            showAlert("Invalid Input", "Error", "Title contains invalid characters.");
            return;
        }

        // Retrieve data from input fields
        String title = LabelTitreQuestionnaire.getText();
        String description = LabelDescrpQuestionnaire.getText();
        LocalDate date = LabelDateQuestionnaire.getValue();

        // Assuming time is not needed, just date
        LocalTime time = LocalTime.MIDNIGHT; // Use midnight time as only date is needed

        // Combine date and time into a LocalDateTime
        LocalDateTime dateTime = LocalDateTime.of(date, time);

        // Parse the user ID from input
        int userId = Integer.parseInt(LabelUserQuestionnaire.getText());

        // Create a new Questionnaire object
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setTitleQuestionnaire(title);
        questionnaire.setDescription(description);
        questionnaire.setDate(java.sql.Date.valueOf(dateTime.toLocalDate()));
        questionnaire.setIdUserId(userId);

        // Using the service to add the questionnaire to the database
        try {
            ServiceQuestionnaire serviceQuestionnaire = new ServiceQuestionnaire();
            serviceQuestionnaire.ajouter(questionnaire);
            System.out.println("Questionnaire added successfully!");
            // Here, you could also clear the form or give visual feedback
        } catch (SQLException e) {
            System.out.println("Error adding questionnaire: " + e.getMessage());
            // Additional error handling can be added here
        }
    }

    private boolean validateTitle(String title) {
        // Regex to check if title contains only letters, numbers, spaces, and basic punctuation
        return title.matches("[\\p{Alnum} \\s,.'-]+");
    }

    public void saveQuestionnaire() {
        String title = LabelTitreQuestionnaire.getText();
        if (!validateTitle(title)) {
            showAlert("Invalid Input", "Error", "Title contains invalid characters.");
            return;
        }
        // Proceed with saving or updating the questionnaire
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static class Validator {
        public static boolean validateQuestion(String title) {
            return title.matches("[A-Za-z0-9 ,.]*");
        }
    }
}