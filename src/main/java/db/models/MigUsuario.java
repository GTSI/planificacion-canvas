package db.models;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

public class MigUsuario {

  public MigUsuario(String id, String nombres, String apellidos, String email, String username) {
    super();
    this.nombres = nombres;
    this.apellidos = apellidos;
    this.id = id;
    this.email = email;
    this.username = username;

    this.cleanFields();
  }

  private void cleanFields() {

    if(this.nombres != null) {
      this.nombres = this.nombres.trim().replaceAll("\"", "").toUpperCase();
    }

    if(this.apellidos != null) {
      this.apellidos = this.apellidos.trim().replaceAll("\"", "").toUpperCase();
    }
    if(this.email != null) {
      this.email = this.email.trim().replaceAll("\"", "").toLowerCase();
    }

    if(this.id != null) {
      this.id = this.id.trim().replaceAll("\"", "").toLowerCase();
    }

    if(this.username != null) {
      this.username = this.username.trim().replaceAll("\"", "").toLowerCase();
    }
  }

  @NotNull
  private String nombres;

  @NotNull
  private String apellidos;

  @Nullable
  private String username;

  @Nullable
  private String id;

  @Nullable
  private String email;

  @Override
  public String toString() {
    return "MigUsuario " +
      "nombres='" + nombres + '\'' +
      ", apellidos='" + apellidos + '\'' +
      ", username='" + username + '\'' +
      ", id='" + id + '\'' +
      ", email='" + email + '\'';
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

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
