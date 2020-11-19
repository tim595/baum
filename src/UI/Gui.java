package UI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import Tree.Tree;
import com.mxgraph.swing.mxGraphComponent;

public class Gui {
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

        System.out.println("CSV");
        System.out.println(csvKeys);

        if (csvKeys.size() != 0) {
            tree.insert(csvKeys.get(0));
            graphComponent = new Graph(tree).getGraphComponent();
            panel.remove(0);
            panel.add(graphComponent);
        }

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

        addButton.addActionListener(ButtonListeners.addButtonAction(keysToInsert, addInput, addQueueBox, tree, panel, f, searchBox, searchInput));
        deleteButton.addActionListener(ButtonListeners.deleteButtonAction(keysToDelete, deleteInput, deleteQueueBox, tree, panel, f, searchBox, searchInput));
        nextButton.addActionListener(ButtonListeners.addnextButtonAction(keysToInsert, addQueueBox, tree, panel, f, searchBox, searchInput));
        nextDeleteButton.addActionListener(ButtonListeners.deletenextButtonAction(keysToDelete, deleteQueueBox, tree, panel, f, searchBox, searchInput));
        searchButton.addActionListener(ButtonListeners.searchButtonAction(searchBox, searchInput, tree, panel, f));
        randomValButton.addActionListener(ButtonListeners.randomButtonAction(keysToInsert, addQueueBox, tree, panel, f));
        changeOrderButton.addActionListener(ButtonListeners.changeorderButtonAction(keysToInsert, keysToDelete, csvKeys, f));

        f.setVisible(true);
        System.out.println(f.getComponents());

        if (csvKeys.size() > 0) {
            for (int key : csvKeys) {
                if (key >= 0) {
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
}
