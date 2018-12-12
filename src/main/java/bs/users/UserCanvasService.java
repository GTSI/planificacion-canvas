package bs.users;

import db.daos.CommunicationChannelDao;
import db.daos.PseudonymsDao;
import db.daos.UserAccountAssociationDao;
import db.daos.UserDao;
import db.models.MigUsuario;
import db.models.User;
import helpers.UsersHelper;

import java.sql.Connection;
import java.sql.SQLException;

public class UserCanvasService {
  private UserDao userDao;
  private PseudonymsDao pseudonymsDao;
  private CommunicationChannelDao communicationChannelDao;
  private UserAccountAssociationDao userAccountAssociationDao;

  private static UserCanvasService instance;


  private UserCanvasService(UserDao userDao,
                            PseudonymsDao pseudonymsDao,
                            CommunicationChannelDao communicationChannelDao,
                            UserAccountAssociationDao userAccountAssociationDao) throws SQLException {
    this.userDao = userDao;
    this.pseudonymsDao = pseudonymsDao;
    this.communicationChannelDao = communicationChannelDao;
    this.userAccountAssociationDao = userAccountAssociationDao;
  }

  public static UserCanvasService getInstance(Connection conn) throws SQLException {
    if (instance == null) {
      instance = new UserCanvasService(
        new UserDao(conn),
        new PseudonymsDao(conn),
        new CommunicationChannelDao(conn),
        new UserAccountAssociationDao(conn));
    }

    return instance;
  }

  /* Transaccion utilizada para crear o actualizar un usuario junto los siguientes datos relacionados:
   * @param communication_channels, pseudonyms, user_account_associations */
  public void createOrUpdateUserDB(MigUsuario migUser) {

    try {
      /* Si el usuario ya existe debemos solo actualizar su informacion. */
      if (pseudonymsDao.userExistsByUniqueId(migUser.getUsername())) {

        /* Si el usuario no existe realizamos la creacion de este. */
      } else {
        txCrearUsuarioCompleto(migUser);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /* transaccion usada para actualizar un usuario modificando las tablas necesarias en el canvas */
  public void txUpdateUser(MigUsuario migUser) throws SQLException {
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
}
