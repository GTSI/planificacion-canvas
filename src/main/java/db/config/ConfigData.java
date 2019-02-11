package db.config;

import java.util.Map;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

public class ConfigData {
  private ConfigData(@NotNull String environment) {
    Map configDB = YAMLReader.readConfigFile("database.yml");
    Map configPlanificacion = YAMLReader.readConfigFile("configPlanificacion.yaml");
    Map celexConfig = YAMLReader.readConfigFile("celex.yaml");

    this.planificacionDestino = (int)((Map)configPlanificacion.get("planificacion")).get("termino_destino");
    this.planificacionOrigen = (int)((Map)configPlanificacion.get("planificacion")).get("termino_origen");
    this.celexDestino = (int)((Map)celexConfig.get("celex")).get("destino");

    Map destinoConfig = (Map) ((Map) configDB.get("destino")).get(environment);
    Map origenConfig = (Map) ((Map) configDB.get("origen")).get(environment);

    this.strConnDestino = "jdbc:postgresql://"
      + destinoConfig.get("host")
      + ":5432/"
      + destinoConfig.get("database") + "?" + "user="
      + destinoConfig.get("username") + "&password="
      + destinoConfig.get("password");

    this.strConnOrigen = "jdbc:postgresql://"
      + origenConfig.get("host")
      + ":5432/"
      + origenConfig.get("database") + "?" + "user="
      + origenConfig.get("username") + "&password="
      + origenConfig.get("password");

    if (environment.equals("production")) {
      sshConnection = (Map) destinoConfig.get("ssh");
    }


  }

  public int planificacionOrigen = -1;
  public int planificacionDestino = -1;
  public int celexDestino = -1;
  public String strConnDestino = null;
  public String strConnOrigen = null;
  public Map sshConnection = null;
  public static ConfigData singleton = null;

  // environment can be 'development' or 'production'
  public static synchronized ConfigData getInstance(@Nullable String environment) {
    if (singleton == null) {
      singleton = new ConfigData(environment);
    }

    return singleton;
  }

}
