package Controllers.Quiz;

import Controllers.Question.ControllerQuestion;
import entities.Questionnaire;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import services.ServiceQuestionnaire;
import entities.User;
import Controllers.User.Session;

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
            showAlert(Alert.AlertType.ERROR, "Invalid Input : Error", "Title contains invalid characters.");
            return;
        }
        try {
            User currentUser = Session.getUser();
            if (currentUser == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur de session", "Aucun utilisateur connecté.");
                return;
            }

        // Retrieve data from input fields
        String title = LabelTitreQuestionnaire.getText();
        String description = LabelDescrpQuestionnaire.getText();
        int idUser = currentUser.getId();
        LocalDate dateLocal = LabelDateQuestionnaire.getValue();
        java.sql.Date date = java.sql.Date.valueOf(dateLocal);
        // Assuming time is not needed, just date
        //LocalTime time = LocalTime.MIDNIGHT; // Use midnight time as only date is needed

        // Combine date and time into a LocalDateTime
        //LocalDateTime dateTime = LocalDateTime.of(date, time);

        // Parse the user ID from input
        //int userId = Integer.parseInt(LabelUserQuestionnaire.getText());

        // Create a new Questionnaire object
        Questionnaire questionnaire = new Questionnaire(title,description,date,idUser);
        questionnaire.setTitleQuestionnaire(title);
        questionnaire.setDescription(description);
        questionnaire.setDate(date);
        questionnaire.setIdUserId(idUser);

        // Using the service to add the questionnaire to the database

            ServiceQuestionnaire serviceQuestionnaire = new ServiceQuestionnaire();
            serviceQuestionnaire.ajouter(questionnaire);
            System.out.println("Questionnaire added successfully!");
            // Here, you could also clear the form or give visual feedback
        } catch (SQLException e) {
            System.out.println("Error adding questionnaire: " + e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur inattendue est survenue.");
            e.printStackTrace();
        }   // Additional error handling can be added here

    }

    private boolean validateTitle(String title) {
        // Regex to check if title contains only letters, numbers, spaces, and basic punctuation
        return title.matches("[\\p{Alnum} \\s,.'-]+");
    }

    public void saveQuestionnaire() {
        String title = LabelTitreQuestionnaire.getText();
        if (!validateTitle(title)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input ,Error", "Title contains invalid characters.");
            return;
        }
        // Proceed with saving or updating the questionnaire
    }

    private void showAlert1(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public static class Validator {
        public static boolean validateQuestion(String title) {
            return title.matches("[A-Za-z0-9 ,.]*");
        }
    }
}