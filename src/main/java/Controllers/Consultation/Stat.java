package Controllers.Consultation;

import Controllers.User.Session;
import entities.User;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import services.ServiceConsultation;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Stat implements Initializable {

    @FXML
    private Button stat;
    @FXML
    private PieChart pieChart;
    @FXML
    private AnchorPane ConsultationPane;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializePieChart(); // Call the method to initialize the pie chart
    }


    // Method to initialize the pie chart
    private void initializePieChart() {
        // Fetch data for confirmed and non-confirmed consultations from your data source
        User currentUser = Session.getUser();
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur de session", "Aucun utilisateur connecté.");
            return;
        }
        ServiceConsultation consultationService = new ServiceConsultation();
        int confirmedCount = 0;
        try {
            confirmedCount = consultationService.getConfirmedConsultationCount(currentUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int nonConfirmedCount = 0;
        try {
            nonConfirmedCount = consultationService.getNonConfirmedConsultationCount(currentUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Calculate percentages
        int totalCount = confirmedCount + nonConfirmedCount;
        double confirmedPercentage = ((double) confirmedCount / totalCount) * 100;
        double nonConfirmedPercentage = ((double) nonConfirmedCount / totalCount) * 100;

        // Create PieChart.Data objects
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Confirmed (" + String.format("%.1f", confirmedPercentage) + "%)", confirmedCount),
                new PieChart.Data("Non-Confirmed (" + String.format("%.1f", nonConfirmedPercentage) + "%)", nonConfirmedCount)
        );

        // Bind name property to display the percentage
        pieChartData.forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), " (", String.format("%.1f", data.getPieValue() / totalCount * 100), "%)"
                        )
                )
        );

        // Add data to PieChart
        pieChart.getData().addAll(pieChartData);
    }
    public void showChatbot(ActionEvent actionEvent) {
        try {
            // Load the chatbot window or overlay
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/Quiz/Chatbot.fxml")); // Adjust path as needed
            Parent chatbotRoot = loader.load();

            // Display the chatbot window as a modal or embedded
            Stage chatbotStage = new Stage();
            chatbotStage.setTitle("Chatbot");
            chatbotStage.setScene(new Scene(chatbotRoot));
            chatbotStage.initOwner(ConsultationPane.getScene().getWindow());
            chatbotStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Method to handle showing the pie chart
    public void showPieChart() {
        initializePieChart();
    }
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
