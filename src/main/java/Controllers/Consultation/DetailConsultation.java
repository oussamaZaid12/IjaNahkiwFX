package Controllers.Consultation;

import entities.Consultation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class DetailConsultation {

    @FXML
    private Label date;

    @FXML
    private Label idp;

    @FXML
    private Label idt;

    @FXML
    private Label pathologie;

    @FXML
    private Label remarques;

    public void setConsultation(Consultation consultation) {
        date.setText(consultation.getDateC().toString());
        idp.setText(String.valueOf(consultation.getIdp()));
        idt.setText(String.valueOf(consultation.getIdt()));
        pathologie.setText(consultation.getPathologie());
        remarques.setText(consultation.getRemarques());
    }
}
