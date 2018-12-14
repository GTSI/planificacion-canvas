package business_services.courses.data;

public final class MigParaleloProfesorData {
  public final int idmateria;
  public final int paralelo;
  public final String cedula;
  public final String nombre_materia;
  public final String codigo;

  public MigParaleloProfesorData(int idmateria, int paralelo, String cedula, String nombre_materia, String codigo) {
    this.idmateria = idmateria;
    this.paralelo = paralelo;
    assert cedula != null;
    this.cedula = cedula.trim().replaceAll("\"", "").toUpperCase();
    assert nombre_materia != null;
    this.nombre_materia = nombre_materia.trim().replaceAll("\"", "").toUpperCase();;
    assert codigo != null;
    this.codigo = codigo.trim().replaceAll("\"", "").toUpperCase();
  }

  @Override
  public String toString() {
    return "MigParaleloProfesorData " +
      "idmateria=" + idmateria +
      ", paralelo=" + paralelo +
      ", cedula='" + cedula + '\'' +
      ", nombre_materia='" + nombre_materia + '\'' +
      ", codigo='" + codigo + '\'';
  }
}
