import java.io.PrintWriter;
import java.util.stream.Collectors;

public class OutputFileWriter {

    public static void writeToOutputFile(Output output, String fileName) {
        try {
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");

            writer.println(output.getNumOfExecutedProjects());
            for (int i = 0; i < output.getNumOfExecutedProjects(); i++) {
                Output.Project project = output.getProjectList().get(i);
                writer.println(project.getProjectName());
                writer.println(String.join(" ", project.getContributorNames()));
            }

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
