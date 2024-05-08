package services;

import entities.Programme;
import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceProgramme {
    private Connection con;

    public ServiceProgramme() {
        con = MyDB.getInstance().getConnection();
    }

    public void ajouter(Programme programme) throws SQLException {
        String req = "INSERT INTO programme (activite_id, exercice_id, lieu, but, nom_l, image) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pre = con.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
        pre.setInt(1, programme.getActiviteId());
        pre.setInt(2, programme.getExerciceId());
        pre.setString(3, programme.getLieu());
        pre.setString(4, programme.getBut());
        pre.setString(5, programme.getNomL());
        pre.setString(6, programme.getImage());
        pre.executeUpdate();

        // Retrieve the generated ID
        ResultSet rs = pre.getGeneratedKeys();
        if (rs.next()) {
            programme.setId(rs.getInt(1));
        }
    }

    public List<Programme> afficher() throws SQLException {
        List<Programme> programmes = new ArrayList<>();
        String req = "SELECT * FROM programme";
        Statement ste = con.createStatement();
        ResultSet res = ste.executeQuery(req);
        while (res.next()) {
            programmes.add(new Programme(res.getInt("id"), res.getInt("activite_id"), res.getInt("exercice_id"),
                    res.getString("lieu"), res.getString("but"), res.getString("nom_l"), res.getString("image")));
        }
        return programmes;
    }
}
