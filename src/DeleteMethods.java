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
}
