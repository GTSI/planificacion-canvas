package db.models;



public class MigParaleloProfesor {

  private String id;

  private String cedula;

  private int idmateria;

  private String materia;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCedula() {
    return cedula;
  }

  public void setCedula(String cedula) {
    this.cedula = cedula;
  }

  public int getIdmateria() {
    return idmateria;
  }

  public void setIdmateria(int idmateria) {
    this.idmateria = idmateria;
  }

  public MigParaleloProfesor(String id, String cedula, int idmateria, String materia) {
    this.id = id;
    this.cedula = cedula;
    this.idmateria = idmateria;
    this.materia = materia;

    cleanFields();
  }

  public String getMateria() {
    return materia;
  }

  public void setMateria(String materia) {
    this.materia = materia;
  }

  private void cleanFields() {
    this.materia = this.materia!= null ? this.materia.trim().replaceAll("\"", "").toUpperCase() : null;
    this.cedula = this.cedula!= null ? this.cedula.trim().replaceAll("\"", "").toUpperCase() : null;
  }

  @Override
  public String toString() {
    return "MigParaleloProfesor " +
      "id='" + id + '\'' +
      ", cedula='" + cedula + '\'' +
      ", idmateria=" + idmateria +
      ", materia='" + materia + '\'';
  }
}
