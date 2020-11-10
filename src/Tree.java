import java.util.ArrayList;
import java.util.List;

public class Tree {
    static int numberOfNodes = 0;
    int m;
    static List<Node> nodeList = new ArrayList<Node>();
    Node root;
    static Node nodePointer;
    // static int[] keys = {1, 10, 20, 15, 16, 2, 3, 4, 5, 6, 7, 8, 17, 18, 19};
    // static int[] keys = {5};
    static int treeHeight = 1;
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

    void addKey(int key) {
        if (root.sons.size() == 0) {   // Prüfung, ob der Baum lediglich aus einem einzigen Knoten besteht
            nodePointer = root;
            if (!nodePointer.keys.contains(key)) {   // Neuer Key wird eingefügt, sofern er nicht bereits vorhanden ist
                nodePointer.insertKey(key);
                boolean overflow = nodePointer.hasOverflown();

                if (overflow) {   // Wurzel wird gesplittet, falls Anzahl zulässiger Schlüssel pro Knoten überschritten wurde
                    int splitKey = (int)Math.floor((m-1)/2);
                    root = InsertMethods.splitRoot(nodePointer, splitKey, m);
                    treeHeight++;
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
                        treeHeight++;
                        overflow = false;
                    }
                }
            }
        }
    }

    void delete(int key) {
        if (root.sons.size() == 0) {  // Löschen aus Wurzel (wenn diese noch keine Söhne besitzt
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
                    int keyIndex = nodePointer.keys.indexOf(key);
                    Node nodeToSwapKey = nodePointer.sons.get(keyIndex+1);
                    while (nodeToSwapKey.sons.size() > 0) {
                        nodeToSwapKey = nodeToSwapKey.sons.get(0);
                    }
                    nodePointer.keys.remove(keyIndex);
                    nodePointer.keys.add(keyIndex, nodeToSwapKey.keys.get(0));
                    nodeToSwapKey.keys.remove(0);
                    nodePointer = nodeToSwapKey;
                }

                boolean underflow = nodePointer.hasUnderflown();
                System.out.println(underflow);
                while (underflow) {
                    if (nodePointer.parent != null) {
                        boolean canBalance = false;
                        int nodePointerIndex = nodePointer.parent.sons.indexOf(nodePointer);
                        Node neighbor;
                        if (nodePointerIndex != 0) {  // Prüfung, ob linker Nachbar vorhanden ist und dieser Keys abgeben kann
                            neighbor = nodePointer.parent.sons.get(nodePointerIndex-1);
                            if (neighbor.keys.size() > minimumKeys) {
                                canBalance = true;

                                nodePointer.keys.add(0, nodePointer.parent.keys.get(nodePointerIndex-1));   // Parent gibt Key an Underflow-Node ab
                                nodePointer.parent.keys.add(nodePointerIndex-1, neighbor.keys.get(neighbor.keys.size()-1));  // linker Nachbar gibt größten Key an Parent ab
                                neighbor.keys.remove(neighbor.keys.size()-1);   // größter Key aus linkem Nachbar wird entfernt

                                if (nodePointer.sons.size() != 0) {  // falls die Knoten Söhne haben, wird der nun überschüssige Knoten vom Nachbar an den ursprünglichen Underflow-Knoten gehängt
                                    nodePointer.sons.add(0, neighbor.sons.get(neighbor.sons.size()-1));
                                    neighbor.sons.remove(neighbor.sons.size()-1);
                                    nodePointer.sons.get(0).parent = nodePointer;
                                }

                            }
                        } else if (nodePointerIndex != nodePointer.parent.sons.size()-1) {  // Prüfung, ob rechter Nachbar vorhanden ist und dieser Keys abgeben kann
                            neighbor = nodePointer.parent.sons.get(nodePointerIndex+1);
                            if (neighbor.keys.size() > minimumKeys) {
                                canBalance = true;

                                nodePointer.keys.add(nodePointer.parent.keys.get(nodePointerIndex));  // Parent gibt Key an Underflow-Node ab
                                nodePointer.parent.keys.add(nodePointerIndex, neighbor.keys.get(0));   // rechter Nachbar gibt kleinsten Key an Parent ab
                                neighbor.keys.remove(0);  // kleinster Key aus rechtem Nachbar wird entfernt

                                if (nodePointer.sons.size() != 0) {   // Umhängen des Sohns
                                    nodePointer.sons.add(neighbor.sons.get(0));
                                    neighbor.sons.remove(0);
                                    nodePointer.sons.get(nodePointer.sons.size()-1).parent = nodePointer;
                                }
                            }
                        }

                        if (!canBalance) {
                            // MERGE
                        }

                        nodePointer = nodePointer.parent;
                        underflow = nodePointer.hasUnderflown();
                    }

                }
            }

        }
    }

    public void printTree() {
        System.out.println("HEIGHT: " + treeHeight);
        System.out.println("\t\t");
        System.out.println(root.keys);
        for(Node node : root.sons) {
            System.out.print(node.keys + "\t");
            System.out.print(node.parent);
        }
        System.out.println();
        if (treeHeight == 3) {
            for (Node node : root.sons) {
                for (Node child : node.sons) {
                    System.out.print(child.keys);
                    System.out.print(child.parent);
                }
            }
        }
    }
}
