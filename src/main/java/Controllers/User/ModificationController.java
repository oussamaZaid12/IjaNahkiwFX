package Controllers.User;

import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import services.UserService;

public class ModificationController {
    @FXML
    private TextField nom_user;

    @FXML
    private TextField prenom_user;
    @FXML
    private TextField age_user;

    @FXML
    private TextField email_user;

    @FXML
    private CheckBox check;
    @FXML
    private Button modifier;

    private User user;
    private UserService userService;
    private ListUsers listUsersController;
    public void initData(User user) {
        this.user = user;
        nom_user.setText(user.getNom());
        prenom_user.setText(user.getPrenom());
        email_user.setText(user.getEmail());
        age_user.setText(Integer.toString(user.getAge()));
        check.setSelected(user.getBanned());
        userService = new UserService();
    }

    public void refreshTableView() {
        if (listUsersController != null) {
            listUsersController.refreshTableView();
        }
    }



    @FXML
    void updateUser(ActionEvent event) {
        user.setNom(nom_user.getText());
        user.setPrenom(prenom_user.getText());
        user.setEmail(email_user.getText());
        user.setAge(Integer.parseInt(age_user.getText()));
        user.setBanned(check.isSelected());
        // Mettre à jour l'utilisateur dans la base de données en utilisant le service UserService
        UserService userService = new UserService();
        userService.updateUser(user);

        // Rafraîchir la TableView dans le contrôleur ListUsers pour refléter les modifications
        refreshTableView();

    }


}
