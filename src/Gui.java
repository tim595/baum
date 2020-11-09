import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import jdk.swing.interop.SwingInterOpUtils;

public class Gui {
    public static void main(String[] args) {
        Tree tree;
        tree = new Tree(6);

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
        box.setBounds(500, 500, 100, 150);
        JButton b=new JButton("Add");//creating instance of JButton
        JTextField input = new JTextField();

        // b.setBounds(1000,1000,100, 40);//x axis, y axis, width, height
        // input.setBounds(10, 10, 100, 25);
        b.setSize(100, 40);
        input.setSize(10, 25);

        box.add(b);
        box.add(input);
        f.add(box, BorderLayout.PAGE_END);

        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonPressed(tree, input.getText(), panel, f, graphComponent);
                // graphComponent = graph.getGraphComponent();
            }
        });
        f.setVisible(true);
    }

    static private void buttonPressed(Tree tree, String input, JPanel panel, JFrame f, mxGraphComponent graphComponent) {
        try {
            int inputValue = Integer.parseInt(input);
            if (inputValue >= 0) {
                tree.addKey(Integer.parseInt(input));
                tree.printTree();
                graphComponent = new Graph(tree).getGraphComponent();
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
