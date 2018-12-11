package db.daos;

import db.models.User;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class UserDao implements Dao<User> {

    private Connection conn;
    public UserDao(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Optional<User> get(long id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id=?";
        ResultSet rsGetUser = null;
        PreparedStatement psfGetUser = conn.prepareStatement(sql);
        psfGetUser.setLong(1, id);

        rsGetUser = psfGetUser.executeQuery();

        if (rsGetUser.next())
            return Optional.of(new User(rsGetUser.getLong("id"),
                    rsGetUser.getString("name"),
                    rsGetUser.getString("sortable_name"),
                    rsGetUser.getString("short_name"),
                    rsGetUser.getString("uuid"),
                    rsGetUser.getString("workflow_state"),
                    rsGetUser.getLong("migration_id")));

        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public void save(User user) {

    }

    @Override
    public void saveAll(List<User> t) {

    }

    @Override
    public void update(User user, String[] params) {
    }

    @Override
    public void delete(User user) {

    }
}
