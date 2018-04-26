public class StudentProfile {

    private String name;
    private int[] scores;

    public StudentProfile(String name, int[] scores) {
        this.name = name;
        this.scores = scores;
    }

    public int getCompatibilityScore(StudentProfile other) {
        int compScore = 0;
        int[] otherScores = other.getScores();

        if (scores.length != otherScores.length)
            throw new IllegalArgumentException("Score arrays size mismatch");

        for (int i = 0; i < scores.length; i++)
            compScore += Math.abs(scores[i] - otherScores[i]);

        return compScore;
    }

    public String getName() {
        return name;
    }

    public int[] getScores() {
        return scores;
    }

    public void setScores(int[] scores) {
        this.scores = scores;
    }

    @Override
    public String toString() {
        return name;
    }
}
