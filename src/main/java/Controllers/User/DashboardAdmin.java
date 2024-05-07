package Controllers.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import entities.Role;
import services.UserService;

import java.io.IOException;
import java.util.Map;


public class DashboardAdmin {
    @FXML
    private Button add;

    @FXML
    private PieChart pieChart;
    @FXML
    private Label averageAgeLabel;


    @FXML
    private Label therapistLabel;

    @FXML
    private Label patientLabel;





    public void initialize() {
        UserService userService = new UserService();
        // Call getUserDataByDate() from UserService
        pieChart.getData().clear();

        // Add data points to the pie chart
        Map<String, Integer> userDataByStatus = userService.getUserDataByStatus();
        for (Map.Entry<String, Integer> entry : userDataByStatus.entrySet()) {
            PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue());
            pieChart.getData().add(slice);
        }
        // Set the series data to the line chart
        Map<String, Integer> countMap = userService.getTherapistAndPatientCount();

        // Update therapist and patient labels
        therapistLabel.setText("Nombre des thérapeutes : " + countMap.getOrDefault("[\"ROLE_THERAPEUTE\"]", 0));
        patientLabel.setText("Nombre des patients : " + countMap.getOrDefault("[\"ROLE_PATIENT\"]", 0));

        double averageAge = userService.getAveragePatientAge();

        // Update label with average age
        averageAgeLabel.setText("Moyenne d'âge des patients : " + averageAge);
    }


    public void logout(){
        Stage stage = (Stage) pieChart.getScene().getWindow();
        stage.close();
        Stage newStage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/gui/login.fxml"));
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void addTep()throws IOException  {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/addUserByAdmin.fxml"));
        Parent root = loader.load();
        addUserByAdmin addController = loader.getController(); // Get the controller instance associated with the FXML
        Role role = Role.ROLE_THERAPEUTE;
        addController.initData(role); // Call initData on the correct controller instance
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.show();


    }


    public void openUsersList(ActionEvent actionEvent)throws IOException  {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ListUsers.fxml"));
        Parent root = loader.load();
        ListUsers addController = loader.getController(); // Get the controller instance associated with the FXML
        Role role = Role.ROLE_THERAPEUTE;
        addController.initData(role); // Call initData on the correct controller instance
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.show();
    }
}
