package entities;

import java.util.Date;

public class Questionnaire {
    int id;
    int idUserId; // Changed from id_user_id to idUserId for better naming convention
    String titleQuestionnaire; // Changed from TitreQuiz to titleQuestionnaire
    Date date;
    String description; // Lowercased first letter to follow Java naming conventions

    public Questionnaire() {
    }

    public Questionnaire(int id, String titleQuestionnaire, String description, Date date, int idUserId) {
        this.id = id;
        this.titleQuestionnaire = titleQuestionnaire;
        this.description = description;
        this.date = date;
        this.idUserId = idUserId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitleQuestionnaire() {
        return titleQuestionnaire;
    }

    public void setTitleQuestionnaire(String titleQuestionnaire) {
        this.titleQuestionnaire = titleQuestionnaire;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getIdUserId() {
        return idUserId;
    }

    public void setIdUserId(int idUserId) {
        this.idUserId = idUserId;
    }

    @Override
    public String toString() {
        return "Questionnaire{" +
                "id=" + id +
                ", idUserId=" + idUserId +
                ", titleQuestionnaire='" + titleQuestionnaire + '\'' +
                ", date=" + date +
                ", description='" + description + '\'' +
                '}';
    }
}
