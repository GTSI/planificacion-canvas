package db.daos;

import business_services.users.data.PasswordData;
import com.sun.istack.internal.NotNull;
import db.models.MigUsuario;
import db.models.Pseudonym;
import db.models.User;
import helpers.CanvasConstants;
import helpers.PasswordGenerator;
import org.apache.commons.dbutils.DbUtils;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class PseudonymsDao extends AbstractDao implements Dao<Pseudonym> {

  public PseudonymsDao(Connection conn) {
    super(conn);
  }

  public Optional<Pseudonym> getFromUniqueId(String unique_id) throws SQLException {

    String sql = "SELECT * FROM pseudonyms WHERE unique_id=? and workflow_state<>'deleted'";
    ResultSet rsGetPseudonym = null;
    PreparedStatement psfGetPseudonym = this.getConn().prepareStatement(sql);
    psfGetPseudonym.setString(1, unique_id);

    rsGetPseudonym = psfGetPseudonym.executeQuery();

    if (rsGetPseudonym.next())
      return Optional.of(new Pseudonym(rsGetPseudonym.getLong("id"),
        rsGetPseudonym.getLong("user_id"),
        rsGetPseudonym.getLong("account_id"),
        rsGetPseudonym.getString("unique_id"),
        rsGetPseudonym.getString("crypted_password"),
        rsGetPseudonym.getString("password_salt"),
        rsGetPseudonym.getInt("login_count"),
        rsGetPseudonym.getString("sis_user_id"),
        rsGetPseudonym.getLong("communication_channel_id")));

    DbUtils.close(psfGetPseudonym);
    DbUtils.close(rsGetPseudonym);

    return Optional.empty();
  }

  @Override
  public Optional<Pseudonym> get(long id) throws SQLException {
    return Optional.empty();
  }

  @Override
  public List<Pseudonym> getAll() throws SQLException {
    return null;
  }

  @Override
  public long save(Pseudonym pseudonym) throws Exception {
    return -1;
  }

  @Override
  public Pseudonym saveAndRetrieveIntance(Pseudonym pseudonym) throws Exception {
    return null;
  }

  @Override
  public List<Long> saveAll(List<Pseudonym> t) {
    return null;
  }

  @Override
  public void update(Pseudonym pseudonym, String[] params) {

  }

  @Override
  public void delete(Pseudonym pseudonym) {

  }

  @Override
  public int count() throws SQLException {
    return 0;
  }

  public boolean userExistsByUniqueId(@NotNull  String unique_id, @NotNull String sis_user_id) throws SQLException {
    String sql;

    ResultSet rsGetPseudonym = null;
    PreparedStatement psfGetPseudonym;
    if(unique_id == null) {
      sql = "SELECT * FROM pseudonyms p INNER JOIN users ON users.id = p.user_id WHERE p.workflow_state = 'active' AND (lower(sis_user_id)=?)";

      psfGetPseudonym = this.getConn().prepareStatement(sql);
      psfGetPseudonym.setString(1, sis_user_id.trim().toLowerCase());
    }
    else {
      sql = "SELECT * FROM pseudonyms p INNER JOIN users ON users.id = p.user_id WHERE p.workflow_state = 'active' AND (lower(unique_id)=? or lower(sis_user_id)=?)";

      psfGetPseudonym = this.getConn().prepareStatement(sql);
      psfGetPseudonym.setString(1, unique_id.trim().toLowerCase());
      psfGetPseudonym.setString(2, sis_user_id.trim().toLowerCase());
    }

    rsGetPseudonym = psfGetPseudonym.executeQuery();

    if (rsGetPseudonym.next()) {
      System.out.println(rsGetPseudonym.getLong(1));
      return true;
    }

    DbUtils.close(psfGetPseudonym);
    DbUtils.close(rsGetPseudonym);

    return false;
  }

  public long saveFromUserData(User usuarioNuevo, MigUsuario migUser) {

    String sql = "insert into pseudonyms (user_id, account_id, workflow_state, unique_id,crypted_password, password_salt, " +
      "persistence_token, single_access_token,perishable_token, reset_password_token, sis_user_id) values (?,?,?,?,?,?,?,?,?,?,?)";

    try {
      PasswordData passwordData = PasswordGenerator.crearPasswordDataFromIdentificacion(migUser.getId());

      PreparedStatement psfCrearPseudonym = this.getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

      psfCrearPseudonym.setLong(1, usuarioNuevo.id); // user_id
      psfCrearPseudonym.setLong(2, CanvasConstants.PARENT_ACCOUNT_ID); // account_id
      psfCrearPseudonym.setString(3, "active"); // workflow_state
      psfCrearPseudonym.setString(4, migUser.getUsername() != null ? migUser.getUsername(): migUser.getEmail()); // unique_id
      psfCrearPseudonym.setString(5, passwordData.crypted_password); // crypted
      psfCrearPseudonym.setString(6, passwordData.salt_password); // salt
      psfCrearPseudonym.setString(7, ""); // persistence token
      psfCrearPseudonym.setString(8, ""); // single_access token
      psfCrearPseudonym.setString(9, ""); // perishable token
      psfCrearPseudonym.setString(10, ""); // reset_password token
      psfCrearPseudonym.setString(11, migUser.getId());

      psfCrearPseudonym.executeUpdate();

      ResultSet rsCrearPseudonym = psfCrearPseudonym.getGeneratedKeys();

      rsCrearPseudonym.next();

      long idPseudonym = rsCrearPseudonym.getLong(1);

      DbUtils.close(psfCrearPseudonym);
      DbUtils.close(rsCrearPseudonym);

      return idPseudonym;

    } catch (SQLException e) {
      e.printStackTrace();
      return -1;
    }
  }

  public Pseudonym getPseudonymFromSisUserId(String sis_user_id) throws SQLException {
    Statement stmtGetPseudonym = getConn().createStatement();
    ResultSet rsGetPseudonym = stmtGetPseudonym.executeQuery(
      "SELECT * FROM pseudonyms WHERE sis_user_id='"+sis_user_id+"'");

    if(rsGetPseudonym.next()) {
      return new Pseudonym(
        rsGetPseudonym.getLong("id"),
        rsGetPseudonym.getLong("user_id"),
        rsGetPseudonym.getLong("account_id"),
        rsGetPseudonym.getString("unique_id"),
        rsGetPseudonym.getString("crypted_password"),
        rsGetPseudonym.getString("password_salt"),
        rsGetPseudonym.getLong("login_count"),
        rsGetPseudonym.getString("sis_user_id"),
        rsGetPseudonym.getLong("communication_channel_id")
      );
    }
    return null;
  }
}
