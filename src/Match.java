import javafx.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Match {

    private static String csvFilePath = "/Users/atotty/Downloads/TestForm.csv";
    private static int nameIndex = 2; // column number that contains names/emails

    public static void main(String[] args) throws Exception {
        // Create student profiles
        List<StudentProfile> students = new ArrayList<StudentProfile>();

        // Read csv file
        Scanner scanner = new Scanner(new File(csvFilePath));
        CSVUtils.parseLine(scanner.nextLine()); // remove top line of headers
        while (scanner.hasNext()) {
            List<String> line = CSVUtils.parseLine(scanner.nextLine());
            students.add( new StudentProfile(line.get(nameIndex)) ); // Make new student with email as name
            for (int i = nameIndex+1; i < line.size(); i++) { // The rest of the entries in line are answers, so add them
                // Get student that was just added to students
                StudentProfile s = students.get(students.size()-1);
                String answers = line.get(i); // get all answers to question i-1 as one string

                // TODO Fixing error from CSV reader
                if (answers.charAt(0) == '"')
                    answers = answers.substring(1);

                // Split string on ", "
                s.addAnswers(Arrays.asList( answers.split(", ") ));
            }
        }
        scanner.close();


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
