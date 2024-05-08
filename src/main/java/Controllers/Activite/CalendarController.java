package Controllers.Activite;

import entities.Activite;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import utils.MyDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CalendarController {

    @FXML
    private Text year;
    @FXML
    private Text month;
    @FXML
    private GridPane calendar;

    private LocalDate dateFocus; // The current month being displayed
    private HashMap<Integer, List<Activite>> activitiesByDate = new HashMap<>(); // Stores activities for each day

    public void initialize() {
        dateFocus = LocalDate.now();
        loadActivitiesForMonth(dateFocus.getYear(), dateFocus.getMonthValue());
        drawCalendar();
    }

    @FXML
    private void backOneMonth() {
        dateFocus = dateFocus.minusMonths(1);
        loadActivitiesForMonth(dateFocus.getYear(), dateFocus.getMonthValue());
        drawCalendar();
    }

    @FXML
    private void forwardOneMonth() {
        dateFocus = dateFocus.plusMonths(1);
        loadActivitiesForMonth(dateFocus.getYear(), dateFocus.getMonthValue());
        drawCalendar();
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        // Get the current stage using the source button
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.close(); // Close the window
    }

    private void loadActivitiesForMonth(int year, int month) {
        activitiesByDate.clear();
        List<Activite> activities = getActivitiesForMonth(year, month);  // Fetch the activities for the given month

        for (Activite activity : activities) {
            int day = activity.getDate().getDayOfMonth();
            // Check if there's already a list for this day; if not, create one
            activitiesByDate.computeIfAbsent(day, k -> new ArrayList<>()).add(activity);
        }
    }

    private List<Activite> getActivitiesForMonth(int year, int month) {
        List<Activite> activities = new ArrayList<>();
        String sql = "SELECT id, nom, date, genre FROM activite WHERE YEAR(date) = ? AND MONTH(date) = ?";
        try (Connection conn = MyDB.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, year);
            stmt.setInt(2, month);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                LocalDateTime date = rs.getTimestamp("date").toLocalDateTime(); // Ensure conversion from Timestamp to LocalDateTime
                String genre = rs.getString("genre");
                activities.add(new Activite(id, nom, date, genre));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching activities from the database: " + e.getMessage());
            e.printStackTrace();
        }
        return activities;
    }

    private void drawCalendar() {
        calendar.getChildren().clear();
        int daysInMonth = YearMonth.from(dateFocus).lengthOfMonth();
        int firstDayOfMonth = dateFocus.withDayOfMonth(1).getDayOfWeek().getValue();
        for (int i = 0; i < daysInMonth; i++) {
            VBox dayCell = new VBox();
            dayCell.setAlignment(Pos.CENTER);
            int currentDay = i + 1;
            Text dayNum = new Text(String.valueOf(currentDay));
            dayCell.getChildren().add(dayNum);

            if (activitiesByDate.containsKey(currentDay)) {
                dayCell.setStyle("-fx-background-color: yellow; -fx-padding: 5; -fx-border-color: black; -fx-border-width: 0.5;");
            } else {
                dayCell.setStyle("-fx-background-color: white; -fx-padding: 5; -fx-border-color: black; -fx-border-width: 0.5;");
            }

            GridPane.setRowIndex(dayCell, (i + firstDayOfMonth - 1) / 7);
            GridPane.setColumnIndex(dayCell, (i + firstDayOfMonth - 1) % 7);
            calendar.getChildren().add(dayCell);
        }

        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(dateFocus.getMonth().toString());
    }
}
