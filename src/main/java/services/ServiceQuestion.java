package services;

import entities.Answer;
import entities.Question;
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

    public void ajouter(Question question) throws SQLException {
        String req = "INSERT INTO question (id_user_id, title_question) VALUES (?, ?)";
        PreparedStatement pre = con.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
        pre.setInt(1, question.getIdUserId());
        pre.setString(2, question.getTitleQuestion());
        pre.executeUpdate();

        // Getting the generated question ID and setting it to the question object
        ResultSet rs = pre.getGeneratedKeys();
        if (rs.next()) {
            question.setId(rs.getInt(1));
        }
    }

    public void modifier(Question question) throws SQLException {
        String req = "UPDATE question SET title_question=?, id_user_id=? WHERE id=?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setString(1, question.getTitleQuestion());
        pre.setInt(2, question.getIdUserId());
        pre.setInt(3, question.getId());
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
            list.add(q);
        }
        return list;
    }
    public List<Question> ss() throws SQLException {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT q.id, q.title_question, q.id_user_id, a.id AS answer_id, a.title_proposition AS answer_text FROM question q LEFT JOIN proposition a ON q.id = a.question_id";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int questionId = rs.getInt("id");
                String title = rs.getString("title_question");
                int userId = rs.getInt("id_user_id");
                Question question = new Question(questionId, userId, title);
                if (rs.getInt("answer_id") != 0) {  // Checks if there is an answer due to LEFT JOIN
                    int answerId = rs.getInt("answer_id");
                    String answerText = rs.getString("answer_text");
                    question.addProposedAnswer(new Answer(answerId, questionId, answerText,userId));
                }
                questions.add(question);
            }
        }
        return questions;
    }
    public List<Question> getAllQuestions() throws SQLException {
        Map<Integer, Question> questionMap = new HashMap<>();
        String sql = "SELECT q.id, q.title_question, q.id_user_id, p.id AS answer_id, p.title_proposition AS answer_text FROM question q LEFT JOIN proposition p ON q.id = p.question_id";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int questionId = rs.getInt("id");
                Question question = questionMap.getOrDefault(questionId, new Question(questionId, rs.getInt("id_user_id"), rs.getString("title_question")));
                questionMap.putIfAbsent(questionId, question);

                if (rs.getInt("answer_id") != 0) {
                    question.addProposedAnswer(new Answer(rs.getInt("answer_id"), questionId, rs.getString("answer_text"),rs.getInt("id_user_id")));
                }
            }
        }
        return new ArrayList<>(questionMap.values());
    }


}

