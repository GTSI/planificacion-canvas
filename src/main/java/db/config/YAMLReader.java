package db.config;

import com.sun.istack.internal.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class YAMLReader {

    public static @Nullable
    Map readConfigFile(String filename) {
        try {
            Yaml configYAML = new Yaml();
            InputStream inputStream = YAMLReader.class.getClassLoader().getResourceAsStream(filename);
            Map <String, Object> dataLoaded = (Map<String, Object>) configYAML.load(inputStream);

            System.out.println("Archivo de configuracion cargado satisfactoriamente" );
            return dataLoaded;

        } catch(Exception e) {
            System.err.println("No se pudo leer el archivo de configuracion" );
            e.printStackTrace();

            return null;
        }
    }
}
