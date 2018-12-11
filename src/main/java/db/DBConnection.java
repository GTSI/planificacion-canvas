package db;
import db.config.ConfigData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author sebas
 */
public class DBConnection {

    private static DBConnection instance;
    private Connection connOrigen;
    private Connection connDestino;

    private DBConnection(ConfigData config) throws SQLException {
        DriverManager.setLoginTimeout(5);
        try {
            Class.forName("org.postgresql.Driver");
            this.connOrigen = DriverManager.getConnection(config.strConnOrigen);
        } catch (ClassNotFoundException ex) {
            System.out.println("Origen database Connection Creation Failed : " + ex.getMessage());
        }

        try {
            Class.forName("org.postgresql.Driver");
            this.connDestino = DriverManager.getConnection(config.strConnDestino);
        } catch (ClassNotFoundException ex) {
            System.out.println("Destino database Connection Creation Failed : " + ex.getMessage());
        }
    }

    public Connection getConnectionOrigen() {
        return connOrigen;
    }

    public Connection getConnectionDestino() {
        return connDestino;
    }

    /* @param environment: String *development or *production */
    public static DBConnection getInstance(String environment) throws SQLException {
        if (instance == null) {
            instance = new DBConnection(ConfigData.getInstance(environment));
        } else if (instance.getConnectionDestino().isClosed() && instance.getConnectionOrigen().isClosed()) {
            instance = new DBConnection(ConfigData.getInstance(environment));
        }

        return instance;
    }
}
