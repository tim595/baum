package UI;

import Tree.Tree;
import Tree.Node;

import com.mxgraph.swing.mxGraphComponent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GuiMethods {
    public static void add(Tree tree, int inputValue, JPanel panel, JFrame f) {  // Element wird zu Baum hinzugefügt, dieser wird anschließend neu gezeichnet
        if (inputValue >= 0) {
            tree.insert(inputValue);
            mxGraphComponent graphComponent = new Graph(tree).getGraphComponent();
            panel.remove(0);
            panel.add(graphComponent);
            f.revalidate();
            f.repaint();
        }
    }

    public static void updateAddQueue(List<Integer> keysToInsert, Box addQueueBox) {  // Element wird aus Warteschlange entfernt
        addQueueBox.remove(1);
        String newQueueKeys = "";
        for (int key : keysToInsert) {
            newQueueKeys += String.valueOf(key + "  ");
        }
        JTextField newQueueTextfield = new JTextField(newQueueKeys, 30);
        newQueueTextfield.setEnabled(false);
        newQueueTextfield.setDisabledTextColor(Color.BLACK);
        newQueueTextfield.setCaretPosition(0);
        addQueueBox.add(newQueueTextfield);
        addQueueBox.revalidate();
        addQueueBox.repaint();
    }

    public static void delete(Tree tree, int inputValue, JPanel panel, JFrame f) {  // Element wird aus Baum entfernt, dieser wird anschließend neu gezeichnet
        if (inputValue >= 0) {
            tree.delete(inputValue);
            mxGraphComponent graphComponent = new Graph(tree).getGraphComponent();
            panel.remove(0);
            panel.add(graphComponent);
            f.revalidate();
            f.repaint();
        }
    }

    public static void updateDeleteQueue(List<Integer> keysToDelete, Box deleteQueueBox) {  // Element wird aus Warteschlange entfernt
        deleteQueueBox.remove(1);
        String newQueueKeys = "";
        for (int key : keysToDelete) {
            newQueueKeys += key + "  ";
        }
        JTextField newQueueTextfield = new JTextField(newQueueKeys, 30);
        newQueueTextfield.setEnabled(false);
        newQueueTextfield.setDisabledTextColor(Color.BLACK);
        newQueueTextfield.setCaretPosition(0);
        deleteQueueBox.add(newQueueTextfield);
        deleteQueueBox.revalidate();
        deleteQueueBox.repaint();
    }


    public static void pickCSV(List<Integer> csvKeys) {  // Dialog zum Auswählen einer csv Datei
        final JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        FileFilter filter = new FileNameExtensionFilter("CSV File", "csv");
        fc.setFileFilter(filter);

        int returnVal = fc.showDialog(null, "Open file");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("debug1");
            File file = fc.getSelectedFile();
            try {
                Scanner fileReader = new Scanner(file);
                List<String> keys = new ArrayList<>();
                String values = "";
                int counter = 0;
                fileReader.nextLine(); // Header einlesen bzw ignorieren
                while (fileReader.hasNextLine()) {
                    counter++;
                    String s = fileReader.nextLine();
                    keys.add(s);
                    values += s + "  ";
                    if (counter == 12) {
                        values += "\n";
                        counter = 0;
                    }
                    /*
                    for (String s : fileReader.nextLine().split(",")) {
                        counter++;
                        keys.add(s);
                        values += s + "  ";
                        if (counter == 12) {
                            values += "\n";
                            counter = 0;
                        }
                    }
                     */
                }
                Object[] options = {"Yes",
                        "Cancel"};
                int chosenOptionVal = JOptionPane.showOptionDialog(null, "Do you want to add these values?\n" + values, "Please confirm", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options, options[0]);
                if (chosenOptionVal == JOptionPane.YES_OPTION) {
                    System.out.println("debug2");
                    for (String key : keys) {
                        try {
                            csvKeys.add(Integer.parseInt(key.trim()));
                        } catch (NumberFormatException e) {
                            System.out.println("Input is not a number");
                        }
                    }
                    System.out.println(csvKeys);
                }
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static Node search(Tree tree, String input, JPanel panel, JFrame f) {
        try {
            int inputValue = Integer.parseInt(input);
            if (inputValue >= 0) {
                Node node = tree.search(inputValue);
                if (node != null) {
                    mxGraphComponent graphComponent = new Graph(tree, node).getGraphComponent();  // Node gefunden -> gesuchtes Element wird markiert
                    panel.remove(0);
                    panel.add(graphComponent);
                    f.revalidate();
                    f.repaint();
                } else {
                    mxGraphComponent graphComponent = new Graph(tree).getGraphComponent();  // Node nicht gefunden -> kein Element wird markiert
                    panel.remove(0);
                    panel.add(graphComponent);
                    f.revalidate();
                    f.repaint();
                }
                return node;
            } else return null;
        } catch (NumberFormatException e) {
            System.out.println("Input is not a number");
            return null;
        }
    }

    public static void addRandomKeys(String minimum, String maximum, String numberOfElements, Tree tree, JPanel panel, JFrame f, Box addQueueBox, List<Integer> keysToInsert) {
        int[] randomKeys = new int[Integer.parseInt(numberOfElements)];
        int min = Integer.parseInt(minimum);
        int max = Integer.parseInt(maximum);
        int range = max - min + 1;

        for (int i = 0; i < Integer.parseInt(numberOfElements); i++) {
            randomKeys[i] = (int) (Math.random() * range) + min;
        }

        if (keysToInsert.size() == 0) {
            if (randomKeys[0] >= 0) {
                tree.insert(randomKeys[0]);
                mxGraphComponent graphComponent = new Graph(tree).getGraphComponent();
                panel.remove(0);
                panel.add(graphComponent);
                f.revalidate();
                f.repaint();
            }
            for (int i = 1; i < randomKeys.length; i++) {
                if (randomKeys[i] >= 0) keysToInsert.add(randomKeys[i]);
            }
        } else {
            for (int key : randomKeys) {
                if (key >= 0) keysToInsert.add(key);
            }
        }
        addQueueBox.remove(1);
        String newQueueKeys = "";
        for (int key : keysToInsert) {
            newQueueKeys += key + "  ";
        }

        JTextField newQueueTextfield = new JTextField(newQueueKeys, 30);
        newQueueTextfield.setEnabled(false);
        newQueueTextfield.setDisabledTextColor(Color.BLACK);
        newQueueTextfield.setCaretPosition(0);
        addQueueBox.add(newQueueTextfield);
        addQueueBox.revalidate();
        addQueueBox.repaint();
    }

    public static void clearSearchField(Box searchBox, JTextField searchInput) {
        searchInput.setText("");
        searchBox.remove(3);
        String cost = "";
        JTextField costTextField = new JTextField(cost, 30);
        costTextField.setEnabled(false);
        costTextField.setDisabledTextColor(Color.BLACK);
        costTextField.setCaretPosition(0);
        costTextField.setBorder(new EmptyBorder(0, 30, 0, 0));
        searchBox.add(costTextField);
        searchBox.revalidate();
        searchBox.repaint();
    }
}
