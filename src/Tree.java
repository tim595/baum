import java.util.ArrayList;
import java.util.List;

public class Tree {
    static int numberOfNodes = 0;
    int m;
    static List<Node> nodeList = new ArrayList<Node>();
    static Node root;
    static Node nodePointer;
    static int[] keys = {1, 10, 20, 15, 16, 2, 3, 4, 5, 6, 7, 8, 17, 18, 19};
    static int treeHeight = 1;

    Tree(int m) {
        this.m = m;
        root = new Node(m);
        if (keys != null) {
            for (int key : keys) {
                addKey(key);
            }
        }
    }

    Node getRoot() {
        return this.root;
    }

    int getTreeHeight() {
        return this.getTreeHeight();
    }

    /*
    public static void main(String[] args) {
        int[] entryArray = {40, 17, 19, 2, 3, 1, 4, 5};

        root = new Node(m);
        numberOfNodes++;
        nodeList.add(root);

        for (int value : entryArray) {
            System.out.println();
            System.out.print("Next input: " + value);
            addKey(value);
            printTree();
            System.out.println();
        }

        // printTree();

        System.out.println(root.keys);
        System.out.println(root.sons);
        System.out.println(root.sons.get(0).keys);
        System.out.println(root.sons.get(1).keys);



    }*/


    void addKey(int key) {
        if (root.sons.size() == 0) {
            boolean overflow = !root.insertKey(key);
            if (overflow) {
                int splitKey = (int)Math.floor((m-1)/2);

                root.sons.add(0, new Node(m));
                root.sons.add(1, new Node(m));
                root.sons.get(0).parent = root;
                root.sons.get(1).parent = root;

                for (int i = 0; i<splitKey; i++) {
                    root.sons.get(0).insertKey(root.keys.get(i));
                }
                for (int j = m-1; j>splitKey; j--) {
                    root.sons.get(1).insertKey(root.keys.get(j));
                }

                for (int k = 0; k<splitKey; k++) {
                    root.keys.remove(0);
                    root.keys.remove(root.keys.size()-1);
                }
                treeHeight++;
            }
        } else {
            nodePointer = root;
            while (!nodePointer.sons.isEmpty()) {
                // System.out.println("1");
                for (int i = 0; i<nodePointer.keys.size(); i++) {
                    if (key < nodePointer.keys.get(i)) {
                        nodePointer = nodePointer.sons.get(i);
                        break;
                    } else if (key > nodePointer.keys.get(i) && i == nodePointer.keys.size()-1) {
                        nodePointer = nodePointer.sons.get(i+1);
                        break;
                    }
                }
            }
            boolean overflow = !nodePointer.insertKey(key);
            if (overflow) {
                while (overflow) {
                    if (nodePointer.parent != null) {
                        int splitKey = (int)Math.floor((m-1)/2);
                        int nodeIndex = nodePointer.parent.sons.indexOf(nodePointer);
                        List<Integer> keysLeft = new ArrayList<Integer>();
                        List<Integer> keysRight = new ArrayList<Integer>();
                        System.out.println("INDEX: " + nodeIndex);

                        for (int i = 0; i<splitKey; i++) {
                            keysLeft.add(nodePointer.keys.get(i));
                            keysRight.add(0, nodePointer.keys.get(nodePointer.keys.size()-1-i));
                        }

                        // System.out.println(keysLeft);
                        // System.out.println(keysRight);

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

                        // nodePointer = nodePointer.parent;
                        overflow = !nodePointer.parent.insertKey(nodePointer.keys.get(splitKey));
                        nodePointer = nodePointer.parent;
                        // System.out.println(overflow);


                        // nodePointer.parent.keys.add(nodePointer.keys.get(splitKey)); // TODO hier dann auf neuen overflow prüfen

                        // System.out.println("test");
                        // System.out.println(root.keys);
                        // System.out.println(root.sons);
                    } else {
                        int splitKey = (int)Math.floor((m-1)/2);

                        // int nodeIndex = nodePointer.parent.sons.indexOf(nodePointer);
                        List<Integer> keysLeft = new ArrayList<Integer>();
                        List<Integer> keysRight = new ArrayList<Integer>();

                        for (int i = 0; i<splitKey; i++) {
                            keysLeft.add(nodePointer.keys.get(i));
                            keysRight.add(0, nodePointer.keys.get(nodePointer.keys.size()-1-i));
                        }

                        root = new Node(m, nodePointer.keys.get(splitKey));

                        root.sons.add(0, new Node(m, keysLeft));
                        root.sons.add(1, new Node(m, keysRight));
                        root.sons.get(0).parent = root;
                        root.sons.get(1).parent = root;

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
                        treeHeight++;
                        overflow = false;
                    }
                }

                /*
                nodePointer.sons.add(0, new Node(m));
                nodePointer.sons.add(1, new Node(m));

                for (int i = 0; i<splitKey; i++) {
                    nodePointer.sons.get(0).insertKey(nodePointer.keys.get(i));
                }
                for (int j = m-1; j>splitKey; j--) {
                    nodePointer.sons.get(1).insertKey(nodePointer.keys.get(j));
                }

                for (int k = 0; k<splitKey; k++) {
                    nodePointer.keys.remove(0);
                    nodePointer.keys.remove(nodePointer.keys.size()-1);
                }

                 */
            }
            // System.out.println("asdf" + nodePointer.keys);
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
