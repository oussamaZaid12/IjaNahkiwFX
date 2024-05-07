package Controllers.Quiz;

import Controllers.Question.AffichageQuestionController;
import Controllers.Question.CardQ;
import Controllers.Question.ControllerQuestion;
import entities.Questionnaire;
import Controllers.User.Session;
import entities.User;

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
import services.ServiceQuestionnaire;
import test.MainFX;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AffichageQuestionnaireController {
    private Questionnaire currentQuestionnaire; // Holds the questionnaire being edited

    @FXML private TableView<Questionnaire> questionnairesTable;
    @FXML private TableColumn<Questionnaire, Integer> idColumn;
    @FXML private TableColumn<Questionnaire, String> titleColumn;
    @FXML private TableColumn<Questionnaire, String> descriptionColumn;
    @FXML private TableColumn<Questionnaire, java.sql.Date> dateColumn;
    @FXML private TableColumn<Questionnaire, Integer> userIdColumn;
    @FXML
    private AnchorPane mainContainerQUES;

    @FXML
    private AnchorPane ContainQuest;
    private ServiceQuestionnaire service = new ServiceQuestionnaire();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("titleQuestionnaire"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("idUserId"));
        loadQuestionnaires();
    }

    private void loadQuestionnaires() {
        try {
            List<Questionnaire> list = service.afficher(); // Your method to get the list of questionnaires
            ObservableList<Questionnaire> observableList = FXCollections.observableArrayList(list);
            questionnairesTable.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        Questionnaire selected = questionnairesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/Quiz/EditQuestionnaire.fxml"));
                Parent root = loader.load();
                EditQuestionnai controller = loader.getController();

                controller.setQuestionnaire(selected); // Ensure selected is not null
                mainContainerQUES.getChildren().setAll(root);
                //Stage stage=new Stage();
                //stage.setScene(new Scene(root));
                //stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No Questionnaire selected for editing");
        }
    }

    @FXML
    private void handleListQuestion(ActionEvent event) {
        Questionnaire selected = questionnairesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                // Load the `AffichageQuestion` view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/Question/QuestionView.fxml"));
                Parent root = loader.load();

                // Retrieve the controller and set the questionnaire
                AffichageQuestionController controller = loader.getController();
                controller.setQuestionnaire(selected);  // Implement the `setQuestionnaire` method in `AffichageQuestionController`
                mainContainerQUES.getChildren().setAll(root);
                // Set up the new stage
                //Stage stage = new Stage();
                //stage.setTitle("Questions for " + selected.getTitleQuestionnaire());
               // stage.setScene(new Scene(root));
               // stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("No Selection", "No Questionnaire Selected", "Please select a questionnaire in the table.");
        }
    }

    @FXML
    private void handleDelete() {
        Questionnaire selected = questionnairesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (showConfirmationDialog("Confirm Delete", "Are you sure you want to delete this questionnaire?")) {
                try {
                    service.supprimer(selected);
                    loadQuestionnaires();  // Refresh list after delete
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    showAlert("Error", "Delete Failed", "Failed to delete the selected questionnaire.");
                }
            }
        } else {
            showAlert("No Selection", "No Questionnaire Selected", "Please select a questionnaire in the table.");
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


    public void handleAddQuest(ActionEvent actionEvent) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/Quiz/AjoutQuestionnaire.fxml"));
                Parent root = loader.load();

                // Pass the questionnaire data to the AddQuestion controller
                AjoutQuestionnaire addController = loader.getController();
                addController.initialize();
                mainContainerQUES.getChildren().setAll(root);

                //Stage stage = new Stage();
                //stage.setTitle("Add Question to " + currentQuestionnaire.getTitleQuestionnaire());
                //stage.setScene(new Scene(root));
                //stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
}
