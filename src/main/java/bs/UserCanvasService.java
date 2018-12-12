package bs;

import db.DBConnection;
import db.daos.PseudonymsDao;
import db.daos.UserDao;
import db.models.MigUsuario;
import db.models.User;
import helpers.UsersHelper;

import java.sql.Connection;
import java.sql.SQLException;

public class UserCanvasService {
  private UserDao userDao;
  private PseudonymsDao pseudonymsDao;

  private static UserCanvasService instance;


  private UserCanvasService(UserDao userDao, PseudonymsDao pseudonymsDao) throws SQLException {
    this.userDao = userDao;
    this.pseudonymsDao = pseudonymsDao;
  }

  public static UserCanvasService getInstance(Connection conn) throws SQLException {
    if (instance == null) {
      instance = new UserCanvasService(new UserDao(conn), new PseudonymsDao(conn));
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
  public void txCrearUsuarioCompleto(MigUsuario migUser) throws SQLException {
    User usuarioNuevo = UsersHelper.getUserFromMigUsuario(migUser);

    // seteamos lo necesario para llevar a cabo la conexion *singleton
    userDao.getConn().setAutoCommit(false);

    usuarioNuevo.id = userDao.save(usuarioNuevo); // guardamos el nuevo usuario

    if(usuarioNuevo.id == -1) {
      System.err.println("No se pudo crear el Usuario: " + usuarioNuevo);
      return;
    }

//        try {
//            this.getConn().setAutoCommit(false);
//            updateSales = con.prepareStatement(updateString);
//            updateTotal = con.prepareStatement(updateStatement);
//
//            for (Map.Entry<String, Integer> e : salesForWeek.entrySet()) {
//                updateSales.setInt(1, e.getValue().intValue());
//                updateSales.setString(2, e.getKey());
//                updateSales.executeUpdate();
//                updateTotal.setInt(1, e.getValue().intValue());
//                updateTotal.setString(2, e.getKey());
//                updateTotal.executeUpdate();
//                con.commit();
//            }
//        } catch (SQLException e ) {
//            JDBCTutorialUtilities.printSQLException(e);
//            if (con != null) {
//                try {
//                    System.err.print("Transaction is being rolled back");
//                    con.rollback();
//                } catch(SQLException excep) {
//                    JDBCTutorialUtilities.printSQLException(excep);
//                }
//            }
//        } finally {
//            if (updateSales != null) {
//                updateSales.close();
//            }
//            if (updateTotal != null) {
//                updateTotal.close();
//            }
//            this.getConn().setAutoCommit(true);
//        }
  }
}
