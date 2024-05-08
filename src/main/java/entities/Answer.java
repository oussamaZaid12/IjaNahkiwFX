package entities;

public class Answer {
    int id;
    int questionId;
    int propositionChoisieId;
    int idUserId;

    public Answer() {
    }

    public Answer(int id, int questionId, int propositionChoisieId, int idUserId) {
        this.id = id;
        this.questionId = questionId;
        this.propositionChoisieId = propositionChoisieId;
        this.idUserId = idUserId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getPropositionChoisieId() {
        return propositionChoisieId;
    }

    public void setPropositionChoisieId(int propositionChoisieId) {
        this.propositionChoisieId = propositionChoisieId;
    }

    public int getIdUserId() {
        return idUserId;
    }

    public void setIdUserId(int idUserId) {
        this.idUserId = idUserId;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", QuestionId=" + questionId +
                ", propositionChoisieId=" + propositionChoisieId +
                ", idUserId=" + idUserId +
                '}';
    }
}
