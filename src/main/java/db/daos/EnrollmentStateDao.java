package db.daos;

import db.models.EnrollmentState;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.List;
import java.util.Optional;

public class EnrollmentStateDao extends AbstractDao implements Dao<EnrollmentState> {
  public EnrollmentStateDao(Connection conn) {
    super(conn);
  }

  @Override
  public Optional<EnrollmentState> get(long id) throws SQLException {
    return Optional.empty();
  }

  @Override
  public List<EnrollmentState> getAll() throws SQLException {
    return null;
  }

  @Override
  public long save(EnrollmentState enrollmentState) throws SQLException {
    return 0;
  }

  @Override
  public EnrollmentState saveAndRetrieveIntance(EnrollmentState enrollmentState) throws SQLException {
String sql = "INSERT INTO enrollment_states("
      + "enrollment_id, state, state_is_current, state_started_at, state_valid_until,"
      + " restricted_access, access_is_current, lock_version)"
      + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    PreparedStatement psf = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

    psf.setLong(1,enrollmentState.getEnrollment_id());
    psf.setString(2,enrollmentState.getState());
    psf.setBoolean(3, enrollmentState.isState_is_current());
    psf.setTimestamp(4, enrollmentState.getState_started_at());
    psf.setTimestamp(5, enrollmentState.getState_valid_until());
    psf.setBoolean(6, enrollmentState.isRestricted_access());
    psf.setBoolean(7, enrollmentState.isAccess_is_current());
    psf.setInt(8, enrollmentState.getLock_verion());

    psf.executeUpdate();

    ResultSet rs = psf.getGeneratedKeys();

    rs.next();

    long id = rs.getLong(1);

    return enrollmentState;
  }

  @Override
  public List<Long> saveAll(List<EnrollmentState> t) {
    return null;
  }

  @Override
  public void update(EnrollmentState enrollmentState, String[] params) {

  }

  @Override
  public void delete(EnrollmentState enrollmentState) {

  }

  @Override
  public int count() throws SQLException {
    return 0;
  }
}
