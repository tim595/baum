package Tree;

public class SearchMethods {
    public static Node getRootElement(Node nodePointer) {
        nodePointer.setSearchCost(1);
        return nodePointer;
    }

    public static Node lookupChildNodes(Node nodePointer, int key) {
        int searchCost = 1;
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
        if (nodePointer.getKeys().contains(key)) {
            nodePointer.setSearchCost(searchCost);
            return nodePointer;
        } else return null;  // wird Key nicht gefunden, gibt die Methode null zurück
    }
}
