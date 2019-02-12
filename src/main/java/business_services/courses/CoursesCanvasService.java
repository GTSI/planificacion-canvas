package business_services.courses;

  import business_services.courses.data.CourseData;
  import business_services.courses.data.MigParaleloProfesorData;
  import db.config.PlanificacionConfig;
  import db.daos.*;
  import db.models.Course;
  import db.models.CourseAccountAssociation;
  import db.models.CourseSection;
  import db.models.Wiki;
  import helpers.CanvasConstants;
  import helpers.DateUtils;

  import java.sql.Connection;
  import java.sql.SQLException;
  import java.util.List;

public class CoursesCanvasService {
  private UserDao userDao;
  private PseudonymsDao pseudonymsDao;
  private CommunicationChannelDao communicationChannelDao;
  private UserAccountAssociationDao userAccountAssociationDao;
  private MigUsuariosDao migUsuariosDao;
  private MigMateriaParaleloDao migMateriaParaleloDao;
  private CourseDao courseDao;
  private WikisDao wikisDao;
  private CourseSectionsDao courseSectionsDao;
  private CourseAccountAssociationDao courseAccountAssociationDao;

  private int terminoOrigen = -1;
  private int terminoDestino = -1;

  private static CoursesCanvasService instance;


  private CoursesCanvasService(UserDao userDao,
                               PseudonymsDao pseudonymsDao,
                               CommunicationChannelDao communicationChannelDao,
                               UserAccountAssociationDao userAccountAssociationDao,
                               MigUsuariosDao migUsuariosDao,
                               MigMateriaParaleloDao migMateriaParaleloDao,
                               CourseDao courseDao,
                               WikisDao wikisDao,
                               CourseSectionsDao courseSectionDao,
                               CourseAccountAssociationDao courseAccountAssociationDao,
                               int terminoOrigen, int terminoDestino
  ) throws SQLException {
    this.userDao = userDao;
    this.pseudonymsDao = pseudonymsDao;
    this.communicationChannelDao = communicationChannelDao;
    this.userAccountAssociationDao = userAccountAssociationDao;
    this.migUsuariosDao = migUsuariosDao;
    this.migMateriaParaleloDao = migMateriaParaleloDao;
    this.migMateriaParaleloDao = migMateriaParaleloDao;
    this.terminoOrigen = terminoOrigen;
    this.courseDao = courseDao;
    this.wikisDao = wikisDao;
    this.courseSectionsDao = courseSectionDao;
    this.courseAccountAssociationDao = courseAccountAssociationDao;
    this.terminoDestino = terminoDestino;
  }

  public static CoursesCanvasService getInstance(Connection conn, PlanificacionConfig planificacionConfig) throws SQLException {
    if (instance == null) {
      instance = new CoursesCanvasService(
        new UserDao(conn),
        new PseudonymsDao(conn),
        new CommunicationChannelDao(conn),
        new UserAccountAssociationDao(conn),
        new MigUsuariosDao(conn),
        new MigMateriaParaleloDao(conn),
        new CourseDao(conn),
        new WikisDao(conn),
        new CourseSectionsDao(conn),
        new CourseAccountAssociationDao(conn),
        planificacionConfig.getOrigen(), planificacionConfig.getDestino());
    }

    return instance;
  }

  /* Metodo usado para obtener los paralelos planificados dentro del termino. */
  public List<MigParaleloProfesorData> obtenerParalelosPlanificadosMaestrias() {
    try {
      assert migMateriaParaleloDao != null;
      return migMateriaParaleloDao.getCourseAndTeachersFromMigsMaetrias();
    } catch (SQLException e) {
      e.printStackTrace();
      System.err.println("No se pudo consultar los usuarios y paralelos planificados para este termino.");
      return null;
    }
  }

