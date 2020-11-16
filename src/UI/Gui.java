package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import Tree.Node;
import Tree.Tree;
import com.mxgraph.swing.mxGraphComponent;

public class Gui {
    // private static int[] keys = {};
    // private static List<Integer> keys = new ArrayList<>();
    private static List<Integer> keysToInsert = new ArrayList<Integer>();
    private static List<Integer> keysToDelete = new ArrayList<Integer>();
    private static List<Integer> csvKeys = new ArrayList<Integer>();

    public static void main(String[] args) throws InterruptedException {
        int m = GuiElements.showWelcomeDialogue(csvKeys);
        buildTree(m);
    }

    public static void buildTree(int m) throws InterruptedException {
        Tree tree = new Tree(m);

        JFrame f = new JFrame();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        f.setBounds(0, 0, screenSize.width - 100, screenSize.height - 100);

        Graph graph = new Graph(tree);
        mxGraphComponent graphComponent = graph.getGraphComponent();
        JPanel panel = new JPanel();
        JScrollPane scroller = new JScrollPane(panel);
        panel.add(graphComponent);

        f.getContentPane().add(scroller);

        Box addBox = new Box(0);
        JButton addButton = new JButton("Add");
        JTextField addInput = new JTextField("", 10);
        JLabel addDescription = new JLabel();
        addDescription.setText("Add new elements   ");
        addBox.add(addDescription);
        addBox.add(addInput);
        addBox.add(addButton);

        Box deleteBox = new Box(0);
        JButton deleteButton = new JButton("Delete");
        JTextField deleteInput = new JTextField("", 10);
        JLabel deleteDescription = new JLabel();
        deleteDescription.setText("Delete elements   ");
        deleteBox.add(deleteDescription);
        deleteBox.add(deleteInput);
        deleteBox.add(deleteButton);

        if (csvKeys.size() != 0) {
            tree.insert(csvKeys.get(0));
            graphComponent = new Graph(tree).getGraphComponent();
            panel.remove(0);
            panel.add(graphComponent);
            /*
            if (csvKeys.size() > 1) {
                for (int i = 1; i < csvKeys.size(); i++) {
                    keysToInsert.add(csvKeys.get(i));
                }
            }

             */
        }

        Box addQueueBox = new Box(0);
        JButton nextButton = new JButton("Insert next element");
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
        JButton nextDeleteButton = new JButton("Delete next element");
        String deleteQueueKeys = "";
        for (int key : keysToDelete) {
            deleteQueueKeys += String.valueOf(key + "  ");
        }

        JTextField deleteQueueElements = new JTextField(deleteQueueKeys, 30);
        deleteQueueElements.setEnabled(false);
        deleteQueueElements.setDisabledTextColor(Color.BLACK);
        deleteQueueElements.setText(deleteQueueKeys);
        deleteQueueElements.setCaretPosition(0);
        deleteQueueBox.add(nextDeleteButton);
        deleteQueueBox.add(deleteQueueElements);
        deleteQueueBox.setBorder(new EmptyBorder(0, 30, 0, 0));
        deleteBox.add(deleteQueueBox);
        deleteBox.setBorder(new EmptyBorder(0, 0, 20, 0));


        Box searchBox = new Box(0);
        JButton searchButton = new JButton("Search");
        JTextField searchInput = new JTextField("", 10);
        JLabel searchDescription = new JLabel();
        searchDescription.setText("Search node with specific key   ");
        searchBox.add(searchDescription);
        searchBox.add(searchInput);
        searchBox.setBorder(new EmptyBorder(0, 0, 20, 0));
        searchBox.add(searchButton);

        Box searchResultBox = new Box(0);
        JTextField searchResultText = new JTextField("", 30);
        searchResultText.setEnabled(false);
        searchResultText.setDisabledTextColor(Color.BLACK);
        searchResultText.setText(deleteQueueKeys);
        searchResultText.setCaretPosition(0);
        searchResultBox.add(searchResultText);
        searchResultBox.setBorder(new EmptyBorder(0, 30, 0, 0));
        searchBox.add(searchResultBox);

        Box randomValBox = new Box(0);
        JButton randomValButton = new JButton("Add random values");
        randomValBox.setBorder(new EmptyBorder(0, 0, 0, 30));
        randomValBox.add(randomValButton);

        Box changeOrderBox = new Box(0);
        JButton changeOrderButton = new JButton("Change order of tree");
        changeOrderBox.add(changeOrderButton);

        Box bottomBox = new Box(0);
        bottomBox.add(randomValBox);
        bottomBox.add(changeOrderBox);

        bottomBox.setBorder(new EmptyBorder(0, 0, 20, 1000));

        Box controlBox = new Box(1);
        controlBox.add(addBox);
        controlBox.add(deleteBox);
        controlBox.add(searchBox);
        controlBox.add(bottomBox);

        JPanel controlPanel = new JPanel();
        controlPanel.add(controlBox);
        f.add(controlPanel, BorderLayout.PAGE_END);

        addButton.addActionListener(e -> {
            String[] inputArray = addInput.getText().split(",");
            // wenn die Warteschlange leer ist, wird Element direkt zu Baum hinzugefügt, ansonsten wird/werden alle eingegebenen Elemente zur Warteschlange hinzugefügt
            if (keysToInsert.size() == 0) {
                try {
                    int inputValue = Integer.parseInt(inputArray[0].trim());
                    GuiMethods.add(tree, inputValue, panel, f);
                } catch (NumberFormatException exp) {
                    System.out.println("Input is not a number");
                }
                clearSearchField(searchBox, searchInput);
            } else {
                if (Integer.parseInt(inputArray[0].trim()) >= 0) {
                    keysToInsert.add(Integer.valueOf(inputArray[0].trim()));
                    GuiMethods.updateAddQueue(keysToInsert, addQueueBox);
                }
            }
            if (inputArray.length > 1) {
                for (int i = 1; i < inputArray.length; i++) {
                    if (Integer.parseInt(inputArray[i].trim()) >= 0)
                        keysToInsert.add(Integer.valueOf(inputArray[i].trim()));
                }
                GuiMethods.updateAddQueue(keysToInsert, addQueueBox);
            }
            addInput.setText("");
        });

        deleteButton.addActionListener(e -> {
            String[] inputArray = deleteInput.getText().split(",");
            // wenn die Warteschlange leer ist, wird Element direkt zu Baum hinzugefügt, ansonsten wird/werden alle eingegebenen Elemente zur Warteschlange hinzugefügt
            if (keysToDelete.size() == 0) {
                try {
                    int inputValue = Integer.parseInt(inputArray[0].trim());
                    GuiMethods.delete(tree, inputValue, panel, f);
                } catch (NumberFormatException exp) {
                    System.out.println("Input is not a number");
                }
                clearSearchField(searchBox, searchInput);
            } else {
                keysToDelete.add(Integer.valueOf(inputArray[0].trim()));
                GuiMethods.updateDeleteQueue(keysToDelete, deleteQueueBox);
            }
            if (inputArray.length > 1) {
                for (int i = 1; i < inputArray.length; i++) {
                    keysToDelete.add(Integer.valueOf(inputArray[i].trim()));
                }
                GuiMethods.updateDeleteQueue(keysToDelete, deleteQueueBox);
            }
            deleteInput.setText("");
        });

        nextButton.addActionListener(e -> {
            if (keysToInsert.size() > 0) {
                GuiMethods.add(tree, keysToInsert.get(0), panel, f);
                keysToInsert.remove(0);
                GuiMethods.updateAddQueue(keysToInsert, addQueueBox);
                clearSearchField(searchBox, searchInput);
            }
        });

        nextDeleteButton.addActionListener(e -> {
            if (keysToDelete.size() > 0) {
                GuiMethods.delete(tree, keysToDelete.get(0), panel, f);
                keysToDelete.remove(0);
                GuiMethods.updateDeleteQueue(keysToDelete, deleteQueueBox);
                clearSearchField(searchBox, searchInput);
            }
        });

        searchButton.addActionListener(e -> {
            String searchKey = searchInput.getText();
            Node searchResultNode;
            if (searchKey != null) {
                searchResultNode = GuiMethods.search(tree, searchKey.trim(), panel, f);
                if (searchResultNode != null) {
                    searchBox.remove(3);
                    String cost = "Node found! Cost of search: " + String.valueOf(searchResultNode.getSearchCost());
                    JTextField costTextField = new JTextField(cost, 30);
                    costTextField.setEnabled(false);
                    costTextField.setDisabledTextColor(Color.BLACK);
                    costTextField.setCaretPosition(0);
                    costTextField.setBorder(new EmptyBorder(0, 30, 0, 0));
                    searchBox.add(costTextField);
                    searchBox.revalidate();
                    searchBox.repaint();
                } else {
                    searchBox.remove(3);
                    String cost = "Node not found.";
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
        });

        JTextField randomMin = new JTextField(5);
        JTextField randomMax = new JTextField(5);
        JTextField numOfElements = new JTextField(5);

        JPanel randomizePanel = new JPanel();
        randomizePanel.add(new JLabel("Minimum value:"));
        randomizePanel.add(randomMin);
        randomizePanel.add(Box.createHorizontalStrut(15)); // a spacer
        randomizePanel.add(new JLabel("Maximum value:"));
        randomizePanel.add(randomMax);
        randomizePanel.add(Box.createHorizontalStrut(15)); // a spacer
        randomizePanel.add(new JLabel("Number of elements:"));
        randomizePanel.add(numOfElements);

        randomValButton.addActionListener(e -> {
            Object[] options = {"Add",
                    "Cancel"};
            int result = JOptionPane.showConfirmDialog(f, randomizePanel, "Add random elements", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                if (!randomMin.getText().isEmpty() && !randomMax.getText().isEmpty() && !numOfElements.getText().isEmpty()) {
                    GuiMethods.addRandomKeys(randomMin.getText(), randomMax.getText(), numOfElements.getText(), tree, panel, f, addQueueBox, keysToInsert);
                }
                randomMin.setText("");
                randomMax.setText("");
                numOfElements.setText("");
            }
        });



        changeOrderButton.addActionListener(e -> {
            JTextField newOrder = new JTextField(5);
            JLabel newOrderLabel = new JLabel("Enter the new order for the tree (minimum: 3):");
            JPanel orderPanel = new JPanel();
            orderPanel.add(newOrderLabel);
            orderPanel.add(newOrder);

            Object[] options = {"Add",
                    "Cancel"};
            int result = JOptionPane.showConfirmDialog(f, orderPanel, "Change order", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                if (newOrder.getText() != null) {
                    int changedOrder = GuiElements.showChangeOrderDialog(newOrder.getText(), keysToInsert, keysToDelete, csvKeys, f);
                    try {
                        buildTree(changedOrder);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        f.setVisible(true);

        if (csvKeys.size() > 0) {
            for (int key : csvKeys) {
                if (key >= 0) {
                    System.out.println("TEEST");
                    tree.insert(key);
                    graphComponent = new Graph(tree).getGraphComponent();
                    panel.remove(0);
                    panel.add(graphComponent);
                    f.revalidate();
                    f.repaint();
                    Thread.sleep(1000);
                }
            }
        }
    }

    private static void clearSearchField(Box searchBox, JTextField searchInput) {
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
