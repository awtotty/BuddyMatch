import java.util.ArrayList;
import java.util.List;

public class StudentProfile {

    private String name;
    private List<List<String>> answers;

    public StudentProfile(String name) {
        this.name = name;
        this.answers = new ArrayList<>();
    }

    public StudentProfile(String name, List<List<String>> answers) {
        this.name = name;
        this.answers = answers;
    }

    public int getCompatibilityScore(StudentProfile other) {
        int compScore = 0;
        List<List<String>> otherAnswers = other.getAnswers();

        if (answers.size() != otherAnswers.size())
            throw new IllegalArgumentException("Number of questions mismatch");

        for (int i = 0; i < answers.size(); i++) // question number
            for (int j = 0; j < answers.get(i).size(); j++) // each answer to question i
                if ( otherAnswers.get(i).contains(answers.get(i).get(j)) )
                    compScore++;

        return compScore;
    }

    public String getName() {
        return name;
    }

    public List<List<String>> getAnswers() {
        return answers;
    }

    public void addAnswers(List<String> answers) {
        this.answers.add(answers);
    }

    @Override
    public String toString() {
        return name;
    }
}
