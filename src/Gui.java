import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

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

        Box addBox = new Box(0);
        JButton addButton=new JButton("Add");//creating instance of JButton
        JTextField addInput = new JTextField("", 10);
        addBox.add(addInput);
        addBox.add(addButton);

        Box deleteBox = new Box(0);
        JButton deleteButton = new JButton("Delete");
        JTextField deleteInput = new JTextField("", 10);
        deleteBox.add(deleteInput);
        deleteBox.add(deleteButton);


        if (keys.length != 0) {
            tree.insert(keys[0]);
            graphComponent = new Graph(tree).getGraphComponent();
            panel.remove(0);
            panel.add(graphComponent);
            if (keys.length > 1) {
                for (int i = 1; i<keys.length; i++) {
                    keysToInsert.add(keys[i]);
                }
            }
        }

        Box addQueueBox = new Box(0);
        JButton nextButton = new JButton("Insert element from queue");
        String queueKeys = "";
        for (int key : keysToInsert) {
            queueKeys += String.valueOf(key + "  ");
        }
        JTextField queueElements = new JTextField(queueKeys, 30);
        queueElements.setEnabled(false);
        queueElements.setDisabledTextColor(Color.BLACK);
        queueElements.setText(queueKeys);
        queueElements.setCaretPosition(0);
        addQueueBox.add(nextButton);
        addQueueBox.add(queueElements);
        addQueueBox.setBorder(new EmptyBorder(0, 30, 0, 0));
        addBox.add(addQueueBox);
        addBox.setBorder(new EmptyBorder(0, 0, 20, 0));


        Box deleteQueueBox = new Box(0);
        JButton nextDeleteButton = new JButton("Delete element from queue");
        String deleteQueueKeys = "";
        /*
        for (int key : keysToInsert) {
            deleteQueueKeys += String.valueOf(key + "  ");
        }
        */
        JTextField deleteQueueElements = new JTextField(deleteQueueKeys, 30);
        deleteQueueElements.setEnabled(false);
        deleteQueueElements.setDisabledTextColor(Color.BLACK);
        deleteQueueElements.setText(deleteQueueKeys);
        deleteQueueElements.setCaretPosition(0);
        deleteQueueBox.add(nextDeleteButton);
        deleteQueueBox.add(deleteQueueElements);
        deleteQueueBox.setBorder(new EmptyBorder(0, 15, 0, 0));
        deleteBox.add(deleteQueueBox);
        deleteBox.setBorder(new EmptyBorder(0, 0, 20, 0));


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

        Box searchBox = new Box(0);
        JButton searchButton = new JButton("Search");
        JTextField searchInput = new JTextField("", 10);
        searchBox.add(searchInput);
        searchBox.add(searchButton);

        Box searchResultBox = new Box(0);
        String searchResult = "";
        /*
        for (int key : keysToInsert) {
            deleteQueueKeys += String.valueOf(key + "  ");
        }
        */
        JTextField searchResultText = new JTextField(searchResult, 30);
        searchResultText.setEnabled(false);
        searchResultText.setDisabledTextColor(Color.BLACK);
        searchResultText.setText(deleteQueueKeys);
        searchResultText.setCaretPosition(0);
        searchResultBox.add(searchResultText);
        searchResultBox.setBorder(new EmptyBorder(0, 30, 0, 0));
        searchBox.add(searchResultBox);



        Box controlBox = new Box(1);
        controlBox.add(addBox);
        controlBox.add(deleteBox);
        controlBox.add(searchBox);

        JPanel controlPanel = new JPanel();
        controlPanel.add(controlBox);
        // controlPanel.add(addBox);
        /*
        controlPanel.add(deleteBox);
        controlPanel.add(box2);
        controlPanel.add(box3);
        controlPanel.add(searchBox);

         */
        f.add(controlPanel, BorderLayout.PAGE_END);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] inputArray = addInput.getText().split(",");
                // wenn die Warteschlange leer ist, wird Element direkt zu Baum hinzugefügt, ansonsten wird/werden alle eingegebenen Elemente zur Warteschlange hinzugefügt
                if (keysToInsert.size() == 0) buttonPressed(tree, inputArray[0].trim(), panel, f);
                else {
                    keysToInsert.add(Integer.valueOf(inputArray[0].trim()));
                    addQueueBox.remove(1);
                    String queueKeys = "";
                    for (int key : keysToInsert) {
                        queueKeys += String.valueOf(key + "  ");
                    }
                    JTextField newQueueTextfield = new JTextField(queueKeys, 30);
                    newQueueTextfield.setEnabled(false);
                    newQueueTextfield.setDisabledTextColor(Color.BLACK);
                    newQueueTextfield.setCaretPosition(0);
                    addQueueBox.add(newQueueTextfield);
                    addQueueBox.revalidate();
                    addQueueBox.repaint();
                }
                if (inputArray.length > 1) {
                    for (int i = 1; i<inputArray.length; i++) {
                        keysToInsert.add(Integer.valueOf(inputArray[i].trim()));
                    }
                    addQueueBox.remove(1);
                    String queueKeys = "";
                    for (int key : keysToInsert) {
                        queueKeys += String.valueOf(key + "  ");
                    }
                    JTextField newQueueTextfield = new JTextField(queueKeys, 30);
                    newQueueTextfield.setEnabled(false);
                    newQueueTextfield.setDisabledTextColor(Color.BLACK);
                    newQueueTextfield.setCaretPosition(0);
                    addQueueBox.add(newQueueTextfield);
                    addQueueBox.revalidate();
                    addQueueBox.repaint();
                }
                System.out.println(keysToInsert);
                addInput.setText("");
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
                    tree.insert(keysToInsert.get(0));
                    keysToInsert.remove(0);
                    mxGraphComponent graphComponent = new Graph(tree).getGraphComponent();
                    panel.remove(0);
                    panel.add(graphComponent);
                    f.revalidate();
                    f.repaint();
                    addQueueBox.remove(1);
                    String queueKeys = "";
                    for (int key : keysToInsert) {
                        queueKeys += String.valueOf(key + "  ");
                    }
                    JTextField newQueueTextfield = new JTextField(queueKeys, 30);
                    newQueueTextfield.setEnabled(false);
                    newQueueTextfield.setDisabledTextColor(Color.BLACK);
                    newQueueTextfield.setCaretPosition(0);
                    addQueueBox.add(newQueueTextfield);
                    addQueueBox.revalidate();
                    addQueueBox.repaint();
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchKey = searchInput.getText();
                Node searchResultNode;
                if (searchKey != null) {
                    searchResultNode = search(tree, searchKey.trim(), panel, f);
                    if (searchResultNode != null) {
                        searchBox.remove(2);
                        String cost = "Cost of search: " + String.valueOf(searchResultNode.searchCost);
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
                            addQueueBox.remove(1);
                            JTextField newQueueTextfield = new JTextField(queueKeys, 10);
                            newQueueTextfield.setEnabled(false);
                            newQueueTextfield.setDisabledTextColor(Color.BLACK);
                            newQueueTextfield.setCaretPosition(0);
                            addQueueBox.add(newQueueTextfield);
                            addQueueBox.revalidate();
                            addQueueBox.repaint();
                        }
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        f.setVisible(true);
    }

    static private void buttonPressed(Tree tree, String input, JPanel panel, JFrame f) {
        try {
            int inputValue = Integer.parseInt(input);
            if (inputValue >= 0) {
                tree.insert(inputValue);
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

    static private Node search(Tree tree, String input, JPanel panel, JFrame f) {
        int inputValue = Integer.parseInt(input);
        if (inputValue >= 0) {
            Node node = tree.search(inputValue);
            mxGraphComponent graphComponent = new Graph(tree, node).getGraphComponent();
            panel.remove(0);
            panel.add(graphComponent);
            f.revalidate();
            f.repaint();

            return node;
        } else return null;
    }
}
