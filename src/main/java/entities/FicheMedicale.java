package entities;

import java.util.Date;
import java.util.List;

public class FicheMedicale {
    private int id;
    private Date dateCreation;
    private Date derniereMaj;
    private int idp;
    private int idt;
    private List<Consultation> consultations;
    // Other attributes as needed

    public FicheMedicale() {
        // Default constructor
    }

    // Constructor with parameters
    public FicheMedicale(Date dateCreation, Date derniereMaj, int idp, int idt, List<Consultation> consultations) {
        this.dateCreation = dateCreation;
        this.derniereMaj = derniereMaj;
        this.idp = idp;
        this.idt = idt;
        this.consultations = consultations;
    }

    // Constructor without consultations
    public FicheMedicale(Date dateCreation, Date derniereMaj, int idp, int idt) {
        this.dateCreation = dateCreation;
        this.derniereMaj = derniereMaj;
        this.idp = idp;
        this.idt = idt;
    }

    // Getters and setters for attributes
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDerniereMaj() {
        return derniereMaj;
    }

    public void setDerniereMaj(Date derniereMaj) {
        this.derniereMaj = derniereMaj;
    }

    public int getIdp() {
        return idp;
    }

    public void setIdp(int idp) {
        this.idp = idp;
    }

    public int getIdt() {
        return idt;
    }

    public void setIdt(int idt) {
        this.idt = idt;
    }

    public List<Consultation> getConsultations() {
        return consultations;
    }

    public void setConsultations(List<Consultation> consultations) {
        this.consultations = consultations;
    }

    @Override
    public String toString() {
        return "FicheMedicale{" +
                "id=" + id +
                ", dateCreation=" + dateCreation +
                ", dernierMaj=" + derniereMaj +
                ", idp=" + idp +
                ", idt=" + idt +
                ", listConsultations=" + consultations +
                '}';
    }

}
