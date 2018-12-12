package db.daos;

import db.models.MigUsuario;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MigUsuariosDao extends AbstractDao implements Dao<MigUsuario> {

  public MigUsuariosDao(Connection conn) {
    super(conn);
  }

  @Override
  public Optional<MigUsuario> get(long id) throws SQLException {
    return Optional.empty();
  }

  @Override
  public List<MigUsuario> getAll() throws SQLException {
    Statement stmtGetMigUsuarios = this.getConn().createStatement();
    ResultSet rsGetMigUsuarios = stmtGetMigUsuarios.executeQuery("SELECT * FROM mig_usuarios  where id is not null  and email is not null order by id;");

    ArrayList<MigUsuario> usuarios = new ArrayList<>();

    while (rsGetMigUsuarios.next()) {
      usuarios.add(new MigUsuario(
        rsGetMigUsuarios.getString("id"),
        rsGetMigUsuarios.getString("nombres"),
        rsGetMigUsuarios.getString("apellidos"),
        rsGetMigUsuarios.getString("email"),
        rsGetMigUsuarios.getString("username")));
    }

    return usuarios;
  }

  @Override
  public long save(MigUsuario migUsuario) throws Exception {
    throw new Exception("No se puede crear un nuevo migUsuario mediante script");
  }

  @Override
  public List<Long> saveAll(List<MigUsuario> t) {
    return null;
  }

  @Override
  public void update(MigUsuario migUsuario, String[] params) {

  }

  @Override
  public void delete(MigUsuario migUsuario) {
  }
}
