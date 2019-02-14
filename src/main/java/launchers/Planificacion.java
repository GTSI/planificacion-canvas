package launchers;

import business_services.enrollments.TeacherEnrollmentsService;
import db.DBConnection;
import db.config.PlanificacionConfig;

import java.sql.SQLException;

public class Planificacion {
    public static void main(String[] args) throws SQLException {
        DBConnection configDB = DBConnection.getInstance("production");
        PlanificacionConfig planificacionConfig = PlanificacionConfig.getInstance("production");

        TeacherEnrollmentsService teacherEnrollmentsService = TeacherEnrollmentsService.getInstance(configDB.getConnectionDestino(), planificacionConfig);

        teacherEnrollmentsService.txCrearEnrollmentProfesorFromUserId(42218,24230);
    }
}
