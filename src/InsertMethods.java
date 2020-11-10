import java.util.ArrayList;
import java.util.List;

public class InsertMethods {
    public static Node searchNodeForInsert(Node nodePointer, int key) {  // suche nach Einfüge-Node -> falls Key bereits vorhanden, wird null zurückgegeben
        if (nodePointer.keys.contains(key)) return null;
        else {
            while (!nodePointer.sons.isEmpty()) {
                for (int i = 0; i<nodePointer.keys.size(); i++) {
                    if (key < nodePointer.keys.get(i)) {
                        nodePointer = nodePointer.sons.get(i);
                        if (nodePointer.keys.contains(key)) return null;
                        break;
                    } else if (key > nodePointer.keys.get(i) && i == nodePointer.keys.size()-1) {
                        nodePointer = nodePointer.sons.get(i+1);
                        if (nodePointer.keys.contains(key)) return null;
                        break;
                    }
                }
            }
            if (nodePointer.keys.contains(key)) return null;
            else return nodePointer;
        }
    }


    public static Node splitRoot(Node nodePointer, int splitKey, int m) {
        List<Integer> keysLeft = new ArrayList<Integer>();
        List<Integer> keysRight = new ArrayList<Integer>();

        for (int i = 0; i<splitKey; i++) {
            keysLeft.add(nodePointer.keys.get(i));
            keysRight.add(0, nodePointer.keys.get(nodePointer.keys.size()-1-i));
        }
        if (m%2 == 0) keysRight.add(0, nodePointer.keys.get(splitKey+1));

        Node root = new Node(m, nodePointer.keys.get(splitKey));

        // Der Wurzel werden 2 Söhne hinzugefügt
        root.sons.add(0, new Node(m, keysLeft));
        root.sons.add(1, new Node(m, keysRight));
        root.sons.get(0).parent = root;
        root.sons.get(1).parent = root;

        return root;
    }

    public static Node splitNonRoot(Node nodePointer, int m) {
        int splitKey = (int)Math.floor((m-1)/2);
        int nodeIndex = nodePointer.parent.sons.indexOf(nodePointer);
        List<Integer> keysLeft = new ArrayList<Integer>();
        List<Integer> keysRight = new ArrayList<Integer>();

        for (int i = 0; i<splitKey; i++) {
            keysLeft.add(nodePointer.keys.get(i));
            keysRight.add(0, nodePointer.keys.get(nodePointer.keys.size()-1-i));
        }
        if (m%2 == 0) keysRight.add(0, nodePointer.keys.get(splitKey+1));

        nodePointer.parent.sons.add(nodeIndex, new Node(m, keysLeft));
        nodePointer.parent.sons.get(nodeIndex).parent = nodePointer.parent;
        nodePointer.parent.sons.add(nodeIndex+1, new Node(m, keysRight));
        nodePointer.parent.sons.get(nodeIndex+1).parent = nodePointer.parent;

        if (nodePointer.sons.size() != 0) {
            for (int i = 0; i<=splitKey; i++) {
                nodePointer.parent.sons.get(nodeIndex).sons.add(nodePointer.sons.get(i));
                nodePointer.sons.get(i).parent = nodePointer.parent.sons.get(nodeIndex);
            }
            for (int i = nodePointer.sons.size()-1; i>splitKey; i--) {
                nodePointer.parent.sons.get(nodeIndex+1).sons.add(0, nodePointer.sons.get(i));
                nodePointer.sons.get(i).parent = nodePointer.parent.sons.get(nodeIndex+1);
            }
        }
        nodePointer.parent.sons.remove(nodePointer);
        nodePointer.parent.insertKey(nodePointer.keys.get(splitKey));
        return nodePointer.parent;
    }


    public static void updateChildParentRelations(Node nodePointer, Node root, int splitKey) {
        for (int i = 0; i<nodePointer.sons.size(); i++) {
            if (i <= splitKey) {
                nodePointer.sons.get(i).parent = root.sons.get(0);
            } else {
                nodePointer.sons.get(i).parent = root.sons.get(1);
            }
        }
        for (int i = 0; i<=splitKey; i++) {
            root.sons.get(0).sons.add(nodePointer.sons.get(i));
        }
        for (int i = nodePointer.sons.size()-1; i>splitKey; i--) {
            root.sons.get(1).sons.add(0, nodePointer.sons.get(i));
        }
    }
}
