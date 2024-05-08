package services;

import entities.Activite;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceActivite extends BaseService implements IService<Activite> {

    @Override
    public void ajouter(Activite activite) throws SQLException {
        String req = "INSERT INTO activite (nom, date, genre) VALUES (?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement pre = con.prepareStatement(req)) {
            pre.setString(1, activite.getNom());
            pre.setObject(2, Timestamp.valueOf(activite.getDate()));
            pre.setString(3, activite.getGenre());
            pre.executeUpdate();
        }
    }

    @Override
    public List<Activite> afficher() throws SQLException {
        List<Activite> activites = new ArrayList<>();
        String req = "SELECT * FROM activite";
        try (Connection con = getConnection();
             Statement ste = con.createStatement();
             ResultSet res = ste.executeQuery(req)) {
            while (res.next()) {
                Activite activite = new Activite();
                activite.setId(res.getInt("id"));
                activite.setNom(res.getString("nom"));
                activite.setDate(res.getObject("date", LocalDateTime.class));
                activite.setGenre(res.getString("genre"));
                activites.add(activite);
            }
        }
        return activites;
    }

    @Override
    public void modifier(Activite activite) throws SQLException {
        String req = "UPDATE activite SET nom=?, date=?, genre=? WHERE id=?";
        try (Connection con = getConnection();
             PreparedStatement pre = con.prepareStatement(req)) {
            pre.setString(1, activite.getNom());
            pre.setObject(2, Timestamp.valueOf(activite.getDate()));
            pre.setString(3, activite.getGenre());
            pre.setInt(4, activite.getId());
            pre.executeUpdate();
        }
    }

    @Override
    public void supprimer(Activite activite) throws SQLException {
        String req = "DELETE FROM activite WHERE id=?";
        try (Connection con = getConnection();
             PreparedStatement pre = con.prepareStatement(req)) {
            pre.setInt(1, activite.getId());
            pre.executeUpdate();
        }
    }

    public List<Activite> getUpcomingActivities() throws SQLException {
        List<Activite> upcomingActivities = new ArrayList<>();
        String req = "SELECT * FROM activite WHERE date > CURRENT_TIMESTAMP AND date < CURRENT_TIMESTAMP + INTERVAL '2 DAY'";
        try (Connection con = getConnection();
             PreparedStatement pre = con.prepareStatement(req);
             ResultSet res = pre.executeQuery()) {
            while (res.next()) {
                Activite activite = new Activite();
                activite.setId(res.getInt("id"));
                activite.setNom(res.getString("nom"));
                activite.setDate(res.getObject("date", LocalDateTime.class));
                activite.setGenre(res.getString("genre"));
                upcomingActivities.add(activite);
            }
        }
        return upcomingActivities;
    }

    public List<Activite> getActivitiesForMonth(int year, int month) throws SQLException {
        List<Activite> activities = new ArrayList<>();
        String sql = "SELECT * FROM activite WHERE YEAR(date) = ? AND MONTH(date) = ?";
        try (Connection con = getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, year);
            preparedStatement.setInt(2, month);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Activite activite = new Activite();
                activite.setId(resultSet.getInt("id"));
                activite.setNom(resultSet.getString("nom"));
                activite.setDate(resultSet.getObject("date", LocalDateTime.class));
                activite.setGenre(resultSet.getString("genre"));
                activities.add(activite);
            }
        }
        return activities;
    }

    public boolean hasDependentProgrammes(int activiteId) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM programme WHERE activite_id = ?";
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, activiteId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        }
        return false;
    }

    public List<Activite> searchActiviteByNomAndGenre(String nom, String genre) throws SQLException {
        List<Activite> results = new ArrayList<>();
        String sql = "SELECT * FROM activite WHERE nom LIKE ? AND genre LIKE ?";
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, "%" + nom + "%");
            stmt.setString(2, "%" + genre + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Activite activite = new Activite();
                activite.setId(rs.getInt("id"));
                activite.setNom(rs.getString("nom"));
                activite.setDate(rs.getObject("date", LocalDateTime.class));
                activite.setGenre(rs.getString("genre"));
                results.add(activite);
            }
        }
        return results;
    }
}
