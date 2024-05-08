package services;

import entities.Proposition;
import utils.MyDB;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class ServiceProposition {
    private Connection con;

    public ServiceProposition() {
        con = MyDB.getInstance().getConnection();
    }

    public void ajouter(Proposition proposition) throws SQLException {
        String req = "INSERT INTO proposition (question_id, title_proposition, score, id_user_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pre = con.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            pre.setInt(1, proposition.getQuestionId());
            pre.setString(2, proposition.getTitleProposition());
            pre.setInt(3, proposition.getScore());
            pre.setInt(4, proposition.getIdUserId());
            pre.executeUpdate();

            ResultSet rs = pre.getGeneratedKeys();
            if (rs.next()) {
                proposition.setId(rs.getInt(1));  // Set the generated ID back to the proposition object
            }
        }
    }

    public void modifier(Proposition proposition) throws SQLException {
        String req = "UPDATE proposition SET title_proposition=?, score=?, id_user_id=? WHERE id=?";
        try (PreparedStatement pre = con.prepareStatement(req)) {
            pre.setString(1, proposition.getTitleProposition());
            pre.setInt(2, proposition.getScore());
            pre.setInt(3, proposition.getIdUserId());
            pre.setInt(4, proposition.getId());
            pre.executeUpdate();
        }
    }

    public void supprimer(Proposition proposition) throws SQLException {
        String req = "DELETE FROM proposition WHERE id=?";
        try (PreparedStatement pre = con.prepareStatement(req)) {
            pre.setInt(1, proposition.getId());
            pre.executeUpdate();
        }
    }

    public List<Proposition> afficher() throws SQLException {
        List<Proposition> propositions = new ArrayList<>();
        String req = "SELECT * FROM proposition";
        try (Statement ste = con.createStatement();
             ResultSet res = ste.executeQuery(req)) {
            while (res.next()) {
                Proposition p = new Proposition();
                p.setId(res.getInt("id"));
                p.setQuestionId(res.getInt("question_id"));
                p.setTitleProposition(res.getString("title_proposition"));
                p.setScore(res.getInt("score"));
                p.setIdUserId(res.getInt("id_user_id"));
                propositions.add(p);
            }
        }
        return propositions;
    }
}
