package business_services.enrollments;

  import db.config.PlanificacionConfig;
  import db.daos.*;
  import db.models.*;
  import helpers.CanvasConstants;

  import java.sql.Connection;
  import java.sql.SQLException;
  import java.util.List;
  import java.util.NoSuchElementException;
  import java.util.Objects;
  import java.util.Optional;

public class StudentsEnrollmentService {


  private int terminoOrigen = -1;
  private int terminoDestino = -1;
  private CourseSectionsDao courseSectionsDao;
  private RolesDao rolesDao;
  private CourseDao courseDao;
  private MigParaleloEstudianteDao migParaleloEstudianteDao;
  private MigUsuariosDao migUsuariosDao;
  private EnrollmentsDao enrollmentsDao;
  private PseudonymsDao pseudonymsDao;
  private EnrollmentStateDao enrollmentStateDao;

  private static StudentsEnrollmentService instance;

  private StudentsEnrollmentService(
    CourseSectionsDao courseSectionsDao,
    RolesDao rolesDao,
    CourseDao courseDao,
    MigParaleloEstudianteDao migParaleloEstudianteDao,
    MigUsuariosDao migUsuariosDao,
    EnrollmentsDao enrollmentsDao,
    PseudonymsDao pseudonymsDao,
    EnrollmentStateDao enrollmentStateDao,
    int terminoOrigen, int terminoDestino
  ) {
    this.terminoDestino = terminoDestino;
    this.terminoOrigen = terminoOrigen;
    this.courseSectionsDao = courseSectionsDao;
    this.rolesDao = rolesDao;
    this.courseDao = courseDao;
    this.migParaleloEstudianteDao = migParaleloEstudianteDao;
    this.migUsuariosDao = migUsuariosDao;
    this.enrollmentsDao = enrollmentsDao;
    this.enrollmentStateDao = enrollmentStateDao;
    this.pseudonymsDao = pseudonymsDao;
  }

  public static StudentsEnrollmentService getInstance(
    Connection conn,
    PlanificacionConfig planificacionConfig) throws SQLException {
    if (instance == null) {
      instance = new StudentsEnrollmentService(
        new CourseSectionsDao(conn),
        new RolesDao(conn),
        new CourseDao(conn),
        new MigParaleloEstudianteDao(conn),
        new MigUsuariosDao(conn),
        new EnrollmentsDao(conn),
        new PseudonymsDao(conn),
        new EnrollmentStateDao(conn),
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

        List<MigParaleloEstudiante> estudiantes = migParaleloEstudianteDao.getUsersFromIDMateria(Integer.parseInt(course.getMigration_id() != null ? course.getMigration_id() : "-1"));
        for(MigParaleloEstudiante estudiante: estudiantes) {
          txCrearEnrollmentEstudiante(estudiante, course, courseSection);
        }
      }
    }
  }

  private void txCrearEnrollmentEstudiante(MigParaleloEstudiante estudiante,
                                         Course course,
                                         CourseSection courseSection) {

    Connection conn = rolesDao.getConn();
    try {
      conn.setAutoCommit(false);

      Roles roleEstudiante = rolesDao.getFromName("StudentEnrollment");
      MigUsuario migUsuarioEstudiante = migUsuariosDao.getFromMatricula(estudiante.getMatricula());

      if(migUsuarioEstudiante != null   && !enrollmentsDao.existeEnrollment(migUsuarioEstudiante.getEmail(), migUsuarioEstudiante.getUsername(), estudiante.getMatricula(), roleEstudiante.getId(), courseSection.getId() ) ) {
        System.out.println("Creando enrollment" + estudiante);
        Pseudonym pseudonymEstudiante = pseudonymsDao.getFromMigUsuario(migUsuarioEstudiante);

        if(pseudonymEstudiante != null || (migUsuarioEstudiante.getUsername()!= null
          && pseudonymsDao.getFromUniqueId(migUsuarioEstudiante.getUsername()).isPresent())) {
          if(pseudonymEstudiante == null)
            pseudonymEstudiante = Objects.requireNonNull(pseudonymsDao.getFromUniqueId(migUsuarioEstudiante.getUsername())).get();

          long enrollment_id = enrollmentsDao.save(new Enrollment(
            -1,
            pseudonymEstudiante.user_id,
            course.getId(),
            roleEstudiante.getName(),
            null,// uuid nunca se toca.
            "active" ,
            courseSection.getId(),
            CanvasConstants.PARENT_ACCOUNT_ID,
            "unpublished",
            false,
            roleEstudiante.getId(),
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
          System.err.println("Usuario no existe " + estudiante);
        }

      } else System.err.println("Ignorando enrollment ya existente en nuestra base " + estudiante);

    } catch (SQLException e) {
      e.printStackTrace();
      try {
        System.err.println("transaccion del enrollment " + estudiante + " no se pudo realizar!");
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

  public void enrollUnicoUsuarioPorMatricula(String matricula) {
    try {
      List<MigParaleloEstudiante> estudiantes = migParaleloEstudianteDao.getMigParaleloEstudiantesFromMatricula(matricula);
      for(MigParaleloEstudiante estudiante: estudiantes) {
        try {
          Course course = courseDao.getFromMigrationId(Long.toString(estudiante.getIdmateria())).get();
          CourseSection courseSection = courseSectionsDao.getFromCourseId(course.getId()).get();
          txCrearEnrollmentEstudiante(
            estudiante,
            course,
            courseSection);
        } catch(NoSuchElementException ex) {
            System.err.println("Curso no existente para migparaleloestudiante... " + estudiante);
        }
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

}
