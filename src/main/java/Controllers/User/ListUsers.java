package Controllers.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import entities.Role;
import entities.User;
import services.UserService;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.UnitValue;

import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


import java.io.IOException;
import java.util.Optional;
import javafx.scene.control.Pagination;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ListUsers {
    @FXML
    private TableColumn<Integer, Integer> age;

    @FXML
    private TableColumn<String, String> email;

    @FXML
    private TableColumn<Boolean, Integer> isbanned;

    @FXML
    private TableColumn<String, String> nom;

    @FXML
    private TableColumn<String, String> prenom;

    @FXML
    private TableView<User> tableView;

    private ObservableList<User> userList;
    @FXML
    private Button suppbutton;
    @FXML
    private Button modifierbutton;
    private User selectedUser;
    private UserService userService;
    @FXML
    private Pagination pagination;
    private int itemsPerPage = 10;

    @FXML
    private void handlePDFExport(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        exporterPDF(tableView, stage);
    }


    @FXML
    void refreshbuttonaction(ActionEvent event) {
        loadUsers();
    }

    public void initData(Role rolee) {
        loadUsers();
    }

    @FXML
    public void initialize() {
        // Initialiser les colonnes de la TableView
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        isbanned.setCellValueFactory(new PropertyValueFactory<>("banned"));
        age.setCellValueFactory(new PropertyValueFactory<>("age"));

        // Initialiser la TableView
        loadUsers();

        // Gérer le clic sur la TableView
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                selectedUser = tableView.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                    openModificationForm();
                }
            }
        });

        // Gérer la pagination
        pagination.setPageFactory(this::createPage);

        // Gérer le clic sur le bouton "Supprimer"
        suppbutton.setOnAction(event -> deleteUser());

        // Gérer le clic sur le bouton "Modifier"
        modifierbutton.setOnAction(event -> {
            if (selectedUser != null) {
                openModificationForm();
            } else {
                showAlert(Alert.AlertType.WARNING, "Aucun utilisateur sélectionné",
                        "Veuillez sélectionner un utilisateur à modifier.");
            }
        });
    }
    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, userList.size());
        tableView.setItems(FXCollections.observableArrayList(userList.subList(fromIndex, toIndex)));
        return new AnchorPane(tableView);
    }

    private void openModificationForm() {
        try {
            // Charger le formulaire de modification depuis le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/modification.fxml"));
            Parent modificationInterface = loader.load();

            // Passer l'utilisateur sélectionné au contrôleur du formulaire de modification
            ModificationController modificationController = loader.getController();
            modificationController.initData(selectedUser);

            // Créer une nouvelle fenêtre pour la modification et afficher le formulaire pré-rempli
            Stage modificationStage = new Stage();
            modificationStage.setScene(new Scene(modificationInterface));
            modificationStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void loadUsers() {
        UserService userService = new UserService();
        userList = FXCollections.observableArrayList(userService.getAll());

        // Calculer le nombre total de pages
        int pageCount = (int) Math.ceil((double) userList.size() / itemsPerPage);
        pagination.setPageCount(pageCount);

        // Charger la première page
        pagination.setCurrentPageIndex(0);

        // Mettre à jour la TableView
        refreshTableView();
    }


    // Méthode pour rafraîchir la TableView
    public void refreshTableView() {
        // Actualiser la TableView pour refléter les changements
        int pageIndex = pagination.getCurrentPageIndex();
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, userList.size());
        tableView.setItems(FXCollections.observableArrayList(userList.subList(fromIndex, toIndex)));
    }
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void exporterPDF(TableView<User> tableView, Stage stage) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName("ListeUtilisateurs.pdf");
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                PdfWriter writer = new PdfWriter(new FileOutputStream(file));
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                document.add(new Paragraph("Liste des Utilisateurs"));

                // Ajouter l'image
                String cheminImage = "/images/logo ff.png"; // Ajustez le chemin selon l'emplacement de votre image
                ImageData imageData = ImageDataFactory.create(getClass().getResource(cheminImage));
                Image image = new Image(imageData);
                document.add(image);

                ObservableList<TableColumn<User, ?>> columns = tableView.getColumns();
                for (TableColumn<User, ?> column : columns) {
                    document.add(new Paragraph(column.getText()));
                    for (User user : tableView.getItems()) {
                        String cellData = column.getCellData(user).toString();
                        document.add(new Paragraph(cellData));
                    }
                    document.add(new Paragraph("\n"));
                }
                document.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*
    public static void exporterPDF(TableView<User> tableView, Stage stage) {
        // Créer un nouveau document PDF
        PdfDocument pdfDoc = null;
        try {
            // Ouvrir un dialogue pour choisir l'emplacement de sauvegarde
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName("ListeUtilisateurs.pdf");
            File file = fileChooser.showSaveDialog(stage);

            // Vérifier si l'utilisateur a sélectionné un fichier
            if (file != null) {
                pdfDoc = new PdfDocument(new PdfWriter(new FileOutputStream(file)));
                Document document = new Document(pdfDoc);

                // Ajouter un titre au document
                document.add(new Paragraph("Liste des Utilisateurs"));

                // Ajouter l'image
                ImageData imageData = ImageDataFactory.create("/images/logo ff.png");
                Image image = new Image(imageData);
                document.add(image);

                // Ajouter les données de la table au document
                ObservableList<TableColumn<User, ?>> columns = tableView.getColumns();
                for (TableColumn<User, ?> column : columns) {
                    document.add(new Paragraph(column.getText()));
                    for (User user : tableView.getItems()) {
                        String cellData = column.getCellData(user).toString();
                        document.add(new Paragraph(cellData));
                    }
                    document.add(new Paragraph("\n"));
                }
                document.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (pdfDoc != null) {
                pdfDoc.close();
            }
        }
    }

     */
    /*
    private void editUser() throws IOException {
        // Récupérer l'utilisateur sélectionné dans la TableView
        User selectedUser = tableView.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            // Charger le formulaire de modification depuis le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chemin/vers/votre/fichier/fxml/modification.fxml"));
            Parent modificationInterface = loader.load();

            // Passer l'utilisateur sélectionné au contrôleur du formulaire de modification
            ModificationController modificationController = loader.getController();
            modificationController.initData(selectedUser);

            // Créer une nouvelle fenêtre pour la modification et afficher le formulaire pré-rempli
            Stage modificationStage = new Stage();
            modificationStage.setScene(new Scene(modificationInterface));
            modificationStage.show();
        } else {
            // Si aucun utilisateur n'est sélectionné, affichez un message d'avertissement
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucun utilisateur sélectionné");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un utilisateur à modifier.");
            alert.showAndWait();
        }
    }

 */
    // Méthode pour initialiser le TableView avec les données de la base de données
    private void populateTableView() {
        UserService userService = new UserService();
        userList = FXCollections.observableArrayList(userService.getAll());
        tableView.setItems(userList);
        // Associer les propriétés des utilisateurs aux colonnes de TableView
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        isbanned.setCellValueFactory(new PropertyValueFactory<>("banned"));
        age.setCellValueFactory(new PropertyValueFactory<>("age"));
    }

    // Méthode pour supprimer un utilisateur sélectionné
    private void deleteUser() {
        // Récupérer l'élément sélectionné dans le TableView
        User selectedUser = tableView.getSelectionModel().getSelectedItem();

        // Vérifier si un élément est sélectionné
        if (selectedUser != null) {
            // Afficher une boîte de dialogue de confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Confirmer la suppression");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cet utilisateur ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Supprimer l'utilisateur de la liste des données
                userList.remove(selectedUser);

                // Supprimer l'utilisateur de la base de données en appelant votre méthode deleteCodePromo(selectedUser.getId())

                // Rafraîchir le TableView
                tableView.refresh();
            }
        } else {
            // Si aucun élément n'est sélectionné, affichez un message d'avertissement
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucun élément sélectionné");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un utilisateur à supprimer.");
            alert.showAndWait();
        }
    }

    @FXML
    private void deleteCodePromo(int id) {
        UserService userService = new UserService();
        userService.deleteUser(userService.getUserById(id));
        populateTableView();
    }

    @FXML
    private void editCodePromo(int id) throws IOException {
        UserService userService = new UserService();
        User user = userService.getUserById(id);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/editUser.fxml"));
        Parent profileInterface = loader.load();
        EditUser profileController = loader.getController();
        profileController.initData(user);
        Scene profileScene = new Scene(profileInterface);
        Stage profileStage = new Stage();
        profileStage.setScene(profileScene);
        profileStage.show();
        Stage currentStage = (Stage) tableView.getScene().getWindow();
        //currentStage.close();
    }

    public void editUser(int id) throws IOException {
        UserService userService = new UserService();
        User user = userService.getUserById(id);
        goToProfile(user);
    }

    public void goToProfile(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/forgetPassword.fxml"));
        Parent profileInterface = loader.load();
        Profile profileController = loader.getController();
        profileController.initData(user);
        Scene profileScene = new Scene(profileInterface);
        Stage profileStage = new Stage();
        profileStage.setScene(profileScene);
        profileStage.show();
        Stage currentStage = (Stage) tableView.getScene().getWindow();
        currentStage.close();
    }
}
