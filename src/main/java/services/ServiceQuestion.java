package services;

import entities.Question;
import entities.Proposition;
import utils.MyDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceQuestion {
    private Connection con;

    public ServiceQuestion() {
        con = MyDB.getInstance().getConnection();
    }

    public void SS(Question question) throws SQLException {
        String req = "INSERT INTO question (id_user_id, title_question, questionnaire_id) VALUES (?, ?, ?)";
        PreparedStatement pre = con.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
        pre.setInt(1, question.getIdUserId());
        pre.setString(2, question.getTitleQuestion());
        pre.setInt(3, question.getQuestionnaireId());
        pre.executeUpdate();

        // Getting the generated question ID and setting it to the question object
        ResultSet rs = pre.getGeneratedKeys();
        if (rs.next()) {
            question.setId(rs.getInt(1));
        }
    }
    public void addQuestionWithPropositions(Question question) throws SQLException {
        String questionSQL = "INSERT INTO question (title_question, user_id) VALUES (?, ?)";
        PreparedStatement questionStmt = con.prepareStatement(questionSQL, Statement.RETURN_GENERATED_KEYS);
        questionStmt.setString(1, question.getTitleQuestion());
        questionStmt.setInt(2, question.getIdUserId());
        questionStmt.executeUpdate();

        ResultSet rs = questionStmt.getGeneratedKeys();
        if (rs.next()) {
            question.setId(rs.getInt(1));
        }

        for (Proposition proposition : question.getPropositions()) {
            String propSQL = "INSERT INTO proposition (question_id, title, score) VALUES (?, ?, ?)";
            PreparedStatement propStmt = con.prepareStatement(propSQL);
            propStmt.setInt(1, question.getId());
            propStmt.setString(2, proposition.getTitleProposition());
            propStmt.setInt(3, proposition.getScore());
            propStmt.executeUpdate();
        }
    }


    public void modifier(Question question) throws SQLException {
        String req = "UPDATE question SET title_question=?, id_user_id=?, questionnaire_id=? WHERE id=?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setString(1, question.getTitleQuestion());
        pre.setInt(2, question.getIdUserId());
        pre.setInt(3, question.getQuestionnaireId());
        pre.setInt(4, question.getId());
        pre.executeUpdate();
    }

    public void supprimer(Question question) throws SQLException {
        PreparedStatement pre = con.prepareStatement("DELETE FROM question WHERE id=?");
        pre.setInt(1, question.getId());
        pre.executeUpdate();
    }

    public List<Question> afficher() throws SQLException {
        List<Question> list = new ArrayList<>();
        String req = "SELECT * FROM question";
        Statement ste = con.createStatement();
        ResultSet res = ste.executeQuery(req);
        while (res.next()) {
            Question q = new Question();
            q.setId(res.getInt("id"));
            q.setTitleQuestion(res.getString("title_question"));
            q.setIdUserId(res.getInt("id_user_id"));
            q.setQuestionnaireId(res.getInt("questionnaire_id"));
            list.add(q);
        }
        return list;
    }

    public List<Question> getAllQuestions() throws SQLException {
        String sql = "SELECT q.id, q.title_question, q.id_user_id, q.questionnaire_id, p.id AS prop_id, p.title_proposition, p.score FROM question q LEFT JOIN proposition p ON q.id = p.question_id ORDER BY q.id, p.id";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        List<Question> questions = new ArrayList<>();
        Question lastQuestion = null;

        while (rs.next()) {
            int questionId = rs.getInt("id");
            if (lastQuestion == null || lastQuestion.getId() != questionId) {
                lastQuestion = new Question(questionId, rs.getInt("id_user_id"), rs.getString("title_question"), rs.getInt("questionnaire_id"));
                questions.add(lastQuestion);
            }
            int propId = rs.getInt("prop_id");
            if (propId != 0) {  // Check if there is a proposition because of LEFT JOIN
                Proposition prop = new Proposition(propId, questionId, rs.getString("title_proposition"), rs.getInt("score"), rs.getInt("id_user_id"));
                lastQuestion.addProposition(prop);
            }
        }
        return questions;
    }

}

