package services;

import entities.Exercice;
import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceExercice {
    private Connection con;

    public ServiceExercice() {
        con = MyDB.getInstance().getConnection();
    }

    public void ajouter(Exercice exercice) throws SQLException {
        String req = "INSERT INTO exercice (nom_coach, duree) VALUES (?, ?)";
        PreparedStatement pre = con.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
        pre.setString(1, exercice.getNomCoach());
        pre.setString(2, exercice.getDuree());
        pre.executeUpdate();
        ResultSet rs = pre.getGeneratedKeys();
        if (rs.next()) {
            exercice.setId(rs.getInt(1));
        }
    }

    public void modifier(Exercice exercice) throws SQLException {
        String req = "UPDATE exercice SET nom_coach=?, duree=? WHERE id=?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setString(1, exercice.getNomCoach());
        pre.setString(2, exercice.getDuree());
        pre.setInt(3, exercice.getId());
        pre.executeUpdate();
    }

    public void supprimer(Exercice exercice) throws SQLException {
        String req = "DELETE FROM exercice WHERE id=?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setInt(1, exercice.getId());
        pre.executeUpdate();
    }
    public List<Exercice> afficher() throws SQLException {
        List<Exercice> exercices = new ArrayList<>();
        String sql = "SELECT * FROM exercice";
        try (Connection conn = MyDB.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nomCoach = rs.getString("nom_coach");
                String duree = rs.getString("duree");
                exercices.add(new Exercice(id, nomCoach, duree));
            }
        }
        return exercices;
    }


}
