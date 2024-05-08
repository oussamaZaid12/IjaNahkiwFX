package Controllers.Activite;

import entities.Activite;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.ServiceActivite;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class affichageActiviteP {

    @FXML
    private TableView<Activite> tableView;
    @FXML
    private TableColumn<Activite, Integer> idColumn;
    @FXML
    private TableColumn<Activite, String> nameColumn;
    @FXML
    private TableColumn<Activite, LocalDateTime> dateColumn;
    @FXML
    private TableColumn<Activite, String> genreColumn;
    @FXML
    private TextField searchNom;
    @FXML
    private TextField searchGenre;

    public void initialize() {
        setupColumns();
        loadData();
    }

    private void setupColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(item));
            }
        });
    }

    private void loadData() {
        ServiceActivite service = new ServiceActivite();
        try {
            tableView.setItems(FXCollections.observableArrayList(service.afficher()));
        } catch (SQLException e) {
            e.printStackTrace(); // Consider showing an error message to the user
        }
    }

    @FXML
    private void handleSearch() {
        String nom = searchNom.getText().trim();
        String genre = searchGenre.getText().trim();
        try {
            tableView.setItems(FXCollections.observableArrayList(new ServiceActivite().searchActiviteByNomAndGenre(nom, genre)));
        } catch (SQLException e) {
            e.printStackTrace();  // Consider showing an error message to the user
        }
    }

    @FXML
    private void handleSortByDate() {
        SortedList<Activite> sortedData = new SortedList<>(tableView.getItems());
        sortedData.setComparator(Comparator.comparing(Activite::getDate));
        tableView.setItems(sortedData);
    }
    @FXML
    private void handleViewProgramme() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/Activite/affichageProgramme.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Programme Details");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleViewExercice() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/Activite/affichageExercice.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Exercice Details");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void handleViewCalendrier() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/Activite/CalendarActivite.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Calendrier Activités");
        stage.setScene(scene);
        stage.show();
    }
}
