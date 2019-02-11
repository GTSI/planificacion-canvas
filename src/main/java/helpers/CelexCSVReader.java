package helpers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class CelexCSVReader {
    public static BufferedReader getFileReaderFromPath(String path) {
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";
        try {
            br = new BufferedReader(new FileReader(path));
            return br;
        } catch(FileNotFoundException e) { e.printStackTrace(); return null;}

    }
}
