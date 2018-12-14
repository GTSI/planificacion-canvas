package db.daos;

import business_services.courses.data.CourseData;
import business_services.courses.data.MigParaleloProfesorData;
import db.models.Course;
import helpers.CanvasConstants;
import org.apache.commons.dbutils.DbUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CourseDao extends AbstractDao implements Dao<Course>{

  public CourseDao(Connection conn) {
    super(conn);
  }

  @Override
  public Optional<Course> get(long id) throws SQLException {
    return Optional.empty();
  }

  @Override
  public List<Course> getAll() throws SQLException {
    return null;
  }

  @Override
  public long save(Course course) throws Exception {
    return 0;
  }

  /* cambiar nombre de funcion a futuro, esto solo aplica para maestrias! */
  @Override
  public Course saveAndRetrieveIntance(Course course) throws SQLException {

    PreparedStatement psfCrearCourse = null;
    String sql = "insert into courses (name, account_id,  workflow_state, uuid,  created_at, updated_at,"
      + " syllabus_body, allow_student_forum_attachments, default_wiki_editing_roles, wiki_id,"
      + " allow_student_organized_groups, course_code, default_view, root_account_id, enrollment_term_id,"
      + " tab_configuration, sis_teacher_id ,start_at, conclude_at, migration_id) values ("
      + "?, ?, ?, concat('maestrias',uuid_generate_v1()), NOW(), NOW(), ?, ?, ?, ?, ?, ?,?, ?,  ?, ?, ?, ?, ?, ?)";

    assert this.getConn() != null;
    psfCrearCourse = this.getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

    assert psfCrearCourse != null;
    psfCrearCourse.setString(1, Objects.requireNonNull(course.getName()));
    psfCrearCourse.setLong(2, course.getAccount_id());
    psfCrearCourse.setString(3,course.getWorkflow_state());
    psfCrearCourse.setString(4,course.getSyllabus_body());
    psfCrearCourse.setBoolean(5, course.isAllow_student_forum_attachments());
    psfCrearCourse.setString(6,course.getDefault_wiki_editing_roles());
    psfCrearCourse.setLong(7, course.getWiki_id());
    psfCrearCourse.setBoolean(8, course.isAllow_student_organized_groups());
    psfCrearCourse.setString(9, Objects.requireNonNull(course.getCourse_code()));
    psfCrearCourse.setString(10, course.getDefault_view());
    psfCrearCourse.setLong(11, course.getRoot_account_id());
    psfCrearCourse.setLong(12, course.getEnrollment_term_id());
    psfCrearCourse.setString(13, course.getTab_configuration());
    psfCrearCourse.setNull(14, Types.VARCHAR); // sis_teacher_id Otra funcion se encargara de esto.
//    psfCrearCourse.setTimestamp(15, Objects.requireNonNull(course.getStart_at()));
//    psfCrearCourse.setTimestamp(16, Objects.requireNonNull(course.getConclude_at()));
    psfCrearCourse.setNull(15, Types.TIMESTAMP);
    psfCrearCourse.setNull(16, Types.TIMESTAMP);
    psfCrearCourse.setString(17, Objects.requireNonNull(course.getMigration_id()));

    psfCrearCourse.executeUpdate();
    ResultSet rsCrearCourse = psfCrearCourse.getGeneratedKeys();

    rsCrearCourse.next();

    long idCrearCourse = rsCrearCourse.getLong(1);

    DbUtils.close(psfCrearCourse);
    DbUtils.close(rsCrearCourse);

    course.setId(idCrearCourse);

    return  course;
  }

  @Override
  public List<Long> saveAll(List<Course> t) {
    return null;
  }

  @Override
  public void update(Course course, String[] params) {

  }

  @Override
  public void delete(Course course) {

  }

  @Override
  public int count() throws SQLException {
    return 0;
  }

  public List<CourseData> getCoursesFromEnrollmentTermMaestrias(MigParaleloProfesorData migParaleloProfesorData, int enrollment_term_id) throws SQLException {
    String sql = "SELECT c.id , c.sis_teacher_id, c.default_view, c.wiki_id, c.migration_id, c.uuid"
      +" FROM courses c"
      +" INNER JOIN course_sections cs ON c.id = cs.course_id"
      +" AND UPPER(cs.name)=?"
      +" AND c.course_code=?"
      +" AND c.enrollment_term_id=?"
      +" AND c.root_account_id=?"
      +" AND c.workflow_state<>'deleted'"
      +" AND c.migration_id=?;";

    ResultSet rsGetCourses = null;
    PreparedStatement psfGetCourses = this.getConn().prepareStatement(sql);
    psfGetCourses.setString(1,"PARALELO " + migParaleloProfesorData.paralelo );
    psfGetCourses.setString(2, migParaleloProfesorData.codigo);
    psfGetCourses.setLong(3, enrollment_term_id);
    psfGetCourses.setLong(4, CanvasConstants.PARENT_ACCOUNT_ID);
    psfGetCourses.setString(5, Long.toString(migParaleloProfesorData.idmateria));

    rsGetCourses = psfGetCourses.executeQuery();

    ArrayList<CourseData> cursos = new ArrayList<>();
    while(rsGetCourses.next()) {
        cursos.add(new CourseData(rsGetCourses.getInt("id"),
          rsGetCourses.getString("sis_teacher_id"),
          rsGetCourses.getString("default_view"),
          rsGetCourses.getInt("migration_id"),
          rsGetCourses.getInt("wiki_id"),
          rsGetCourses.getString("uuid")
          ));
    }


    DbUtils.close(psfGetCourses);
    DbUtils.close(rsGetCourses);

    return cursos;
  }
}
