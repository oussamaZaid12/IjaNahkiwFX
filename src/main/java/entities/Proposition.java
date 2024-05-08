package entities;

public class Proposition {
    int id;
    int questionId;
    String titleProposition;
    int score;

    int idUserId;
    public Proposition() {
    }


    public Proposition(int id, int questionId, String titleProposition, int score, int idUserId) {
        this.id = id;
        this.questionId = questionId;
        this.titleProposition = titleProposition;
        this.score = score;
        this.idUserId = idUserId;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public void setTitleProposition(String titleProposition) {
        this.titleProposition = titleProposition;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setIdUserId(int idUserId) {
        this.idUserId = idUserId;
    }

    public int getId() {
        return id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public String getTitleProposition() {
        return titleProposition;
    }

    public int getScore() {
        return score;
    }

    public int getIdUserId() {
        return idUserId;
    }
}
