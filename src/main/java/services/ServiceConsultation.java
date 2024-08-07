package services;

import entities.Consultation;
import entities.FicheMedicale;
import entities.User;
import utils.MyDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceConsultation implements IService<Consultation> {
    private static Connection con;

    public ServiceConsultation() {
        con = MyDB.getInstance().getConnection();
    }

    @Override
    public void ajouter(Consultation consultation) throws SQLException {
        String req = "INSERT INTO consultation (idp_id, idt_id, date_c, pathologie, remarques, confirmation, fichemedicale_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setInt(1, consultation.getIdp());
        pre.setInt(2, consultation.getIdt());
        pre.setObject(3, consultation.getDateC());
        pre.setString(4, consultation.getPathologie());
        pre.setString(5, consultation.getRemarques());
        pre.setBoolean(6, consultation.isConfirmation());
        //pre.setInt(7, consultation.getFiche());
        ServiceFicheMedicale serviceFiche = new ServiceFicheMedicale();
        int idp = consultation.getIdp();
        int idt = consultation.getIdt();
        FicheMedicale fiche = serviceFiche.getFicheByTherapistAndPatientId(idp, idt);
        pre.setInt(7, fiche.getId());
        System.out.println("La fiche trouvée dans la base: " + fiche);
        pre.executeUpdate();
    }


    @Override
    public void modifier(Consultation consultation) throws SQLException {
        String req = "UPDATE consultation SET idp_id=?, idt_id=?, fichemedicale_id=?, date_c=?, pathologie=?, remarques=?, confirmation=? WHERE id=?";
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
            consultation.setIdp(res.getInt("idp_id"));
            consultation.setIdt(res.getInt("idt_id"));
            consultation.setFiche(res.getInt("fichemedicale_id"));
            consultation.setDateC(res.getObject("date_c", LocalDateTime.class));
            consultation.setPathologie(res.getString("pathologie"));
            consultation.setRemarques(res.getString("remarques"));
            consultation.setConfirmation(res.getBoolean("confirmation"));
            consultations.add(consultation);
        }
        return consultations;
    }
    public List<Consultation> getConsultationsForMonth(int year, int month, User user) throws SQLException {
        List<Consultation> consultations = new ArrayList<>();

        String query = "SELECT * FROM consultation WHERE YEAR(date_c) = ? AND MONTH(date_c) = ? AND idt_id = ?";

        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, year);
            statement.setInt(2, month);
            statement.setInt(3, user.getId());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Consultation consultation = new Consultation();
                    consultation.setId(resultSet.getInt("id"));
                    consultation.setIdp(resultSet.getInt("idp_id"));
                    consultation.setIdt(resultSet.getInt("idt_id"));
                    consultation.setDateC(resultSet.getObject("date_c", LocalDateTime.class));
                    consultation.setPathologie(resultSet.getString("pathologie"));
                    consultation.setRemarques(resultSet.getString("remarques"));
                    consultation.setConfirmation(resultSet.getBoolean("confirmation"));
                    consultation.setFiche(resultSet.getInt("fichemedicale_id"));

                    consultations.add(consultation);
                }
            }
        }

        return consultations;
    }


    public List<Consultation> getConsultationsForMonthPatient(int year, int month, User user) throws SQLException {
        List<Consultation> consultations = new ArrayList<>();

        String query = "SELECT * FROM consultation WHERE YEAR(date_c) = ? AND MONTH(date_c) = ? AND idp_id = ?";

        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, year);
            statement.setInt(2, month);
            statement.setInt(3, user.getId());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Consultation consultation = new Consultation();
                    consultation.setId(resultSet.getInt("id"));
                    consultation.setIdp(resultSet.getInt("idp_id"));
                    consultation.setIdt(resultSet.getInt("idt_id"));
                    consultation.setDateC(resultSet.getObject("date_c", LocalDateTime.class));
                    consultation.setPathologie(resultSet.getString("pathologie"));
                    consultation.setRemarques(resultSet.getString("remarques"));
                    consultation.setConfirmation(resultSet.getBoolean("confirmation"));
                    consultation.setFiche(resultSet.getInt("fichemedicale_id"));

                    consultations.add(consultation);
                }
            }
        }

        return consultations;
    }



    public int getConfirmedConsultationCount(User user) throws SQLException {
        String query = "SELECT COUNT(*) FROM consultation WHERE confirmation = ? AND idt_id = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setBoolean(1, true);
            statement.setInt(2, user.getId());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0; // Return 0 if no confirmed consultations found
    }

    public int getNonConfirmedConsultationCount(User user) throws SQLException {
        String query = "SELECT COUNT(*) FROM consultation WHERE confirmation = ? AND idt_id = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setBoolean(1, false);
            statement.setInt(2, user.getId());
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
        String query = "SELECT * FROM consultation WHERE idt_id = ?";

        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, therapistId);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Consultation consultation = new Consultation();
                consultation.setId(resultSet.getInt("id"));
                consultation.setIdp(resultSet.getInt("idp_id"));
                consultation.setIdt(resultSet.getInt("idt_id"));
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
            String query = "SELECT * FROM consultation WHERE idp_id = ?";

            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, patientid);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Consultation consultation = new Consultation();
                    consultation.setId(resultSet.getInt("id"));
                    consultation.setIdp(resultSet.getInt("idp_id"));
                    consultation.setIdt(resultSet.getInt("idt_id"));
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
    public static List<Consultation> getConsultationsByFiche(FicheMedicale currentFiche) throws SQLException {
        List<Consultation> consultations = new ArrayList<>();
        String query = "SELECT * FROM consultation WHERE fichemedicale_id = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, currentFiche.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Consultation consultation = new Consultation();
                    consultation.setId(resultSet.getInt("id"));
                    consultation.setIdp(resultSet.getInt("idp_id"));
                    consultation.setIdt(resultSet.getInt("idt_id"));
                    consultation.setDateC(resultSet.getObject("date_c", LocalDateTime.class));
                    consultation.setPathologie(resultSet.getString("pathologie"));
                    consultation.setRemarques(resultSet.getString("remarques"));
                    consultation.setConfirmation(resultSet.getBoolean("confirmation"));
                    consultation.setFiche(resultSet.getInt("fichemedicale_id"));

                    consultations.add(consultation);
                }
            }
        }
        return consultations;
    }
    public List<Consultation> getUpcomingConsultations(User user) throws SQLException {
        List<Consultation> upcomingConsultations = new ArrayList<>();
        String query = "SELECT * FROM consultation WHERE date_c >= ? AND date_c < ?  AND idp_id = ?";
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1);

        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setObject(1, now);
            statement.setObject(2, tomorrow);
            statement.setInt(3, user.getId());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Consultation consultation = new Consultation();
                    consultation.setId(resultSet.getInt("id"));
                    consultation.setIdp(resultSet.getInt("idp_id"));
                    consultation.setIdt(resultSet.getInt("idt_id"));
                    consultation.setDateC(resultSet.getObject("date_c", LocalDateTime.class));
                    consultation.setPathologie(resultSet.getString("pathologie"));
                    consultation.setRemarques(resultSet.getString("remarques"));
                    consultation.setConfirmation(resultSet.getBoolean("confirmation"));
                    consultation.setFiche(resultSet.getInt("fichemedicale_id"));

                    upcomingConsultations.add(consultation);
                }
            }
        }
        return upcomingConsultations;
    }
    public void modifyFicheMedicaleId(int consultationId, int newFicheMedicaleId) throws SQLException {
        String req = "UPDATE consultation SET fichemedicale_id=? WHERE id=?";
        try (PreparedStatement pre = con.prepareStatement(req)) {
            pre.setInt(1, newFicheMedicaleId);
            pre.setInt(2, consultationId);
            pre.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database access error:");
            e.printStackTrace();
            throw e;  // Rethrow the exception after logging it
        }
    }

}

