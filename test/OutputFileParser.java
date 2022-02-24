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

            int numOfExecutedProjects = Integer.parseInt(reader.readLine());

            List<Output.Project> projectList = new ArrayList<>();
            for (int i = 0; i < numOfExecutedProjects; i++) {
                String projectName = reader.readLine();
                String contributerNameListString = reader.readLine();
                List<String> contributerNameList = Arrays.asList(contributerNameListString.split(" "));
                projectList.add(
                        new Output.Project(projectName, contributerNameList)
                );
            }

            reader.close();

            return new Output(numOfExecutedProjects, projectList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
