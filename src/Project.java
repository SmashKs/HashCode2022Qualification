import java.util.List;

public class Project {

    private final String name; // name of the project

    private final int numOfDaysToComplete; // 1 <= D <= 10^5, number of days to complete the project

    private final int scoreForCompletion; // 1 <= S <= 10^5, the score awarded for project completion

    private final int bestBeforeDay; // 1 <= B <= 10^5, the "best before" day for the project

    private final int numOfRoles; // 1 <= R <= 100, the number of roles in the project

    private final List<Skill> skillList;

    public Project(String name, int numOfDaysToComplete, int scoreForCompletion, int bestBeforeDay, int numOfRoles, List<Skill> skillList) {
        this.name = name;
        this.numOfDaysToComplete = numOfDaysToComplete;
        this.scoreForCompletion = scoreForCompletion;
        this.bestBeforeDay = bestBeforeDay;
        this.numOfRoles = numOfRoles;
        this.skillList = skillList;
    }

    public String getName() {
        return name;
    }

    public int getNumOfDaysToComplete() {
        return numOfDaysToComplete;
    }

    public int getScoreForCompletion() {
        return scoreForCompletion;
    }

    public int getBestBeforeDay() {
        return bestBeforeDay;
    }

    public int getNumOfRoles() {
        return numOfRoles;
    }

    public List<Skill> getSkillList() {
        return skillList;
    }

    static final class Skill {
        private final String name; // name of the skill

        private final int requiredLevel; // 1 <= L <= 100, the required skill level

        public Skill(String name, int requiredLevel) {
            this.name = name;
            this.requiredLevel = requiredLevel;
        }

        public String getName() {
            return name;
        }

        public int getRequiredLevel() {
            return requiredLevel;
        }
    }
}
