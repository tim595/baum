package view;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import tree.Node;
import tree.Tree;

public class Graph {
    mxGraphComponent graphComponent;
    static int nodeWidth;

    public Graph(Tree tree, Node...nodeToHighlight) {
        mxGraph graph = new mxGraph();
        graph.setConnectableEdges(false);
        graph.setCellsLocked(true);
        graph.getModel().beginUpdate();
        Object parent = graph.getDefaultParent();
        nodeWidth = 30*tree.getOrder();

        try {
            String rootValues = "";
            for (int i = 0; i<tree.getRoot().getKeys().size(); i++) {
                rootValues += (tree.getRoot().getKeys().get(i));
                if (i != tree.getRoot().getKeys().size()-1) rootValues += "   ";
            }

            Object root;
            if (nodeToHighlight.length != 0 && nodeToHighlight[0] == tree.getRoot()) {
                rootValues = "SELECTED\n" + rootValues;
                root = graph.insertVertex(parent, null, rootValues, 0, 0, nodeWidth+30, 50);
            } else {
                root = graph.insertVertex(parent, null, rootValues, 0, 0, nodeWidth, 30);
            }

            if (tree.getRoot().getSons().size() != 0 ) {
                if (nodeToHighlight.length != 0) displaySons(tree.getRoot(), graph, parent, root, nodeToHighlight[0]);
                else displaySons(tree.getRoot(), graph, parent, root);
            }

            mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
            layout.setUseBoundingBox(false);

            layout.execute(parent);
        } finally {
            graph.getModel().endUpdate();
        }
        graphComponent = new mxGraphComponent(graph);
    }

    private void displaySons(Node node, mxGraph graph, Object parent, Object newParent, Node...nodeToHighlight) {
        for (Node son : node.getSons()) {
            String values = "";
            for (int i = 0; i<son.getKeys().size(); i++) {
                values += (son.getKeys().get(i));
                if (i != son.getKeys().size()-1) values += "   ";
            }

            Object o;
            if (nodeToHighlight.length != 0 && nodeToHighlight[0] == son) {
                values = "SELECTED\n" +values;
                o = graph.insertVertex(parent, null, values, 0, 0, nodeWidth+30, 50);
                System.out.println("HIER");
            } else {
                o = graph.insertVertex(parent, null, values, 0, 0, nodeWidth, 30);
            }

            graph.insertEdge(parent, null, "", newParent, o);
            if (son.getSons().size() != 0) {
                if (nodeToHighlight.length != 0) displaySons(son, graph, parent, o, nodeToHighlight);
                else displaySons(son, graph, parent, o);
            }
        }
    }

    public mxGraphComponent getGraphComponent() {
        return this.graphComponent;
    }
}
