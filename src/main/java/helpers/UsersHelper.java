package helpers;

import db.models.MigUsuario;
import db.models.User;

public class UsersHelper {

  public static User getUserFromMigUsuario(MigUsuario migUsuario) {
    Long id = null;
    String name = migUsuario.getNombres() + " " + migUsuario.getApellidos();
    String sortable_name = migUsuario.getApellidos() + ", " + migUsuario.getNombres();
    String workflow_state = "registered";
    return new User(0, name, sortable_name, name, null, workflow_state, -1);
  }

}
