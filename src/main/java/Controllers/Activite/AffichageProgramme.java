package Controllers.Activite;

import entities.Programme;
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
import services.ServiceProgramme;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;

public class AffichageProgramme {

    @FXML
    private TableView<Programme> tableView;
    @FXML
    private TableColumn<Programme, Integer> idColumn;
    @FXML
    private TableColumn<Programme, Integer> activiteIdColumn;
    @FXML
    private TableColumn<Programme, Integer> exerciceIdColumn;
    @FXML
    private TableColumn<Programme, String> lieuColumn;
    @FXML
    private TableColumn<Programme, String> butColumn;
    @FXML
    private TableColumn<Programme, String> nomLColumn;
    @FXML
    private TableColumn<Programme, String> imageColumn;
    @FXML
    private TextField searchField;

    private ServiceProgramme serviceProgramme = new ServiceProgramme();

    public void initialize() {
        setupColumns();
        loadData();
        addActionButtons();
    }

    private void setupColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        activiteIdColumn.setCellValueFactory(new PropertyValueFactory<>("activiteId"));
        exerciceIdColumn.setCellValueFactory(new PropertyValueFactory<>("exerciceId"));
        lieuColumn.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        butColumn.setCellValueFactory(new PropertyValueFactory<>("but"));
        nomLColumn.setCellValueFactory(new PropertyValueFactory<>("nomL"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
    }

    private void loadData() {
        try {
            tableView.setItems(FXCollections.observableArrayList(serviceProgramme.afficher()));
        } catch (SQLException e) {
            showError("Database Error", "Failed to load programmes.");
        }
    }

    private void addActionButtons() {
        TableColumn<Programme, Void> actionColumn = new TableColumn<>("Actions");
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            {
                editBtn.setOnAction(event -> {
                    Programme programme = getTableView().getItems().get(getIndex());
                    editProgramme(programme);
                });
                deleteBtn.setOnAction(event -> {
                    Programme programme = getTableView().getItems().get(getIndex());
                    deleteProgramme(programme);
                });
                HBox container = new HBox(editBtn, deleteBtn);
                container.setSpacing(10);
                setGraphic(container);
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : getGraphic());
            }
        });
        tableView.getColumns().add(actionColumn);
    }

    private void editProgramme(Programme programme) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/Activite/editProgramme.fxml"));
            Parent root = loader.load();
            EditProgramme controller = loader.getController();
            controller.setProgramme(programme);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Programme");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tableView.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            loadData(); // Refresh data after edit
        } catch (IOException e) {
            showError("IO Error", "Failed to load edit form.");
        }
    }

    private void deleteProgramme(Programme programme) {
        try {
            if (!serviceProgramme.hasDependentExercices(programme.getId())) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the programme: " + programme.getNomL() + "?", ButtonType.YES, ButtonType.NO);
                alert.setHeaderText("Confirm Deletion");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        try {
                            serviceProgramme.supprimer(programme);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        tableView.getItems().remove(programme);  // Update the UI directly
                        System.out.println("Programme deleted successfully!");
                    }
                });
            } else {
                showError("Deletion Error", "This programme cannot be deleted because it is referenced by one or more exercises.");
            }
        } catch (SQLException e) {
            showError("Database Error", "Failed to delete the programme due to a database error.");
        }
    }


    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().trim();
        try {
            tableView.setItems(FXCollections.observableArrayList(serviceProgramme.searchByLieuAndBut(searchTerm, searchTerm)));
        } catch (SQLException e) {
            showError("Database Error", "Failed to search for programmes.");
        }
    }

    @FXML
    private void handleSortByBut() {
        SortedList<Programme> sortedData = new SortedList<>(tableView.getItems(), Comparator.comparing(Programme::getBut));
        tableView.setItems(sortedData);
    }

    private void showError(String title, String content) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle(title);
        errorAlert.setHeaderText(null);
        errorAlert.setContentText(content);
        errorAlert.showAndWait();
    }
}
