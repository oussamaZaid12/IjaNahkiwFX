package Controllers.Activite;

import entities.Activite;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.ServiceActivite;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class affichageActivite {

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
    private TableColumn<Activite, Void> actionColumn;

    public void initialize() {
        // Initialize columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

        // Setup the date column with proper formatting
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setCellFactory(column -> new TableCell<Activite, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(item));
                }
            }
        });

        // Load data
        loadData();

        // Add action buttons
        addActionButtons();
    }

    private void loadData() {
        try {
            List<Activite> list = new ServiceActivite().afficher();
            tableView.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            e.printStackTrace(); // handle exception
        }
    }

    private void addActionButtons() {
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            {
                editBtn.setOnAction(event -> {
                    Activite activite = getTableView().getItems().get(getIndex());
                    editActivite(activite);
                });
                deleteBtn.setOnAction(event -> {
                    Activite activite = getTableView().getItems().get(getIndex());
                    deleteActivite(activite);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox container = new HBox(editBtn, deleteBtn);
                    container.setSpacing(10);
                    setGraphic(container);
                }
            }
        });
    }

    private void editActivite(Activite activite) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/Activite/editActivite.fxml"));
            Parent root = loader.load();
            editActivite controller = loader.getController();
            controller.setActivite(activite);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Activite");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tableView.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            loadData(); // Refresh data
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteActivite(Activite activite) {
        try {
            new ServiceActivite().supprimer(activite);
            loadData(); // Refresh table after deletion
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
