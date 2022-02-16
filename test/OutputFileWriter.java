import java.io.PrintWriter;
import java.util.stream.Collectors;

public class OutputFileWriter {

    public static void writeToOutputFile(Output output, String fileName) {
        try {
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");

            writer.println("Hello World");

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
