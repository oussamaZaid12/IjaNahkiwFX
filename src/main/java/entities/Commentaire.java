package entities;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Commentaire {
    private int id;
    private int publication_id;
    private String contenu_c;
    private int id_user;
    private transient boolean hasProfanity;

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
        this.hasProfanity = checkForProfanity(contenu_c);

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

    public void setHasProfanity(boolean hasProfanity) {
        this.hasProfanity = hasProfanity;
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

    private boolean checkForProfanity(String content) {
        Set<String> profanities = new HashSet<>(Arrays.asList("fuck", "bitch"));
        String[] words = content.toLowerCase().split("\\s+"); // Split content into words
        for (String word : words) {
            if (profanities.contains(word)) {
                return true;
            }
        }
        return false;
    }
    public boolean isHasProfanity() {
        return hasProfanity;
    }
}
