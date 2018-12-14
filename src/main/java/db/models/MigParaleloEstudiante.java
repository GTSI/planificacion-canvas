package db.models;

import java.util.Objects;

public class MigParaleloEstudiante {
  private long idmateria;
  private String matricula;

  public long getIdmateria() {
    return idmateria;
  }

  public void setIdmateria(long idmateria) {
    this.idmateria = idmateria;
  }

  public String getMatricula() {
    return matricula;
  }

  public void setMatricula(String matricula) {
    this.matricula = matricula;
  }

  public MigParaleloEstudiante(long idmateria, String matricula) {
    this.idmateria = idmateria;
    this.matricula = matricula;

    cleanFields();
  }

  private void cleanFields() {
    this.matricula = Objects.requireNonNull(this.matricula).trim().replaceAll("\"", "");
  }
}
