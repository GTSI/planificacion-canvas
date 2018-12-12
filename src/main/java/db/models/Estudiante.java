package db.models;


import java.util.ArrayList;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

public class Estudiante {

  public Estudiante(String nombres, String apellidos, String cedula, String password, String usuario) {
    super();
    this.nombres = nombres;
    this.apellidos = apellidos;
    this.cedula = cedula;
    this.password = password;
    this.usuario = usuario;

    correos = new ArrayList<>();
  }

  @NotNull
  private String nombres;

  @NotNull
  private String apellidos;

  @Nullable
  private String usuario;

  private ArrayList<String> correos;

  @Nullable
  private String cedula;

  @Nullable
  private String password;


  public ArrayList<String> getCorreos() {
    return correos;
  }

  public void setCorreos(ArrayList<String> correos) {
    this.correos = correos;
  }

  public String getNombres() {
    return nombres;
  }

  public void setNombres(String nombres) {
    this.nombres = nombres;
  }

  public String getApellidos() {
    return apellidos;
  }

  public void setApellidos(String apellidos) {
    this.apellidos = apellidos;
  }

  public String getCedula() {
    return cedula;
  }

  public void setCedula(String cedula) {
    this.cedula = cedula;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsuario() {
    return usuario;
  }

  public void setUsuario(String usuario) {
    this.usuario = usuario;
  }

  public void cleanFields() {
    this.nombres = this.nombres.trim().replaceAll("\"", "").toUpperCase();
    this.apellidos = this.apellidos.trim().replaceAll("\"", "").toUpperCase();
    for (int i = 0; i < this.correos.size(); i++) {
      String correo = this.correos.get(i).trim().replaceAll("\"", "");
      this.correos.set(i, correo);
    }

    this.usuario = this.usuario.trim().replaceAll("\"", "");
    if (this.password != null)
      this.password = this.password.trim().replaceAll("\"", "");
    this.cedula = this.cedula.trim().replaceAll("\"", "");
  }

  @Override
  public String toString() {
    return "Estudiante [nombres=" + nombres + ", apellidos=" + apellidos + ", usuario=" + usuario + ", correos="
      + correos + ", cedula=" + cedula + ", password=" + password + "]";
  }


}