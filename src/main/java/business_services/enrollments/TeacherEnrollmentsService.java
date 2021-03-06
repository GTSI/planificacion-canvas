package business_services.enrollments;

import db.config.PlanificacionConfig;
import db.daos.*;
import db.models.*;
import helpers.CanvasConstants;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TeacherEnrollmentsService {


  private int terminoOrigen = -1;
  private int terminoDestino = -1;
  private CourseSectionsDao courseSectionsDao;
  private RolesDao rolesDao;
  private CourseDao courseDao;
  private MigParaleloProfesorDao migParaleloProfesorDao;
  private MigUsuariosDao migUsuariosDao;
  private EnrollmentsDao enrollmentsDao;
  private PseudonymsDao pseudonymsDao;
  private EnrollmentStateDao enrollmentStateDao;
  private UserDao userDao;

  private static TeacherEnrollmentsService instance;

  private TeacherEnrollmentsService(
    CourseSectionsDao courseSectionsDao,
    RolesDao rolesDao,
    CourseDao courseDao,
    MigParaleloProfesorDao migParaleloProfesorDao,
    MigUsuariosDao migUsuariosDao,
    EnrollmentsDao enrollmentsDao,
    PseudonymsDao pseudonymsDao,
    EnrollmentStateDao enrollmentStateDao,
    UserDao userDao,
    int terminoOrigen, int terminoDestino
  ) {
    this.terminoDestino = terminoDestino;
    this.terminoOrigen = terminoOrigen;
    this.courseSectionsDao = courseSectionsDao;
    this.rolesDao = rolesDao;
    this.courseDao = courseDao;
    this.migParaleloProfesorDao = migParaleloProfesorDao;
    this.migUsuariosDao = migUsuariosDao;
    this.enrollmentsDao = enrollmentsDao;
    this.pseudonymsDao = pseudonymsDao;
    this.enrollmentStateDao = enrollmentStateDao;
    this.userDao = userDao;
  }

  public static TeacherEnrollmentsService getInstance(
    Connection conn,
    PlanificacionConfig planificacionConfig) throws SQLException {
    if (instance == null) {
      instance = new TeacherEnrollmentsService(
        new CourseSectionsDao(conn),
        new RolesDao(conn),
        new CourseDao(conn),
        new MigParaleloProfesorDao(conn),
        new MigUsuariosDao(conn),
        new EnrollmentsDao(conn),
        new PseudonymsDao(conn),
        new EnrollmentStateDao(conn),
        new UserDao(conn),
        planificacionConfig.getOrigen(), planificacionConfig.getDestino());
    }

    return instance;
  }

  /* Este metodo se encarga de la creacion de los enrollments
    de los profesores a cada curso
    dentro del termino designado.
    SOLO CREACION */

  public void crearEnrollments() throws SQLException {

    List<CourseSection> courseSections = null;

    courseSections = courseSectionsDao.getCourseSectionsFromEnrollmentTerm(terminoDestino);

    for (CourseSection courseSection : courseSections) {
      Optional<Course> optionalCourse = courseDao.get(courseSection.getCourse_id());
      if (optionalCourse.isPresent()) {
        Course course = optionalCourse.get();

        List<MigParaleloProfesor> profesores = migParaleloProfesorDao.getUsersFromIDMateria(Integer.parseInt(course.getMigration_id() != null ? course.getMigration_id() : "-1"));
        for(MigParaleloProfesor profesor: profesores) {
          txCrearEnrollmentProfesor(profesor, course, courseSection);
        }
      }
    }
  }

  public void txCrearEnrollmentProfesorFromUserId(long user_id, long course_id) {
    Connection conn = rolesDao.getConn();
    try {
      conn.setAutoCommit(false);

      User usuario = userDao.get(user_id).get();
      Course course = courseDao.get(course_id).get();
      CourseSection section = courseSectionsDao.getFromCourseId(course_id).get();

      Roles roleTeacher = rolesDao.getFromName("TeacherEnrollment");

      if(usuario != null) {
        System.out.println("Creando enrollment" + usuario);

          long enrollment_id = enrollmentsDao.save(new Enrollment(
                  -1,
                  usuario.id,
                  course_id,
                  roleTeacher.getName(),
                  null,// uuid nunca se toca.
                  "active" ,
                  section.getId(),
                  CanvasConstants.PARENT_ACCOUNT_ID,
                  "unpublished",
                  false,
                  roleTeacher.getId(),
                  true ));

          enrollmentStateDao.saveAndRetrieveIntance(new EnrollmentState(
                  enrollment_id,
                  "active",
                  true,
                  course.getStart_at(),
                  course.getConclude_at(),
                  false,
                  true,
                  1));

        } else {
          System.err.println("Usuario no existe " + usuario);
        }

    } catch (SQLException e) {
      e.printStackTrace();
      try {
        System.err.println("transaccion del enrollment " + " no se pudo realizar!");
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

  private void txCrearEnrollmentProfesor(MigParaleloProfesor profesor,
                                         Course course,
                                         CourseSection courseSection) {

    Connection conn = rolesDao.getConn();
    try {
      conn.setAutoCommit(false);

      Roles roleTeacher = rolesDao.getFromName("TeacherEnrollment");
      MigUsuario migUsuarioProfesor = migUsuariosDao.getFromMatricula(profesor.getCedula());

      if(migUsuarioProfesor != null   && !enrollmentsDao.existeEnrollment(
        migUsuarioProfesor.getEmail(),
        migUsuarioProfesor.getUsername(),
        profesor.getCedula(),
        roleTeacher.getId(),
        courseSection.getId() ) ) {
        System.out.println("Creando enrollment" + profesor);
        Pseudonym pseudonymProfesor = pseudonymsDao.getPseudonymFromSisUserId(profesor.getCedula());

        if(pseudonymProfesor != null || (migUsuarioProfesor != null && migUsuarioProfesor.getUsername()!= null
        && pseudonymsDao.getFromUniqueId(migUsuarioProfesor.getUsername()).isPresent())) {
          if(pseudonymProfesor == null)
            pseudonymProfesor = Objects.requireNonNull(pseudonymsDao.getFromUniqueId(migUsuarioProfesor.getUsername())).get();

          long enrollment_id = enrollmentsDao.save(new Enrollment(
            -1,
            pseudonymProfesor.user_id,
            course.getId(),
            roleTeacher.getName(),
            null,// uuid nunca se toca.
            "active" ,
            courseSection.getId(),
            CanvasConstants.PARENT_ACCOUNT_ID,
            "unpublished",
            false,
            roleTeacher.getId(),
            true ));

          enrollmentStateDao.saveAndRetrieveIntance(new EnrollmentState(
            enrollment_id,
            "active",
            true,
            course.getStart_at(),
            course.getConclude_at(),
            false,
            true,
            1));

        } else {
          System.err.println("Usuario no existe " + profesor);
        }

      } else System.err.println("Ignorando enrollment ya existente en nuestra base " + profesor);

    } catch (SQLException e) {
      e.printStackTrace();
      try {
        System.err.println("transaccion del enrollment " + profesor + " no se pudo realizar!");
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
