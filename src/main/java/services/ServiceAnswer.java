package services;

import entities.Answer;
import utils.MyDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceAnswer {
    private Connection con;

    public ServiceAnswer() {
        con = MyDB.getInstance().getConnection();
    }


    public List<Answer> afficher() throws SQLException {
        List<Answer> list = new ArrayList<>();
        String req = "SELECT * FROM answer";
        Statement ste = con.createStatement();
        ResultSet res = ste.executeQuery(req);
        while (res.next()) {
            Answer a = new Answer();
            a.setId(res.getInt("id"));
            a.setQuestionId(res.getInt("question_id"));
            a.setPropositionChoisieId(res.getInt("proposition_choisie_id"));
            a.setIdUserId(res.getInt("id_user_id"));
            list.add(a);
        }
        return list;
    }
    public void saveAnswer(Answer answer) throws SQLException {
        String sql = "INSERT INTO answer (question_id, proposition_choisie_id, id_user_id) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, answer.getQuestionId());
            pstmt.setInt(2, answer.getPropositionChoisieId());
            pstmt.setInt(3, answer.getIdUserId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            throw e;  // Rethrow the exception to handle it further up if necessary
        }
    }
    public List<Answer> fetchAnswersByUserId(int userId) throws SQLException {
        List<Answer> list = new ArrayList<>();
        String req = "SELECT * FROM answer WHERE id_user_id = ?";
        PreparedStatement pstmt = con.prepareStatement(req);
        pstmt.setInt(1, userId);
        ResultSet res = pstmt.executeQuery();
        while (res.next()) {
            Answer a = new Answer(
                    res.getInt("id"),
                    res.getInt("question_id"),
                    res.getInt("proposition_choisie_id"),
                    res.getInt("id_user_id")
            );
            list.add(a);
        }
        return list;
    }
}

