package entities;

import java.util.Date;
import java.util.Objects;

public class publication {

    private int id;
    private int idUserId; // Matched the camelCase naming convention
    private String titreP; // Matched the camelCase naming convention
    private String descriptionP; // Matched the camelCase naming convention
    private String imageP; // Matched the camelCase naming convention
    private Date dateP; // Matched the camelCase naming convention

    public publication(int id, int idUserId, String titreP, String descriptionP, String imageP, Date dateP) {
        this.id = id;
        this.idUserId = idUserId;
        this.titreP = titreP;
        this.descriptionP = descriptionP;
        this.imageP = imageP;
        this.dateP = dateP;
    }

    public publication(int idUserId, String titreP, String descriptionP, String imageP, Date dateP) {
        this.idUserId = idUserId;
        this.titreP = titreP;
        this.descriptionP = descriptionP;
        this.imageP = imageP;
        this.dateP = dateP;
    }

    public publication() {
        // Default constructor
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUserId() {
        return idUserId;
    }

    public void setIdUserId(int idUserId) {
        this.idUserId = idUserId;
    }

    public String getTitreP() {
        return titreP;
    }

    public void setTitreP(String titreP) {
        this.titreP = titreP;
    }

    public String getDescriptionP() {
        return descriptionP;
    }

    public void setDescriptionP(String descriptionP) {
        this.descriptionP = descriptionP;
    }

    public String getImageP() {
        return imageP;
    }

    public void setImageP(String imageP) {
        this.imageP = imageP;
    }

    public Date getDateP() {
        return dateP;
    }

    public void setDateP(Date dateP) {
        this.dateP = dateP;
    }

    // equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        publication that = (publication) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString method
    @Override
    public String toString() {
        return "publication{" +
                "id=" + id +
                ", idUserId=" + idUserId +
                ", titreP='" + titreP + '\'' +
                ", descriptionP='" + descriptionP + '\'' +
                ", imageP='" + imageP + '\'' +
                ", dateP=" + dateP +
                '}';
    }
}
