package Controllers.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import services.UserService;
import utils.MailUtil;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ForgetPassword {
    @FXML
    private TextField codeField;
    @FXML
    private Label codeLabel;
    @FXML
    private Button loginButton;
    @FXML
    private TextField emailTextfield;
    @FXML
    private Label invalidText;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmpassword;
    @FXML
    private Button verifierButton;
    @FXML
    private Label passwordL;
    @FXML
    private Label confirmL;
    @FXML
    private Button changeButton;
    String secretCode;
    public void getVerificationCode(ActionEvent event){
        emailTextfield.setDisable(true);
        UserService us = new UserService();
        if(!us.UserExistsByEmail(emailTextfield.getText())){

            invalidText.setText("User does not exist");
            invalidText.setVisible(true);
            emailTextfield.setDisable(false);

            return;
        }
        String s = generateRandomCode(12);
        System.out.println(s);
        secretCode = s;
        //    if( MailUtil.sendPasswordResetMail(emailTextfield.getText(), us.getVerificationCodeByEmail(emailTextfield.getText()))) {
        if( MailUtil.sendPasswordResetMail(emailTextfield.getText(),s)) {
            codeField.setVisible(true);
            codeLabel.setVisible(true);
            loginButton.setVisible(false);
            verifierButton.setVisible(true);

        }
    }

    public static String generateRandomCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    public void changePassword(ActionEvent event){
        UserService us = new UserService() ;
        //  String code = us.getVerificationCodeByEmail(emailTextfield.getText());
        if(codeField.getText().equals(secretCode)){
            System.out.println("ok");
            codeField.setDisable(true);
            loginButton.setVisible(false);
            verifierButton.setVisible(false);
            password.setVisible(true);
            confirmpassword.setVisible(true);
            passwordL.setVisible(true);
            confirmL.setVisible(true);
            changeButton.setVisible(true);
        }
        else{
            invalidText.setText("Invalid code");
            System.out.println(secretCode);
            invalidText.setVisible(true);
        }
    }
    public void updatePassword(ActionEvent event){
        UserService us = new UserService();
        String code = us.getVerificationCodeByEmail(emailTextfield.getText());
        if(codeField.getText().equals(secretCode)){

            if(!password.getText().equals(confirmpassword.getText())){
                invalidText.setText("Passwords do not match");
                invalidText.setVisible(true);
                return;
            }

            String hashedPassword = BCrypt.hashpw(password.getText(), BCrypt.gensalt());
            System.out.println(password.getText());
            System.out.println(hashedPassword);
            us.changePasswordByEmail(emailTextfield.getText(), hashedPassword);


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Password Changed");
            alert.setHeaderText(null);
            alert.setContentText("Your password has been changed successfully.");
            alert.showAndWait();

// Change page to login interface
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/login.fxml"));
                Parent loginInterface = loader.load();
                Scene loginScene = new Scene(loginInterface);
                Stage loginStage = new Stage();
                loginStage.setScene(loginScene);

                // Close the current stage
                Stage currentStage = (Stage) verifierButton.getScene().getWindow();
                currentStage.close();

                // Show the login stage
                loginStage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }
    public void goBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent profileInterface = loader.load();
        Scene profileScene = new Scene(profileInterface);
        Stage profileStage = new Stage();
        profileStage.setScene(profileScene);

        // Close the current stage (assuming loginButton is accessible from here)
        Stage currentStage = (Stage) verifierButton.getScene().getWindow();
        currentStage.close();

        // Show the profile stage
        profileStage.show();
    }
}