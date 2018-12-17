package db.daos;

import db.models.CourseAccountAssociation;
import org.apache.commons.dbutils.DbUtils;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class CourseAccountAssociationDao extends AbstractDao implements Dao<CourseAccountAssociation> {
  public CourseAccountAssociationDao(Connection conn) {
    super(conn);
  }

  @Override
  public Optional<CourseAccountAssociation> get(long id) throws SQLException {
    return Optional.empty();
  }

  @Override
  public List<CourseAccountAssociation> getAll() throws SQLException {
    return null;
  }

  @Override
  public long save(CourseAccountAssociation courseAccountAssociation) throws SQLException {
    PreparedStatement psfCrearCourseAccountAssociation = null;

    String sql = "insert into course_account_associations ( course_id, " +
      "account_id, " +
      "depth, created_at, updated_at,course_section_id) " +
      "values (?, ?, ?,  NOW(), NOW(), ?)";

    psfCrearCourseAccountAssociation = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

    psfCrearCourseAccountAssociation.setLong(1,courseAccountAssociation.getCourse_id());
    psfCrearCourseAccountAssociation.setLong(2, courseAccountAssociation.getAccount_id());
    psfCrearCourseAccountAssociation.setInt(3,0);
    psfCrearCourseAccountAssociation.setLong(4, courseAccountAssociation.getCourse_section_id());

    psfCrearCourseAccountAssociation.executeUpdate();


    ResultSet rsCrearCourseAccountAssociation = psfCrearCourseAccountAssociation.getGeneratedKeys();

    rsCrearCourseAccountAssociation.next();

    long id = rsCrearCourseAccountAssociation.getLong(1);

    DbUtils.close(psfCrearCourseAccountAssociation);
    DbUtils.close(rsCrearCourseAccountAssociation);

    return id;
  }

  @Override
  public CourseAccountAssociation saveAndRetrieveIntance(CourseAccountAssociation courseAccountAssociation) throws Exception {
    return null;
  }

  @Override
  public List<Long> saveAll(List<CourseAccountAssociation> t) {
    return null;
  }

  @Override
  public void update(CourseAccountAssociation courseAccountAssociation, String[] params) {

  }

  @Override
  public void delete(CourseAccountAssociation courseAccountAssociation) {

  }

  @Override
  public int count() throws SQLException {
    return 0;
  }
}
