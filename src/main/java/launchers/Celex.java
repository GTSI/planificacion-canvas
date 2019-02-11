package launchers;

import db.config.CelexConfig;
import helpers.CelexCSVReader;

import java.io.BufferedReader;
import java.sql.SQLException;

public class Celex {

    public static void main(String[] args) throws SQLException {
        CelexConfig celexConfig = CelexConfig.getInstance("production");

        BufferedReader fileReader = CelexCSVReader.getFileReaderFromPath("C:/Users/ascacere/Downloads/celex.csv");

        CSVService csvService = UserCanvasService.getInstance(configDB.getConnectionDestino());
    }
}
