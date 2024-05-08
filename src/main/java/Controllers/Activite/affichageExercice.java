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

public class affichageExercice {

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
        addActionButtons();
    }

    private void loadData() {
        try {
            List<Exercice> list = new ServiceExercice().afficher();
            tableView.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            e.printStackTrace(); // handle exception
        }
    }

    private void addActionButtons() {
        TableColumn<Exercice, Void> actionColumn = new TableColumn<>("Actions");
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            {
                editBtn.setOnAction(event -> {
                    Exercice exercice = getTableView().getItems().get(getIndex());
                    editExercice(exercice);
                });
                deleteBtn.setOnAction(event -> {
                    Exercice exercice = getTableView().getItems().get(getIndex());
                    deleteExercice(exercice);
                });
                HBox container = new HBox(editBtn, deleteBtn);
                container.setSpacing(10);
                setGraphic(container);
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(getGraphic());
                }
            }
        });
        tableView.getColumns().add(actionColumn);
    }

    private void editExercice(Exercice exercice) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/Activite/editExercice.fxml"));
            Parent root = loader.load();
            EditExercice controller = loader.getController();
            controller.setExercice(exercice);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Exercice");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tableView.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            loadData(); // Refresh data
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteExercice(Exercice exercice) {
        ServiceExercice serviceExercice = new ServiceExercice();
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to delete this exercice: " + exercice.getNomCoach() + "?");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    if (!serviceExercice.hasDependentProgrammes(exercice.getId())) {
                        serviceExercice.supprimer(exercice);
                        tableView.getItems().remove(exercice);
                        System.out.println("Exercice deleted successfully!");
                    } else {
                        showError("Deletion Error", "This exercice cannot be deleted because it is referenced by one or more programmes.");
                    }
                } catch (SQLException e) {
                    showError("Database Error", "Failed to delete the exercice due to a database error: " + e.getMessage());
                }
            }
        });
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
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}

