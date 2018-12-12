package db.daos;

import db.models.AbstractDao;
import db.models.Pseudonym;
import org.apache.commons.dbutils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
  public List<Long> saveAll(List<Pseudonym> t) {
    return null;
  }

  @Override
  public void update(Pseudonym pseudonym, String[] params) {

  }

  @Override
  public void delete(Pseudonym pseudonym) {

  }

  public boolean userExistsByUniqueId(String unique_id) throws SQLException {
    String sql = "SELECT * FROM pseudonyms p INNER JOIN users ON users.id = p.user_id WHERE p.workflow_state = 'active' AND lower(unique_id)='" + unique_id.trim().toLowerCase() + "'";
    ResultSet rsGetPseudonym = null;
    PreparedStatement psfGetPseudonym = this.getConn().prepareStatement(sql);
    psfGetPseudonym.setString(1, unique_id);

    rsGetPseudonym = psfGetPseudonym.executeQuery();

    if (rsGetPseudonym.next())
      return true;

    DbUtils.close(psfGetPseudonym);
    DbUtils.close(rsGetPseudonym);

    return false;
  }
}
