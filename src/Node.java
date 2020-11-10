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
    void insertKey(int key) {
        keys.add(key);
        Collections.sort(keys);
    }

    void removeKey(int key) {
        keys.remove((Integer) key);
    }

    // gibt true zurück, wenn der Knoten im Overflow ist
    boolean hasOverflown() {
        return keys.size() > m - 1;
    }

    boolean hasUnderflown() {
        if (m%2 == 0) {
            if (keys.size() < Math.floor((m-1)/2)) return true;
            else return true;
        } else {
            if (keys.size() < (m-1)/2) return true;
            else return false;
        }
    }
}
