package Controllers.Consultation;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import services.ServiceConsultation;
import javafx.scene.control.Button;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Stat implements Initializable {

    @FXML
    private Button stat;
    @FXML
    private PieChart pieChart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializePieChart(); // Call the method to initialize the pie chart
    }


    // Method to initialize the pie chart
    private void initializePieChart() {
        // Fetch data for confirmed and non-confirmed consultations from your data source
        ServiceConsultation consultationService = new ServiceConsultation();
        int confirmedCount = 0;
        try {
            confirmedCount = consultationService.getConfirmedConsultationCount();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int nonConfirmedCount = 0;
        try {
            nonConfirmedCount = consultationService.getNonConfirmedConsultationCount();
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

    // Method to handle showing the pie chart
    public void showPieChart() {
        initializePieChart();
    }
}
