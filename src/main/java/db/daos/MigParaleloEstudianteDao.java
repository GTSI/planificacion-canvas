package db.daos;

import db.models.MigParaleloEstudiante;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MigParaleloEstudianteDao extends AbstractDao implements Dao<MigParaleloEstudiante>{
  public MigParaleloEstudianteDao(Connection conn) {
    super(conn);
  }

  @Override
  public Optional<MigParaleloEstudiante> get(long id) throws SQLException {
    return Optional.empty();
  }

  @Override
  public List<MigParaleloEstudiante> getAll() throws SQLException {
    String sql = "SELECT * FROM mig_paralelo_estudiante;";

    Statement stmtGetMateriaEstudiantes = this.getConn().createStatement();
    ResultSet rsGetMateriaEstudiantes = stmtGetMateriaEstudiantes.executeQuery(sql);

    ArrayList<MigParaleloEstudiante> usuariosParalelos = new ArrayList<>();
    while (rsGetMateriaEstudiantes.next()) {
      usuariosParalelos.add(new MigParaleloEstudiante(
        rsGetMateriaEstudiantes.getInt("idmateria"),
        rsGetMateriaEstudiantes.getString("matricula")));
    }

    return usuariosParalelos;
  }

  @Override
  public long save(MigParaleloEstudiante migParaleloEstudiante) throws Exception {
    return 0;
  }

  @Override
  public MigParaleloEstudiante saveAndRetrieveIntance(MigParaleloEstudiante migParaleloEstudiante) throws Exception {
    return null;
  }

  @Override
  public List<Long> saveAll(List<MigParaleloEstudiante> t) {
    return null;
  }

  @Override
  public void update(MigParaleloEstudiante migParaleloEstudiante, String[] params) {

  }

  @Override
  public void delete(MigParaleloEstudiante migParaleloEstudiante) {

  }

  @Override
  public int count() throws SQLException {
    return 0;
  }

  public List<MigParaleloEstudiante> getUsersFromIDMateria(int idmateria) throws SQLException {
    Statement stmtGetMigParaleloProfesores = this.getConn().createStatement();
    ResultSet rsGetMigParaleloProfesores = stmtGetMigParaleloProfesores.executeQuery(
      "SELECT distinct * FROM mig_paralelo_estudiante  " +
        "where id is not null  " +
        "and matricula is not null and matricula='1310373566' and idmateria="+idmateria + " order by id ");

    ArrayList<MigParaleloEstudiante> usuarios = new ArrayList<>();

    while (rsGetMigParaleloProfesores.next()) {
      usuarios.add(new MigParaleloEstudiante(
        rsGetMigParaleloProfesores.getInt("idmateria"),
        rsGetMigParaleloProfesores.getString("matricula")
      ));
    }

    return usuarios;
  }
}
