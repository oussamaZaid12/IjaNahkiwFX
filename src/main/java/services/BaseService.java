package services;

import utils.MyDB;
import java.sql.Connection;
import java.sql.SQLException;

public class BaseService {
    protected Connection getConnection() throws SQLException {
        return MyDB.getInstance().getConnection();
    }
}
