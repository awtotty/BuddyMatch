import javafx.util.Pair;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Gui3 {

    static JTextArea log;
    static JLabel headerLabel;
    static JLabel statusLabel;
    static JPanel controlPanel;
    static JScrollPane logScrollPane;
    static List<StudentProfile> students;

    public Gui3() {
        createAndShowGUI();
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Buddy Match");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(800,300);
        frame.setLayout(new GridLayout(3, 1));

        //Create the log first, because the action listeners
        //need to refer to it.
        log = new JTextArea(15,50);
        log.setMargin(new Insets(5,5,5,5));
//        log.setEditable(false);
        logScrollPane = new JScrollPane(log);

        headerLabel = new JLabel("",JLabel.CENTER);
        statusLabel = new JLabel("",JLabel.CENTER);
//        statusLabel.setSize(350,100);
        headerLabel.setSize(800,50);

        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.setSize(new Dimension(20,20));

        //Add content to the window.
        frame.add(headerLabel);
        frame.add(controlPanel);
//        frame.add(statusLabel);
        frame.add(logScrollPane);

        JButton runButton = new JButton("RUN");
        runButton.setActionCommand("RUN");
        runButton.addActionListener(new ButtonClickListener());

        controlPanel.add(runButton);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    private class ButtonClickListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if( command.equals( "RUN" ) )  {
                // Find data
                headerLabel.setText("Retrieving data ...");
                makeTestData();

                // Run match
                headerLabel.setText("Running Match Algorithm ...");
                List<Pair<StudentProfile, StudentProfile>> matches = match(students);

                // Print matches and comp scores (rework to export to csv)
                headerLabel.setText("Match complete");
                for (Pair match : matches) {
                    StudentProfile s1 = (StudentProfile) match.getKey();
                    StudentProfile s2 = (StudentProfile) match.getValue();
//                    System.out.println(s1 + " matched with " + s2 + " \t(with compatibility score " + s1.getCompatibilityScore(s2) + ")");
                    log.append(s1 + " matched with " + s2 + " \t(with compatibility score " + s1.getCompatibilityScore(s2) + ")\n");
                }
            }

//            else if( command.equals( "Submit" ) )  {
//                statusLabel.setText("Submit Button clicked.");
//            } else {
//                statusLabel.setText("Cancel Button clicked.");
//            }
        }
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                new Gui3();
            }
        });
    }




    //
    // Non-view stuff. Move to new class in future.
    private static void makeTestData() {
        students = new ArrayList<StudentProfile>();

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
//            System.out.println(student + ": " + Arrays.toString(student.getScores()));
        }
//        System.out.println();
    }


    private static List<Pair<StudentProfile, StudentProfile>> match(List<StudentProfile> students) {
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
            matches.add(new Pair(student, bestMatch));
            students.remove(student);
            students.remove(bestMatch);
        }

        return matches;
    }


}
