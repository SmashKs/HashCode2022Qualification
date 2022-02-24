import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

/**
 * Algorithm notes:
 * - 1. sort the project by the project skill size
 * - 2. pick a project which can be finished
 */
public class Solution1 implements Solution {

    @Override
    public Output getSolution(Input input) {
        List<Output.Project> res = new ArrayList<>();

        List<Contributor> contributors = input.getContributorList();
        List<Project> projects = input.getProjectList();
        Map<String, Object[]> map = new HashMap<>();
        contributors.forEach(contributor -> {
            contributor.getSkillList().forEach(skill -> {
                if (!map.containsKey(skill.getName())) {
                    map.put(skill.getName(), new Object[] {
                        new PriorityQueue<Contributor>(Comparator.comparingInt(c -> c.getSkillMap()
                                                                                     .get(skill.getName())
                                                                                     .getLevel())), Integer.MIN_VALUE
                    });
                }
                // 0: heap, 1: max level of this skill
                Object[] arr = map.get(skill.getName());
                PriorityQueue<Contributor> heap = (PriorityQueue<Contributor>) arr[0];
                heap.add(contributor);
                int maxLevel = Math.max((int) arr[1], contributor.getSkillMap().get(skill.getName()).getLevel());
                map.put(skill.getName(), new Object[] {heap, maxLevel});
            });
        });

        projects.sort((p1, p2) -> p1.getSkillList().size() - p2.getSkillList().size());

        ArrayList<Project> tmp = new ArrayList<>(projects);
        while (!tmp.isEmpty()) {
            int start = tmp.size();
            // try to run a project
            for (Project project : tmp) {
                List<Contributor> willWorkOnProjectContributors = new ArrayList<>();
                // check can run the project
                boolean canRunProject = true;
                List<Project.Skill> requiredSkills = project.getSkillList();
                for (final Project.Skill requiredSkill : requiredSkills) {
                    // if not match all required level
                    Object[] obj = map.get(requiredSkill.getName());
                    if (requiredSkill.getRequiredLevel() > (int) obj[1]) {
                        canRunProject = false;
                        break;
                    }
                }
                if (!canRunProject) {
                    continue;
                }
                // can run
                for (final Project.Skill requiredSkill : requiredSkills) {
                    // if not match all required level
                    Object[] obj = map.get(requiredSkill.getName());
                    PriorityQueue<Contributor> heap = (PriorityQueue<Contributor>) obj[0];
                    int maxLevel = (int) obj[1];
                    List<Contributor> lowerLevelOfContributors = new ArrayList<>();
                    Contributor contributor = null;
                    while (!heap.isEmpty()) {
                        Contributor c = heap.remove();
                        if (c.getSkillMap().get(requiredSkill.getName()).getLevel() >=
                            requiredSkill.getRequiredLevel() && !willWorkOnProjectContributors.contains(c)) {
                            contributor = c;
                            break;
                        }
                        lowerLevelOfContributors.add(c);
                    }
                    heap.addAll(lowerLevelOfContributors);
                    if (contributor == null) {
                        canRunProject = false;
                        break;
                    }
                    // level up
                    if (requiredSkill.getRequiredLevel() >=
                        contributor.getSkillMap().get(requiredSkill.getName()).getLevel()) {
                        contributor.levelUp(contributor.getSkillMap().get(requiredSkill.getName()));
                        maxLevel = Math.max(maxLevel,
                                            contributor.getSkillMap().get(requiredSkill.getName()).getLevel()
                                           );
                    }
                    heap.add(contributor);
                    willWorkOnProjectContributors.add(contributor);
                    map.put(requiredSkill.getName(), new Object[] {heap, maxLevel});
                }
                if (!canRunProject) {
                    break;
                }
                List<String> contributorsName = willWorkOnProjectContributors.stream()
                                                                             .map(c -> c.getName())
                                                                             .collect(Collectors.toList());
                res.add(new Output.Project(project.getName(), contributorsName));
                tmp.remove(project);
                break;
            }
            // try to find a mentor
            for (Project project : tmp) {
                List<Contributor> willWorkOnProjectContributors = new ArrayList<>();
                List<Project.Skill> requiredSkills = project.getSkillList();
                Optional<Contributor> mentor = contributors.stream().filter(c -> {
                    boolean isMatched = true;
                    for (final Project.Skill requiredSkill : requiredSkills) {
                        // doesn't have the skill
                        if (!c.getSkillMap().containsKey(requiredSkill.getName())) {
                            isMatched = false;
                            break;
                        }
                        // one of the skill is not matched
                        if (c.getSkillMap().get(requiredSkill.getName()).getLevel() <
                            requiredSkill.getRequiredLevel()) {
                            isMatched = false;
                            break;
                        }
                    }
                    return isMatched;
                }).findFirst();
                // no mentor 😭
                if (mentor.isEmpty()) {
                    break;
                }
                willWorkOnProjectContributors.add(mentor.get());
                if (requiredSkills.size() - 1 != 0) {
                    // find mentored
                    for (final Project.Skill requiredSkill : requiredSkills) {
                        List<Contributor> lowerLevelOfContributors = new ArrayList<>();
                        Object[] obj = map.get(requiredSkill.getName());
                        PriorityQueue<Contributor> heap = (PriorityQueue<Contributor>) obj[0];
                        int maxLevel = (int) obj[1];
                        // find the same person
                        if (!heap.isEmpty() && willWorkOnProjectContributors.contains(heap.peek())) {
                            lowerLevelOfContributors.add(heap.remove());
                        }
                        if (heap.isEmpty()) {
                            heap.addAll(lowerLevelOfContributors);
                            continue;
                        }
                        Contributor mentored = heap.remove();
                        // level up
                        mentored.levelUp(mentored.getSkillMap().get(requiredSkill.getName()));
                        maxLevel = Math.max(maxLevel, mentored.getSkillMap().get(requiredSkill.getName()).getLevel());
                        heap.add(mentored);
                        willWorkOnProjectContributors.add(mentored);
                        map.put(requiredSkill.getName(), new Object[] {heap, maxLevel});
                    }
                }
                List<String> contributorsName = willWorkOnProjectContributors.stream()
                                                                             .map(c -> c.getName())
                                                                             .collect(Collectors.toList());
                res.add(new Output.Project(project.getName(), contributorsName));
                tmp.remove(project);
                break;
            }
            // can't find any matched people to run a project
            if (start == tmp.size()) {
                break;
            }
        }

        return new Output(res.size(), res);
    }
}
