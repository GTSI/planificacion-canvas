package db.daos;

import business_services.users.data.PasswordData;
import com.sun.istack.internal.NotNull;
import db.models.MigParaleloEstudiante;
import db.models.MigUsuario;
import db.models.Pseudonym;
import db.models.User;
import helpers.CanvasConstants;
import helpers.PasswordGenerator;
import org.apache.commons.dbutils.DbUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class PseudonymsDao extends AbstractDao implements Dao<Pseudonym> {

  public PseudonymsDao(Connection conn) {
    super(conn);
  }

  public List<Pseudonym> getAllPseudonymsFromSisUserId(String sis_user_id) throws SQLException {
    ArrayList<Pseudonym> pseudonyms = new ArrayList<>();
    Statement stmtGetPseudonym = getConn().createStatement();
    ResultSet rsGetPseudonym = stmtGetPseudonym.executeQuery(
      "SELECT * FROM pseudonyms WHERE workflow_state<>'deleted' and sis_user_id like '%"+sis_user_id+"'");

    if(rsGetPseudonym.next()) {
      pseudonyms.add(new Pseudonym(
        rsGetPseudonym.getLong("id"),
        rsGetPseudonym.getLong("user_id"),
        rsGetPseudonym.getLong("account_id"),
        rsGetPseudonym.getString("unique_id"),
        rsGetPseudonym.getString("crypted_password"),
        rsGetPseudonym.getString("password_salt"),
        rsGetPseudonym.getLong("login_count"),
        rsGetPseudonym.getString("sis_user_id"),
        rsGetPseudonym.getLong("communication_channel_id")
      ));
    }

    return pseudonyms;
  }

  public @NotNull List<Pseudonym> getAllPseudonymsFromUniqueId(String unique_id) throws SQLException {
    ArrayList<Pseudonym> pseudonyms = new ArrayList<>();

    String sql = "SELECT * FROM pseudonyms WHERE (lower(unique_id)=? or lower(unique_id)=concat(?, '@espol.edu.ec')) and workflow_state<>'deleted'";
    ResultSet rsGetPseudonym = null;
    PreparedStatement psfGetPseudonym = this.getConn().prepareStatement(sql);
    psfGetPseudonym.setString(1, unique_id.toLowerCase().trim());
    psfGetPseudonym.setString(2, unique_id.toLowerCase().trim());

    rsGetPseudonym = psfGetPseudonym.executeQuery();

    while (rsGetPseudonym.next())
      pseudonyms.add(new Pseudonym(rsGetPseudonym.getLong("id"),
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

    return pseudonyms;
  }


  public Optional<Pseudonym> getFromUniqueId(String unique_id) throws SQLException {

    String sql = "SELECT * FROM pseudonyms WHERE (lower(unique_id)=? or lower(unique_id)=concat(?, '@espol.edu.ec')) and workflow_state<>'deleted'";
    ResultSet rsGetPseudonym = null;
    PreparedStatement psfGetPseudonym = this.getConn().prepareStatement(sql);
    psfGetPseudonym.setString(1, unique_id.toLowerCase().trim());
    psfGetPseudonym.setString(2, unique_id.toLowerCase().trim());

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
    PreparedStatement psfUpdatePseudonym = null;
    try {
      psfUpdatePseudonym = this.getConn().prepareStatement("update pseudonyms "
        + " set unique_id=?, sis_user_id=? where id=?");

      psfUpdatePseudonym.setString(1, pseudonym.unique_id); // name
      psfUpdatePseudonym.setString(2, pseudonym.sis_user_id); // sis_user_id
      psfUpdatePseudonym.setLong(3, pseudonym.id); // id

      psfUpdatePseudonym.executeUpdate();

      DbUtils.close(psfUpdatePseudonym);

    } catch (SQLException e) {
      e.printStackTrace();
    }
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
      sql = "SELECT * FROM pseudonyms p INNER JOIN users ON users.id = p.user_id " +
        "WHERE p.workflow_state <>'deleted' and users.workflow_state<>'deleted' AND (lower(sis_user_id)=?)";

      psfGetPseudonym = this.getConn().prepareStatement(sql);
      psfGetPseudonym.setString(1, sis_user_id.trim().toLowerCase().trim());
    }
    else {
      sql = "SELECT * FROM pseudonyms p INNER JOIN users ON users.id = p.user_id " +
        "WHERE p.workflow_state<>'deleted' AND users.workflow_state<>'deleted' AND (lower(unique_id)=? or lower(sis_user_id)=?)";

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
      "SELECT * FROM pseudonyms WHERE workflow_state<>'deleted' and sis_user_id='"+sis_user_id+"'");

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

  public Pseudonym getFromMigUsuario(MigUsuario estudiante) throws SQLException {
    Pseudonym pseudonym = null;
    String usuario = estudiante.getUsername();
    String correo = estudiante.getEmail();
    String matricula = estudiante.getId();
    Optional<Pseudonym> optionalPseudonym;

    if(usuario != null) {
      optionalPseudonym = this.getFromUniqueId(usuario);
      if(optionalPseudonym.isPresent())  return optionalPseudonym.get();

    }

    if(correo != null) {
      optionalPseudonym = this.getFromUniqueId(correo);
      if(optionalPseudonym.isPresent())  return optionalPseudonym.get();
    }

    if(matricula != null) {
      Pseudonym ps= this.getPseudonymFromSisUserId(matricula);
      if(ps != null)  return ps;
    }

    return null;
  }

  public List<Pseudonym> getAllPseudonymsFromMigUsuario(MigUsuario estudiante) throws SQLException {
    HashSet<Pseudonym> pseudonyms = new HashSet<>();

    String usuario = estudiante.getUsername();
    String correo = estudiante.getEmail();
    String matricula = estudiante.getId();

    if(usuario != null) {
      pseudonyms.addAll(this.getAllPseudonymsFromUniqueId(usuario));
    }

    if(correo != null) {
      pseudonyms.addAll(this.getAllPseudonymsFromUniqueId(correo));
    }

    if(matricula != null) {

      pseudonyms.addAll(this.getAllPseudonymsFromSisUserId(matricula));
    }

    if(pseudonyms.isEmpty()) {
      //pseudonyms.addAll(this.getAllPseudonymsFromNamesAndLastNames(estudiante.getNombres(), estudiante.getApellidos()));
    }

    return new ArrayList<>(pseudonyms);
  }

  public Pseudonym getPseudonymFromUser_Id(long user_id) throws SQLException {
    Statement stmtGetPseudonym = getConn().createStatement();
    ResultSet rsGetPseudonym = stmtGetPseudonym.executeQuery(
      "SELECT * FROM pseudonyms WHERE workflow_state<>'deleted' and user_id="+user_id);

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

  public @NotNull List<Pseudonym> getAllPseudonymsFromNamesAndLastNames(String nombres, String apellidos) throws SQLException {
    ArrayList<Pseudonym> pseudonyms = new ArrayList<>();

    String sql = "SELECT * FROM pseudonyms WHERE workflow_state<>'deleted' and user_id in (select id from users " +
      "where upper(name) like ? or upper(name) like ?)";
    ResultSet rsGetPseudonym = null;
    PreparedStatement psfGetPseudonym = this.getConn().prepareStatement(sql);
    psfGetPseudonym.setString(1, nombres + " " + apellidos + "%");
    psfGetPseudonym.setString(2, apellidos + " " + nombres + "%");

    rsGetPseudonym = psfGetPseudonym.executeQuery();

    while (rsGetPseudonym.next())
      pseudonyms.add(new Pseudonym(rsGetPseudonym.getLong("id"),
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

    return pseudonyms;
  }

}
