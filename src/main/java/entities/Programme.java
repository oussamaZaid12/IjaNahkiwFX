package entities;

public class Programme {
    private int id;
    private int activiteId;
    private int exerciceId;
    private String lieu;
    private String but;
    private String nomL;
    private String image;

    // Constructors
    public Programme() {
    }

    public Programme(int id, int activiteId, int exerciceId, String lieu, String but, String nomL, String image) {
        this.id = id;
        this.activiteId = activiteId;
        this.exerciceId = exerciceId;
        this.lieu = lieu;
        this.but = but;
        this.nomL = nomL;
        this.image = image;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActiviteId() {
        return activiteId;
    }

    public void setActiviteId(int activiteId) {
        this.activiteId = activiteId;
    }

    public int getExerciceId() {
        return exerciceId;
    }

    public void setExerciceId(int exerciceId) {
        this.exerciceId = exerciceId;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getBut() {
        return but;
    }

    public void setBut(String but) {
        this.but = but;
    }

    public String getNomL() {
        return nomL;
    }

    public void setNomL(String nomL) {
        this.nomL = nomL;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
