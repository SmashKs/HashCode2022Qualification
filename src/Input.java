import java.util.List;

public class Input {

    private final int numOfContributors; // 1 <= C <= 1-^5, number of contributors

    private final int numOfProjects; // 1 <= P <= 10^5, number of projects

    private final List<Contributor> contributorList;

    private final List<Project> projectList;

    public Input(int numOfContributors, int numOfProjects, List<Contributor> contributorList, List<Project> projectList) {
        this.numOfContributors = numOfContributors;
        this.numOfProjects = numOfProjects;
        this.contributorList = contributorList;
        this.projectList = projectList;
    }

    public int getNumOfContributors() {
        return numOfContributors;
    }

    public int getNumOfProjects() {
        return numOfProjects;
    }

    public List<Contributor> getContributorList() {
        return contributorList;
    }

    public List<Project> getProjectList() {
        return projectList;
    }
}