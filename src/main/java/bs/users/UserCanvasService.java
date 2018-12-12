package bs.users;

import db.daos.*;
import db.models.MigUsuario;
import db.models.User;
import helpers.UsersHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
                            ) throws SQLException {
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
  public void createOrUpdateUserDB(MigUsuario migUser) {

      /* Si el usuario ya existe debemos solo actualizar su informacion. */
    try {

      if (pseudonymsDao.userExistsByUniqueId(migUser.getUsername() != null ? migUser.getUsername() : migUser.getEmail(), migUser.getId())) {
        System.out.println("Actualizando usuario: " + migUser);
        txUpdateUser(migUser);

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
  public void txUpdateUser(MigUsuario migUser) throws SQLException {
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
        throw new SQLException();
      }

      long idUserAccountAssociation = userAccountAssociationDao.saveFromUser(usuarioNuevo);

      if (idUserAccountAssociation == -1) {
        throw new SQLException();
      }

      long idPseudonym = pseudonymsDao.saveFromUserData(usuarioNuevo, migUser);

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
}
