    package Controllers;

    import Controllers.User.Profile;
    import Controllers.User.Session;
    import entities.User;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Node;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.layout.BorderPane;
    import javafx.stage.Stage;

    import java.io.IOException;

    public class Dashboard {

        @FXML
        private BorderPane mainContainer;

        @FXML
        public void showAddPublication() {
            try {
                Node addPub = FXMLLoader.load(getClass().getResource("/Back/Publication/ajoutPub.fxml"));
                mainContainer.setCenter(addPub);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception, for example, by showing an error message
            }
        }

        @FXML
        public void showDisplayPublications() {
            try {
                Node displayPubs = FXMLLoader.load(getClass().getResource("/Back/Publication/affichagePub.fxml"));
                mainContainer.setCenter(displayPubs);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception, for example, by showing an error message
            }
        }

        @FXML
        public void showHome() {
            try {
                Node home = FXMLLoader.load(getClass().getResource("/Front/call.fxml"));
                mainContainer.setCenter(home);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception, for example, by showing an error message
            }
        }

        @FXML
        public void displaylistAct() {
            try {
                Node act = FXMLLoader.load(getClass().getResource("/Back/Activite/affichageActivite.fxml"));
                mainContainer.setCenter(act);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception, for example, by showing an error message
            }
        }

        @FXML
        void diplayaddAct2( ) {
            try {
                Node act2 = FXMLLoader.load(getClass().getResource("/Back/Activite/AjouProgramme.fxml"));
                mainContainer.setCenter(act2);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception, for example, by showing an error message
            }
        }


        @FXML
        public void diplayaddAct() {
            try {
                Node acti = FXMLLoader.load(getClass().getResource("/Back/Activite/ajoutActivite.fxml"));
                mainContainer.setCenter(acti);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception, for example, by showing an error message
            }
        }
        @FXML
        public void displayQuiz() {
            try {
                Node quiz = FXMLLoader.load(getClass().getResource("/Back/Quiz/ajoutQuestionnaire.fxml"));
                mainContainer.setCenter(quiz);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception, for example, by showing an error message
            }
        }



        public void addQuiz() {
            try {
                Node quiz = FXMLLoader.load(getClass().getResource("/Back/Quiz/affichageQuestionnaire.fxml"));
                mainContainer.setCenter(quiz);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception, for example, by showing an error message
            }
        }
        @FXML
        public void listeUsers(){
            try {
                Node addPub = FXMLLoader.load(getClass().getResource("/gui/ListUsers.fxml"));
                mainContainer.setCenter(addPub);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception, for example, by showing an error message
            }
        }
        @FXML
        public void ajouteruser(){
            try {
                Node addPub = FXMLLoader.load(getClass().getResource("/gui/addUserByAdmin.fxml"));
                mainContainer.setCenter(addPub);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception, for example, by showing an error message
            }
        }
        @FXML
        public void profile() {
            User currentUser = Session.getUser();
            if (currentUser != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/profile.fxml"));
                    Parent profileParent = loader.load();
                    Profile profileController = loader.getController();
                    profileController.initialize(currentUser);
                    Stage profileStage = new Stage();
                    profileStage.setTitle("Profile");
                    profileStage.setScene(new Scene(profileParent));
                    profileStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @FXML
        public void logout(){
            try {
                Node addPub = FXMLLoader.load(getClass().getResource("/gui/profile.fxml"));
                mainContainer.setCenter(addPub);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception, for example, by showing an error message
            }
        }
        @FXML
        private void initialize() {
            showHome();
        }
    }
