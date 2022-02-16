import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class InputFileParser {

    public static Input parseInputFile(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            String firstLine = reader.readLine();

            reader.close();
            return new Input();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
