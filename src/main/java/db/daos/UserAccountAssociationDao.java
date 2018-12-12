package db.daos;

import db.models.User;
import db.models.UserAccountAssociation;
import helpers.CanvasConstants;
import org.apache.commons.dbutils.DbUtils;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class UserAccountAssociationDao extends AbstractDao implements Dao<UserAccountAssociation> {

  public UserAccountAssociationDao(Connection conn) {
    super(conn);
  }

  @Override
  public Optional<UserAccountAssociation> get(long id) throws SQLException {
    return Optional.empty();
  }

  @Override
  public List<UserAccountAssociation> getAll() throws SQLException {
    return null;
  }

  @Override
  public long save(UserAccountAssociation userAccountAssociation) throws Exception {
    return 0;
  }

  public long saveFromUser(User user) {
    PreparedStatement psfCrearUserAccountAssociation = null;
    try {

      psfCrearUserAccountAssociation = this.getConn().prepareStatement("insert into user_account_associations (user_id, account_id, depth, created_at, updated_at) "
        + " values (?,?,?,NOW(),NOW())", Statement.RETURN_GENERATED_KEYS);

      psfCrearUserAccountAssociation.setLong(1, user.id); // user_id
      psfCrearUserAccountAssociation.setLong(2, CanvasConstants.PARENT_ACCOUNT_ID); // account_id
      psfCrearUserAccountAssociation.setLong(3, 0); // depth

      psfCrearUserAccountAssociation.executeUpdate();

      ResultSet rsCrearuserAccountAssociation = psfCrearUserAccountAssociation.getGeneratedKeys();

      rsCrearuserAccountAssociation.next();

      long id = rsCrearuserAccountAssociation.getLong(1);

      DbUtils.close(psfCrearUserAccountAssociation);
      DbUtils.close(rsCrearuserAccountAssociation);

      return id;

    } catch (SQLException e) {
      e.printStackTrace();

      return -1;
    }
  }

  @Override
  public List<Long> saveAll(List<UserAccountAssociation> t) {
    return null;
  }

  @Override
  public void update(UserAccountAssociation userAccountAssociation, String[] params) {

  }

  @Override
  public void delete(UserAccountAssociation userAccountAssociation) {

  }
}
