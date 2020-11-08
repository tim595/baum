import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class Graph {
    mxGraphComponent graphComponent;

    public Graph(Tree tree) {
        mxGraph graph = new mxGraph();
        graph.getModel().beginUpdate();
        Object parent = graph.getDefaultParent();

        try {
            String rootValues = "";
            for (int i = 0; i<tree.getRoot().keys.size(); i++) {
                rootValues += (tree.getRoot().keys.get(i));
                if (i != tree.getRoot().keys.size()-1) rootValues += "   ";
            }
            Object root = graph.insertVertex(parent, null, rootValues, 0, 0, 120, 30);
            // Object son = graph.insertVertex(parent, null, "SON", 0, 0, 80, 30);
            // Object son2 = graph.insertVertex(parent, null, "SON", 0, 0, 80, 30);

            // graph.insertEdge(parent, null, "", root, son);
            // graph.insertEdge(parent, null, "", root, son2);

            if (tree.getRoot().sons.size() != 0 ) {
                displaySons(tree.getRoot(), graph, parent, root);
            }

            mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
            layout.setUseBoundingBox(false);

            layout.execute(parent);
        } finally {
            graph.getModel().endUpdate();
        }
        graphComponent = new mxGraphComponent(graph);
    }

    private void displaySons(Node node, mxGraph graph, Object parent, Object newParent) {
        for (Node son : node.sons) {
            String values = "";
            for (int i = 0; i<son.keys.size(); i++) {
                values += (son.keys.get(i));
                if (i != son.keys.size()-1) values += "   ";
            }
            Object o = graph.insertVertex(parent, null, values, 0, 0, 120, 30);
            graph.insertEdge(parent, null, "", newParent, o);
            if (son.sons.size() != 0) displaySons(son, graph, parent, o);
        }
    }

    public mxGraphComponent getGraphComponent() {
        return this.graphComponent;
    }
}
