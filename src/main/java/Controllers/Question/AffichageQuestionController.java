package Controllers.Question;

import entities.Question;
import entities.Questionnaire;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import services.ServiceQuestion;
import test.MainFX;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AffichageQuestionController {
    @FXML
    private TableView<Question> questionsTable;
    @FXML
    private TableColumn<Question, Integer> questionIdColumn;
    @FXML
    private TableColumn<Question, String> questionTitleColumn;
    @FXML
    private TableColumn<Question, String> proposition1Column;
    @FXML
    private TableColumn<Question, String> proposition2Column;
    @FXML
    private TableColumn<Question, String> proposition3Column;
    @FXML
    private AnchorPane mainContainerQUES;
    private ServiceQuestion service = new ServiceQuestion();
    private Questionnaire currentQuestionnaire;

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.currentQuestionnaire = questionnaire;
        loadQuestionsByQuestionnaire(questionnaire.getId());
    }

    @FXML
    public void initialize() {
        questionIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        questionTitleColumn.setCellValueFactory(new PropertyValueFactory<>("titleQuestion"));
        proposition1Column.setCellValueFactory(new PropertyValueFactory<>("proposition1"));
        proposition2Column.setCellValueFactory(new PropertyValueFactory<>("proposition2"));
        proposition3Column.setCellValueFactory(new PropertyValueFactory<>("proposition3"));
    }

    @FXML
    private void loadQuestionsByQuestionnaire(int questionnaireId) {
        try {
            List<Question> questions = service.getQuestionsByQuestionnaire(questionnaireId);
            ObservableList<Question> observableList = FXCollections.observableArrayList(questions);
            questionsTable.setItems(observableList);
        } catch (SQLException e) {
            showAlert("Error", "Loading Error", "Error loading questions from the database.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEdit() {
        Question selected = questionsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/Question/EditQuestion.fxml")); // Correct the path
                Parent root = loader.load();
                EditQuestionController controller = loader.getController(); // Use the correct controller class for editing questions
                controller.setQuestion(selected); // Make sure the edit controller has this method
                Stage stage = new Stage();
                stage.setTitle("Edit Question");
                stage.setScene(new Scene(root));
                stage.showAndWait();  // Use showAndWait to handle modifications before reloading data
                loadQuestions();// Assuming you have a method setCenterView to change the main content
                MainFX.setCenterView(root); // You may need to adapt this line to your main application's navigation logic
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("No Selection", "No Question Selected", "Please select a question in the table.");
        }
    }

    @FXML
    private void handleAddQuestion() {
        if (currentQuestionnaire != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/Question/AjoutQuestion.fxml"));
                Parent root = loader.load();

                // Pass the questionnaire data to the AddQuestion controller
                ControllerQuestion addController = loader.getController();
                addController.setQuestionnaire(currentQuestionnaire);
                //ajoutQues.getChildren().setAll(root);

                Stage stage = new Stage();
                stage.setTitle("Add Question to " + currentQuestionnaire.getTitleQuestionnaire());
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("No Questionnaire", "No Questionnaire Selected", "Please select a questionnaire before adding questions.");
        }
    }

    @FXML
    private void handleDelete() {
        Question selected = questionsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (showConfirmationDialog("Confirm Delete", "Are you sure you want to delete this question?")) {
                try {
                    service.supprimer(selected); // Passing the whole Question object
                    loadQuestions();
                    showAlert("Success", "Delete Successful", "The question has been deleted successfully.");
                    // Refresh list after delete
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    showAlert("Error", "Delete Failed", "Failed to delete the selected question.");
                }
            }
        } else {
            showAlert("No Selection", "No Question Selected", "Please select a question in the table.");
        }
    }

    @FXML
    private void loadQuestions5() {
        if (currentQuestionnaire == null) {
            showAlert("No Questionnaire", "Please select a valid questionnaire first.", "");
            return;
        }

        try {
            List<Question> questions = service.getQuestionsByQuestionnaire(currentQuestionnaire.getId());
            ObservableList<Question> observableList = FXCollections.observableArrayList(questions);
            questionsTable.setItems(observableList);
        } catch (SQLException e) {
            showAlert("Error", "Loading Error", "Error loading questions from the database.");
            e.printStackTrace();
        }
    }

    @FXML
    private void loadQuestions2() {
        try {
            List<Question> list = service.afficher(); // Adapt this method to fetch questions with concatenated answers
            ObservableList<Question> observableList = FXCollections.observableArrayList(list);
            questionsTable.setItems(observableList);
        } catch (Exception e) {
            showAlert("Error", "Loading Error", "Error loading questions from the database.");
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean showConfirmationDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void loadQuestionsByQuestionnaire2(int questionnaireId) {
        try {
            // Fetch questions associated with the given questionnaire
            List<Question> list = service.getQuestionsByQuestionnaire(questionnaireId);  // Implement `getQuestionsByQuestionnaire`
            ObservableList<Question> observableList = FXCollections.observableArrayList(list);
            questionsTable.setItems(observableList);
        } catch (Exception e) {
            showAlert("Error", "Loading Error", "Error loading questions for the selected questionnaire.");
            e.printStackTrace();
        }
    }

    @FXML
    private void goBackToQuestionnaire() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/Quiz/affichageQuestionnaire.fxml"));  // Adjust this path
            Parent root = loader.load();
            mainContainerQUES.getChildren().setAll(root);  // Replace content with the questionnaire view
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void loadQuestions() {
        // Use your updated service method to fetch questions
        try {
            List<Question> questions = service.getQuestionsByQuestionnaire(currentQuestionnaire.getId());
            ObservableList<Question> observableList = FXCollections.observableArrayList(questions);
            questionsTable.setItems(observableList);
        } catch (SQLException e) {
            showAlert("Error", "Loading Error", "Error loading questions from the database.");
            e.printStackTrace();
        }

    }
}
