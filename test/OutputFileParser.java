import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OutputFileParser {

    public static Output parseOutputFile(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            String firstLine = reader.readLine();

            reader.close();

            return new Output();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
