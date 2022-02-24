import java.util.List;
import java.util.Optional;

public class Contributor {

    private final String name; // Contributor's name

    private final int numOfSkills; // 1 <= N <= 100, the number of skills the contributor has

    private final List<Skill> skillList;

    public Contributor(String name, int numOfSkills, List<Skill> skillList) {
        this.name = name;
        this.numOfSkills = numOfSkills;
        this.skillList = skillList;
    }

    public String getName() {
        return name;
    }

    public int getNumOfSkills() {
        return numOfSkills;
    }

    public List<Skill> getSkillList() {
        return skillList;
    }

    public void levelUp(Project.Skill skill) {
        Optional<Skill> levellingUpSkill = skillList.stream()
                .filter(sk -> sk.getName().equals(skill.getName()))
                .filter(sk -> sk.getLevel() == skill.getRequiredLevel())
                .findFirst();
        levellingUpSkill.ifPresent(Skill::levelUpSkill);
    }

    static final class Skill {
        private final String name; // name of the skill

        private int level; // 1 <= L <= 10, level of the skill

        public Skill(String name, int level) {
            this.name = name;
            this.level = level;
        }

        public String getName() {
            return name;
        }

        public int getLevel() {
            return level;
        }

        public void levelUpSkill() {
            this.level = level + 1;
        }
    }
}
