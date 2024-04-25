package Controllers.Question;

import entities.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import services.ServiceQuestion;
import test.MainFX;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AffichageQuestionController {
    @FXML private TableView<Question> questionsTable;
    @FXML private TableColumn<Question, Integer> questionIdColumn;
    @FXML private TableColumn<Question, String> questionTitleColumn;
    @FXML private TableColumn<Question, String> answer1Column;
    @FXML private TableColumn<Question, String> answer2Column;
    @FXML private TableColumn<Question, String> answer3Column;

    private ServiceQuestion service = new ServiceQuestion();

    @FXML
    public void initialize() {
        questionIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        questionTitleColumn.setCellValueFactory(new PropertyValueFactory<>("titleQuestion"));
        answer1Column.setCellValueFactory(new PropertyValueFactory<>("answer1"));
        answer2Column.setCellValueFactory(new PropertyValueFactory<>("answer2"));
        answer3Column.setCellValueFactory(new PropertyValueFactory<>("answer3"));
        loadQuestions();
    }
    @FXML
    private void handleEdit(ActionEvent event) {
        Question selected = questionsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/EditQuestion.fxml")); // Correct the path
                Parent root = loader.load();
                EditQuestionController controller = loader.getController(); // Use the correct controller class for editing questions
                controller.setQuestion(selected); // Make sure the edit controller has this method
                // Assuming you have a method setCenterView to change the main content
                MainFX.setCenterView(root); // You may need to adapt this line to your main application's navigation logic
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("No Selection", "No Question Selected", "Please select a question in the table.");
        }
    }

    @FXML
    private void handleDelete() {
        Question selected = questionsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (showConfirmationDialog("Confirm Delete", "Are you sure you want to delete this question?")) {
                try {
                    service.supprimer(selected); // Passing the whole Question object
                    loadQuestions(); // Refresh list after delete
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
    private void loadQuestions() {
        try {
            List<Question> list = service.afficher(); // Adapt this method to fetch questions with concatenated answers
            ObservableList<Question> observableList = FXCollections.observableArrayList(list);
            questionsTable.setItems(observableList);
        } catch (Exception e) {
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

}
