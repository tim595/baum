package tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Node {
    private List<Integer> keys = new ArrayList<Integer>();
    private List<Node> sons = new ArrayList<Node>();

    private Node parent;
    private int m;
    private int searchCost;

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


    public int getSearchCost() {
        return searchCost;
    }

    public void setSearchCost(int cost) {
        this.searchCost = cost;
    }

    public List<Node> getSons() {
        return sons;
    }

    public void setSon(int index, Node son) {
        sons.add(index, son);
    }

    public void setSon(Node son) {
        sons.add(son);
    }

    public void removeSon(Node son) {
        sons.remove(son);
    }

    public void removeSon(int index) {
        sons.remove(index);
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Integer> getKeys() {
        return keys;
    }

    // key wird nur dann eingefügt, wenn dieser nicht bereits vorhanden ist, ansonsten wird insert abgewiesen und false zurückgegeben
    public void insertKey(int key) {
        keys.add(key);
        Collections.sort(keys);
    }

    // wird für underflow-Operationen genutzt, um unnötigen Sort zu vermeiden
    public void insertKeyAtIndex(int index, int key) {
        keys.add(index, key);
    }

    public void removeKey(int key) {
        keys.remove((Integer) key);
    }

    public void removeKeyAtIndex(int index) {
        keys.remove(index);
    }

    // gibt true zurück, wenn der Knoten im Overflow ist
    public boolean hasOverflown() {
        return keys.size() > m - 1;
    }

    public boolean hasUnderflown() {
        if (m%2 == 0) {
            return keys.size() < (int) Math.floor((m - 1) / 2);
        } else {
            return keys.size() < (m - 1) / 2;
        }
    }
}
