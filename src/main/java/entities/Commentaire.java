package entities;

import java.util.Date;
import java.util.Objects;

public class Commentaire {
    private int id;
    private int publication_id;
    private String contenu_c;
    private int id_user;

    public Commentaire(int id, int publication_id, String contenu_c, int id_user) {
        this.id = id;
        this.publication_id = publication_id;
        this.contenu_c = contenu_c;
        this.id_user = id_user;

    }

    public Commentaire(int publication_id, String contenu_c, int id_user) {
        this.publication_id = publication_id;
        this.contenu_c = contenu_c;
        this.id_user = id_user;

    }

    public Commentaire() {
        // Default constructor
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPublication_id() {
        return publication_id;
    }

    public void setPublication_id(int publication_id) {
        this.publication_id = publication_id;
    }

    public String getContenu_c() {
        return contenu_c;
    }

    public void setContenu_c(String contenu_c) {
        this.contenu_c = contenu_c;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }



    // equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Commentaire that = (Commentaire) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString method
    @Override
    public String toString() {
        return "Commentaire{" +
                "id=" + id +
                ", publication_id=" + publication_id +
                ", contenu_c='" + contenu_c + '\'' +
                ", id_user=" + id_user +
                '}';
    }
}
