package business_services.celexcsv;

        import business_services.celexcsv.data.CourseCelexCSVData;
        import db.config.CelexConfig;
        import db.daos.*;
        import db.models.*;
        import helpers.CanvasConstants;
        import helpers.DateUtils;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.sql.Connection;
        import java.sql.SQLException;
        import java.sql.Timestamp;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Optional;

public class CelexCSVService {
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
    private EnrollmentsDao enrollmentsDao;
    private EnrollmentStateDao enrollmentStateDao;
    private RolesDao rolesDao;

    private int terminoDestino = -1;

    private static CelexCSVService instance;


    private CelexCSVService(UserDao userDao,
                                 PseudonymsDao pseudonymsDao,
                                 CommunicationChannelDao communicationChannelDao,
                                 UserAccountAssociationDao userAccountAssociationDao,
                                 MigUsuariosDao migUsuariosDao,
                                 MigMateriaParaleloDao migMateriaParaleloDao,
                                 CourseDao courseDao,
                                 WikisDao wikisDao,
                                 CourseSectionsDao courseSectionDao,
                                 CourseAccountAssociationDao courseAccountAssociationDao,
                                 EnrollmentsDao enrollmentsDao,
                                 EnrollmentStateDao enrollmentStateDao,
                                 RolesDao rolesDao,
                                 int terminoDestino
    ) throws SQLException {
        this.userDao = userDao;
        this.pseudonymsDao = pseudonymsDao;
        this.communicationChannelDao = communicationChannelDao;
        this.userAccountAssociationDao = userAccountAssociationDao;
        this.migUsuariosDao = migUsuariosDao;
        this.migMateriaParaleloDao = migMateriaParaleloDao;
        this.migMateriaParaleloDao = migMateriaParaleloDao;
        this.courseDao = courseDao;
        this.wikisDao = wikisDao;
        this.courseSectionsDao = courseSectionDao;
        this.courseAccountAssociationDao = courseAccountAssociationDao;
        this.terminoDestino = terminoDestino;
        this.enrollmentsDao = enrollmentsDao;
        this.rolesDao = rolesDao;
        this.enrollmentStateDao = enrollmentStateDao;
    }

    public static CelexCSVService getInstance(Connection conn, CelexConfig celexConfig) throws SQLException {
        if (instance == null) {
            instance = new CelexCSVService(
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
                    new EnrollmentsDao(conn),
                    new EnrollmentStateDao(conn),
                    new RolesDao(conn),
                    celexConfig.getDestino());
        }

        return instance;
    }

    public void readCSVAndLoadCoursesWithData(BufferedReader fileReader) throws IOException, SQLException {
        fileReader.readLine(); // skip title
        String line = "";
        while((line = fileReader.readLine()) != null) {
            String[] courseData = line.split(",");

            CourseCelexCSVData celexCSVData = new CourseCelexCSVData(courseData[0], courseData[1], courseData[2], courseData[3], courseData[4], courseData[5]);
            Course curso_creado = txCrearCursoFromCSV(celexCSVData, 2019, 02,12);
            Optional<CourseSection> optionalCourseSection = courseSectionsDao.getFromCourseId(curso_creado.getId());

            List<Pseudonym> listPsTeachers = new ArrayList<>();
            listPsTeachers.add(pseudonymsDao.getPseudonymFromSisUserId(courseData[3]));
            listPsTeachers.add(pseudonymsDao.getPseudonymFromSisUserId(courseData[4]));
            listPsTeachers.add(pseudonymsDao.getPseudonymFromSisUserId(courseData[5]));

            for(Pseudonym teacher: listPsTeachers) {
                txCrearEnrollmentFromPseudonym(curso_creado, optionalCourseSection.get(), teacher);
            }
        }

    }
    private void txCrearEnrollmentFromPseudonym(Course course, CourseSection courseSection, Pseudonym ps) {

        Connection conn = userDao.getConn();
        try {
            conn.setAutoCommit(false);

            Roles roleEstudiante = rolesDao.getFromName("TeacherEnrollment");

            long enrollment_id = enrollmentsDao.save(new Enrollment(
                    -1,
                    ps.user_id,
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


        } catch (SQLException e) {
            e.printStackTrace();
            try {
                System.err.println("transaccion del enrollment " + ps + " no se pudo realizar!");
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

    private Course txCrearCursoFromCSV(CourseCelexCSVData celexCSVData,
                                     int yearStart,
                                     int monthStart,
                                     int dayStart) {


        Timestamp start_at = DateUtils.getTimeStampFromDateData(yearStart, monthStart, dayStart);
        Timestamp end_at = DateUtils.getTimeStampFromDateData(yearStart, monthStart, dayStart);

        DateUtils.modifyTimestamp(end_at, DateUtils.TypeOperation.ADD, 30);

        Connection conn = userDao.getConn();

        try {
            conn.setAutoCommit(false);

            Wiki wikiCreada = wikisDao.saveAndRetrieveIntance(new Wiki(
                    -1,
                    celexCSVData.nombre_curso + " Wiki",
                    true,
                    "") );

            Course cursoCreado = courseDao.saveAndRetrieveIntance(new Course(
                    -1,
                    celexCSVData.nombre_curso,
                    CanvasConstants.PARENT_ACCOUNT_ID,
                    "available",
                    "<p>Bienvenidos al curso</p>",
                    false,
                    "teachers",
                    wikiCreada.getId(),
                    true,
                    celexCSVData.codigo_materia,
                    "feed",
                    CanvasConstants.PARENT_ACCOUNT_ID,
                    terminoDestino,
                    "---\n- id: 0\n- id: 1\n- id: 19\n- id: 15\n" +
                            "- id: 10\n- id: 3\n- id: 5\n- id: 4\n- id: 14\n- id: 8\n" +
                            "- id: 18\n- id: 6\n- id: 2\n- id: 16\n- id: 11\n  hidden: true\n",
                    null,
                    start_at,
                    end_at,
                    "" ));

            CourseSection courseSectionCreado = courseSectionsDao.saveAndRetrieveIntance(new CourseSection(
                    -1,
                    cursoCreado.getId(),
                    CanvasConstants.PARENT_ACCOUNT_ID,
                    terminoDestino,
                    "Paralelo " + celexCSVData.paralelo,
                    true,
                    start_at,
                    end_at
            ));

            courseAccountAssociationDao.save(new CourseAccountAssociation(
                    cursoCreado.getId(),
                    CanvasConstants.PARENT_ACCOUNT_ID,
                    courseSectionCreado.getId()
            ));

            return cursoCreado;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                System.err.println("transaccion del paralelo " + celexCSVData.paralelo + " no se pudo realizar!");
                conn.rollback();
            } catch (SQLException excep) { }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
