import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreCalculator {

    public static long calculateScore(Input input, Output output) {

        long score = 0L;

        Map<String, Long> mapContributorNameToFirstFreeDay = new HashMap<>();

        for (Output.Project project : output.getProjectList()) {
            List<String> contributorNameList = project.getContributorNames();
            long firstWorkingDay = contributorNameList.stream()
                    .map(name -> mapContributorNameToFirstFreeDay.getOrDefault(name, 0L))
                    .max(Comparator.naturalOrder())
                    .get();

            Project projectDetails = input.getProjectList().stream()
                    .filter(p -> p.getName().equals(project.getProjectName()))
                    .findFirst().get();

            // For example:
            // First Working Day = Day 0
            // The num of days to complete = 2 days
            // Then the End Working Day is Day 1 and the next free day is Day 2
            long nextFreeDay = firstWorkingDay + projectDetails.getNumOfDaysToComplete();
            project.getContributorNames()
                    .forEach(contributorName -> mapContributorNameToFirstFreeDay.put(contributorName, nextFreeDay));

            score += calculateScoreForProject(projectDetails, nextFreeDay - 1);
        }

        return score;
    }

    static long calculateScoreForProject(Project projectDetails, long endWorkingDay) {
        if (endWorkingDay <= projectDetails.getBestBeforeDay()) {
            return projectDetails.getScoreForCompletion();
        }
        long daysLate = endWorkingDay - projectDetails.getBestBeforeDay() + 1;

        return Math.max(0L, projectDetails.getScoreForCompletion() - daysLate);
    }
}
