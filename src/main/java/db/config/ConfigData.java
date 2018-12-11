package db.config;

import java.util.Map;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

public class ConfigData {
    private ConfigData(@NotNull String environment) {
        Map configFile = YAMLReader.readConfigFile();

        Map destinoConfig = (Map) ((Map) configFile.get("destino")).get(environment);
        Map origenConfig = (Map) ((Map) configFile.get("origen")).get(environment);

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

        if (environment == "production") {
            sshConnection = (Map) destinoConfig.get("ssh");
        }


    }


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
