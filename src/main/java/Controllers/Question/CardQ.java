package Controllers.Question;

import entities.Question;
import entities.Answer;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import services.ServiceQuestion;
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
    private List<Question> allQuestions;

    private ServiceQuestion serviceQuestion = new ServiceQuestion();

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

    private AnchorPane TEST(Question question) {
        AnchorPane card = new AnchorPane();
        card.getStyleClass().add("question-card"); // Add a style class for CSS styling

        Label questionLabel = new Label(question.getTitleQuestion());
        questionLabel.getStyleClass().add("question-label");
        AnchorPane.setTopAnchor(questionLabel, 10.0);
        AnchorPane.setLeftAnchor(questionLabel, 10.0);
        AnchorPane.setRightAnchor(questionLabel, 10.0);
        card.getChildren().add(questionLabel);

        double yPos = 40.0;
        for (Answer answer : question.getProposedAnswers()) {
            Label answerLabel = new Label(answer.getPropositionChoisieId());
            answerLabel.getStyleClass().add("answer-label");
            AnchorPane.setTopAnchor(answerLabel, yPos);
            AnchorPane.setLeftAnchor(answerLabel, 10.0);
            AnchorPane.setRightAnchor(answerLabel, 10.0);
            card.getChildren().add(answerLabel);
            yPos += 30.0;
        }

        return card;
    }
    private VBox TEST2(Question question) {
        VBox card = new VBox(5);
        card.getStyleClass().add("question-card");

        Label questionLabel = new Label(question.getTitleQuestion());
        questionLabel.getStyleClass().add("question-label");
        card.getChildren().add(questionLabel);

        for (Answer answer : question.getProposedAnswers()) {
            Label answerLabel = new Label(answer.getPropositionChoisieId()); // Use the correct method to get answer text
            answerLabel.getStyleClass().add("answer-label");
            card.getChildren().add(answerLabel);
        }

        return card; // Return type should be VBox since this is the unique createCard method
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

        for (Answer answer : question.getProposedAnswers()) {
            RadioButton answerButton = new RadioButton(answer.getPropositionChoisieId());
            answerButton.setToggleGroup(group);
            answerButton.getStyleClass().add("answer-button");
            // Add a listener or event handler if needed to handle answer selection
            card.getChildren().add(answerButton);
        }

        // Event handler for card click (if needed to handle card selection)
        card.setOnMouseClicked(event -> handleCardSelection(question, group));

        return card;
    }

    private Map<Question, Answer> selectedAnswers = new HashMap<>();

    private void handleCardSelection(Question question, ToggleGroup group) {
        // This method is called when a card is clicked.
        // You can handle card click specifically if needed.
        // For now, we'll just listen to changes on the ToggleGroup to record the answer.

        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                RadioButton selectedButton = (RadioButton) newValue;
                String answerText = selectedButton.getText();

                // Find the Answer object corresponding to the selected text
                Answer selectedAnswer = question.getProposedAnswers().stream()
                        .filter(answer -> answer.getPropositionChoisieId().equals(answerText))
                        .findFirst()
                        .orElse(null); // or handle the case where the answer is not found

                // Store the selected answer for the question
                selectedAnswers.put(question, selectedAnswer);
            } else {
                // If no answer is selected (e.g., the user deselected an answer)
                selectedAnswers.remove(question);
            }
        });
    }

    // A method that can be called to submit the answers or move to the next page
    public void submitAnswers() {
        // Do something with the selected answers
        // For example, you can iterate over the selectedAnswers map and save them to a database
        for (Map.Entry<Question, Answer> entry : selectedAnswers.entrySet()) {
            Question question = entry.getKey();
            Answer answer = entry.getValue();
            System.out.println("Question: " + question.getTitleQuestion() + ", Selected Answer: " + (answer != null ? answer.getPropositionChoisieId() : "None"));
            // Here you would write code to save this information
        }
    }

}


