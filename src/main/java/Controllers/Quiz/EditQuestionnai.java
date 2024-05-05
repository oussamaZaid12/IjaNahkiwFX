package Controllers.Quiz;

import entities.Questionnaire;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import services.ServiceQuestionnaire;

import Controllers.User.Session;
import entities.User;
import java.sql.SQLException;

public class EditQuestionnai {

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

    private Questionnaire currentQuestionnaire; // Holds the questionnaire being edited

    // Method to initialize the form with a Questionnaire object
    public void setQuestionnaire(Questionnaire questionnaire) {
        if (questionnaire != null) {
            this.currentQuestionnaire = questionnaire;
            LabelTitreQuestionnaire.setText(questionnaire.getTitleQuestionnaire());
            LabelDescrpQuestionnaire.setText(questionnaire.getDescription());
            LabelDateQuestionnaire.setValue(((java.sql.Date) questionnaire.getDate()).toLocalDate());
            //LabelUserQuestionnaire.setText(String.valueOf(questionnaire.getIdUserId()));
        } else {
            System.out.println("Questionnaire is null");
            // Optionally handle the scenario when questionnaire is null, e.g., show an error message or log it.
        }
    }


    @FXML
    void EditQuestionnaire(ActionEvent event) {
        try {
            User currentUser = Session.getUser();  // Get the logged-in user
            if (currentUser == null) {
                showAlert("Error", "No user logged in.");
                return;
            }
            // Update the currentQuestionnaire object with new values from form
            currentQuestionnaire.setTitleQuestionnaire(LabelTitreQuestionnaire.getText());
            currentQuestionnaire.setDescription(LabelDescrpQuestionnaire.getText());
            //currentQuestionnaire.setDate(java.sql.Date.valueOf(LabelDateQuestionnaire.getValue()));

            // Attempt to update questionnaire in the database
            ServiceQuestionnaire serviceQuestionnaire = new ServiceQuestionnaire();
            serviceQuestionnaire.modifier(currentQuestionnaire);  // Assuming there's a method to update questionnaire in your service
            System.out.println("Questionnaire updated successfully!");

            // Optionally, close the window or clear the form here if needed
        } catch (SQLException e) {
            System.out.println("Error updating questionnaire: " + e.getMessage());
            // Handle error, possibly show an error message to the user
        }    catch (Exception e) {
        showAlert( "Erreur", "Une erreur inattendue est survenue.");
        e.printStackTrace();
    }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
