package Controllers.Activite;

import entities.Programme;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import services.ServiceProgramme;

import java.sql.SQLException;
import java.util.Comparator;

public class affichageProgrammeP {

    @FXML
    private TableView<Programme> tableView;
    @FXML
    private TableColumn<Programme, Integer> idColumn;
    @FXML
    private TableColumn<Programme, String> nameColumn; // Assuming Programme has a name field
    @FXML
    private TableColumn<Programme, String> lieuColumn;
    @FXML
    private TableColumn<Programme, String> butColumn;
    @FXML
    private TextField searchLieu;
    @FXML
    private TextField searchBut;

    public void initialize() {
        setupColumns();
        loadData();
    }

    private void setupColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nomL")); // Make sure Programme has a 'nomL' field
        lieuColumn.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        butColumn.setCellValueFactory(new PropertyValueFactory<>("but"));

        // Add any other necessary columns here
    }

    private void loadData() {
        ServiceProgramme service = new ServiceProgramme();
        try {
            tableView.setItems(FXCollections.observableArrayList(service.afficher()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch() {
        String lieu = searchLieu.getText().trim();
        String but = searchBut.getText().trim();
        try {
            tableView.setItems(FXCollections.observableArrayList(new ServiceProgramme().searchByLieuAndBut(lieu, but)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSortByBut() {
        SortedList<Programme> sortedData = new SortedList<>(tableView.getItems());
        sortedData.setComparator(Comparator.comparing(Programme::getBut));
        tableView.setItems(sortedData);
    }
}
