package db.daos;

import business_services.courses.data.MigParaleloProfesorData;
import db.models.MigMateriaParalelo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MigMateriaParaleloDao extends AbstractDao implements Dao<MigMateriaParalelo> {

  public MigMateriaParaleloDao(Connection conn) {
    super(conn);
  }

  @Override
  public Optional<MigMateriaParalelo> get(long id) throws SQLException {
    return Optional.empty();
  }

  @Override
  public List<MigMateriaParalelo> getAll() throws SQLException {
    return null;
  }

  @Override
  public long save(MigMateriaParalelo migMateriaParalelo) throws Exception {
    return 0;
  }

  @Override
  public MigMateriaParalelo saveAndRetrieveIntance(MigMateriaParalelo migMateriaParalelo) throws Exception {
    return null;
  }

  @Override
  public List<Long> saveAll(List<MigMateriaParalelo> t) {
    return null;
  }

  @Override
  public void update(MigMateriaParalelo migMateriaParalelo, String[] params) {

  }

  @Override
  public void delete(MigMateriaParalelo migMateriaParalelo) {

  }

  @Override
  public int count() throws SQLException {
    return 0;
  }

  /* Obtenemos informacion de los paralelos planificados para este termino*/
  public List<MigParaleloProfesorData> getCourseAndTeachersFromMigs() throws SQLException {
    String sql = "SELECT distinct pp.idmateria, pp.paralelo,  pp.cedula, mp.nombre, mp.materia, mp.start_at, mp.end_at FROM mig_paralelo_profesor pp " +
      "INNER JOIN mig_materia_paralelo mp ON  upper(pp.materia)=upper(mp.materia) where mp.start_at>'2018-12-28' ORDER BY mp.nombre";

    Statement stmtGetParalelosPlanificados = this.getConn().createStatement();
    ResultSet rsGetParalelosPlanificados = stmtGetParalelosPlanificados.executeQuery(sql);

    ArrayList<MigParaleloProfesorData> paralelosPlanificados = new ArrayList<>();
    while (rsGetParalelosPlanificados.next()) {
      paralelosPlanificados.add(new MigParaleloProfesorData(
        rsGetParalelosPlanificados.getInt("idmateria"),
        rsGetParalelosPlanificados.getInt("paralelo"),
        Objects.requireNonNull(rsGetParalelosPlanificados.getString("cedula")),
        Objects.requireNonNull(rsGetParalelosPlanificados.getString("nombre")),
        Objects.requireNonNull(rsGetParalelosPlanificados.getString("materia")),
        Objects.requireNonNull(rsGetParalelosPlanificados.getTimestamp("start_at")),
        Objects.requireNonNull(rsGetParalelosPlanificados.getTimestamp("end_at"))));
    }

    return paralelosPlanificados;
  }


  public int countMaestrias() throws SQLException {
    String sql = "SELECT count(distinct pp.idmateria) FROM mig_paralelo_profesor pp " +
      "INNER JOIN mig_materia_paralelo mp ON mp.idmateria = pp.idmateria " +
      "AND mp.paralelo = pp.paralelo";

    Statement stmtCountParalelos = this.getConn().createStatement();
    ResultSet rsCountParalelos = stmtCountParalelos.executeQuery(sql);


    if (rsCountParalelos.next()) {
      return rsCountParalelos.getInt(1);
    } else throw new SQLException("Error countando cursos w t f!");
  }

}
