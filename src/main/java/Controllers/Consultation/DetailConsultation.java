package Controllers.Consultation;

import entities.Consultation;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

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
