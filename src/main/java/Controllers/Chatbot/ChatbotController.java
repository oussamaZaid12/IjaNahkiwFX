package Controllers.Chatbot;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
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


    public void initialize() {
        try {
            socket = new Socket("localhost", 5000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            receiveMessage();
        } catch (IOException e) {
            chatArea.appendText("Failed to connect to the server.\n");
            e.printStackTrace();
        }
    }
    public void sendMessage() {
        String message = messageField.getText();
        chatArea.appendText("You: " + message + "\n");
        out.println(message);
        messageField.clear();
        receiveMessage();
    }

    private void receiveMessage() {
        new Thread(() -> {
            try {
                String response;
                while ((response = in.readLine()) != null) {
                    String finalResponse = response;
                    Platform.runLater(() -> chatArea.appendText("Server: " + finalResponse + "\n"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
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
