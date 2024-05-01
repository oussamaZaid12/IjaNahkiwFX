package Controllers.Question;

import entities.Proposition;
import entities.Question;
import entities.Answer;
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

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardQ {
    private static final int QUESTIONS_PER_PAGE = 3; // Define the number of questions per page

    @FXML
    private VBox cardsContainer;
    @FXML
    private Pagination pagination;

    @FXML
    private Button submitButton;
    private List<Question> allQuestions;

    private ServiceQuestion serviceQuestion = new ServiceQuestion();
    private ServiceAnswer serviceAnswer = new ServiceAnswer(); // Service for answer-related operations

    @FXML
    public void initialize() {
        try {
            allQuestions = serviceQuestion.getAllQuestions();
            setupPagination();
        } catch (SQLException e) {
            e.printStackTrace(); // Proper exception handling
        }

    }
    private void loadQuestions() throws SQLException {
        List<Question> questions = serviceQuestion.getAllQuestions();
        for (Question question : questions) {
            VBox card = createCard(question);
            cardsContainer.getChildren().add(card);
        }
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
                selectedAnswer.setIdUserId(1); // Example user ID, should be dynamically set based on the logged-in user

                // Store or process the selected answer
                selectedAnswers.put(question, selectedAnswer);
            } else {
                selectedAnswers.remove(question);
            }
        });

        return card;
    }

    private void switchToResultsPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/Dashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) submitButton.getScene().getWindow();  // Assuming submitButton is part of the current stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Map<Question, Answer> selectedAnswers = new HashMap<>();


    // A method that can be called to submit the answers or move to the next page

    public void submitAnswers() {
        for (Map.Entry<Question, Answer> entry : selectedAnswers.entrySet()) {
            Question question = entry.getKey();
            Answer answer = entry.getValue();
            try {
                serviceAnswer.saveAnswer(answer);
                System.out.println("Saved Answer for Question ID: " + question.getId());
            } catch (SQLException e) {
                System.err.println("Failed to save answer for question ID " + question.getId());
                e.printStackTrace();
            }
        }
        switchToResultsPage();
    }


}


