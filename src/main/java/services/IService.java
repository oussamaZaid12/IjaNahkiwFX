package services;

import java.sql.SQLException;
import java.util.List;

public interface IService<publication> {
    //4 methodes de CRUD
    void ajouter(publication t) throws SQLException;
    void modifier(publication t) throws SQLException;
    void supprimer(publication t) throws SQLException;
    List<publication> afficher() throws SQLException;

}
