public class DeleteMethods {
    public static Node searchNodeForDelete(Node nodePointer, int key) {
        if (nodePointer.keys.contains(key)) return nodePointer;
        else {
            while (!nodePointer.sons.isEmpty()) {
                for (int i = 0; i<nodePointer.keys.size(); i++) {
                    if (key < nodePointer.keys.get(i)) {
                        nodePointer = nodePointer.sons.get(i);
                        if (nodePointer.keys.contains(key)) return nodePointer;
                        break;
                    } else if (key > nodePointer.keys.get(i) && i == nodePointer.keys.size()-1) {
                        nodePointer = nodePointer.sons.get(i+1);
                        if (nodePointer.keys.contains(key)) return nodePointer;
                        break;
                    }
                }
            }
            if (nodePointer.keys.contains(key)) return nodePointer;
            else return null;
        }
    }

    public static boolean balance(Node nodePointer, int nodePointerIndex, int minimumKeys) {
        boolean balanced = false;

        if (nodePointerIndex != 0) {  // Prüfung, ob linker Nachbar vorhanden ist
            balanced = balanceFromLeft(nodePointer, nodePointerIndex, minimumKeys);
        } else if (nodePointerIndex != nodePointer.parent.sons.size()-1) {  // Prüfung, ob rechter Nachbar vorhanden ist
            balanced = balanceFromRight(nodePointer, nodePointerIndex, minimumKeys);
        }
        return balanced;
    }

    // Linker Nachbar gibt Element ab
    private static boolean balanceFromLeft(Node nodePointer, int nodePointerIndex, int minimumKeys) {
        Node neighbor = nodePointer.parent.sons.get(nodePointerIndex-1);
        if (neighbor.keys.size() > minimumKeys) {

            nodePointer.keys.add(0, nodePointer.parent.keys.get(nodePointerIndex-1));   // Parent gibt Key an Underflow-Node ab
            nodePointer.parent.keys.remove(nodePointerIndex-1);
            nodePointer.parent.keys.add(nodePointerIndex-1, neighbor.keys.get(neighbor.keys.size()-1));  // linker Nachbar gibt größten Key an Parent ab
            neighbor.keys.remove(neighbor.keys.size()-1);   // größter Key aus linkem Nachbar wird entfernt

            if (nodePointer.sons.size() != 0) {  // falls die Knoten Söhne haben, wird der nun überschüssige Knoten vom Nachbar an den ursprünglichen Underflow-Knoten gehängt
                nodePointer.sons.add(0, neighbor.sons.get(neighbor.sons.size()-1));
                neighbor.sons.remove(neighbor.sons.size()-1);
                nodePointer.sons.get(0).parent = nodePointer;
            }
            return true;
        } else return false;
    }

    // Rechter Nachbar gibt Element ab, ähnlicher Ablauf wie in balanceFromRight()
    private static boolean balanceFromRight(Node nodePointer, int nodePointerIndex, int minimumKeys) {
        Node neighbor = nodePointer.parent.sons.get(nodePointerIndex+1);
        if (neighbor.keys.size() > minimumKeys) {

            nodePointer.keys.add(nodePointer.parent.keys.get(nodePointerIndex));
            nodePointer.parent.keys.remove(nodePointerIndex);
            nodePointer.parent.keys.add(nodePointerIndex, neighbor.keys.get(0));
            neighbor.keys.remove(0);

            if (nodePointer.sons.size() != 0) {
                nodePointer.sons.add(neighbor.sons.get(0));
                neighbor.sons.remove(0);
                nodePointer.sons.get(nodePointer.sons.size()-1).parent = nodePointer;
            }
            return true;
        } else return false;
    }

    public static Node deleteFromInnerNode(Node nodePointer, int key) {
        int keyIndex = nodePointer.keys.indexOf(key);
        Node nodeToSwapKey = nodePointer.sons.get(keyIndex+1);
        while (nodeToSwapKey.sons.size() > 0) {
            nodeToSwapKey = nodeToSwapKey.sons.get(0);
        }
        nodePointer.keys.remove(keyIndex);
        nodePointer.keys.add(keyIndex, nodeToSwapKey.keys.get(0));
        nodeToSwapKey.keys.remove(0);
        nodePointer = nodeToSwapKey;

        return nodePointer;
    }

    public static void merge(Node nodePointer, int nodePointerIndex) {
        if (nodePointerIndex > 0) {
            DeleteMethods.mergeWithLeft(nodePointer, nodePointerIndex);
        } else if (nodePointerIndex < nodePointer.parent.sons.size()-1) {  // Merge mit rechtem Nachbar, falls kein linker Nachbar vorhanden ist
            DeleteMethods.mergeWithRight(nodePointer, nodePointerIndex);
        }
    }

    public static void mergeWithLeft(Node nodePointer, int nodePointerIndex) {
        Node neighbor = nodePointer.parent.sons.get(nodePointerIndex-1);
        System.out.println(neighbor.keys);
        for (int neighborKey : neighbor.keys) {  // Alle Keys vom Nachbar zu Underflow-Knoten hinzufügen
            nodePointer.insertKey(neighborKey);
        }
        nodePointer.insertKey(nodePointer.parent.keys.get(nodePointerIndex-1));  // Key aus parent in Underflow-Knoten hinzufügen und aus Parent entfernen
        nodePointer.parent.keys.remove(nodePointerIndex-1);

        if (nodePointer.sons.size() != 0) {
            for (int i = neighbor.sons.size()-1; i>=0; i--) {   // Falls vorhanden, Söhne von Nachbar zu Underflow-Knoten hinzufügen
                nodePointer.sons.add(0, neighbor.sons.get(i));
                neighbor.sons.get(i).parent = nodePointer;
            }
        }
        nodePointer.parent.sons.remove(nodePointerIndex-1);   // Referenz auf Nachbar von Parent entfernen
    }

    public static void mergeWithRight(Node nodePointer, int nodePointerIndex) {
        Node neighbor = nodePointer.parent.sons.get(nodePointerIndex+1);
        for (int neighborKey : neighbor.keys) {
            nodePointer.insertKey(neighborKey);
        }
        nodePointer.insertKey(nodePointer.parent.keys.get(nodePointerIndex));
        nodePointer.parent.keys.remove(nodePointerIndex);

        if (nodePointer.sons.size() != 0) {
            for (int i = 0; i<neighbor.sons.size(); i++) {
                nodePointer.sons.add(neighbor.sons.get(i));
                neighbor.sons.get(i).parent = nodePointer;
            }
        }
        nodePointer.parent.sons.remove(nodePointerIndex+1);
    }
}
