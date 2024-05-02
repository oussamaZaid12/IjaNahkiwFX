package services;

import entities.FicheMedicale;
import entities.Consultation;
import utils.MyDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceFicheMedicale implements IService<FicheMedicale> {
    private Connection con;

    public ServiceFicheMedicale() {
        con = MyDB.getInstance().getConnection();
    }

    @Override
    public void ajouter(FicheMedicale ficheMedicale) throws SQLException {
        String req = "INSERT INTO fichemedicale (date_creation, derniere_maj, id_p_id, id_t_id) VALUES (?, ?, ?, ?)";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setObject(1, ficheMedicale.getDateCreation());
        pre.setObject(2, ficheMedicale.getDerniereMaj());
        pre.setInt(3, ficheMedicale.getIdp());
        pre.setInt(4, ficheMedicale.getIdt());
        pre.executeUpdate();
    }

    @Override
    public void modifier(FicheMedicale ficheMedicale) throws SQLException {
        String req = "UPDATE fichemedicale SET date_creation=?, derniere_maj=?, id_p_id=?, id_t_id=? WHERE id=?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setObject(1, ficheMedicale.getDateCreation());
        pre.setObject(2, ficheMedicale.getDerniereMaj());
        pre.setInt(3, ficheMedicale.getIdp());
        pre.setInt(4, ficheMedicale.getIdt());
        pre.setInt(5, ficheMedicale.getId());
        pre.executeUpdate();
    }

    @Override
    public void supprimer(FicheMedicale ficheMedicale) throws SQLException {
        PreparedStatement pre = con.prepareStatement("DELETE FROM fichemedicale WHERE id=?");
        pre.setInt(1, ficheMedicale.getId());
        pre.executeUpdate();
    }

    @Override
    public List<FicheMedicale> afficher() throws SQLException {
        List<FicheMedicale> fichesMedicales = new ArrayList<>();
        String req = "SELECT * FROM fichemedicale";
        try (Statement ste = con.createStatement();
             ResultSet res = ste.executeQuery(req)) {
            while (res.next()) {
                FicheMedicale ficheMedicale = new FicheMedicale();
                ficheMedicale.setId(res.getInt("id"));
                ficheMedicale.setDateCreation(res.getDate("date_creation"));
                ficheMedicale.setDerniereMaj(res.getDate("derniere_maj"));
                ficheMedicale.setIdp(res.getInt("id_p_id"));
                ficheMedicale.setIdt(res.getInt("id_t_id"));
                fichesMedicales.add(ficheMedicale);
            }
        }
        return fichesMedicales;
    }

}
