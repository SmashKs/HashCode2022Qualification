import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Algorithm notes:
 * 1. Sort projects by highest possible score
 *   a) Each role is assigned to the first remaining contributor with the required skill and skill level >= required level
 */
public class Solution2 implements Solution {

    @Override
    public Output getSolution(Input input) {
        List<Project> remainingProjectList = new ArrayList<>(input.getProjectList());
        Map<Contributor, Long> contributorToFreeDayMap = new HashMap<>();
        for (Contributor contributor : input.getContributorList()) {
            contributorToFreeDayMap.put(contributor, 0L);
        }

        List<Output.Project> outputProjectList = new ArrayList<>();

        while(!remainingProjectList.isEmpty()) {
            Project highestScoreProject = getProjectWithHighestScore(remainingProjectList, contributorToFreeDayMap);

            // System.out.println("Current highest score project is " + highestScoreProject.getName());

            List<Long> scoreAndStartDay = calculateProjectScore(highestScoreProject, contributorToFreeDayMap);
            if (scoreAndStartDay.get(0) == 0) {
                break;
            }
            Long startDay = scoreAndStartDay.get(1);

            List<Contributor> contributorList = getContributorList(highestScoreProject, contributorToFreeDayMap);

            contributorToFreeDayMap = updateContributorMap(highestScoreProject, contributorToFreeDayMap, startDay);

            outputProjectList.add(new Output.Project(
                    highestScoreProject.getName(),
                    contributorList.stream().map(Contributor::getName).collect(Collectors.toList())
            ));

            remainingProjectList.remove(highestScoreProject);
        }

        return new Output(
                outputProjectList.size(),
                outputProjectList
        );
    }

    Project getProjectWithHighestScore(List<Project> projectList, Map<Contributor, Long> contributorToFreeDayMap) {
        Project bestProject = projectList.get(0);
        long bestScore = -1L;

        for (Project project : projectList) {
            List<Long> scoreAndStartDay = calculateProjectScore(project, contributorToFreeDayMap);
            if (scoreAndStartDay.get(0) > bestScore) {
                bestProject = project;
                bestScore = scoreAndStartDay.get(0);
            }
        }
        return bestProject;
    }

    // Returns list of [project score, project start day]
    private List<Long> calculateProjectScore(Project project, Map<Contributor, Long> contributorToFreeDayMap) {
        List<Project.Skill> skillList = project.getSkillList();
        long startDay = 0L;

        Map<Contributor, Long> availableContributorMap = new HashMap<>(contributorToFreeDayMap);

        for (Project.Skill projectSkill : skillList) {
            Optional<Map.Entry<Contributor, Long>> freeContributor = availableContributorMap.entrySet().stream()
                    .filter(e -> e.getKey().getSkillList().stream()
                            .anyMatch(contributorSkill ->
                                    contributorSkill.getName().equals(projectSkill.getName()) &&
                                            contributorSkill.getLevel() >= projectSkill.getRequiredLevel())
                    )
                    .min(Map.Entry.comparingByValue());
            if (freeContributor.isPresent()) {
                if (startDay < freeContributor.get().getValue()) {
                    startDay = freeContributor.get().getValue();
                }
                availableContributorMap.remove(freeContributor.get().getKey());
            } else {
                return Collections.singletonList(0L);
            }
        }

        long endWorkingDay = startDay + project.getNumOfDaysToComplete() - 1;
        if (endWorkingDay <= project.getBestBeforeDay()) {
            // System.out.println("project " + project.getName() + " with score " + project.getScoreForCompletion());
            return Arrays.asList((long) project.getScoreForCompletion(), startDay);
        }
        long daysLate = endWorkingDay - project.getBestBeforeDay() + 1;

        // System.out.println("project " + project.getName() + " with score " + Math.max(0L, project.getScoreForCompletion() - daysLate));

        return Arrays.asList(
                Math.max(0L, project.getScoreForCompletion() - daysLate),
                startDay
        );
    }

    private Map<Contributor, Long> updateContributorMap(Project project, Map<Contributor, Long> contributorToFreeDayMap, Long startDay) {
        List<Project.Skill> skillList = project.getSkillList();

        Set<Contributor> alreadyAssignedContributorList = new HashSet<>();

        Map<Contributor, Long> availableContributorMap = new HashMap<>(contributorToFreeDayMap);

        long freeDay = startDay + project.getNumOfDaysToComplete();

        for (Project.Skill projectSkill : skillList) {
            Map.Entry<Contributor, Long> freeContributor = availableContributorMap.entrySet().stream()
                    .filter(e -> e.getKey().getSkillList().stream()
                            .anyMatch(contributorSkill ->
                                    contributorSkill.getName().equals(projectSkill.getName()) &&
                                            contributorSkill.getLevel() >= projectSkill.getRequiredLevel())
                    )
                    .filter(e -> !alreadyAssignedContributorList.contains(e.getKey()))
                    .min(Map.Entry.comparingByValue()).get();
            alreadyAssignedContributorList.add(freeContributor.getKey());
            availableContributorMap.put(freeContributor.getKey(), freeDay);
        }
        return availableContributorMap;
    }

    private List<Contributor> getContributorList(Project project, Map<Contributor, Long> contributorToFreeDayMap) {
        List<Project.Skill> skillList = project.getSkillList();

        List<Contributor> alreadyAssignedContributorList = new ArrayList<>();

        for (Project.Skill projectSkill : skillList) {
            Map.Entry<Contributor, Long> freeContributor = contributorToFreeDayMap.entrySet().stream()
                    .filter(e -> e.getKey().getSkillList().stream()
                            .anyMatch(contributorSkill ->
                                    contributorSkill.getName().equals(projectSkill.getName()) &&
                                            contributorSkill.getLevel() >= projectSkill.getRequiredLevel())
                    )
                    .filter(e -> !alreadyAssignedContributorList.contains(e.getKey()))
                    .min(Map.Entry.comparingByValue()).get();
            alreadyAssignedContributorList.add(freeContributor.getKey());
        }
        return alreadyAssignedContributorList;
    }
}
