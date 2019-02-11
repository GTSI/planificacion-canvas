package db.config;

import java.util.Objects;

/**
 *
 * @author sebas
 */

public class CelexConfig {

    private static CelexConfig instance;
    private int destino;

    private CelexConfig(ConfigData config) {
        assert config != null;
        this.destino =  config.celexDestino;
    }

    public int getDestino() {
        return destino;
    }

    public static CelexConfig getInstance(String environment) {
        if (instance == null) {
            instance = new CelexConfig(Objects.requireNonNull(ConfigData.getInstance(environment)));
        }

        return instance;
    }
}
