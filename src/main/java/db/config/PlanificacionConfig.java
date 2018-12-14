package db.config;


import java.util.Objects;

/**
 *
 * @author sebas
 */
public class PlanificacionConfig {

  private static PlanificacionConfig instance;
  private int origen;
  private int destino;

  private PlanificacionConfig(ConfigData config) {
    assert config != null;
    this.origen =  config.planificacionOrigen;
    this.destino =  config.planificacionDestino;
  }

  public int getOrigen() {
    return origen;
  }

  public int getDestino() {
    return destino;
  }

  public static PlanificacionConfig getInstance(String environment) {
    if (instance == null) {
      instance = new PlanificacionConfig(Objects.requireNonNull(ConfigData.getInstance(environment)));
    }

    return instance;
  }
}
