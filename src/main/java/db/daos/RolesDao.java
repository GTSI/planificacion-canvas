package db.daos;

import db.models.Roles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class RolesDao extends AbstractDao implements Dao<Roles>{

  public RolesDao(Connection conn) {
    super(conn);
  }

  @Override
  public Optional<Roles> get(long id) throws SQLException {
    return Optional.empty();
  }

  @Override
  public List<Roles> getAll() throws SQLException {
    return null;
  }

  @Override
  public long save(Roles roles) throws Exception {
    return 0;
  }

  @Override
  public Roles saveAndRetrieveIntance(Roles roles) throws Exception {
    return null;
  }

  @Override
  public List<Long> saveAll(List<Roles> t) {
    return null;
  }

  @Override
  public void update(Roles roles, String[] params) {

  }

  @Override
  public void delete(Roles roles) {

  }

  @Override
  public int count() throws SQLException {
    return 0;
  }

  public Roles getFromName(String name) throws SQLException {
    String sql = "select * from roles where name=? and workflow_state<>'deleted';";

    ResultSet rsGetRoles = null;

    PreparedStatement psfGetRoles = this.getConn().prepareStatement(sql);

    psfGetRoles.setString(1, name);

    rsGetRoles = psfGetRoles.executeQuery();

    if(rsGetRoles.next()) {
      return new Roles(
        rsGetRoles.getLong("id"),
        rsGetRoles.getString("workflow_state"),
        rsGetRoles.getString("name"));
    }

    return null;
  }

}
