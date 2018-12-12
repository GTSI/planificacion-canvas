package db.daos;

import db.models.AbstractDao;
import db.models.User;
import org.apache.commons.dbutils.DbUtils;

import java.math.BigInteger;
import java.sql.*;
import java.util.List;
import java.util.Optional;

public class UserDao extends AbstractDao implements Dao<User> {

  public UserDao(Connection conn) {
    super(conn);
  }

  @Override
  public Optional<User> get(long id) throws SQLException {
    String sql = "SELECT * FROM users WHERE id=? and workflow_state<>'deleted'";
    ResultSet rsGetUser = null;
    PreparedStatement psfGetUser = this.getConn().prepareStatement(sql);
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

    DbUtils.close(psfGetUser);
    DbUtils.close(rsGetUser);

    return Optional.empty();
  }

  @Override
  public List<User> getAll() {
    return null;
  }

  @Override
  public long save(User user) {
    PreparedStatement psfCrearUsuario = null;
    try {
      psfCrearUsuario = this.getConn().prepareStatement("insert into users (name,"
        + "sortable_name, workflow_state,time_zone,short_name,created_at, "
        + "updated_at, uuid, avatar_image_url) values(?,?,'registered', 'Quito',?,now(),now(), concat('20181207',uuid_generate_v1()),'')", Statement.RETURN_GENERATED_KEYS);

      psfCrearUsuario.setString(1, user.name); // name
      psfCrearUsuario.setString(2, user.sortable_name); // sortable_name
      psfCrearUsuario.setString(3, user.short_name); // short_name

      psfCrearUsuario.executeUpdate();

      ResultSet rsCrearUsuario = psfCrearUsuario.getGeneratedKeys();

      rsCrearUsuario.next();

      long idUser = rsCrearUsuario.getLong(1);

      DbUtils.close(psfCrearUsuario);
      DbUtils.close(rsCrearUsuario);

      return idUser;

    } catch (SQLException e) {
      e.printStackTrace();

      return -1;
    }
  }

  @Override
  public List<Long> saveAll(List<User> t) {
    return null;
  }

  @Override
  public void update(User user, String[] params) {
  }

  @Override
  public void delete(User user) {

  }

}
