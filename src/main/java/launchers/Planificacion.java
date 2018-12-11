package launchers;

import db.DBConnection;
import db.daos.UserDao;

import java.sql.SQLException;

public class Planificacion {
    public static void main(String[] args) throws SQLException {
        DBConnection configDB = DBConnection.getInstance("development");

        UserDao dao = new UserDao(configDB.getConnectionDestino());

        System.out.println(dao.get(1));
    }
}