  /* Metodo utilizado para planificar los cursos */
  public void planificarCursos(CanvasConstants.TIPO_PLANIFICACION tipo_planificacion) {

    switch(tipo_planificacion) {
      case PREGRADO: {
        System.err.println("No implementado");
        break;
      }

      case MAESTRIAS: {
        planificarMaestrias();
        break;
      }

      case ADMISIONES: {
        System.err.println("No implementado");
        break;
      }
      default: {
        System.err.println("No se pudo consultar los usuarios y paralelos planificados para este termino.");
        break;
      }
    }

  }

  private void planificarMaestrias() {

    List<MigParaleloProfesorData> paralelosPlanificados =  obtenerParalelosPlanificadosMaestrias();

    System.out.println("INICIO DE PLANIFICACION");

    assert paralelosPlanificados != null;
    if(paralelosPlanificados.isEmpty())  {
      System.err.println("La cantidad de paralelos planificados es 0. Revisar el query correspondiente.");
      return;
    }

    for(MigParaleloProfesorData paralelo: paralelosPlanificados) {
      planificarCursoMaestria(paralelo);
    }
  }

  private void planificarCursoMaestria(MigParaleloProfesorData paralelo) {
    try {
      List<CourseData> cursosExistentes = courseDao.getCoursesFromEnrollmentTermMaestrias(paralelo, terminoDestino);

      if(cursosExistentes.isEmpty()) {
        System.out.println("Creando curso usado en maestrias. " + paralelo);
        txCrearCursoMaestria(paralelo);
      } else {
        System.out.println("Actualizando curso usado en maestrias. " + paralelo);
        actualizarCursoMaestria(cursosExistentes.get(0));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void txCrearCursoMaestria(MigParaleloProfesorData paralelo) {
    Connection conn = userDao.getConn();

    try {
      conn.setAutoCommit(false);

      DateUtils.modifyTimestamp(
        paralelo.start_at,
        DateUtils.TypeOperation.SUBSTRACT, 14);

      DateUtils.modifyTimestamp(
        paralelo.end_at,
        DateUtils.TypeOperation.ADD, 20);

      Wiki wikiCreada = wikisDao.saveAndRetrieveIntance(new Wiki(
        -1,
        paralelo.nombre_materia + " Wiki",
        true,
        "") );

      Course cursoCreado = courseDao.saveAndRetrieveIntance(new Course(
        -1,
        paralelo.nombre_materia,
        CanvasConstants.PARENT_ACCOUNT_ID,
        "available",
        "<p>Bienvenidos al curso</p>",
        false,
        "teachers",
        wikiCreada.getId(),
        true,
        paralelo.codigo,
        "feed",
        CanvasConstants.PARENT_ACCOUNT_ID,
        terminoDestino,
        "---\n- id: 0\n- id: 1\n- id: 19\n- id: 15\n" +
      "- id: 10\n- id: 3\n- id: 5\n- id: 4\n- id: 14\n- id: 8\n" +
      "- id: 18\n- id: 6\n- id: 2\n- id: 16\n- id: 11\n  hidden: true\n",
      null,
        paralelo.start_at,
        paralelo.end_at,
        Integer.toString(paralelo.idmateria)
      ));

      CourseSection courseSectionCreado = courseSectionsDao.saveAndRetrieveIntance(new CourseSection(
        -1,
        cursoCreado.getId(),
        CanvasConstants.PARENT_ACCOUNT_ID,
        terminoDestino,
        "Paralelo " + paralelo.paralelo,
        true,
        paralelo.start_at,
        paralelo.end_at
      ));

      courseAccountAssociationDao.save(new CourseAccountAssociation(
        cursoCreado.getId(),
        CanvasConstants.PARENT_ACCOUNT_ID,
        courseSectionCreado.getId()
      ));

    } catch (SQLException e) {
      e.printStackTrace();
      try {
        System.err.println("transaccion del paralelo " + paralelo + " no se pudo realizar!");
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

  private void actualizarCursoMaestria(CourseData courseData) {
  }

}
