package launchers;

import business_services.celexcsv.CelexCSVService;
import db.DBConnection;
import db.config.CelexConfig;
import helpers.CelexCSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

public class Celex {

    public static void main(String[] args) throws SQLException {

        DBConnection configDB = DBConnection.getInstance("development");
        CelexConfig celexConfig = CelexConfig.getInstance("development");

        CelexCSVService csvService = CelexCSVService.getInstance(configDB.getConnectionDestino(), celexConfig);
        //BufferedReader fileReader = CelexCSVReader.getFileReaderFromPath("C:/Users/ascacere/Downloads/celex.csv");
        BufferedReader fileReader = CelexCSVReader.getFileReaderFromPath("/home/sebas/Downloads/celex.csv");


        try {
            /* CSV FILE FORMAT:
            * coursename
            * paralelo
            * codigo
            * cedula1
            * cedula2
            * cedula3
            * */
            csvService.readCSVAndLoadCoursesWithData(fileReader);

        } catch (IOException e) { e.printStackTrace(); }
    }
}
