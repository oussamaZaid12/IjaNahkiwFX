package Controllers.Question;

import Controllers.Consultation.AffichageConsultationpatient;
import Controllers.Quiz.ResultController;
import Controllers.User.Session;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import entities.Proposition;
import entities.Question;
import entities.Answer;
import entities.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import services.ServiceAnswer;
import services.ServiceQuestion;
import test.PredictionService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CardQ {
    private static final int QUESTIONS_PER_PAGE = 3; // Define the number of questions per page
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private VBox cardsContainer;
    @FXML
    private Pagination pagination;
    @FXML
    private VBox vbox;
    @FXML
    private Button submitButton;

    private List<Question> allQuestions;

    private ServiceQuestion serviceQuestion = new ServiceQuestion();
    private ServiceAnswer serviceAnswer = new ServiceAnswer(); // Service for answer-related operations
    private Map<Question, Answer> selectedAnswers = new HashMap<>();
    private PredictionService predictionService = new PredictionService("http://localhost:8000/fastapi/");
    private User currentUser = Session.getUser();

    @FXML
    public void initialize() {
        try {
            allQuestions = serviceQuestion.getQuestionsByQuestionnaire(2);
            setupPagination();

            // Bind the VBox's anchors to dynamically center it in the AnchorPane
            vbox.prefWidthProperty().addListener((obs, oldVal, newVal) -> updateVBoxAnchors());
            anchorPane.widthProperty().addListener((obs, oldVal, newVal) -> updateVBoxAnchors());

        } catch (SQLException e) {
            e.printStackTrace(); // Proper exception handling
        }
    }

    private void updateVBoxAnchors() {
        double paneWidth = anchorPane.getWidth();
        double vboxWidth = vbox.getPrefWidth();
        double anchorValue = Math.max((paneWidth - vboxWidth) / 2, 0);
        AnchorPane.setLeftAnchor(vbox, anchorValue);
        AnchorPane.setRightAnchor(vbox, anchorValue);
    }


    private void setupPagination() {
        int numPages = (int) Math.ceil((double) allQuestions.size() / QUESTIONS_PER_PAGE);
        pagination.setPageCount(numPages);
        pagination.setPageFactory(pageIndex -> {
            VBox box = new VBox(5);
            int pageStartIndex = pageIndex * QUESTIONS_PER_PAGE;
            int pageEndIndex = Math.min(pageStartIndex + QUESTIONS_PER_PAGE, allQuestions.size());
            for (int i = pageStartIndex; i < pageEndIndex; i++) {
                box.getChildren().add(createCard(allQuestions.get(i)));
            }
            submitButton.setVisible(pageIndex == numPages - 1);
            return box;
        });
    }
    private VBox createCard(Question question) {
        VBox card = new VBox(5);
        card.getStyleClass().add("question-card");

        Label questionLabel = new Label(question.getTitleQuestion());
        questionLabel.getStyleClass().add("question-label");
        card.getChildren().add(questionLabel);

        // Create a toggle group for the radio buttons
        ToggleGroup group = new ToggleGroup();

        for (Proposition proposition : question.getPropositions()) {
            RadioButton answerButton = new RadioButton(proposition.getTitleProposition());
            answerButton.setUserData(proposition.getId()); // Store proposition ID as user data
            answerButton.setToggleGroup(group);
            answerButton.getStyleClass().add("answer-button");
            card.getChildren().add(answerButton);
        }

        // Handling selection from the toggle group
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                RadioButton selectedButton = (RadioButton) newValue;
                int propositionId = (Integer) selectedButton.getUserData(); // Retrieve the stored proposition ID

                // Here, you would create an Answer object or handle the response
                Answer selectedAnswer = new Answer();
                selectedAnswer.setQuestionId(question.getId());
                selectedAnswer.setPropositionChoisieId(propositionId);
                selectedAnswer.setIdUserId(currentUser.getId()); // Example user ID, should be dynamically set based on the logged-in user

                // Store or process the selected answer
                selectedAnswers.put(question, selectedAnswer);
            } else {
                selectedAnswers.remove(question);
            }
        });

        return card;
    }


    public void submitAnswers() {
        List<Integer> scores = selectedAnswers.values().stream()
                .map(answer -> {
                    Proposition prop = findPropositionById(answer.getPropositionChoisieId());
                    return prop != null ? prop.getScore() : 0;
                })
                .collect(Collectors.toList());

        System.out.println("Scores to be submitted: " + scores); // Debug line

        new Thread(() -> {
            try {
                for (Answer answer : selectedAnswers.values()) {
                    serviceAnswer.saveAnswer(answer);
                    System.out.println("Successfully saved answer for question ID " + answer.getQuestionId());
                }
                Map<String, Object> results = predictionService.predict(scores);
                Platform.runLater(() -> updateUIAfterPrediction(results));
            } catch (Exception e) {
                Platform.runLater(() -> {
                    showErrorOnUI("Error during API request:  " + e.getMessage());
                });
            }
        }).start();
    }


    private void updateUIAfterPrediction(Map<String, Object> results) {

        if ((boolean) results.getOrDefault("success", false)) {
            Object predictionObj = results.get("prediction");
            int prediction;
            if (predictionObj instanceof Integer) {
                prediction = (Integer) predictionObj;
                System.out.println("prediction" + prediction);
            } else if (predictionObj instanceof Double) {
                prediction = ((Double) predictionObj).intValue();
                System.out.println("prediction" + prediction);
            } else {
                showErrorOnUI("Invalid data type for prediction result.");
                return;
            }

            if (prediction == 1) {
               displayResults("We advise you to take an appointment with one of our therapists.");
            } else {
                displayResults("Your health status is good. Check our activities of the week.");
            }
        } else {
            String error = (String) results.getOrDefault("error", "Failed to obtain a valid prediction result.");
            showErrorOnUI(error);
        }
    }




    private void displayResults(String results) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/Quiz/Results.fxml"));
            Parent root = loader.load();
            ResultController controller = loader.getController();
            controller.setResults(results);

            Stage stage = new Stage();
            stage.setTitle("Prediction Results");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showErrorOnUI(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("A Problem Occurred");
        alert.setContentText("Error details: " + error);
        alert.showAndWait();
    }


    private Proposition findPropositionById(int id) {
        if (allQuestions == null) {
            return null;
        }
        for (Question question : allQuestions) {
            for (Proposition proposition : question.getPropositions()) {
                if (proposition.getId() == id) {
                    return proposition;
                }
            }
        }
        return null; // Return null if the proposition is not found
    }



}


