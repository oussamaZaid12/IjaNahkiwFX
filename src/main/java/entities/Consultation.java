package entities;

import java.time.LocalDateTime;

public class Consultation {
    private int id;
    private int idp;
    private int idt;
    private LocalDateTime date_c;
    private String pathologie;
    private String remarques;
    private boolean confirmation;
    private int fiche;

    // Other attributes as needed

    public Consultation() {
        // Default constructor
    }

    // Constructor with parameters
    public Consultation(int idp, int idt, LocalDateTime dateC, String pathologie, String remarques, boolean confirmation, int fiche) {

        this.idp = idp;
        this.idt = idt; // Updated idc to idt
        this.date_c = dateC;
        this.pathologie = pathologie;
        this.remarques = remarques;
        this.confirmation = confirmation;
        this.fiche = fiche; // Set fiche attribute
    }

    // Getters and setters for attributes
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDateTime getDateC() {
        return date_c;
    }

    public void setDateC(LocalDateTime dateC) {
        this.date_c = dateC;
    }

    public String getPathologie() {
        return pathologie;
    }

    public void setPathologie(String pathologie) {
        this.pathologie = pathologie;
    }

    public String getRemarques() {
        return remarques;
    }

    public void setRemarques(String remarques) {
        this.remarques = remarques;
    }

    public boolean isConfirmation() {
        return confirmation;
    }

    public void setConfirmation(boolean confirmation) {
        this.confirmation = confirmation;
    }

    public int getFiche() {
        return fiche;
    }

    public void setFiche(int fiche) {
        this.fiche = fiche;
    }

    @Override
    public String toString() {
        return "Consultation{" +
                "id=" + id +
                ", idp=" + idp +
                ", idt=" + idt +
                ", date_c=" + date_c +
                ", pathologie='" + pathologie + '\'' +
                ", remarques='" + remarques + '\'' +
                ", confirmation=" + confirmation +
                ", fiche=" + fiche +
                '}';
    }

}
