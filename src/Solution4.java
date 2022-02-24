import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Algorithm notes:
 * Solution 3 but simpler since e and f don't pass
 */
public class Solution4 implements Solution {

    @Override
    public Output getSolution(Input input) {
        List<Project> remainingProjectList = new ArrayList<>(input.getProjectList());
        Map<Contributor, Long> contributorToFreeDayMap = new HashMap<>();
        for (Contributor contributor : input.getContributorList()) {
            contributorToFreeDayMap.put(contributor, 0L);
        }

        List<Output.Project> outputProjectList = remainingProjectList.stream()
                .map(project -> getProjectInfo(project, contributorToFreeDayMap))
                .sorted(Comparator.comparing(HighestScoreProjectInfo::getScore).reversed())
                .filter(project -> project.getScore() != 0)
                .map(projectInfo -> new Output.Project(
                        projectInfo.getName(),
                        projectInfo.getContributorList().stream()
                                .map(Contributor::getName)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return new Output(
                outputProjectList.size(),
                outputProjectList
        );
    }

    HighestScoreProjectInfo getProjectWithHighestScore(List<Project> projectList, Map<Contributor, Long> contributorToFreeDayMap) {
        HighestScoreProjectInfo bestProject = null;
        long bestScore = -1L;

        for (Project project : projectList) {
            HighestScoreProjectInfo projectInfo = getProjectInfo(project, contributorToFreeDayMap);
            if (projectInfo.getScore() > bestScore) {
                bestProject = projectInfo;
                bestScore = projectInfo.getScore();
            }
        }
        return bestProject;
    }

    private HighestScoreProjectInfo getProjectInfo(Project project, Map<Contributor, Long> contributorToFreeDayMap) {
        List<Project.Skill> skillList = project.getSkillList();
        long startDay = 0L;

        List<Contributor> usedContributors = new ArrayList<>();
        Map<Contributor, Long> availableContributorMap = new HashMap<>(contributorToFreeDayMap);

        for (Project.Skill projectSkill : skillList) {
            Optional<Map.Entry<Contributor, Long>> freeContributor =
                    chooseContributor(availableContributorMap, projectSkill, usedContributors);

            if (freeContributor.isPresent()) {
                if (startDay < freeContributor.get().getValue()) {
                    startDay = freeContributor.get().getValue();
                }
                usedContributors.add(freeContributor.get().getKey());
                availableContributorMap.remove(freeContributor.get().getKey());
            } else {
                return new HighestScoreProjectInfo(0L);
            }
        }

        Map<Contributor, Long> updatedMap = new HashMap<>(contributorToFreeDayMap);
        long freeDay = startDay + project.getNumOfDaysToComplete();
        usedContributors.forEach(c -> updatedMap.put(c, freeDay));

        return new HighestScoreProjectInfo(
                project.getName(),
                getScore(project, startDay),
                startDay,
                usedContributors,
                updatedMap
        );
    }

    private Optional<Map.Entry<Contributor, Long>> chooseContributor(Map<Contributor, Long> availableContributorMap,
                                                                     Project.Skill projectSkill,
                                                                     List<Contributor> usedContributors) {
        return availableContributorMap.entrySet().stream()
                .filter(e -> e.getKey().getSkillList().stream()
                        .anyMatch(contributorSkill ->
                                contributorSkill.getName().equals(projectSkill.getName()) &&
                                        contributorSkill.getLevel() >= projectSkill.getRequiredLevel())
                )
                .filter(e -> !usedContributors.contains(e.getKey()))
                .min(Map.Entry.comparingByValue());
    }

    private long getScore(Project project, long startDay) {
        long endWorkingDay = startDay + project.getNumOfDaysToComplete() - 1;
        if (endWorkingDay <= project.getBestBeforeDay()) {
            return project.getScoreForCompletion();
        }
        long daysLate = endWorkingDay - project.getBestBeforeDay() + 1;

        return Math.max(0L, project.getScoreForCompletion() - daysLate);
    }

    static class HighestScoreProjectInfo {
        private final String name;
        private final long score;
        private final long startDay;
        private final List<Contributor> contributorList;
        private final Map<Contributor, Long> contributorToFreeDayMap;

        public HighestScoreProjectInfo(long score) {
            this(null, score, 0L, null, null);
        }

        public HighestScoreProjectInfo(String name, long score, long startDay, List<Contributor> contributorList,
                                       Map<Contributor, Long> contributorToFreeDayMap) {
            this.name = name;
            this.score = score;
            this.startDay = startDay;
            this.contributorList = contributorList;
            this.contributorToFreeDayMap = contributorToFreeDayMap;
        }

        public String getName() {
            return name;
        }

        public long getScore() {
            return score;
        }

        public long getStartDay() {
            return startDay;
        }

        public List<Contributor> getContributorList() {
            return contributorList;
        }

        public Map<Contributor, Long> getContributorToFreeDayMap() {
            return contributorToFreeDayMap;
        }
    }
}
