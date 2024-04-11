package services;

import entities.Consultation;
import utils.MyDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceConsultation implements IService<Consultation> {
    private Connection con;

    public ServiceConsultation() {
        con = MyDB.getInstance().getConnection();
    }

    @Override
    public void ajouter(Consultation consultation) throws SQLException {
        String req = "INSERT INTO consultation (idp, idt, date_c, pathologie, remarques, confirmation, fichemedicale_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setInt(1, consultation.getIdp());
        pre.setInt(2, consultation.getIdt());
        pre.setObject(3, consultation.getDateC());
        pre.setString(4, consultation.getPathologie());
        pre.setString(5, consultation.getRemarques());
        pre.setBoolean(6, consultation.isConfirmation());
        pre.setInt(7, consultation.getFiche());
        pre.executeUpdate();
    }


    @Override
    public void modifier(Consultation consultation) throws SQLException {
        String req = "UPDATE consultation SET idp=?, idt=?, fichemedicale_id=?, date_c=?, pathologie=?, remarques=?, confirmation=? WHERE id=?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setInt(1, consultation.getIdp());
        pre.setInt(2, consultation.getIdt());
        pre.setInt(3, consultation.getFiche());
        pre.setObject(4, consultation.getDateC());
        pre.setString(5, consultation.getPathologie());
        pre.setString(6, consultation.getRemarques());
        pre.setBoolean(7, consultation.isConfirmation());
        pre.setInt(8, consultation.getId());
        pre.executeUpdate();
    }


    @Override
    public void supprimer(Consultation consultation) throws SQLException {
        PreparedStatement pre = con.prepareStatement("DELETE FROM consultation WHERE id=?");
        pre.setInt(1, consultation.getId());
        pre.executeUpdate();
    }

    @Override
    public List<Consultation> afficher() throws SQLException {
        List<Consultation> consultations = new ArrayList<>();
        String req = "SELECT * FROM consultation";
        Statement ste = con.createStatement();
        ResultSet res = ste.executeQuery(req);
        while (res.next()) {
            Consultation consultation = new Consultation();
            consultation.setId(res.getInt("id"));
            consultation.setIdp(res.getInt("idp"));
            consultation.setIdt(res.getInt("idt"));
            consultation.setFiche(res.getInt("fichemedicale_id"));
            consultation.setDateC(res.getObject("date_c", LocalDateTime.class));
            consultation.setPathologie(res.getString("pathologie"));
            consultation.setRemarques(res.getString("remarques"));
            consultation.setConfirmation(res.getBoolean("confirmation"));
            consultations.add(consultation);
        }
        return consultations;
    }
}
