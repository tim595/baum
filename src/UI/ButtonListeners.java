package UI;

import Tree.Tree;
import Tree.Node;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ButtonListeners {
    public static ActionListener addButtonAction(List<Integer> keysToInsert, JTextField addInput, Box addQueueBox, Tree tree, JPanel panel, JFrame f, Box searchBox, JTextField searchInput) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] inputArray = addInput.getText().split(",");
                // wenn die Warteschlange leer ist, wird Element direkt zu Baum hinzugefügt, ansonsten wird/werden alle eingegebenen Elemente zur Warteschlange hinzugefügt
                if (keysToInsert.size() == 0) {
                    try {
                        int inputValue = Integer.parseInt(inputArray[0].trim());
                        GuiMethods.add(tree, inputValue, panel, f);
                    } catch (NumberFormatException exp) {
                        System.out.println("Input is not a number");
                    }
                    GuiMethods.clearSearchField(searchBox, searchInput);
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
            }
        };
    }

    public static ActionListener deleteButtonAction(List<Integer> keysToDelete, JTextField deleteInput, Box deleteQueueBox, Tree tree, JPanel panel, JFrame f, Box searchBox, JTextField searchInput) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] inputArray = deleteInput.getText().split(",");
                // wenn die Warteschlange leer ist, wird Element direkt zu Baum hinzugefügt, ansonsten wird/werden alle eingegebenen Elemente zur Warteschlange hinzugefügt
                if (keysToDelete.size() == 0) {
                    try {
                        int inputValue = Integer.parseInt(inputArray[0].trim());
                        GuiMethods.delete(tree, inputValue, panel, f);
                    } catch (NumberFormatException exp) {
                        System.out.println("Input is not a number");
                    }
                    GuiMethods.clearSearchField(searchBox, searchInput);
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
            }
        };
    }

    public static ActionListener addnextButtonAction(List<Integer> keysToInsert, Box addQueueBox, Tree tree, JPanel panel, JFrame f, Box searchBox, JTextField searchInput) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Wenn Warteschlange Elemente enthält, wird das nächste Element eingefügt
                if (keysToInsert.size() > 0) {
                    GuiMethods.add(tree, keysToInsert.get(0), panel, f);
                    keysToInsert.remove(0);
                    GuiMethods.updateAddQueue(keysToInsert, addQueueBox);
                    GuiMethods.clearSearchField(searchBox, searchInput);
                }
            }
        };
    }

    public static ActionListener deletenextButtonAction(List<Integer> keysToDelete, Box deleteQueueBox, Tree tree, JPanel panel, JFrame f, Box searchBox, JTextField searchInput) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Wenn Warteschlange Elemente enthält, wird das nächste Element gelöscht
                if (keysToDelete.size() > 0) {
                    GuiMethods.delete(tree, keysToDelete.get(0), panel, f);
                    keysToDelete.remove(0);
                    GuiMethods.updateDeleteQueue(keysToDelete, deleteQueueBox);
                    GuiMethods.clearSearchField(searchBox, searchInput);
                }
            }
        };
    }

    public static ActionListener searchButtonAction(Box searchBox, JTextField searchInput, Tree tree, JPanel panel, JFrame f) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchKey = searchInput.getText();
                Node searchResultNode;
                if (searchKey != null) {
                    searchResultNode = GuiMethods.search(tree, searchKey.trim(), panel, f);
                    if (searchResultNode != null) {
                        searchBox.remove(3);
                        String cost = "Node found! Cost of search: " + searchResultNode.getSearchCost();
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
            }
        };
    }

    public static ActionListener randomButtonAction(List<Integer> keysToInsert, Box addQueueBox, Tree tree, JPanel panel, JFrame f) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        };
    }

    public static ActionListener changeorderButtonAction(List<Integer> keysToInsert, List<Integer> keysToDelete, List<Integer> csvKeys, JFrame f) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                            Gui.buildTree(changedOrder);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        };
    }
}
