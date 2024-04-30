package Controllers.Chatbot;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
public class ChatbotController {

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField messageField;

    @FXML
    private Button sendButton;

    @FXML
    private ScrollPane scrollPane;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    @FXML
    private VBox messageContainer;

    public ChatbotController() {
        new Thread(() -> {
            try {
                socket = new Socket("localhost", 5000);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                receiveMessage();  // Start receiving messages
            } catch (IOException e) {
                Platform.runLater(() -> chatArea.appendText("Failed to connect to the server.\n"));
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    public void sendMessage() {
        String userMessage = messageField.getText().trim();
        if (!userMessage.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String timestamp = sdf.format(new Date());
            appendUserMessage(userMessage, timestamp);
            out.println(userMessage);  // Send message through socket
            messageField.clear();
        }
    }


    private void receiveMessage() {
        new Thread(() -> {
            try {
                String serverResponse;
                while ((serverResponse = in.readLine()) != null) {
                    String finalResponse = serverResponse;
                    Platform.runLater(() -> appendBotMessage(finalResponse, new SimpleDateFormat("HH:mm").format(new Date())));
                }
            } catch (IOException e) {
                Platform.runLater(() -> chatArea.appendText("Server connection lost.\n"));
                e.printStackTrace();
            }
        }).start();
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



    public void closeConnection() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void stop() {
        closeConnection();
    }

}
