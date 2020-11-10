import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.mxgraph.swing.mxGraphComponent;

public class Gui {
    // static int[] keys = {1, 10, 20, 15, 16, 2, 3, 4, 5, 6, 7, 8, 17, 18, 19};
    static int[] keys = {};
    static List<Integer> keysToInsert = new ArrayList<Integer>();
    static List<Integer> keysToDelete = new ArrayList<Integer>();


    public static void main(String[] args) {
        Tree tree;
        tree = new Tree(5);

        Graph graph = new Graph(tree);
        mxGraphComponent graphComponent = graph.getGraphComponent();

        JFrame f = new JFrame();
        JPanel panel = new JPanel();
        JScrollPane scroller = new JScrollPane(panel);
        panel.add(graphComponent);

        f.getContentPane().add(scroller);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        f.setBounds(0, 0, screenSize.width-100, screenSize.height-100);

        Box box = new Box(0);
        // box.setBounds(500, 500, 100, 150);
        JButton b=new JButton("Add");//creating instance of JButton
        JTextField input = new JTextField("", 10);
        box.add(input);
        box.add(b);

        Box deleteBox = new Box(0);
        JButton deleteButton = new JButton("Delete");
        JTextField deleteInput = new JTextField("", 10);
        deleteBox.add(deleteInput);
        deleteBox.add(deleteButton);


        if (keys.length != 0) {
            tree.addKey(keys[0]);
            graphComponent = new Graph(tree).getGraphComponent();
            panel.remove(0);
            panel.add(graphComponent);
            if (keys.length > 1) {
                for (int i = 1; i<keys.length; i++) {
                    keysToInsert.add(keys[i]);
                }
            }
            System.out.println(keysToInsert);
        }

        Box box2 = new Box(0);
        JButton nextButton = new JButton("Insert element from queue");
        String queueKeys = "";
        for (int key : keysToInsert) {
            queueKeys += String.valueOf(key + "  ");
        }
        JTextField queueElements = new JTextField(queueKeys, 10);
        queueElements.setEnabled(false);
        queueElements.setDisabledTextColor(Color.BLACK);
        queueElements.setText(queueKeys);
        queueElements.setCaretPosition(0);
        box2.add(nextButton);
        box2.add(queueElements);

        Box box3 = new Box(0);
        JButton pickFileButton = new JButton("Load CSV file");
        final JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return false;
            }

