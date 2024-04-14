package entities;

public class Like {
    private int id;
    private int userId;
    private int publicationId;
    private boolean type; // true for like, false for dislike

    // Constructors
    public Like() {
    }

    public Like(int id, int userId, int publicationId, boolean type) {
        this.id = id;
        this.userId = userId;
        this.publicationId = publicationId;
        this.type = type;
    }

    public Like(int userId, int publicationId, boolean type) {
        this.userId = userId;
        this.publicationId = publicationId;
        this.type = type;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(int publicationId) {
        this.publicationId = publicationId;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }
}
