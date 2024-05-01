package Controllers.User;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import entities.Role;
import entities.User;
import services.UserService;

import java.util.Random;

import static utils.MailUtil.sendPassword;

public class addUserByAdmin {
    @FXML
    private TextField fname;
    @FXML
    private TextField lname;
    @FXML
    private TextField email;
    @FXML
    private Label invalidText;
    @FXML
    private TextField phone;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Label titleid;

    private Role role; // Assuming Role is the enum type

    public void initData(Role rolee) {
        this.role = rolee;
        this.titleid.setText("Add " + this.role.toString());
    }

    public void signupButtonOnAction(ActionEvent event) {
        if (role == null) {
            System.out.println("Role is not initialized. Make sure to call initData method before signupButtonOnAction.");
            return;
        }


        if (fname.getText().isEmpty() || lname.getText().isEmpty() || email.getText().isEmpty() || phone.getText().isEmpty()) {
            invalidText.setText("Please fill in all fields");
            invalidText.setVisible(true);
            return;
        }
        UserService us = new UserService();
        if (us.getUserByEmail(email.getText()) != null) {
            invalidText.setText("Email already exists");
            invalidText.setVisible(true);
            return;
        }

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 6;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        String password = random.ints(leftLimit, rightLimit + 1)
                .limit(12)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        User u = new User(email.getText(), password, "avatar.png", Role.valueOf(roleComboBox.getValue()), true, fname.getText(), lname.getText(), Integer.parseInt(phone.getText()));
        if (us.ajouterUser(u)) {
            sendPassword(email.getText(), password);

            System.out.println("User added successfully");
        } else {
            invalidText.setText("User already exists");
            invalidText.setVisible(true);
            return;
        }
    }
}
