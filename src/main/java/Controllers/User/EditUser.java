package Controllers.User;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import entities.User;
import services.UserService;

public class EditUser {

    @FXML
    private TextField userEmailLabel;

    @FXML
    private CheckBox userActiveCheckBox;

    private User user;
    private UserService userService;

    public EditUser() {
        userService = new UserService();
    }

    public void initData(User user) {
        this.user = user;
        userEmailLabel.setText(user.getEmail());
        userActiveCheckBox.setSelected(user.getBanned().booleanValue());
    }

    @FXML
    private void saveChanges() {
        if (user != null) {
            user.setBanned(userActiveCheckBox.isSelected());
            userService.modifierUser(user); // Assuming you have a method to update the user in your UserService
        }
        closeDialog();
    }

    @FXML
    private void closeDialog() {
        userActiveCheckBox.getScene().getWindow().hide();
    }

}
