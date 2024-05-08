    package entities;

import java.time.LocalDateTime;

public class Activite {
    private int id;
    private String nom;
    private LocalDateTime date;
    private String genre;

    // Default constructor
    public Activite() {
    }

    // Constructor with parameters
    public Activite(int id, String nom, LocalDateTime date, String genre) {
        this.id = id;
        this.nom = nom;
        this.date = date;
        this.genre = genre;
    }

    // Getters and setters for each attribute
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Activite{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", date=" + date +
                ", genre='" + genre + '\'' +
                '}';
    }
}
