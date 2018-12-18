package launchers;

import business_services.courses.CoursesCanvasService;
import business_services.enrollments.StudentsEnrollmentService;
import business_services.enrollments.TeacherEnrollmentsService;
import business_services.users.UserCanvasService;
import db.DBConnection;
import db.config.PlanificacionConfig;
import helpers.CanvasConstants;

import java.sql.SQLException;

public class Planificacion {
  public static void main(String[] args) throws SQLException {
    DBConnection configDB = DBConnection.getInstance("development");
    PlanificacionConfig planificacionConfig = PlanificacionConfig.getInstance("development");

    UserCanvasService userService = UserCanvasService.getInstance(configDB.getConnectionDestino());
    assert userService != null;
    userService.migrarUsuarios(); // Creacion de Usuarios dentro del sistema canvas, tomando su informacion desde la tabla mig_usuarios

    // una vez migrados los usuarios realizamos la creacion de los cursos
    CoursesCanvasService coursesCanvasService = CoursesCanvasService.getInstance(configDB.getConnectionDestino(), planificacionConfig);
    assert coursesCanvasService != null;

    coursesCanvasService.planificarCursos(CanvasConstants.TIPO_PLANIFICACION.MAESTRIAS);

    TeacherEnrollmentsService teacherCanvasService = TeacherEnrollmentsService.getInstance(
      configDB.getConnectionDestino(), planificacionConfig);
    teacherCanvasService.crearEnrollments();

    StudentsEnrollmentService studentsEnrollmentService = StudentsEnrollmentService.getInstance(
      configDB.getConnectionDestino(), planificacionConfig);
    studentsEnrollmentService.crearEnrollments();
  }
}
