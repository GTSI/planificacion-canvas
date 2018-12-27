package db.daos;

import db.models.User;
import org.apache.commons.dbutils.DbUtils;

import java.sql.*;
import java.util.ArrayList;
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
  public User saveAndRetrieveIntance(User user) throws Exception {
    return null;
  }

  @Override
  public List<Long> saveAll(List<User> t) {
    return null;
  }

  @Override
  public void update(User user, String[] params) {
    PreparedStatement psfUpdateUsuario = null;
    try {
      psfUpdateUsuario = this.getConn().prepareStatement("update users "
        + " set name=?, sortable_name=?, short_name=? where id=?");

      psfUpdateUsuario.setString(1, user.name); // name
      psfUpdateUsuario.setString(2, user.sortable_name); // sortable_name
      psfUpdateUsuario.setString(3, user.short_name); // short_name
      psfUpdateUsuario.setLong(4, user.id); // id

      psfUpdateUsuario.executeUpdate();


      DbUtils.close(psfUpdateUsuario);

    } catch (SQLException e) {
      e.printStackTrace();

    }
  }

  @Override
  public void delete(User user) {

  }

  @Override
  public int count() throws SQLException {
    return 0;
  }

  public List<User> getAllFromNameAndLastNames(String nombres, String apellidos) throws SQLException {
    ArrayList<User>  usuarios = new ArrayList<>();

    String sql = "SELECT * FROM users u WHERE upper(name) like ? and upper(name) like ? and workflow_state<>'deleted' " +
      "and exists (select id from pseudonyms where user_id=u.id and workflow_state<>'deleted')";
    ResultSet rsGetUser = null;
    PreparedStatement psfGetUser = this.getConn().prepareStatement(sql);
    psfGetUser.setString(1, "%"+nombres+"%");
    psfGetUser.setString(2, "%"+apellidos+"%");

    rsGetUser = psfGetUser.executeQuery();

    while (rsGetUser.next())
      usuarios.add(new User(rsGetUser.getLong("id"),
        rsGetUser.getString("name"),
        rsGetUser.getString("sortable_name"),
        rsGetUser.getString("short_name"),
        rsGetUser.getString("uuid"),
        rsGetUser.getString("workflow_state"),
        rsGetUser.getLong("migration_id")));

    DbUtils.close(psfGetUser);
    DbUtils.close(rsGetUser);

    return usuarios;
  }
}
