package db.daos;

import bs.courses.data.CourseData;
import db.models.CourseSection;
import org.apache.commons.dbutils.DbUtils;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class CourseSectionsDao extends AbstractDao implements Dao<CourseSection> {
  public CourseSectionsDao(Connection conn) {
    super(conn);
  }

  @Override
  public Optional<CourseSection> get(long id) throws SQLException {
    return Optional.empty();
  }

  @Override
  public List<CourseSection> getAll() throws SQLException {
    return null;
  }

  @Override
  public long save(CourseSection courseSection) throws Exception {
    return 0;
  }

  @Override
  public CourseSection saveAndRetrieveIntance(CourseSection courseSection) throws SQLException {
    PreparedStatement psfCrearCourseSection = null;

    String sql = "insert into course_sections (course_id, root_account_id, enrollment_term_id, "
      + "name,  default_section,start_at, end_at, created_at, updated_at, workflow_state) values ("
      + "?, ?, ?, ?, ?, ?, ?,  NOW(), NOW(), ?)";

    psfCrearCourseSection = this.getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

    psfCrearCourseSection.setLong(1, courseSection.getCourse_id());
    psfCrearCourseSection.setLong(2, courseSection.getRoot_account_id());
    psfCrearCourseSection.setLong(3, courseSection.getEnrollment_term_id());
    psfCrearCourseSection.setString(4, courseSection.getName());
    psfCrearCourseSection.setBoolean(5, courseSection.isDefault_section());
//    psfCrearCourseSection.setTimestamp(6, courseSection.getStart_at());
//    psfCrearCourseSection.setTimestamp(7, courseSection.getEnd_at());

    psfCrearCourseSection.setNull(6, Types.TIMESTAMP);
    psfCrearCourseSection.setNull(7, Types.TIMESTAMP);
    psfCrearCourseSection.setString(8, "active");

    psfCrearCourseSection.executeUpdate();

    ResultSet rsCrearCourseSection = psfCrearCourseSection.getGeneratedKeys();

    rsCrearCourseSection.next();

    long idCourseSection = rsCrearCourseSection.getLong(1);

    DbUtils.close(psfCrearCourseSection);
    DbUtils.close(rsCrearCourseSection);

    courseSection.setId(idCourseSection);
    return courseSection;

  }

  @Override
  public List<Long> saveAll(List<CourseSection> t) {
    return null;
  }

  @Override
  public void update(CourseSection courseSection, String[] params) {

  }

  @Override
  public void delete(CourseSection courseSection) {

  }

  @Override
  public int count() throws SQLException {
    return 0;
  }
}
