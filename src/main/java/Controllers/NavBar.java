package Controllers;

import entities.Consultation;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import services.ServiceConsultation;
import services.ServiceNotification;
import Controllers.Consultation.UpcomingConsultationChecker;
import java.io.IOException;

public class NavBar {

    @FXML
    private BorderPane mainContainer;
    private ServiceConsultation serviceConsultation;
    private ServiceNotification serviceNotification;
    private UpcomingConsultationChecker upcomingConsultationChecker;


    public void setServiceConsultation(ServiceConsultation serviceConsultation, ServiceNotification serviceNotification) {
        this.serviceConsultation = serviceConsultation;
        this.serviceNotification = serviceNotification;
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
        serviceNotification = new ServiceNotification();
        initializeUpcomingConsultationChecker();
    }

    private void initializeUpcomingConsultationChecker() {
        if (serviceConsultation != null && serviceNotification != null) {
            upcomingConsultationChecker = new UpcomingConsultationChecker(serviceConsultation, serviceNotification);
            upcomingConsultationChecker.checkUpcomingConsultations();
        } else {
            System.out.println("ServiceConsultation or ServiceNotification is not set. Please set the instances.");
        }
    }
}