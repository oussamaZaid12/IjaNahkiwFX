package Controllers.Activite;

import entities.Exercice;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.ServiceExercice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class affichageExerciceP {

    @FXML
    private TableView<Exercice> tableView;
    @FXML
    private TableColumn<Exercice, Integer> idColumn;
    @FXML
    private TableColumn<Exercice, String> nameColumn;
    @FXML
    private TableColumn<Exercice, String> durationColumn;
    @FXML
    private TextField searchName;

    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nomCoach"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duree"));

        loadData();
    }

    private void loadData() {
        try {
            List<Exercice> list = new ServiceExercice().afficher();
            tableView.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            e.printStackTrace(); // handle exception
        }
    }
    @FXML
    private void handleSearch() {
        String name = searchName.getText().trim();
        ServiceExercice service = new ServiceExercice();
        try {
            tableView.setItems(FXCollections.observableArrayList(service.searchByNom(name)));
        } catch (SQLException e) {
            e.printStackTrace();  // Consider showing an error message to the user
        }
    }
}

