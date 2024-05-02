package Controllers.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import entities.Role;
import entities.User;
import services.UserService;

import java.io.IOException;
import java.util.Optional;

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
    void refreshbuttonaction(ActionEvent event) {
        refreshTableView();
    }

    public void initData(Role rolee) {
        populateTableView();
    }

    @FXML
    public void initialize() {
        // Initialisation du TableView avec les données de la base de données
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Vérifiez si c'est un double clic
                selectedUser = tableView.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                    openModificationForm();
                }
            }
        });

        // Ajout d'un gestionnaire d'événements au bouton "Supprimer"
        suppbutton.setOnAction(event -> {
            deleteUser();
        });
        modifierbutton.setOnAction(event -> {
            if (selectedUser != null) {
                openModificationForm();
            } else {
                // Si aucun utilisateur n'est sélectionné, affichez un message d'avertissement
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aucun utilisateur sélectionné");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez sélectionner un utilisateur à modifier.");
                alert.showAndWait();
            }
        });
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
        // Utiliser le service UserService pour récupérer la liste des utilisateurs depuis la base de données
        UserService userService = new UserService();
        ObservableList<User> users = FXCollections.observableArrayList(userService.getAll());

        // Mettre à jour la liste des utilisateurs de la TableView
        userList.clear(); // Nettoyer la liste existante
        userList.addAll(users); // Ajouter les nouveaux utilisateurs
    }

    // Méthode pour rafraîchir la TableView
    public void refreshTableView() {
        loadUsers();
        // Actualiser la TableView pour refléter les changements
        tableView.refresh();
    }


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
