package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility;

import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class CSVFileWriter {

    public void writeDataToCSV(List<String[]> data, String filePath) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, true))) {
            writer.writeAll(data);
        } catch (IOException e) {
            e.printStackTrace();
            // Itt lehet kezelni az esetleges hibákat
        }
    }

}
