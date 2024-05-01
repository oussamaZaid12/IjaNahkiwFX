package services;

import entities.Answer;
import entities.Proposition;
import entities.Question;
import entities.Questionnaire;
import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceQuestionnaire implements IService<Questionnaire> {
    private Connection con;

    public ServiceQuestionnaire() {
        con = MyDB.getInstance().getConnection();
    }
    @Override
    public void ajouter(Questionnaire questionnaire) throws SQLException {
        String req = "INSERT INTO questionnaire (id_user_id, title_questionnaire, description, date) VALUES (?, ?, ?, ?)";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setInt(1, questionnaire.getIdUserId());
        pre.setString(2, questionnaire.getTitleQuestionnaire());
        pre.setString(3, questionnaire.getDescription());
        pre.setDate(4, new Date(questionnaire.getDate().getTime()));
        pre.executeUpdate();
    }

    @Override
    public void modifier(Questionnaire questionnaire) throws SQLException {
        String req = "UPDATE questionnaire SET id_user_id=?, title_questionnaire=?, description=?, date=? WHERE id=?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setInt(1, questionnaire.getIdUserId());
        pre.setString(2, questionnaire.getTitleQuestionnaire());
        pre.setString(3, questionnaire.getDescription());
        pre.setDate(4, new Date(questionnaire.getDate().getTime()));
        pre.setInt(5, questionnaire.getId());
        pre.executeUpdate();
    }

    @Override
    public void supprimer(Questionnaire questionnaire) throws SQLException {
        PreparedStatement pre = con.prepareStatement("DELETE FROM questionnaire WHERE id=?");
        pre.setInt(1, questionnaire.getId());
        pre.executeUpdate();
    }


    public List<Questionnaire> afficher() throws SQLException {
        List<Questionnaire> list = new ArrayList<>();
        String req = "SELECT * FROM questionnaire";
        Statement ste = con.createStatement();
        ResultSet res = ste.executeQuery(req);
        while (res.next()) {
            Questionnaire q = new Questionnaire(
                    res.getInt("id"),
                    res.getString("title_questionnaire"),
                    res.getString("description"),
                    res.getDate("date"),
                    res.getInt("id_user_id"));
            list.add(q);
        }
        return list;
    }


}

