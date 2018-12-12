package launchers;

import bs.users.UserCanvasService;
import db.DBConnection;

import java.sql.SQLException;

public class Planificacion {
  public static void main(String[] args) throws SQLException {
    DBConnection configDB = DBConnection.getInstance("development");

    UserCanvasService service = UserCanvasService.getInstance(configDB.getConnectionDestino());
    service.migrarUsuarios(); // Creacion de Usuarios dentro del sistema canvas, tomando su informacion desde la tabla mig_usuarios
  }
}
