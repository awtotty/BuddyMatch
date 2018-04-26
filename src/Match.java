import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Match {

    public static void main(String[] args) {
        // Create student profiles for testing
        List<StudentProfile> students = new ArrayList<StudentProfile>();

        students.add( new StudentProfile("Jim",       new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}) );
        students.add( new StudentProfile("Jack",      new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}) );
        students.add( new StudentProfile("Fred",      new int[]{5, 1, 10, 10, 10, 4, 5, 2, 2, 1}) );
        students.add( new StudentProfile("Frida",     new int[]{4, 0, 9, 9, 10, 3, 5, 2, 1, 2}) );
        students.add( new StudentProfile("Ally",      new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}) );
        students.add( new StudentProfile("Augustus",  new int[]{1, 1, 0, 1, 2, 0, 0, 2, 1, 0}) );
        students.add( new StudentProfile("Nicole",    new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}) );
        students.add( new StudentProfile("Nancy",     new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}) );

        students.add( new StudentProfile("Catherine", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}) );
        students.add( new StudentProfile("Cameron",   new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}) );
        students.add( new StudentProfile("Zoe",       new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}) );
        students.add( new StudentProfile("Zoey",      new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}) );
        students.add( new StudentProfile("Bill",      new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}) );
        students.add( new StudentProfile("Beverly",   new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}) );
        students.add( new StudentProfile("Maurice",   new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}) );
        students.add( new StudentProfile("Matt",      new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}) );

        // Randomize arrays for testing
        for (StudentProfile student : students) {
            int[] randScores = new int[10];
            for (int i = 0; i < randScores.length; i++)
                randScores[i] = (int) ( Math.random()* 10 + 1 );
            student.setScores(randScores);
            System.out.println(student + ": " + Arrays.toString(student.getScores()));
        }
        System.out.println();


        // Match algo
        List<Pair<StudentProfile, StudentProfile>> matches = new ArrayList<Pair<StudentProfile, StudentProfile>>(); // stores the pairs
        while (students.size() > 1) {
            // Find match for next student
            StudentProfile student = students.get(0);
            int minScore = Integer.MAX_VALUE;
            StudentProfile bestMatch = null;

            for (StudentProfile other : students) {
                if (student != other && student.getCompatibilityScore(other) < minScore) {
                    minScore = student.getCompatibilityScore(other);
                    bestMatch = other;
                }
            }

            // Add match to matches list and remove matched students from students list
            matches.add( new Pair(student, bestMatch) );
            students.remove(student);
            students.remove(bestMatch);
        }

        // Print matches and comp scores (rework to export to csv)
        for (Pair match : matches) {
            StudentProfile s1 = (StudentProfile) match.getKey();
            StudentProfile s2 = (StudentProfile) match.getValue();
            System.out.println(s1 + " matched with " + s2 + " \t(with compatibility score " + s1.getCompatibilityScore(s2) + ")");
        }

        // Notify of unmatched students
        if ( students.size() > 0 ) {
            System.out.println("*~*~*~*~*~*~*~*~*~*~*~*~*~");
            System.out.println("The following students did not find a match: ");
            for (StudentProfile student : students)
                System.out.println(student);

        }

    }
}
