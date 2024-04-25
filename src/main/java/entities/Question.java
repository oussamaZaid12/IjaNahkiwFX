package entities;


import java.util.ArrayList;
import java.util.List;

public class Question {
    int id;
    int idUserId; // Changed from id_user_id to idUserId for better naming convention
    String titleQuestion;
    private List<Answer> proposedAnswers;

    public Question(){

    }
    public Question(int id, String titleQuestion) {
        this.id = id;
        this.titleQuestion = titleQuestion;
        this.proposedAnswers = new ArrayList<>();
    }
    public Question(int id, int idUserId, String titleQuestion) {
        this(id, titleQuestion); // Call the simpler constructor
        this.idUserId = idUserId;
    }
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

    public String getTitleQuestion() {
        return titleQuestion;
    }

    public void setTitleQuestion(String titleQuestion) {
        this.titleQuestion = titleQuestion;
    }
    public List<Answer> getProposedAnswers() {
        return proposedAnswers;
    }
    public void setProposedAnswers(List<Answer> proposedAnswers) {
        this.proposedAnswers = new ArrayList<>(proposedAnswers);
    }
    public void addProposedAnswer(Answer proposedAnswer) {
        proposedAnswers.add(proposedAnswer);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", idUserId=" + idUserId +
                ", titleQuestion='" + titleQuestion + '\'' +
                '}';
    }
}
