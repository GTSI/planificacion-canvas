package db.daos;

import db.models.MigUsuario;
import org.apache.commons.dbutils.DbUtils;
import org.springframework.lang.Nullable;

import java.sql.*;
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
    ResultSet rsGetMigUsuarios = stmtGetMigUsuarios.executeQuery(
      "SELECT * FROM mig_usuarios  " +
         "where id is not null  " +
         "and email is not null and email like '%@%' " +
         "and apellidos is not null " +
         "and nombres is not null " +
         //"and  id='0925789562asdsad'" +
         "order by id;");

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

  public List<MigUsuario> getAllPaginated(int page, int size) throws SQLException {
    Statement stmtGetMigUsuarios = this.getConn().createStatement();
    ResultSet rsGetMigUsuarios = stmtGetMigUsuarios.executeQuery(
      "SELECT * FROM mig_usuarios  " +
         "where id is not null  " +
         "and email is not null and email like '%@%' " +
         "and apellidos is not null " +
         "and nombres is not null " +
         //"and  id='0925789562asdsad'" +
         "order by id" + 
         " limit " + size + " offset " + page );

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
  public MigUsuario saveAndRetrieveIntance(MigUsuario migUsuario) throws Exception {
    return null;
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

  @Override
  public int count() throws SQLException {
    return 0;
  }

  public @Nullable
  MigUsuario getFromMatricula(String matricula) throws SQLException {

    PreparedStatement psfGetMigUsuario = null;

    String sql = "select * from mig_usuarios where id=?";

    psfGetMigUsuario = getConn().prepareStatement(sql);

    psfGetMigUsuario.setString(1, matricula);

    ResultSet rsGetMigUsuario = psfGetMigUsuario.executeQuery();


    if (rsGetMigUsuario.next()) {

      MigUsuario migUsuario = new MigUsuario(
        rsGetMigUsuario.getString("id"),
        rsGetMigUsuario.getString("nombres"),
        rsGetMigUsuario.getString("apellidos"),
        rsGetMigUsuario.getString("email"),
        rsGetMigUsuario.getString("username"));

      DbUtils.close(psfGetMigUsuario);
      DbUtils.close(rsGetMigUsuario);

      return migUsuario;

    }

    return null;
  }

  public Optional<MigUsuario> getFromNameAndLastName(String name) throws SQLException {
    PreparedStatement psfGetMigUsuario = null;

    String sql = "select * from mig_usuarios " +
      "where concat(nombres,' ',apellidos)=?";

    psfGetMigUsuario = getConn().prepareStatement(sql);

    psfGetMigUsuario.setString(1, name.toUpperCase());

    ResultSet rsGetMigUsuario = psfGetMigUsuario.executeQuery();

    if (rsGetMigUsuario.next()) {

      MigUsuario migUsuario = new MigUsuario(
        rsGetMigUsuario.getString("id"),
        rsGetMigUsuario.getString("nombres"),
        rsGetMigUsuario.getString("apellidos"),
        rsGetMigUsuario.getString("email"),
        rsGetMigUsuario.getString("username"));

      DbUtils.close(psfGetMigUsuario);
      DbUtils.close(rsGetMigUsuario);

      return Optional.of(migUsuario);

    }

    return Optional.empty();
  }
}
