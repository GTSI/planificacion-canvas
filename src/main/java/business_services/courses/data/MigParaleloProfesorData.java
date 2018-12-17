package business_services.courses.data;

import java.sql.Timestamp;

public final class MigParaleloProfesorData {
  public final int idmateria;
  public final int paralelo;
  public final String cedula;
  public final String nombre_materia;
  public final String codigo;
  public final Timestamp start_at;
  public final Timestamp end_at;

  public MigParaleloProfesorData(
    int idmateria,
    int paralelo,
    String cedula,
    String nombre_materia,
    String codigo,
    Timestamp start_at,
    Timestamp end_at) {
    this.idmateria = idmateria;
    this.paralelo = paralelo;
    assert cedula != null;
    this.cedula = cedula.trim().replaceAll("\"", "").toUpperCase();
    assert nombre_materia != null;
    this.nombre_materia = nombre_materia.trim().replaceAll("\"", "").toUpperCase();;
    assert codigo != null;
    this.codigo = codigo.trim().replaceAll("\"", "").toUpperCase();
    this.start_at = start_at;
    this.end_at = end_at;
  }

  @Override
  public String toString() {
    return "MigParaleloProfesorData " +
      "idmateria=" + idmateria +
      ", paralelo=" + paralelo +
      ", cedula='" + cedula + '\'' +
      ", nombre_materia='" + nombre_materia + '\'' +
      ", codigo='" + codigo + '\'' +
      ", start_at='" + start_at+ '\'' +
    ", end_at ='" + end_at + '\'';
  }
}
