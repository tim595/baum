package Tree;

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
        if (root.getSons().isEmpty()) {  // Löschen aus Wurzel (wenn diese noch keine Söhne besitzt)
            if (root.getKeys().contains(key)) {
                root.removeKey(key);
            }
        } else {
            DeleteMethods.getNodeForDelete(root, key)
                    .ifPresent(DeleteMethods.deleteAndResolveUnderflows(key, minimumKeys));
        }
    }

    public Node search(int key) {
        nodePointer = root;
        if (nodePointer.getKeys().contains(key)) {   // Fall, dass root den Key enthält
            return SearchMethods.getRootElement(nodePointer);
        } else {
            return SearchMethods.lookupChildNodes(nodePointer, key);
        }
    }
}
