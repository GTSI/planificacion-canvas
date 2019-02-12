package db.models;

import org.springframework.lang.Nullable;

public class MigMateriaParalelo {

  private int paralelo;

  @Nullable
  private String codigos_carreras;

  private String codigo_materia;

  private String nombre;

  @Nullable
  private int paralelo_ktl;

  private int idmateria;

  public MigMateriaParalelo(int paralelo, String codigos_carreras, String codigo_materia, String nombre, int paralelo_ktl, int idmateria) {
    this.paralelo = paralelo;
    this.codigos_carreras = codigos_carreras;
    this.codigo_materia = codigo_materia;
    this.paralelo_ktl = paralelo_ktl;
    this.idmateria = idmateria;
    this.nombre = nombre;

    this.cleanFields();
  }

  public int getParalelo() {
    return paralelo;
  }

  public void setParalelo(int paralelo) {
    this.paralelo = paralelo;
  }

  public String getCodigos_carreras() {
    return codigos_carreras;
  }

  public void setCodigos_carreras(String codigos_carreras) {
    this.codigos_carreras = codigos_carreras;
  }

  public int getParalelo_ktl() {
    return paralelo_ktl;
  }

  public void setParalelo_ktl(int paralelo_ktl) {
    this.paralelo_ktl = paralelo_ktl;
  }

  public int getIdmateria() {
    return idmateria;
  }

  public void setIdmateria(int idmateria) {
    this.idmateria = idmateria;
  }

  public String getCodigo_materia() {
    return codigo_materia;
  }

  public void setCodigo_materia(String codigo_materia) {
    this.codigo_materia = codigo_materia;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public void cleanFields() {
    this.codigo_materia = this.codigo_materia != null ? this.codigo_materia.trim().replaceAll("\"", "").toUpperCase() : null;
    this.nombre = this.nombre != null ? this.nombre.trim().replaceAll("\"", "").toUpperCase() : null;
    this.codigos_carreras = this.codigos_carreras != null ? this.codigos_carreras.trim().replaceAll("\"", "").toUpperCase() : null;
  }
}
