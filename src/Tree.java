import java.util.ArrayList;
import java.util.List;

public class Tree {
    static int numberOfNodes = 0;
    int m;
    static List<Node> nodeList = new ArrayList<Node>();
    Node root;
    static Node nodePointer;
    int minimumKeys, maximumKeys;

    Tree(int m) {
        this.m = m;
        root = new Node(m);
        if (m%2 == 0) minimumKeys = (int)Math.floor((m-1)/2);
        else minimumKeys = (m-1)/2;
        maximumKeys = m-1;
    }

    Node getRoot() {
        return this.root;
    }

    void insert(int key) {
        if (root.sons.size() == 0) {   // Prüfung, ob der Baum lediglich aus einem einzigen Knoten besteht
            nodePointer = root;
            if (!nodePointer.keys.contains(key)) {   // Neuer Key wird eingefügt, sofern er nicht bereits vorhanden ist
                nodePointer.insertKey(key);
                boolean overflow = nodePointer.hasOverflown();

                if (overflow) {   // Wurzel wird gesplittet, falls Anzahl zulässiger Schlüssel pro Knoten überschritten wurde
                    int splitKey = (int)Math.floor((m-1)/2);
                    root = InsertMethods.splitRoot(nodePointer, splitKey, m);
                }
            }
        } else {   // Fall, dass bereits mehrere Knoten bestehen
            nodePointer = InsertMethods.searchNodeForInsert(root, key);   // Von der Wurzel aus wird nach unten gewandert, um Einfüge-Position zu suchen
            if (nodePointer != null) {
                nodePointer.insertKey(key);
                boolean overflow = nodePointer.hasOverflown();
                // Bei Overflow wird Knoten gesplittet, der nodePointer auf das parent-Element gesetzt und hier auf erneuten Overflow geprüft
                while (overflow) {
                    if (nodePointer.parent != null) {   // Nicht-wurzel-Split
                        nodePointer = InsertMethods.splitNonRoot(nodePointer, m);  // Knoten wird gesplittet, anschließend wird nodePointer auf dessen parent gesetzt
                        overflow = nodePointer.hasOverflown();
                    } else {   // Wurzel-Split
                        int splitKey = (int)Math.floor((m-1)/2);
                        root = InsertMethods.splitRoot(nodePointer, splitKey, m);
                        InsertMethods.updateChildParentRelations(nodePointer, root, splitKey);
                        overflow = false;
                    }
                }
            }
        }
    }

    void delete(int key) {
        if (root.sons.size() == 0) {  // Löschen aus Wurzel (wenn diese noch keine Söhne besitzt)
            if (root.keys.contains(key)) {
                root.removeKey(key);
            }
        } else {
            nodePointer = DeleteMethods.searchNodeForDelete(root, key);
            if (nodePointer != null) {
                if (nodePointer.sons.size() == 0) { // Löschen aus Blatt
                    nodePointer.removeKey(key);
                } else {  // Löschen aus innerem Knoten
                    // Suche nach nächstgrößerem Element (vom zu löschenden Key aus betrachtet)
                    nodePointer = DeleteMethods.deleteFromInnerNode(nodePointer, key);
                }
                boolean underflow = nodePointer.hasUnderflown();   // Prüfung auf Underflow
                while (underflow) {
                    boolean balanced;
                    int nodePointerIndex = nodePointer.parent.sons.indexOf(nodePointer);

                    balanced = DeleteMethods.balance(nodePointer,nodePointerIndex, minimumKeys);  // versuchen, Underflow auszugleichen, indem Nachbarn Schlüssel abgeben
                    if (!balanced) DeleteMethods.merge(nodePointer, nodePointerIndex);  // wenn nicht ausbalanciert werden konnte -> Merge

                    if (nodePointer.parent != root) {  // wenn parent von nodePointer nicht die root ist, wird bei parent auf erneuten Overflow geprüft
                        nodePointer = nodePointer.parent;
                        underflow = nodePointer.hasUnderflown();
                    } else {   // wenn parent von nodePointer die root ist, wird geprüft ob diese noch Keys enthält, falls nicht ist nodePointer die neue root
                        if (root.keys.size() > 0) underflow = false;
                        else {
                            nodePointer.parent = null;
                            root = nodePointer;
                            underflow = false;
                        }
                    }
                }
            }
        }
    }

    Node search(int key) {
        Node nodeThatContainsKey;
        int searchCost = 1;

        nodePointer = root;
        if (nodePointer.keys.contains(key)) nodeThatContainsKey =  nodePointer;
        else {
            while (!nodePointer.sons.isEmpty()) {
                for (int i = 0; i<nodePointer.keys.size(); i++) {
                    if (key < nodePointer.keys.get(i)) {
                        searchCost++;
                        nodePointer = nodePointer.sons.get(i);
                        if (nodePointer.keys.contains(key)) {
                            nodePointer.searchCost = searchCost;
                            return nodePointer;
                        }
                        break;
                    } else if (key > nodePointer.keys.get(i) && i == nodePointer.keys.size()-1) {
                        searchCost++;
                        nodePointer = nodePointer.sons.get(i+1);
                        if (nodePointer.keys.contains(key)) {
                            nodePointer.searchCost = searchCost;
                            return nodePointer;
                        }
                        break;
                    }
                }
            }
            if (nodePointer.keys.contains(key)) nodeThatContainsKey = nodePointer;
            else nodeThatContainsKey = null;
        }
        if (nodeThatContainsKey != null) nodeThatContainsKey.searchCost = searchCost;
        return nodeThatContainsKey;
    }

    public void printTree() {
        System.out.println("\t\t");
        System.out.println(root.keys);
        for (Node node : root.sons) {
            System.out.print(node.keys + "\t");
            System.out.print(node.parent);
        }
        System.out.println();
    }
}