            @Override
            public String getDescription() {
                return null;
            }
        });
        box3.add(pickFileButton);


        JPanel controlPanel = new JPanel();
        controlPanel.add(box);
        controlPanel.add(deleteBox);
        controlPanel.add(box2);
        controlPanel.add(box3);
        f.add(controlPanel, BorderLayout.PAGE_END);

        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] inputArray = input.getText().split(",");
                // wenn die Warteschlange leer ist, wird Element direkt zu Baum hinzugefügt, ansonsten wird/werden alle eingegebenen Elemente zur Warteschlange hinzugefügt
                if (keysToInsert.size() == 0) buttonPressed(tree, inputArray[0].trim(), panel, f);
                else {
                    keysToInsert.add(Integer.valueOf(inputArray[0].trim()));
                    box2.remove(1);
                    String queueKeys = "";
                    for (int key : keysToInsert) {
                        queueKeys += String.valueOf(key + "  ");
                    }
                    JTextField newQueueTextfield = new JTextField(queueKeys, 10);
                    newQueueTextfield.setEnabled(false);
                    newQueueTextfield.setDisabledTextColor(Color.BLACK);
                    newQueueTextfield.setCaretPosition(0);
                    box2.add(newQueueTextfield);
                    box2.revalidate();
                    box2.repaint();
                }
                if (inputArray.length > 1) {
                    for (int i = 1; i<inputArray.length; i++) {
                        keysToInsert.add(Integer.valueOf(inputArray[i].trim()));
                    }
                    box2.remove(1);
                    String queueKeys = "";
                    for (int key : keysToInsert) {
                        queueKeys += String.valueOf(key + "  ");
                    }
                    JTextField newQueueTextfield = new JTextField(queueKeys, 10);
                    newQueueTextfield.setEnabled(false);
                    newQueueTextfield.setDisabledTextColor(Color.BLACK);
                    newQueueTextfield.setCaretPosition(0);
                    box2.add(newQueueTextfield);
                    box2.revalidate();
                    box2.repaint();
                }
                System.out.println(keysToInsert);
                input.setText("");
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] inputArray = deleteInput.getText().split(",");
                // wenn die Warteschlange leer ist, wird Element direkt vom Baum entfernt, ansonsten wird/werden alle eingegebenen Elemente zur Warteschlange hinzugefügt
                if (keysToDelete.size() == 0) delete(tree, inputArray[0].trim(), panel, f);
                /*else {
                    keysToInsert.add(Integer.valueOf(inputArray[0].trim()));
                    box2.remove(1);
                    String queueKeys = "";
                    for (int key : keysToInsert) {
                        queueKeys += String.valueOf(key + "  ");
                    }
                    JTextField newQueueTextfield = new JTextField(queueKeys, 10);
                    newQueueTextfield.setEnabled(false);
                    newQueueTextfield.setDisabledTextColor(Color.BLACK);
                    newQueueTextfield.setCaretPosition(0);
                    box2.add(newQueueTextfield);
                    box2.revalidate();
                    box2.repaint();
                }*/
            }
        });


        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (keysToInsert.size() > 0) {
                    tree.addKey(keysToInsert.get(0));
                    keysToInsert.remove(0);
                    mxGraphComponent graphComponent = new Graph(tree).getGraphComponent();
                    panel.remove(0);
                    panel.add(graphComponent);
                    f.revalidate();
                    f.repaint();
                    box2.remove(1);
                    String queueKeys = "";
                    for (int key : keysToInsert) {
                        queueKeys += String.valueOf(key + "  ");
                    }
                    JTextField newQueueTextfield = new JTextField(queueKeys, 10);
                    newQueueTextfield.setEnabled(false);
                    newQueueTextfield.setDisabledTextColor(Color.BLACK);
                    newQueueTextfield.setCaretPosition(0);
                    box2.add(newQueueTextfield);
                    box2.revalidate();
                    box2.repaint();
                }
            }
        });

        pickFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fc.showDialog(f, "Okay");
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        Scanner fileReader = new Scanner(file);
                        List<String> keys = new ArrayList<>();
                        String values = "";
                        while (fileReader.hasNextLine()) {
                            for (String s : fileReader.nextLine().split(",")) {
                                keys.add(s);
                                values += s + " " ;
                            }
                        }
                        Object[] options = {"Yes",
                                "Cancel"};
                        int chosenOptionVal = JOptionPane.showOptionDialog(f, "Do you want to add these values?\n" + values, "Please confirm", JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options, options[0]);
                        if (chosenOptionVal == JOptionPane.YES_OPTION) {
                            for (String key : keys) {
                                keysToInsert.add(Integer.parseInt(key.trim()));
                            }
                            String queueKeys = "";
                            for (int key : keysToInsert) {
                                queueKeys += String.valueOf(key + "  ");
                            }
                            box2.remove(1);
                            JTextField newQueueTextfield = new JTextField(queueKeys, 10);
                            newQueueTextfield.setEnabled(false);
                            newQueueTextfield.setDisabledTextColor(Color.BLACK);
                            newQueueTextfield.setCaretPosition(0);
                            box2.add(newQueueTextfield);
                            box2.revalidate();
                            box2.repaint();
                        }
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }

                    //This is where a real application would open the file.
                }
            }
        });

        f.setVisible(true);
    }

    static private void buttonPressed(Tree tree, String input, JPanel panel, JFrame f) {
        try {
            int inputValue = Integer.parseInt(input);
            if (inputValue >= 0) {
                tree.addKey(inputValue);
                mxGraphComponent graphComponent = new Graph(tree).getGraphComponent();
                panel.remove(0);
                panel.add(graphComponent);
                f.revalidate();
                f.repaint();
            }
        } catch (NumberFormatException e) {
            System.out.println("Input is not a number");
        }
    }

    static private void delete(Tree tree, String input, JPanel panel, JFrame f) {
        try {
            int inputValue = Integer.parseInt(input);
            if (inputValue >= 0) {
                tree.delete(inputValue);
                mxGraphComponent graphComponent = new Graph(tree).getGraphComponent();
                panel.remove(0);
                panel.add(graphComponent);
                f.revalidate();
                f.repaint();
            }
        } catch (NumberFormatException e) {
            System.out.println("Input is not a number");
        }
    }
}
