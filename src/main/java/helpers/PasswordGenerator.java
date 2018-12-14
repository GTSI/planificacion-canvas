package helpers;

import business_services.users.data.PasswordData;

public class PasswordGenerator {

  /* Creamos las contrasenias usando la identificacion de las tablas mig_usuarios. Puede ser el pasaporte o cedula */
  public static PasswordData crearPasswordDataFromIdentificacion(String identificacion) {
    String password_salt = Misc.randomString(20);

    String password_encrypted = identificacion + password_salt;
    for(int i=0; i<20;i++) {
      password_encrypted = Misc.generateHash(password_encrypted);
    }

    return new PasswordData(identificacion, password_encrypted, password_salt);
  }

}
