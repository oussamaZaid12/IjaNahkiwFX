package services;

import entities.Exercice;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceExercice extends BaseService {

    public void ajouter(Exercice exercice) throws SQLException {
        String req = "INSERT INTO exercice (nom_coach, duree) VALUES (?, ?)";
        try (Connection con = getConnection();
             PreparedStatement pre = con.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            pre.setString(1, exercice.getNomCoach());
            pre.setString(2, exercice.getDuree());
            pre.executeUpdate();
            try (ResultSet rs = pre.getGeneratedKeys()) {
                if (rs.next()) {
                    exercice.setId(rs.getInt(1));
                }
            }
        }
    }

    public void modifier(Exercice exercice) throws SQLException {
        String req = "UPDATE exercice SET nom_coach=?, duree=? WHERE id=?";
        try (Connection con = getConnection();
             PreparedStatement pre = con.prepareStatement(req)) {
            pre.setString(1, exercice.getNomCoach());
            pre.setString(2, exercice.getDuree());
            pre.setInt(3, exercice.getId());
            pre.executeUpdate();
        }
    }

    public void supprimer(Exercice exercice) throws SQLException {
        String req = "DELETE FROM exercice WHERE id=?";
        try (Connection con = getConnection();
             PreparedStatement pre = con.prepareStatement(req)) {
            pre.setInt(1, exercice.getId());
            pre.executeUpdate();
        }
    }

    public List<Exercice> afficher() throws SQLException {
        List<Exercice> exercices = new ArrayList<>();
        String sql = "SELECT * FROM exercice";
        try (Connection con = getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Exercice exercice = new Exercice();
                exercice.setId(rs.getInt("id"));
                exercice.setNomCoach(rs.getString("nom_coach"));
                exercice.setDuree(rs.getString("duree"));
                exercices.add(exercice);
            }
        }
        return exercices;
    }

    public List<Exercice> searchByNom(String nom) throws SQLException {
        List<Exercice> results = new ArrayList<>();
        String sql = "SELECT * FROM exercice WHERE nom_coach LIKE ?";
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, "%" + nom + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Exercice exercice = new Exercice();
                exercice.setId(rs.getInt("id"));
                exercice.setNomCoach(rs.getString("nom_coach"));
                exercice.setDuree(rs.getString("duree"));
                results.add(exercice);
            }
        }
        return results;
    }
    public boolean hasDependentProgrammes(int exerciceId) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM programme WHERE exercice_id = ?";
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, exerciceId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
                return false;
            }
        }
    }

}
