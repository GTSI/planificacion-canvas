package db.daos;

import db.models.CommunicationChannel;
import db.models.MigUsuario;
import db.models.User;
import org.apache.commons.dbutils.DbUtils;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class CommunicationChannelDao extends AbstractDao implements Dao<CommunicationChannel> {

  public CommunicationChannelDao(Connection conn) {
    super(conn);
  }

  @Override
  public Optional<CommunicationChannel> get(long id) throws SQLException {
    return Optional.empty();
  }

  @Override
  public List<CommunicationChannel> getAll() throws SQLException {
    return null;
  }

  @Override
  public long save(CommunicationChannel communicationChannel) throws Exception {
    return 0;
  }

  @Override
  public CommunicationChannel saveAndRetrieveIntance(CommunicationChannel communicationChannel) throws Exception {
    return null;
  }

  /* necesitamos los datos del usuario y del mig usuario para crear el communication channel */
  public long saveFromUserData(User user, MigUsuario migUsuario) {
    if(migUsuario.getEmail() == null) return -1; // esto no deberia pasar pero existen usuarios que no tienen email ni usuario!
    PreparedStatement psfCrearCommunicationChannel = null;
    try {

      psfCrearCommunicationChannel = this.getConn().prepareStatement("insert into communication_channels (path,path_type,position,user_id,created_at, updated_at,workflow_state) "
        + " values (?,?,?,?,NOW(),NOW(),?)", Statement.RETURN_GENERATED_KEYS);

      psfCrearCommunicationChannel.setString(1, migUsuario.getEmail()); // path
      psfCrearCommunicationChannel.setString(2, "email"); // path_type
      psfCrearCommunicationChannel.setLong(3, 1); // position
      psfCrearCommunicationChannel.setLong(4, user.id); // id del usuario
      psfCrearCommunicationChannel.setString(5, "active"); // workflow_state

      psfCrearCommunicationChannel.executeUpdate();

      ResultSet rsCrearCommunicationChannel = psfCrearCommunicationChannel.getGeneratedKeys();

      rsCrearCommunicationChannel.next();

      long idCommunicationChannel = rsCrearCommunicationChannel.getLong(1);

      DbUtils.close(psfCrearCommunicationChannel);
      DbUtils.close(rsCrearCommunicationChannel);

      return idCommunicationChannel;

    } catch (SQLException e) {
      e.printStackTrace();

      return -1;
    }
  }

  @Override
  public List<Long> saveAll(List<CommunicationChannel> t) {
    return null;
  }

  @Override
  public void update(CommunicationChannel communicationChannel, String[] params) {

  }

  @Override
  public void delete(CommunicationChannel communicationChannel) {

  }

  @Override
  public int count() throws SQLException {
    return 0;
  }
}
