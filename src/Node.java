import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Node {
    List<Integer> keys = new ArrayList<Integer>();
    List<Node> sons = new ArrayList<Node>();
    Node parent;

    int m;

    public Node(int m) {
        this.m = m;
        for (int key : keys) {
            this.keys.add(key);
        }
        Collections.sort(this.keys);
    }

    public Node(int m, int key) {
        this(m);
        keys.add(key);
    }

    public Node(int m, List<Integer> keyList) {
        this(m);
        this.keys = keyList;
    }

    // key wird nur dann eingefügt, wenn dieser nicht bereits vorhanden ist, ansonsten wird insert abgewiesen und false zurückgegeben
    boolean insertKey(int key) {
        if (keys.contains(key)) {
            return false;
        } else {
            keys.add(key);
            Collections.sort(keys);
            return true;
        }
    }
    // gibt true zurück, wenn der Knoten im Overflow ist
    boolean hasOverflown() {
        return keys.size() > m - 1;
    }
}
