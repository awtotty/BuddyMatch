import javafx.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Match {

    private static String csvFilePath = "/Users/mkyong/csv/country2.csv";
    private static int nameIndex = 1; // column number that contains names/emails

    public static void main(String[] args) throws Exception {
        // Create student profiles
        List<StudentProfile> students = new ArrayList<StudentProfile>();

        // Read csv file
        Scanner scanner = new Scanner(new File(csvFilePath));
        while (scanner.hasNext()) {
            List<String> line = CSVUtils.parseLine(scanner.nextLine());
            students.add( new StudentProfile(line.get(nameIndex)) ); // Make new student with email as name
            for (int i = nameIndex+1; i < line.size(); i++) { // The rest of the entries in line are answers, so add them
                // Get student that was just added to students
                StudentProfile s = students.get(students.size()-1);
                s.addAnswers(line.get(i));
            }
        }
        scanner.close();


        
//        students.add( new StudentProfile("Jim",       new ArrayList<>()) );
//        students.add( new StudentProfile("Jack",      new ArrayList<>()) );
//        students.add( new StudentProfile("Fred",      new ArrayList<>()) );
//        students.add( new StudentProfile("Frida",     new ArrayList<>()) );
//        students.add( new StudentProfile("Ally",      new ArrayList<>()) );
//        students.add( new StudentProfile("Augustus",  new ArrayList<>()) );
//        students.add( new StudentProfile("Nicole",    new ArrayList<>()) );
//        students.add( new StudentProfile("Nancy",     new ArrayList<>()) );
//
//        students.add( new StudentProfile("Catherine", new ArrayList<>()) );
//        students.add( new StudentProfile("Cameron",   new ArrayList<>()) );
//        students.add( new StudentProfile("Zoe",       new ArrayList<>()) );
//        students.add( new StudentProfile("Zoey",      new ArrayList<>()) );
//        students.add( new StudentProfile("Bill",      new ArrayList<>()) );
//        students.add( new StudentProfile("Beverly",   new ArrayList<>()) );
//        students.add( new StudentProfile("Maurice",   new ArrayList<>()) );
//        students.add( new StudentProfile("Matt",      new ArrayList<>()) );

        // Randomize arrays for testing
//        for (StudentProfile student : students) {
//            int[] randScores = new int[10];
//            for (int i = 0; i < randScores.length; i++)
//                randScores[i] = (int) ( Math.random()* 10 + 1 );
//            student.get(randScores);
//            System.out.println(student + ": " + Arrays.toString(student.getScores()));
//        }
//        System.out.println();


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
