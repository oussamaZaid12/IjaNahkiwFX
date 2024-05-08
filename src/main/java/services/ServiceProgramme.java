package services;

import entities.Programme;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceProgramme extends BaseService implements IService<Programme> {

    @Override
    public void ajouter(Programme programme) throws SQLException {
        String req = "INSERT INTO programme (activite_id, exercice_id, lieu, but, nom_l, image) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement pre = con.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            pre.setInt(1, programme.getActiviteId());
            pre.setInt(2, programme.getExerciceId());
            pre.setString(3, programme.getLieu());
            pre.setString(4, programme.getBut());
            pre.setString(5, programme.getNomL());
            pre.setString(6, programme.getImage());
            pre.executeUpdate();
            try (ResultSet rs = pre.getGeneratedKeys()) {
                if (rs.next()) {
                    programme.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public List<Programme> afficher() throws SQLException {
        List<Programme> programmes = new ArrayList<>();
        String req = "SELECT * FROM programme";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(req);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                programmes.add(new Programme(rs.getInt("id"), rs.getInt("activite_id"), rs.getInt("exercice_id"),
                        rs.getString("lieu"), rs.getString("but"), rs.getString("nom_l"), rs.getString("image")));
            }
        }
        return programmes;
    }

    @Override
    public void modifier(Programme programme) throws SQLException {
        String req = "UPDATE programme SET activite_id=?, exercice_id=?, lieu=?, but=?, nom_l=?, image=? WHERE id=?";
        try (Connection con = getConnection();
             PreparedStatement pre = con.prepareStatement(req)) {
            pre.setInt(1, programme.getActiviteId());
            pre.setInt(2, programme.getExerciceId());
            pre.setString(3, programme.getLieu());
            pre.setString(4, programme.getBut());
            pre.setString(5, programme.getNomL());
            pre.setString(6, programme.getImage());
            pre.setInt(7, programme.getId());
            pre.executeUpdate();
        }
    }

    @Override
    public void supprimer(Programme programme) throws SQLException {
        String req = "DELETE FROM programme WHERE id=?";
        try (Connection con = getConnection();
             PreparedStatement pre = con.prepareStatement(req)) {
            pre.setInt(1, programme.getId());
            pre.executeUpdate();
        }
    }

    public List<Programme> searchByTerm(String searchTerm) throws SQLException {
        List<Programme> filteredProgrammes = new ArrayList<>();
        String req = "SELECT * FROM programme WHERE lieu LIKE ? OR but LIKE ? OR nom_l LIKE ?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(req)) {
            pst.setString(1, "%" + searchTerm + "%");
            pst.setString(2, "%" + searchTerm + "%");
            pst.setString(3, "%" + searchTerm + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                filteredProgrammes.add(new Programme(rs.getInt("id"), rs.getInt("activite_id"), rs.getInt("exercice_id"),
                        rs.getString("lieu"), rs.getString("but"), rs.getString("nom_l"), rs.getString("image")));
            }
        }
        return filteredProgrammes;
    }

    public List<Programme> searchByLieuAndBut(String lieu, String but) throws SQLException {
        List<Programme> programmes = new ArrayList<>();
        String req = "SELECT * FROM programme WHERE lieu LIKE ? AND but LIKE ?";
        try (Connection con = getConnection();
             PreparedStatement pre = con.prepareStatement(req)) {
            pre.setString(1, "%" + lieu + "%");
            pre.setString(2, "%" + but + "%");
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                programmes.add(new Programme(
                        res.getInt("id"),
                        res.getInt("activite_id"),
                        res.getInt("exercice_id"),
                        res.getString("lieu"),
                        res.getString("but"),
                        res.getString("nom_l"),
                        res.getString("image")
                ));
            }
        }
        return programmes;
    }
    public boolean hasDependentExercices(int programmeId) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM exercice WHERE programme_id = ?";
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, programmeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
            return false;
        }
    }

}



