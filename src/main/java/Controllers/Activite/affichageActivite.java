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
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import services.ServiceActivite;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
    @FXML
    private TextField searchNom;
    @FXML
    private TextField searchGenre;

    public void initialize() {
        // Initialize columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

        // Setup the date column with proper formatting
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setComparator(Comparator.comparing(LocalDateTime::toLocalDate));
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
        ServiceActivite serviceActivite = new ServiceActivite();
        try {
            // First, check for dependent 'programme' records.
            if (serviceActivite.hasDependentProgrammes(activite.getId())) {
                showError("Delete Error", "This activity cannot be deleted because it is referenced by one or more programmes.");
            } else {
                // If no dependent records, ask for user confirmation.
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the activity: " + activite.getNom() + "?", ButtonType.YES, ButtonType.NO);
                alert.setHeaderText("Confirm Deletion");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        // User confirmed, proceed with deletion.
                        try {
                            serviceActivite.supprimer(activite);
                            tableView.getItems().remove(activite);  // Update the UI directly
                            System.out.println("Activité supprimée avec succès!");
                        } catch (SQLException e) {
                            showError("Error", "Failed to delete the activity due to a database error.");
                        }
                    }
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database Error", "Failed to check dependent programmes due to a database error.");
        }
    }

    private void showError(String title, String content) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle(title);
        errorAlert.setHeaderText(null);
        errorAlert.setContentText(content);
        errorAlert.showAndWait();
    }

    @FXML
    private void handleViewProgramme() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/Activite/AffichageProgramme.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Programme Details");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleViewExercice() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/Activite/affichageExercice.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Exercice Details");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleSearch() {
        String nom = searchNom.getText().trim();
        String genre = searchGenre.getText().trim();
        ServiceActivite service = new ServiceActivite();
        try {
            tableView.setItems(FXCollections.observableArrayList(service.searchActiviteByNomAndGenre(nom, genre)));
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

    public void exportToExcel() {
        String[] columns = {"ID", "Name", "Date", "Genre"};
        try (Workbook workbook = new XSSFWorkbook(); FileOutputStream fileOut = new FileOutputStream("Activite.xlsx")) {
            Sheet sheet = workbook.createSheet("Activite");

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 14);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Create a Row for Header
            Row headerRow = sheet.createRow(0);

            // Create cells
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }

            // Fill data
            int rowNum = 1;
            List<Activite> list = new ServiceActivite().afficher();
            for (Activite activite : list) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(activite.getId());
                row.createCell(1).setCellValue(activite.getNom());
                row.createCell(2).setCellValue(activite.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                row.createCell(3).setCellValue(activite.getGenre());
            }

            // Resize all columns to fit the content size
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the output to a file
            workbook.write(fileOut);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }


}
