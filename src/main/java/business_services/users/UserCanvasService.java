package business_services.users;

import db.daos.*;
import db.models.MigUsuario;
import db.models.Pseudonym;
import db.models.User;
import helpers.PseudonymsHelper;
import helpers.UsersHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserCanvasService {
  private UserDao userDao;
  private PseudonymsDao pseudonymsDao;
  private CommunicationChannelDao communicationChannelDao;
  private UserAccountAssociationDao userAccountAssociationDao;
  private MigUsuariosDao migUsuariosDao;

  private static UserCanvasService instance;


  private UserCanvasService(UserDao userDao,
                            PseudonymsDao pseudonymsDao,
                            CommunicationChannelDao communicationChannelDao,
                            UserAccountAssociationDao userAccountAssociationDao,
                            MigUsuariosDao migUsuariosDao
                            ) {
    this.userDao = userDao;
    this.pseudonymsDao = pseudonymsDao;
    this.communicationChannelDao = communicationChannelDao;
    this.userAccountAssociationDao = userAccountAssociationDao;
    this.migUsuariosDao = migUsuariosDao;
  }

  public static UserCanvasService getInstance(Connection conn) throws SQLException {
    if (instance == null) {
      instance = new UserCanvasService(
        new UserDao(conn),
        new PseudonymsDao(conn),
        new CommunicationChannelDao(conn),
        new UserAccountAssociationDao(conn),
        new MigUsuariosDao(conn));
    }

    return instance;
  }

  /* Transaccion utilizada para crear o actualizar un usuario junto los siguientes datos relacionados:
   * @param communication_channels, pseudonyms, user_account_associations */
  private void createOrUpdateUserDB(MigUsuario migUser) {

      /* Si el usuario ya existe debemos solo actualizar su informacion. */
    try {

      if (pseudonymsDao.userExistsByUniqueId(migUser.getUsername() != null ? migUser.getUsername() : migUser.getEmail(), migUser.getId())) {
        // System.out.println("Actualizando usuario: " + migUser);
        Pseudonym pseudonym = pseudonymsDao.getFromMigUsuario(migUser);
        // System.out.println(pseudonym);

        /* Si el usuario no existe realizamos la creacion de este. */
      } else {
        System.out.println("Creando usuario: " + migUser);
        txCrearUsuarioCompleto(migUser);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.err.println("No se pudo crear al usuario: " + migUser);
    }
  }

  /* transaccion usada para actualizar un usuario modificando las tablas necesarias en el canvas */
  private void txUpdateUser(MigUsuario migUser) throws SQLException {
    System.out.println("Aun no esta implementado el metodo para realizar la actualizacion de informacion de los usuarios" + migUser);
    return;
  }

  /* transaccion usada para crear un usuario modificando las tablas necesarias en el canvas */
  public void txCrearUsuarioCompleto(MigUsuario migUser) {
    Connection conn = userDao.getConn();
    User usuarioNuevo = UsersHelper.getUserFromMigUsuario(migUser);

    // seteamos lo necesario para llevar a cabo la conexion *singleton
    try {
      conn.setAutoCommit(false);

      usuarioNuevo.id = userDao.save(usuarioNuevo); // guardamos el nuevo usuario

      if (usuarioNuevo.id == -1) {
        System.err.println("No se pudo crear el Usuario: " + usuarioNuevo);
        throw new SQLException();
      }

      long idCommChannel = communicationChannelDao.saveFromUserData(usuarioNuevo, migUser);

      if (idCommChannel == -1) {
        System.err.println("No se pudo crear el correo: " + usuarioNuevo);
        throw new SQLException();
      }

      long idUserAccountAssociation = userAccountAssociationDao.saveFromUser(usuarioNuevo);

      if (idUserAccountAssociation == -1) {
        System.err.println("No se pudo crear el user account association: " + usuarioNuevo);
        throw new SQLException();
      }

      long idPseudonym = pseudonymsDao.saveFromUserData(usuarioNuevo, migUser);

      if (idPseudonym == -1) {
        System.err.println("No se pudo crear el user seudonimo: " + usuarioNuevo);
        throw new SQLException();
      }

    } catch (SQLException e) {
      e.printStackTrace();
      try {
        System.err.println("transaccion del usuario " + usuarioNuevo + " no se pudo realizar!");
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

  public void migrarUsuarios() {
    List<MigUsuario> migUsuarios = null;
    try {
      migUsuarios = migUsuariosDao.getAll();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    for(MigUsuario migUser: migUsuarios)  {
        createOrUpdateUserDB(migUser);
      }
  }

  public void migrarUsuario(String id) {
    try {
      MigUsuario usuario = migUsuariosDao.getFromMatricula(id);
      createOrUpdateUserDB(usuario);
    } catch(Exception e) {
      e.printStackTrace();
    }

  }

  public void actualizarUsuariosFromMigUsuarios() throws SQLException {
      List<MigUsuario> migUsuarios = migUsuariosDao.getAll();

      for(MigUsuario migUsuario: migUsuarios) {
        txActualizarUsuarioFromMigUsuario(migUsuario);
      }
  }

  public void txActualizarUsuarioFromMigUsuario(MigUsuario migUsuario) {
    Connection conn = pseudonymsDao.getConn();
    try {
      conn.setAutoCommit(false);
      if(migUsuario.getUsername() != null) {
        List<Pseudonym> pseudonyms = pseudonymsDao.getAllPseudonymsFromMigUsuario(migUsuario);

        if(!pseudonyms.isEmpty()) {
          Optional<User> optionalUser = userDao.get(pseudonyms.get(0).user_id);
          if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(UsersHelper.shouldUpdateNameOrSortableName(user, user.sortable_name)) {
              String name = migUsuario.getNombres() + " " + migUsuario.getApellidos();
              String sortable_name = migUsuario.getApellidos() + ", " + migUsuario.getNombres();
              user.name = name;
              user.sortable_name = sortable_name;
              user.short_name = name;

              userDao.update(user, null);
            } else {
              System.err.println("No se puede actualizar user al  usuario " + user);
            }

            if(!PseudonymsHelper.hasUsernameAsUniqueId(pseudonyms, migUsuario.getUsername())) {
              // username del migUsuario ha cambiado, entonces debemos crear un nuevo pseudonimo
              System.out.println("Agregando pseudonym a migUser" + migUsuario);
              if(pseudonyms.size() == 1) {
                Pseudonym pseudonym = pseudonyms.get(0);
                pseudonym.unique_id = migUsuario.getUsername();
                pseudonym.sis_user_id = pseudonym.sis_user_id.isEmpty()? migUsuario.getId(): pseudonym.sis_user_id;

                pseudonymsDao.update(pseudonym, null);

                long idComm = communicationChannelDao.saveFromUserData(user, migUsuario);
                if(idComm == -1) System.err.println("No se pudo crear el communication channel nuevo");
              } else {
                pseudonymsDao.saveFromUserData(user, migUsuario);
              }
            } else {
              System.err.println("No se puede agregar pseudonym al usuario " + user);
            }
          }
        }
      } else { // si aun no se actualiza el usuario
        List<Pseudonym> pseudonyms = pseudonymsDao.getAllPseudonymsFromMigUsuario(migUsuario);

        if(!pseudonyms.isEmpty()) {
          Optional<User> optionalUser = userDao.get(pseudonyms.get(0).user_id);
          if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            Pseudonym pseudonym = pseudonyms.get(0);
            pseudonym.sis_user_id = pseudonym.sis_user_id.isEmpty()? migUsuario.getId(): pseudonym.sis_user_id;

            pseudonymsDao.update(pseudonym, null);
          }
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
      try {
        System.err.println("actualizacion del migUsuario " + migUsuario + " no se pudo realizar!");
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

  public void printDuplicatesFromMigUsuarios() throws SQLException {
    List<MigUsuario> migUsuarios = migUsuariosDao.getAll();

    for(MigUsuario migUsuario: migUsuarios) {
      txPrintPossibleDuplicates(migUsuario);
    }
  }

  public void txPrintPossibleDuplicates(MigUsuario migUsuario) {
    Connection conn = pseudonymsDao.getConn();
    if (migUsuario.getUsername() != null) {
      List<User> usuarios = null;
      try {
        usuarios = userDao.getAllFromNameAndLastNames(migUsuario.getNombres(), migUsuario.getApellidos());
      } catch (SQLException e) {
        e.printStackTrace();
      }

      if (usuarios.size() > 1) {
        System.out.println(migUsuario);
        for (User u : usuarios) {
          System.out.println(u);
        }
      }
    }
  }
}
