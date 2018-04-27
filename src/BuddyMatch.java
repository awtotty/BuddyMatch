
import javafx.util.Pair;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.*;

/*
 * TODO comments
 */
public class BuddyMatch extends JPanel
                        implements ActionListener {
    static private final String newline = "\n";
    JButton src1Button, src2Button, saveButton, runButton;
    JTextArea log;
    JFileChooser fc;
    private static String csvNewStudents;
    private static String csvBuddies;
    private static int nameIndex = 2; // column number that contains names/emails

    public BuddyMatch() {
        super(new BorderLayout());

        //Create the log first, because the action listeners
        //need to refer to it.
        log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        //Create a file chooser
        fc = new JFileChooser();

        //Create the open button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        src1Button = new JButton("Select new students");
        src1Button.addActionListener(this);

        //Create the open button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        src2Button = new JButton("Select eligible buddies");
        src2Button.addActionListener(this);

        //Create the save button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        saveButton = new JButton("Export results to...");
        saveButton.addActionListener(this);

        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(src1Button);
        buttonPanel.add(src2Button);
        buttonPanel.add(saveButton);

        // Run button
        JPanel runButtonPanel = new JPanel();
        runButton = new JButton("Run");
        runButton.addActionListener(this);
        runButtonPanel.add(runButton);

        //Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
        add(runButtonPanel, BorderLayout.AFTER_LAST_LINE);
        add(logScrollPane, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {

        // TODO Handle src1 button action.
        if (e.getSource() == src1Button) {
            int returnVal = fc.showOpenDialog(BuddyMatch.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.
                log.append("Opening: " + file.getName() + "." + newline);
            } else {
                log.append("Open command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());

            // TODO Handle src2 button action.
        } else if (e.getSource() == src2Button) {
            int returnVal = fc.showOpenDialog(BuddyMatch.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.
                log.append("Opening: " + file.getName() + "." + newline);
            } else {
                log.append("Open command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());

            // TODO Handle save button action.
        } else if (e.getSource() == saveButton) {
            int returnVal = fc.showSaveDialog(BuddyMatch.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would save the file.
                log.append("Saving: " + file.getName() + "." + newline);
            } else {
                log.append("Save command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());

            // Handle run button action TODO test
        } else if (e.getSource() == runButton) {
            if (csvNewStudents != null && csvBuddies != null) {
                log.append("Running match algorithm." + newline);

                ///////////////////////////////////////////////////////
                // run match
                try {
                    List<StudentProfile> newStudents = readCSV(csvNewStudents);
                    List<StudentProfile> buddies = readCSV(csvBuddies);

                    List<Pair<StudentProfile, StudentProfile>> matches = match(newStudents, buddies);

                    log.append("Match complete." + newline);

                    // Print matches and comp scores (rework to export to csv)
                    for (Pair match : matches) {
                        StudentProfile s1 = (StudentProfile) match.getKey();
                        StudentProfile s2 = (StudentProfile) match.getValue();
                        log.append(s1 + " matched with " + s2 + " \t(with compatibility score " + s1.getCompatibilityScore(s2) + ")");
                    }
                }
                catch (Exception o) {}
                // end match algo
                ///////////////////////////////////////////////////////

            } else {
                log.append("Please select all files." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
        }
    }

    /**
     * TODO comments
     */
    public static List<StudentProfile> readCSV(String filePath) throws Exception {
        List<StudentProfile> newStudents = new ArrayList<StudentProfile>();

        // Read new students csv file
        Scanner scanner = new Scanner(new File(filePath));


        // TODO find the name index in the file
        List<String> headers = CSVUtils.parseLine(scanner.nextLine()); // remove top line of headers
        // If any of the column headers is "Name", use that.
        // Else if any of teh column headers is "Email Address", use that.
        // Else throw an error about incorrect data formatting.


        while (scanner.hasNext()) {
            List<String> line = CSVUtils.parseLine(scanner.nextLine());
            newStudents.add( new StudentProfile(line.get(nameIndex)) ); // Make new student with email as name
            for (int i = nameIndex+1; i < line.size(); i++) { // The rest of the entries in line are answers, so add them
                // Get student that was just added to students
                StudentProfile s = newStudents.get(newStudents.size()-1);
                String answers = line.get(i); // get all answers to question i-1 as one string

                // TODO Fixing error from CSV reader
                if (answers.charAt(0) == '"')
                    answers = answers.substring(1);

                // Split string on ", "
                s.addAnswers(Arrays.asList( answers.split(", ") ));
            }
        }
        scanner.close();

        return newStudents;
    }

    /**
     * TODO comments
     */
    public static List<Pair<StudentProfile, StudentProfile>> match(List<StudentProfile> newStudents, List<StudentProfile> buddies) {
        List<Pair<StudentProfile, StudentProfile>> matches = new ArrayList<Pair<StudentProfile, StudentProfile>>(); // stores the pairs
        while (newStudents.size() > 0 && buddies.size() > 0) {
            // Find match for next student
            StudentProfile student = newStudents.get(0);
            int maxScore = Integer.MIN_VALUE;
            StudentProfile bestMatch = null;

            for (StudentProfile other : buddies) {
                if (student != other && student.getCompatibilityScore(other) < maxScore) {
                    maxScore = student.getCompatibilityScore(other);
                    bestMatch = other;
                }
            }

            // Add match to matches list and remove matched students from students list
            matches.add( new Pair(student, bestMatch) );
            newStudents.remove(student);
            buddies.remove(bestMatch);
        }
        return matches;
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("BuddyMatch");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new BuddyMatch());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }
}