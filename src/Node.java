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

    boolean insertKey(int key) {
        if (keys.size() < m-1) {
            keys.add(key);
            Collections.sort(keys);
            // System.out.println(keys);
            return true;
        } else {
            keys.add(key);
            Collections.sort(keys);
            return false;
        }
    }
}
