package db.daos;

import db.models.MigParaleloProfesor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MigParaleloProfesorDao extends AbstractDao implements Dao<MigParaleloProfesor>{
  public MigParaleloProfesorDao(Connection conn) {
    super(conn);
  }

  @Override
  public Optional<MigParaleloProfesor> get(long id) throws SQLException {
    return Optional.empty();
  }

  @Override
  public List<MigParaleloProfesor> getAll() throws SQLException {
    Statement stmtGetMigParaleloProfesores = this.getConn().createStatement();
    ResultSet rsGetMigParaleloProfesores = stmtGetMigParaleloProfesores.executeQuery(
      "SELECT * FROM mig_paralelo_profesor  " +
        "where id is not null  " +
        "and cedula is not null and cedula not like 'FI%' order by id;");

    ArrayList<MigParaleloProfesor> usuarios = new ArrayList<>();

    while (rsGetMigParaleloProfesores.next()) {
      usuarios.add(new MigParaleloProfesor(
        rsGetMigParaleloProfesores.getString("id"),
        rsGetMigParaleloProfesores.getString("cedula"),
        rsGetMigParaleloProfesores.getInt("idmateria"),
        rsGetMigParaleloProfesores.getString("materia")
        ));
    }

    return usuarios;
  }

  @Override
  public long save(MigParaleloProfesor migParaleloProfesor) throws Exception {
    return 0;
  }

  @Override
  public MigParaleloProfesor saveAndRetrieveIntance(MigParaleloProfesor migParaleloProfesor) throws Exception {
    return null;
  }

  @Override
  public List<Long> saveAll(List<MigParaleloProfesor> t) {
    return null;
  }

  @Override
  public void update(MigParaleloProfesor migParaleloProfesor, String[] params) {

  }

  @Override
  public void delete(MigParaleloProfesor migParaleloProfesor) {

  }

  @Override
  public int count() throws SQLException {
    return 0;
  }

  public List<MigParaleloProfesor> getUsersFromIDMateria(int idmateria) throws SQLException {
    Statement stmtGetMigParaleloProfesores = this.getConn().createStatement();
    ResultSet rsGetMigParaleloProfesores = stmtGetMigParaleloProfesores.executeQuery(
      "SELECT distinct * FROM mig_paralelo_profesor  " +
        "where id is not null  " +
        "and cedula is not null and cedula not like 'FI%' and idmateria="+idmateria + " order by id ");

    ArrayList<MigParaleloProfesor> usuarios = new ArrayList<>();

    while (rsGetMigParaleloProfesores.next()) {
      usuarios.add(new MigParaleloProfesor(
        rsGetMigParaleloProfesores.getString("id"),
        rsGetMigParaleloProfesores.getString("cedula"),
        rsGetMigParaleloProfesores.getInt("idmateria"),
        rsGetMigParaleloProfesores.getString("materia")
      ));
    }

    return usuarios;
  }
}
