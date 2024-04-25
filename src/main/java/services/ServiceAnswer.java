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

    public void ajouter(Answer answer) throws SQLException {
        String req = "INSERT INTO answer (question_id, proposition_choisie_id, id_user_id) VALUES (?, ?, ?)";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setInt(1, answer.getQuestionId());
        pre.setString(2, answer.getPropositionChoisieId());
        pre.setInt(3, answer.getIdUserId());
        pre.executeUpdate();
    }

    public void modifier(Answer answer) throws SQLException {
        String req = "UPDATE answer SET proposition_choisie_id=?, question_id=?, id_user_id=? WHERE id=?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setString(1, answer.getPropositionChoisieId());
        pre.setInt(2, answer.getQuestionId());
        pre.setInt(3, answer.getIdUserId());
        pre.setInt(4, answer.getId());
        pre.executeUpdate();
    }

    public void supprimer(Answer answer) throws SQLException {
        PreparedStatement pre = con.prepareStatement("DELETE FROM answer WHERE id=?");
        pre.setInt(1, answer.getId());
        pre.executeUpdate();
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
            a.setPropositionChoisieId(res.getString("proposition_choisie_id"));
            a.setIdUserId(res.getInt("id_user_id"));
            list.add(a);
        }
        return list;
    }
}
