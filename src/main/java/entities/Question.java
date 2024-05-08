package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Question {
    private int id;
    private int questionnaireId; // Identifier for the Questionnaire to which this Question belongs
    private String titleQuestion;
    private List<Proposition> propositions;
    private int idUserId; // Changed from id_user_id to idUserId for better naming convention
    private Answer answer;
    private String allPropositions;

    // Default constructor initializes the list of proposed answers
    public Question() {
        this.propositions = new ArrayList<>();
    }

    // Constructor for initializing a new Question with essential fields
    public Question(int id, int questionnaireId, String titleQuestion, int idUserId) {
        this();
        this.id = id;
        this.questionnaireId = questionnaireId;
        this.titleQuestion = titleQuestion;
        this.idUserId = idUserId;
    }

    // Getters and Setters for all properties
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(int questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public String getTitleQuestion() {
        return titleQuestion;
    }

    public void setTitleQuestion(String titleQuestion) {
        this.titleQuestion = titleQuestion;
    }
    public String getAllPropositions() {
        return allPropositions;
    }

    public void addProposition(Proposition proposition) {
        this.propositions.add(proposition);
        updateAllPropositions(); // Update concatenated string when adding propositions
    }

    public void updateAllPropositions() {
        allPropositions = propositions.stream()
                .map(Proposition::getTitleProposition)
                .collect(Collectors.joining(", "));
    }
    public List<Proposition> getPropositions() {
        return propositions;
    }

    // Accepts a list of propositions and sets it to the question
    public void setPropositions(List<Proposition> propositions) {
        this.propositions = propositions;
        updateAllPropositions(); // Update concatenated string whenever propositions are set
    }
    // Adds a single proposition to the list of proposed answers
    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public int getIdUserId() {
        return idUserId;
    }

    public void setIdUserId(int idUserId) {
        this.idUserId = idUserId;
    }

    // Utility method for printing the state of a question
    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", questionnaireId=" + questionnaireId +
                ", titleQuestion='" + titleQuestion + '\'' +
                ", proposedAnswers=" + propositions +
                ", idUserId=" + idUserId +
                '}';
    }
    public String getProposition1() {
        return propositions.size() > 0 ? propositions.get(0).getTitleProposition() : "";
    }

    public String getProposition2() {
        return propositions.size() > 1 ? propositions.get(1).getTitleProposition() : "";
    }

    public String getProposition3() {
        return propositions.size() > 2 ? propositions.get(2).getTitleProposition() : "";
    }

}
