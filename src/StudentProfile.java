import java.util.ArrayList;
import java.util.List;

public class StudentProfile {

    private String name;
    private List<String> answers;

    public StudentProfile(String name) {
        this.name = name;
        this.answers = new ArrayList<>();
    }

    public StudentProfile(String name, List<String> answers) {
        this.name = name;
        this.answers = answers;
    }

    public int getCompatibilityScore(StudentProfile other) {
        int compScore = 0;
        List<String> otherAnswers = other.getAnswers();

//        if (answers.size() != otherScores.size())
//            throw new IllegalArgumentException("Score arrays size mismatch");

        for (int i = 0; i < answers.size(); i++)
            if ( otherAnswers.get(i).contains(answers.get(i)) )
                compScore++;

        return compScore;
    }

    public String getName() {
        return name;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void addAnswers(String answers) {
        this.answers.add(answers);
    }

    @Override
    public String toString() {
        return name;
    }
}
