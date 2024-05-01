package services;

import entities.Activite;
import utils.MyDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceActivite implements IService<Activite> {
    private Connection con;

    public ServiceActivite() {
        con = MyDB.getInstance().getConnection();
    }

    @Override
    public void ajouter(Activite activite) throws SQLException {
        String req = "INSERT INTO activite (id, nom, date, genre) VALUES (?, ?, ?, ?)";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setInt(1, activite.getId());
        pre.setString(2, activite.getNom());
        pre.setObject(3, activite.getDate());
        pre.setString(4, activite.getGenre());
        pre.executeUpdate();
    }

    @Override
    public void modifier(Activite activite) throws SQLException {
        String req = "UPDATE activite SET nom=?, date=?, genre=? WHERE id=?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setString(1, activite.getNom());
        pre.setObject(2, activite.getDate());
        pre.setString(3, activite.getGenre());
        pre.setInt(4, activite.getId());
        pre.executeUpdate();
    }

    @Override
    public void supprimer(Activite activite) throws SQLException {
        PreparedStatement pre = con.prepareStatement("DELETE FROM activite WHERE id=?");
        pre.setInt(1, activite.getId());
        pre.executeUpdate();
    }

    @Override
    public List<Activite> afficher() throws SQLException {
        List<Activite> activites = new ArrayList<>();
        String req = "SELECT * FROM activite";
        Statement ste = con.createStatement();
        ResultSet res = ste.executeQuery(req);
        while (res.next()) {
            Activite activite = new Activite();
            activite.setId(res.getInt("id"));
            activite.setNom(res.getString("nom"));
            activite.setDate(res.getObject("date", LocalDateTime.class));
            activite.setGenre(res.getString("genre"));
            activites.add(activite);
        }
        return activites;
    }
}
