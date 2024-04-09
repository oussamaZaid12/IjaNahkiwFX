package services;

import entities.Personne;
import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePersonne implements IService<Personne>{
    private Connection con;
    public ServicePersonne(){
        con= MyDB.getInstance().getConnection();
    }
    @Override
    public void ajouter(Personne personne) throws SQLException {
        String req = "insert into personne (nom,prenom,age) values('"+personne.getNom()
                +"','"+personne.getPrenom()+"',"+personne.getAge()+")";
        Statement ste = con.createStatement();
        ste.executeUpdate(req);

    }

    @Override
    public void modifier(Personne personne) throws SQLException {
      String req ="update personne set nom=? , prenom=? ,age= ? where id=?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setString(1,personne.getNom());
        pre.setString(2,personne.getPrenom());
        pre.setInt(3,personne.getAge());
        pre.setInt(4,personne.getId());
        pre.executeUpdate();
    }

    @Override
    public void supprimer(Personne personne) throws SQLException {
        PreparedStatement pre = con.prepareStatement("delete from personne where id=?");
        pre.setInt(1,personne.getId());
        pre.executeUpdate();
    }

    @Override
    public List<Personne> afficher() throws SQLException {

        //declarer la liste
        List<Personne> listpersonnes = new ArrayList<>();

        String req = "select * from personne";
        Statement ste = con.createStatement();
        ResultSet res = ste.executeQuery(req);
        while (res.next()){
              Personne p = new Personne();
              p.setId(res.getInt(1));
              p.setNom(res.getString("nom"));
              p.setPrenom(res.getString(3));
              p.setAge(res.getInt(4));
              listpersonnes.add(p);
        }
        return  listpersonnes;
    }
}
