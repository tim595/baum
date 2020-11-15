package Tree;

import java.util.function.Consumer;

public class Tree {
    public static Node root;
    private int m;
    private Node nodePointer;
    private int minimumKeys;

    public Tree(int m) {
        this.m = m;
        root = new Node(m);
        minimumKeys = (m-1)/2;  // Ergebnis wird implizit abgerundet, somit keine Unterscheidung zw. geradem und ungeradem m notwendig
    }

    public Node getRoot() {
        return root;
    }

    public int getOrder() {
        return this.m;
    }

    public void insert(int key) {
        if (root.getSons().isEmpty()) {   // Prüfung, ob der Baum lediglich aus einem einzigen Knoten besteht
            nodePointer = root;
            InsertMethods.addKeyIfNotExisting(key, nodePointer, m);
        } else {   // Fall, dass bereits mehrere Knoten bestehen
            InsertMethods.getNodeForInsert(root, key)  // Von der Wurzel aus wird nach unten gewandert, um Einfüge-Position zu suchen
                    .ifPresent(InsertMethods.insertAndResolveOverflows(key, m));  // wenn Node gefunden wurde füge key ein und behebe overflows
        }
    }

    public void delete(int key) {
        if (root.getSons().size() == 0) {  // Löschen aus Wurzel (wenn diese noch keine Söhne besitzt)
            if (root.getKeys().contains(key)) {
                root.removeKey(key);
            }
        } else {
            nodePointer = DeleteMethods.searchNodeForDelete(root, key);
            if (nodePointer != null) {
                if (nodePointer.getSons().size() == 0) { // Löschen aus Blatt
                    nodePointer.removeKey(key);
                } else {  // Löschen aus innerem Knoten
                    nodePointer = DeleteMethods.deleteFromInnerNode(nodePointer, key);
                }
                boolean underflow = nodePointer.hasUnderflown();   // Prüfung auf Underflow
                while (underflow) {
                    boolean balanced;
                    int nodePointerIndex = nodePointer.getParent().getSons().indexOf(nodePointer);

                    balanced = DeleteMethods.balance(nodePointer,nodePointerIndex, minimumKeys);  // versuchen, Underflow auszugleichen, indem Nachbarn Schlüssel abgeben
                    if (!balanced) DeleteMethods.merge(nodePointer, nodePointerIndex);  // wenn nicht ausbalanciert werden konnte -> Merge

                    if (nodePointer.getParent() != root) {  // wenn parent von nodePointer nicht die root ist, wird bei parent auf erneuten Overflow geprüft
                        nodePointer = nodePointer.getParent();
                        underflow = nodePointer.hasUnderflown();
                    } else {   // wenn parent von nodePointer die root ist, wird geprüft ob diese noch Keys enthält, falls nicht ist nodePointer die neue root
                        if (root.getKeys().size() > 0) underflow = false;
                        else {
                            nodePointer.setParent(null);
                            root = nodePointer;
                            underflow = false;
                        }
                    }
                }
            }
        }
    }

    public Node search(int key) {
        Node nodeThatContainsKey;
        int searchCost = 1;

        nodePointer = root;
        if (nodePointer.getKeys().contains(key)) nodeThatContainsKey =  nodePointer;   // Fall, dass root den Key enthält
        else {
            while (!nodePointer.getSons().isEmpty()) {   // von root aus wird so lange nach unten gewandert, bis aktuelle Node keine Söhne besitzt oder den gesuchten Key enthält
                for (int i = 0; i<nodePointer.getKeys().size(); i++) {
                    if (key < nodePointer.getKeys().get(i)) {
                        searchCost++;
                        nodePointer = nodePointer.getSons().get(i);
                        if (nodePointer.getKeys().contains(key)) {
                            nodePointer.setSearchCost(searchCost);
                            return nodePointer;
                        }
                        break;
                    } else if (key > nodePointer.getKeys().get(i) && i == nodePointer.getKeys().size()-1) {
                        searchCost++;
                        nodePointer = nodePointer.getSons().get(i+1);
                        if (nodePointer.getKeys().contains(key)) {
                            nodePointer.setSearchCost(searchCost);
                            return nodePointer;
                        }
                        break;
                    }
                }
            }
            if (nodePointer.getKeys().contains(key)) nodeThatContainsKey = nodePointer;
            else nodeThatContainsKey = null;  // wird Key nicht gefunden, gibt die Methode null zurück
        }
        if (nodeThatContainsKey != null) nodeThatContainsKey.setSearchCost(searchCost);
        return nodeThatContainsKey;
    }
}
