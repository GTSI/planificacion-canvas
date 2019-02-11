package db.daos;

import db.models.Enrollment;
import org.apache.commons.dbutils.DbUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnrollmentsDao extends AbstractDao implements Dao<Enrollment> {
  public EnrollmentsDao(Connection conn) {
    super(conn);
  }

  @Override
  public Optional<Enrollment> get(long id) throws SQLException {
    return Optional.empty();
  }

  @Override
  public List<Enrollment> getAll() throws SQLException {
    return null;
  }

  @Override
  public long save(Enrollment enrollment) throws SQLException {
    PreparedStatement psfCrearEnrollment = null;
    String sql = "INSERT INTO enrollments("
      + "user_id, course_id, type, uuid, workflow_state, created_at, updated_at,"
      + "course_section_id, root_account_id,grade_publishing_status,"
      + "limit_privileges_to_course_section,role_id,from_script)"
      + "VALUES (?, ?, ?, concat('maestrias', uuid_generate_v1()), ?, NOW(), NOW(), ?, ?, ?, ?, ?, ?);";

    psfCrearEnrollment = this.getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

    psfCrearEnrollment.setLong(1,enrollment.getUser_id());
    psfCrearEnrollment.setLong(2,enrollment.getCourse_d());
    psfCrearEnrollment.setString(3, enrollment.getType());
    psfCrearEnrollment.setString(4, enrollment.getWorkflow_state());
    psfCrearEnrollment.setLong(5, enrollment.getCourse_section_id());
    psfCrearEnrollment.setLong(6,enrollment.getRoot_account_id());
    psfCrearEnrollment.setString(7, enrollment.getGrade_publishing_status());
    psfCrearEnrollment.setBoolean(8, enrollment.isLimit_privileges_to_course_section());
    psfCrearEnrollment.setLong(9, enrollment.getRole_id());
    psfCrearEnrollment.setBoolean(10, enrollment.isFrom_script());

    psfCrearEnrollment.executeUpdate();

    ResultSet rsCrearEnrollment = psfCrearEnrollment.getGeneratedKeys();

    rsCrearEnrollment.next();

    long idEnrollment = rsCrearEnrollment.getLong(1);

    DbUtils.close(psfCrearEnrollment);
    DbUtils.close(rsCrearEnrollment);

    return idEnrollment;
  }

  @Override
  public Enrollment saveAndRetrieveIntance(Enrollment enrollment) throws Exception {
    return null;
  }

  @Override
  public List<Long> saveAll(List<Enrollment> t) {
    return null;
  }

  @Override
  public void update(Enrollment enrollment, String[] params) {

  }

  @Override
  public void delete(Enrollment enrollment) {

  }

  @Override
  public int count() throws SQLException {
    return 0;
  }

  public boolean existeEnrollment(String unique_id, String cedula, long role_id, long course_section_id) throws SQLException {
    if(unique_id != null) {
      String sql = "SELECT * FROM enrollments e" + " JOIN pseudonyms p ON e.user_id=p.user_id" +
        " WHERE p.workflow_state<>'deleted' and e.workflow_state<>'deleted' and (p.sis_user_id='"
        + cedula + "' or p.unique_id='"+unique_id+"' or p.unique_id=concat('"+unique_id+"','@espol.edu.ec'))" +
        " and e.role_id=" + role_id + " and e.course_section_id=" + course_section_id;

      Statement stmtExistEnrollment = getConn().createStatement();
      ResultSet rsExistsEnrollment = stmtExistEnrollment.executeQuery(sql);

      return rsExistsEnrollment.next();
    } else {
      return existeEnrollment(cedula, role_id, course_section_id);
    }
  }

  public boolean existeEnrollment(String correo, String unique_id, String cedula, long role_id, long course_section_id) throws SQLException {
    String sql = "";
    if(correo != null && unique_id == null) {
      sql = "SELECT * FROM enrollments e" + " JOIN pseudonyms p ON e.user_id=p.user_id" +
        " WHERE p.workflow_state<>'deleted' and e.workflow_state<>'deleted' and (p.sis_user_id='"
        + cedula + "' " +
        "or p.unique_id='" + correo + "' " +
        "or p.unique_id=concat('" + unique_id + "','@espol.edu.ec'))" +
        " and e.role_id=" + role_id + " and e.course_section_id=" + course_section_id;
    } else if(unique_id != null && correo != null) {
      sql = "SELECT * FROM enrollments e" + " JOIN pseudonyms p ON e.user_id=p.user_id" +
        " WHERE p.workflow_state<>'deleted' and e.workflow_state<>'deleted' and (p.sis_user_id='"
        + cedula + "' or p.unique_id='" + unique_id + "' " +
        "or p.unique_id='" + correo + "' " +
        "or p.unique_id=concat('" + unique_id + "','@espol.edu.ec'))" +
        " and e.role_id=" + role_id + " and e.course_section_id=" + course_section_id;
    } else if(unique_id != null ) {
        sql = "SELECT * FROM enrollments e" + " JOIN pseudonyms p ON e.user_id=p.user_id" +
          " WHERE p.workflow_state<>'deleted' and e.workflow_state<>'deleted' and (p.sis_user_id='"
          + cedula + "' or p.unique_id='"+unique_id+"' " +
          "or p.unique_id=concat('"+unique_id+"','@espol.edu.ec'))" +
          " and e.role_id=" + role_id + " and e.course_section_id=" + course_section_id;
      } else {
      return existeEnrollment(cedula, role_id, course_section_id);
    }

      Statement stmtExistEnrollment = getConn().createStatement();
      ResultSet rsExistsEnrollment = stmtExistEnrollment.executeQuery(sql);

      return rsExistsEnrollment.next();
  }

  public boolean existeEnrollment(String cedula, long role_id, long course_section_id) throws SQLException {
    String sql = "SELECT * FROM enrollments e" + " JOIN pseudonyms p ON e.user_id=p.user_id" +
      " WHERE p.workflow_state<>'deleted' and e.workflow_state<>'deleted' and (p.sis_user_id='"
      + cedula + "')" +
      " and e.role_id=" + role_id + " and e.course_section_id=" + course_section_id;

    Statement stmtExistEnrollment = getConn().createStatement();
    ResultSet rsExistsEnrollment = stmtExistEnrollment.executeQuery(sql);

    return rsExistsEnrollment.next();
  }

  public List<Enrollment> getAllEnrollmentsFromCourseSection(long course_section_id, long role_id) throws SQLException {
    ArrayList<Enrollment> enrollments = new ArrayList<>();
    String sql = "SELECT e.* FROM enrollments e" + " JOIN pseudonyms p ON e.user_id=p.user_id" +
      " WHERE p.workflow_state<>'deleted' and e.workflow_state<>'deleted' " +
      " and e.role_id=" + role_id + " and e.course_section_id=" + course_section_id;

    Statement stmtExistEnrollment = getConn().createStatement();
    ResultSet rsExistsEnrollment = stmtExistEnrollment.executeQuery(sql);

    while (rsExistsEnrollment.next()) {
      enrollments.add(new Enrollment(
            rsExistsEnrollment.getLong("id"),
            rsExistsEnrollment.getLong("user_id"),
            rsExistsEnrollment.getLong("course_id"),
            rsExistsEnrollment.getString("type"),
            rsExistsEnrollment.getString("uuid"),// uuid nunca se toca.
            rsExistsEnrollment.getString("workflow_state"),
            rsExistsEnrollment.getLong("course_section_id"),
            rsExistsEnrollment.getLong("root_account_id"),
            rsExistsEnrollment.getString("grade_publishing_status"),
            rsExistsEnrollment.getBoolean("limit_privileges_to_course_section"),
            rsExistsEnrollment.getLong("role_id"),
        rsExistsEnrollment.getBoolean("from_script")
      ));
    }

    return enrollments;
  }
}

