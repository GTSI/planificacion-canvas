package db.daos;

import db.models.EnrollmentState;

import java.sql.Connection;
import java.sql.SQLException;
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
  public long save(EnrollmentState enrollmentState) throws Exception {
    return 0;
  }

  @Override
  public EnrollmentState saveAndRetrieveIntance(EnrollmentState enrollmentState) throws Exception {
    return null;
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
