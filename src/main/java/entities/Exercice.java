package entities;

public class Exercice {
    private int id;
    private String nomCoach;
    private String duree;

    // Constructor
    public Exercice(int id, String nomCoach, String duree) {
        this.id = id;
        this.nomCoach = nomCoach;
        this.duree = duree;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomCoach() {
        return nomCoach;
    }

    public void setNomCoach(String nomCoach) {
        this.nomCoach = nomCoach;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }
    @Override
    public String toString() {
        return nomCoach + " (" + id + ")";
    }
}
