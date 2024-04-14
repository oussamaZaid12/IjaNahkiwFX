package services;

import entities.publication;
import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePublication implements IService<publication> {
    private Connection con;

    public ServicePublication() {
        con = MyDB.getInstance().getConnection();
    }

    @Override
    public void ajouter(publication publication) throws SQLException {
        String req = "INSERT INTO publication (id_user_id, titre_p, description_p, image_p, date_p) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setInt(1, publication.getIdUserId());
        pre.setString(2, publication.getTitreP());
        pre.setString(3, publication.getDescriptionP());
        pre.setString(4, publication.getImageP());
        pre.setDate(5, new Date(publication.getDateP().getTime()));
        pre.executeUpdate();
    }

    @Override
    public void modifier(publication publication) throws SQLException {
        String req = "UPDATE publication SET id_user_id=?, titre_p=?, description_p=?, image_p=?, date_p=? WHERE id=?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setInt(1, publication.getIdUserId());
        pre.setString(2, publication.getTitreP());
        pre.setString(3, publication.getDescriptionP());
        pre.setString(4, publication.getImageP());
        pre.setDate(5, new Date(publication.getDateP().getTime()));
        pre.setInt(6, publication.getId());
        pre.executeUpdate();
    }

    @Override
    public void supprimer(publication publication) throws SQLException {
        PreparedStatement pre = con.prepareStatement("DELETE FROM publication WHERE id=?");
        pre.setInt(1, publication.getId());
        pre.executeUpdate();
    }

    @Override
    public List<publication> afficher() throws SQLException {
        List<publication> listPublications = new ArrayList<>();
        String req = "SELECT * FROM publication";
        Statement ste = con.createStatement();
        ResultSet res = ste.executeQuery(req);
        while (res.next()) {
            publication p = new publication();
            p.setId(res.getInt("id"));
            p.setIdUserId(res.getInt("id_user_id"));
            p.setTitreP(res.getString("titre_p"));
            p.setDescriptionP(res.getString("description_p"));
            p.setImageP(res.getString("image_p"));
            p.setDateP(res.getDate("date_p"));
            listPublications.add(p);
        }
        return listPublications;
    }
}
