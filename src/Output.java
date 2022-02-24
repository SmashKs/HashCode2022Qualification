import java.util.List;

public class Output {

    private final int numOfExecutedProjects; // the number of executed projects

    private final List<Project> projectList;

    public Output(int numOfExecutedProjects, List<Project> projectList) {
        this.numOfExecutedProjects = numOfExecutedProjects;
        this.projectList = projectList;
    }

    public int getNumOfExecutedProjects() {
        return numOfExecutedProjects;
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    static final class Project {
        private final String projectName; // name of the project

        private final List<String> contributorNames; // names of the contributors in order of roles

        public Project(String projectName, List<String> contributorNames) {
            this.projectName = projectName;
            this.contributorNames = contributorNames;
        }

        public String getProjectName() {
            return projectName;
        }

        public List<String> getContributorNames() {
            return contributorNames;
        }
    }
}
