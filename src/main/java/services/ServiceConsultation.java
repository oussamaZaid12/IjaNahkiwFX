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
    public List<Consultation> getConsultationsForMonth(int year, int month) throws SQLException {
        List<Consultation> consultations = new ArrayList<>();

        String query = "SELECT * FROM consultation WHERE YEAR(date_c) = ? AND MONTH(date_c) = ?";

             PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, year);
            statement.setInt(2, month);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Consultation consultation = new Consultation();
                    consultation.setId(resultSet.getInt("id"));
                    consultation.setIdp(resultSet.getInt("idp"));
                    consultation.setIdt(resultSet.getInt("idt"));
                    consultation.setDateC(resultSet.getObject("date_c", LocalDateTime.class));
                    consultation.setPathologie(resultSet.getString("pathologie"));
                    consultation.setRemarques(resultSet.getString("remarques"));
                    consultation.setConfirmation(resultSet.getBoolean("confirmation"));
                    consultation.setFiche(resultSet.getInt("fichemedicale_id"));

                    consultations.add(consultation);
                }
            }

        return consultations;
    }


    public int getConfirmedConsultationCount() throws SQLException {
        String query = "SELECT COUNT(*) FROM consultation WHERE confirmation = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setBoolean(1, true);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0; // Return 0 if no confirmed consultations found
    }

    public int getNonConfirmedConsultationCount() throws SQLException {
        String query = "SELECT COUNT(*) FROM consultation WHERE confirmation = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setBoolean(1, false);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0; // Return 0 if no non-confirmed consultations found
    }

    public List<Consultation> getConsultationsByTherapistId(int therapistId) throws SQLException {
        List<Consultation> consultations = new ArrayList<>();
        String query = "SELECT * FROM consultation WHERE idt = ?";

        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, therapistId);

        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Consultation consultation = new Consultation();
                consultation.setId(resultSet.getInt("id"));
                consultation.setIdp(resultSet.getInt("idp"));
                consultation.setIdt(resultSet.getInt("idt"));
                consultation.setFiche(resultSet.getInt("fichemedicale_id"));
                consultation.setDateC(resultSet.getObject("date_c", LocalDateTime.class));
                consultation.setPathologie(resultSet.getString("pathologie"));
                consultation.setRemarques(resultSet.getString("remarques"));
                consultation.setConfirmation(resultSet.getBoolean("confirmation"));
                consultations.add(consultation);
            }
        }
        return consultations;
    }

    public List<Consultation> getConsultationsByPatientId(int patientid) throws SQLException {
            List<Consultation> consultations = new ArrayList<>();
            String query = "SELECT * FROM consultation WHERE idt = ?";

            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, patientid);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Consultation consultation = new Consultation();
                    consultation.setId(resultSet.getInt("id"));
                    consultation.setIdp(resultSet.getInt("idp"));
                    consultation.setIdt(resultSet.getInt("idt"));
                    consultation.setFiche(resultSet.getInt("fichemedicale_id"));
                    consultation.setDateC(resultSet.getObject("date_c", LocalDateTime.class));
                    consultation.setPathologie(resultSet.getString("pathologie"));
                    consultation.setRemarques(resultSet.getString("remarques"));
                    consultation.setConfirmation(resultSet.getBoolean("confirmation"));
                    consultations.add(consultation);
                }
            }
            return consultations;
        }
    }

