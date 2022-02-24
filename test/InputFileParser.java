import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class InputFileParser {

    public static Input parseInputFile(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            String[] firstLine = reader.readLine().split(" ");
            int numOfContributors = Integer.parseInt(firstLine[0]), numOfProjects = Integer.parseInt(firstLine[1]);
            List<Contributor> contributors = new ArrayList<>();
            List<Project> projects = new ArrayList<>();
            // read contributors
            for (int i = 0 ; i < numOfContributors ; i++) {
                String[] second = reader.readLine().split(" ");
                String name = second[0];
                int numOfSkills = Integer.parseInt(second[1]);
                List<Contributor.Skill> skills = new ArrayList<>();
                for (int j = 0 ; j < numOfSkills ; j++) {
                    String[] third = reader.readLine().split(" ");
                    skills.add(new Contributor.Skill(third[0], Integer.parseInt(third[1])));
                }
                contributors.add(new Contributor(name, numOfSkills, skills));
            }
            // read projects
            for (int i = 0 ; i < numOfProjects ; i++) {
                String[] second = reader.readLine().split(" ");
                String projectName = second[0];
                int days = Integer.parseInt(second[1]), score = Integer.parseInt(second[2]);
                int requiredDays = Integer.parseInt(second[3]), requiredPeople = Integer.parseInt(second[4]);
                List<Project.Skill> skills = new ArrayList<>();
                for (int j = 0 ; j < requiredPeople ; j++) {
                    String[] third = reader.readLine().split(" ");
                    skills.add(new Project.Skill(third[0], Integer.parseInt(third[1])));
                }
                projects.add(new Project(projectName, days, score, requiredDays, requiredPeople, skills));
            }

            reader.close();
            return new Input(numOfContributors, numOfProjects, contributors, projects);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
