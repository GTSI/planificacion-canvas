package db.models;

import java.sql.Connection;

public abstract class AbstractDao {

    private Connection conn;
    public AbstractDao(Connection conn) {
        this.conn = conn;
    }

    public Connection getConn() {
        return conn;
    }
}
