package Controllers.Chatbot;

import java.util.*;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.IOException;
public class Chatbit {
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField messageField;
    @FXML
    private Button sendButton;
    @FXML
    private VBox messageContainer;

    @FXML
    private ScrollPane scrollPane;


    private NLPProcessor nlpController;

    public Chatbit() {
        this.nlpController = new NLPProcessor();  // Initialize the NLP controller
    }

    @FXML

    public void sendMessage() {
        String userMessage = messageField.getText().trim();
        if (!userMessage.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String timestamp = sdf.format(new Date());
            appendUserMessage(userMessage, timestamp);
            messageField.clear();

            // Tokenize the message and prepare tags
            String[] tokens = userMessage.split("\\s+");  // Simple whitespace split
            String[] tags = new String[tokens.length];  // Dummy tags array for example

            // Run NLP processing in a background thread
            new Thread(() -> {
                try {
                    // Use the processTokens method from NLPProcessorsController
                    String botResponse = nlpController.processTokens(tokens, tags);

                    // Update UI in the JavaFX Application Thread
                    Platform.runLater(() -> appendBotMessage(botResponse, timestamp));
                } catch (Exception e) {
                    Platform.runLater(() -> appendBotMessage("Error processing your message. Please try again.", timestamp));
                }
            }).start();
        }
    }


    private void appendUserMessage(String message, String timestamp) {
        Text text = new Text(timestamp + " You: " + message);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: lightblue; -fx-padding: 5px; -fx-background-radius: 5px;");
        textFlow.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(textFlow, new Insets(5, 10, 5, 10)); // Add some margin for aesthetic spacing
        HBox hbox = new HBox(textFlow);  // Wrap TextFlow in HBox for better alignment control
        hbox.setAlignment(Pos.CENTER_RIGHT);  // Align to the right
        messageContainer.getChildren().add(hbox);
        scrollToBottom();
    }

    private void appendBotMessage(String message,String timestamp) {
        Text text = new Text(timestamp + " You: " + message);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: lightgrey; -fx-padding: 5px; -fx-background-radius: 5px;");
        textFlow.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(textFlow, new Insets(5, 10, 5, 10)); // Add some margin for aesthetic spacing
        HBox hbox = new HBox(textFlow);  // Wrap TextFlow in HBox for better alignment control
        hbox.setAlignment(Pos.CENTER_LEFT);  // Align to the left
        messageContainer.getChildren().add(hbox);
        scrollToBottom();
    }

    private void scrollToBottom() {
        // This should be run on the JavaFX application thread
        Platform.runLater(() -> {
            scrollPane.setVvalue(1.0); // Scroll to the bottom
        });
    }

}

