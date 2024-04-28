package Controllers;

import entities.Consultation;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import services.ServiceConsultation;
import Controllers.Consultation.UpcomingConsultationChecker;
import java.io.IOException;

public class NavBar {

    @FXML
    private BorderPane mainContainer;
    private ServiceConsultation serviceConsultation;
    private UpcomingConsultationChecker upcomingConsultationChecker;


    public void setServiceConsultation(ServiceConsultation serviceConsultation) {
        this.serviceConsultation = serviceConsultation;
        initializeUpcomingConsultationChecker();
    }



    @FXML
    public void showDisplayPublications() {
        try {
            Node displayPubs = FXMLLoader.load(getClass().getResource("/Front/Publication/affichagePub.fxml"));
            mainContainer.setCenter(displayPubs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showDisplayConsultations() {
        try {
            Node displayCons = FXMLLoader.load(getClass().getResource("/Front/Consultation/affichageConsultation.fxml"));
            mainContainer.setCenter(displayCons);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, for example, by showing an error message
        }
    }

    @FXML
    public void showDisplayConsultationspatient() {
        try {
            Node displayCons = FXMLLoader.load(getClass().getResource("/Front/Consultation/affichageConsultationpatient.fxml"));
            mainContainer.setCenter(displayCons);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, for example, by showing an error message
        }
    }

    @FXML
    public void showDisplayFiches() {
        try {
            Node displayFiches = FXMLLoader.load(getClass().getResource("/Front/FicheMedicale/AffichageFiche.fxml"));
            mainContainer.setCenter(displayFiches);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, for example, by showing an error message
        }
    }

    @FXML
    public void showHome() {
        try {
            Node home = FXMLLoader.load(getClass().getResource("/Front/HomeView.fxml"));
            mainContainer.setCenter(home);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        showHome();
        serviceConsultation = new ServiceConsultation();
        initializeUpcomingConsultationChecker();
    }
    private void initializeUpcomingConsultationChecker() {
        if (serviceConsultation != null) {
            upcomingConsultationChecker = new UpcomingConsultationChecker(serviceConsultation);
            upcomingConsultationChecker.checkUpcomingConsultations();
        } else {
            System.out.println("ServiceConsultation is not set. Please set the ServiceConsultation instance.");
        }
    }
}