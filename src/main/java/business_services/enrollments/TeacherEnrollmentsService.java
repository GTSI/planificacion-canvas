package business_services.enrollments;

import db.config.PlanificacionConfig;
import db.daos.CourseDao;
import db.daos.CourseSectionsDao;
import db.daos.RolesDao;
import db.models.Course;
import db.models.CourseSection;
import db.models.Roles;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TeacherEnrollmentsService {


  private int terminoOrigen = -1;
  private int terminoDestino = -1;
  private CourseSectionsDao courseSectionsDao;
  private RolesDao rolesDao;
  private CourseDao courseDao;

  private static TeacherEnrollmentsService instance;

  private TeacherEnrollmentsService(
    CourseSectionsDao courseSectionsDao,
    RolesDao rolesDao,
  CourseDao courseDao,
    int terminoOrigen, int terminoDestino
  ) {
    this.terminoDestino = terminoDestino;
    this.terminoOrigen = terminoOrigen;
    this.courseSectionsDao = courseSectionsDao;
    this.rolesDao = rolesDao;
    this.courseDao = courseDao;
  }

  public static TeacherEnrollmentsService getInstance(
    Connection conn,
    PlanificacionConfig planificacionConfig) throws SQLException {
    if (instance == null) {
      instance = new TeacherEnrollmentsService(
        new CourseSectionsDao(conn),
        new RolesDao(conn),
        new CourseDao(conn),
        planificacionConfig.getOrigen(), planificacionConfig.getDestino());
    }

    return instance;
  }

  /* Esta funcion se encarga de la creacion de los enrollments
    de los profesores a cada curso
    dentro del termino designado.
    SOLO CREACION */
  public void txCrearEnrollments() {
    Connection conn = rolesDao.getConn();
    try {
      conn.setAutoCommit(false);
      Roles roleTeacher = rolesDao.getFromName("TeacherEnrollment");
      List<CourseSection> courseSections = courseSectionsDao.getCourseSectionsFromEnrollmentTerm(terminoDestino);
      for(CourseSection courseSection: courseSections) {
        Optional<Course > optionalCourse = courseDao.get(courseSection.getCourse_id());
        if(optionalCourse.isPresent()) {
          Course course = optionalCourse.get();

        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
      try {
        System.err.println("transaccion de los enrollments no se pudo realizar!");
        conn.rollback();
      } catch (SQLException excep) { }
    } finally {
      try {
        conn.setAutoCommit(true);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  /*eliminacion de los enrollments dentro de un termino seleccionado*/
  public void txEliminarEnrollmentsInexistentes() {

  }

}
