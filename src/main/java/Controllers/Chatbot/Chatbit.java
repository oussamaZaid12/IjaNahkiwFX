package Controllers.Chatbot;

import java.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.IOException;
public class Chatbit {
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField messageField;
    @FXML
    private Button sendButton;
    private NLPProcessor nlpProcessor = new NLPProcessor();

    public String TESTgetResponse(String input) {
        String[] sentences = nlpProcessor.detectSentences(input);
        StringBuilder response = new StringBuilder();

        for (String sentence : sentences) {
            String[] tokens = nlpProcessor.tokenize(sentence);
            String[] tags = nlpProcessor.tagPOS(tokens);

            // Process each token with its tag
            for (int i = 0; i < tokens.length; i++) {
                System.out.println(tokens[i] + "/" + tags[i]);  // Debugging: print each token and its part of speech
            }

            response.append(processTokensTEST(tokens, tags));
        }

        return response.toString();
    }

    private String processTokensTEST(String[] tokens, String[] tags) {
        // Example: respond differently if a noun is mentioned
        for (int i = 0; i < tags.length; i++) {
            if (tags[i].startsWith("NN")) {  // If the tag starts with 'NN' (noun)
                return "It sounds like you're talking about " + tokens[i] + ". Can you tell me more?";
            }
        }

        return "Tell me more...";
    }

    @FXML
    private void sendMessage() {
        String userText = messageField.getText();
        if (!userText.isEmpty()) {
            chatArea.appendText("You: " + userText + "\n");
            String response = nlpProcessor.getResponse(userText);  // Ensure this method is defined in NLPProcessor
            chatArea.appendText("Bot: " + response + "\n");
            messageField.clear();
        }
    }
}

