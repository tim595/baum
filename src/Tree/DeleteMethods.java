package Tree;

import java.util.Optional;
import java.util.function.Consumer;

public class DeleteMethods {
    public static Optional<Node> getNodeForDelete(Node nodePointer, int key) {  // suche nach Delete-Node -> gibt null zurück, wenn keine Node mit übergebenen Key gefunden wird
        if (nodePointer.getKeys().contains(key)) return Optional.of(nodePointer);
        else {
            while (!nodePointer.getSons().isEmpty()) {
                for (int i = 0; i<nodePointer.getKeys().size(); i++) {
                    if (key < nodePointer.getKeys().get(i)) {
                        nodePointer = nodePointer.getSons().get(i);
                        if (nodePointer.getKeys().contains(key)) return Optional.of(nodePointer);
                        break;
                    } else if (key > nodePointer.getKeys().get(i) && i == nodePointer.getKeys().size()-1) {
                        nodePointer = nodePointer.getSons().get(i+1);
                        if (nodePointer.getKeys().contains(key)) return Optional.of(nodePointer);
                        break;
                    }
                }
            }
            if (nodePointer.getKeys().contains(key)) return Optional.of(nodePointer);
            else return Optional.empty();
        }
    }

    public static Consumer<Node> deleteAndResolveUnderflows(int key, int minimumKeys) {
        return (Node nodePointer) -> {
            if (nodePointer.getSons().size() == 0) { // Löschen aus Blatt
                nodePointer.removeKey(key);
            } else {  // Löschen aus innerem Knoten
                nodePointer = DeleteMethods.deleteFromInnerNode(nodePointer, key);
            }
            resolveUnderflows(nodePointer, minimumKeys);
        };
    }

    private static void resolveUnderflows(Node nodePointer, int minimumKeys) {
        boolean underflow = nodePointer.hasUnderflown();   // Prüfung auf Underflow
        while (underflow) {
            boolean balanced;
            int nodePointerIndex = nodePointer.getParent().getSons().indexOf(nodePointer);

            balanced = DeleteMethods.balance(nodePointer,nodePointerIndex, minimumKeys);  // versuchen, Underflow auszugleichen, indem Nachbarn Schlüssel abgeben
            if (!balanced) DeleteMethods.merge(nodePointer, nodePointerIndex);  // wenn nicht ausbalanciert werden konnte -> Merge

            if (nodePointer.getParent() != Tree.root) {  // wenn parent von nodePointer nicht die root ist, wird bei parent auf erneuten Overflow geprüft
                nodePointer = nodePointer.getParent();
                underflow = nodePointer.hasUnderflown();
            } else {   // wenn parent von nodePointer die root ist, wird geprüft ob diese noch Keys enthält, falls nicht ist nodePointer die neue root
                if (Tree.root.getKeys().size() > 0) underflow = false;
                else {
                    nodePointer.setParent(null);
                    Tree.root = nodePointer;
                    underflow = false;
                }
            }
        }
    }

    public static boolean balance(Node nodePointer, int nodePointerIndex, int minimumKeys) {
        boolean balanced = false;

        if (nodePointerIndex != 0) {  // Prüfung, ob linker Nachbar vorhanden ist
            balanced = balanceFromLeft(nodePointer, nodePointerIndex, minimumKeys);
        } else if (nodePointerIndex != nodePointer.getParent().getSons().size()-1) {  // Prüfung, ob rechter Nachbar vorhanden ist
            balanced = balanceFromRight(nodePointer, nodePointerIndex, minimumKeys);
        }
        return balanced;
    }

    // Linker Nachbar gibt Element ab
    private static boolean balanceFromLeft(Node nodePointer, int nodePointerIndex, int minimumKeys) {
        Node neighbor = nodePointer.getParent().getSons().get(nodePointerIndex-1);
        if (neighbor.getKeys().size() > minimumKeys) {

            nodePointer.insertKeyAtIndex(0, nodePointer.getParent().getKeys().get(nodePointerIndex-1));   // Parent gibt Key an Underflow-Tree.Node ab
            nodePointer.getParent().removeKeyAtIndex(nodePointerIndex-1);
            nodePointer.getParent().insertKeyAtIndex(nodePointerIndex-1, neighbor.getKeys().get(neighbor.getKeys().size()-1));  // linker Nachbar gibt größten Key an Parent ab
            neighbor.removeKeyAtIndex(neighbor.getKeys().size()-1);   // größter Key aus linkem Nachbar wird entfernt

            if (nodePointer.getSons().size() != 0) {  // falls die Knoten Söhne haben, wird der nun überschüssige Knoten vom Nachbar an den ursprünglichen Underflow-Knoten gehängt
                nodePointer.setSon(0, neighbor.getSons().get(neighbor.getSons().size()-1));
                neighbor.removeSon(neighbor.getSons().size()-1);
                nodePointer.getSons().get(0).setParent(nodePointer);
            }
            return true;
        } else return false;
    }

    // Rechter Nachbar gibt Element ab, ähnlicher Ablauf wie in balanceFromRight()
    private static boolean balanceFromRight(Node nodePointer, int nodePointerIndex, int minimumKeys) {
        Node neighbor = nodePointer.getParent().getSons().get(nodePointerIndex+1);
        if (neighbor.getKeys().size() > minimumKeys) {

            nodePointer.insertKey(nodePointer.getParent().getKeys().get(nodePointerIndex));
            nodePointer.getParent().removeKeyAtIndex(nodePointerIndex);
            nodePointer.getParent().insertKeyAtIndex(nodePointerIndex, neighbor.getKeys().get(0));
            neighbor.removeKeyAtIndex(0);

            if (nodePointer.getSons().size() != 0) {
                nodePointer.setSon(neighbor.getSons().get(0));
                neighbor.removeSon(0);
                nodePointer.getSons().get(nodePointer.getSons().size()-1).setParent(nodePointer);
            }
            return true;
        } else return false;
    }

    public static Node deleteFromInnerNode(Node nodePointer, int key) {
        int keyIndex = nodePointer.getKeys().indexOf(key);
        Node nodeToSwapKey = nodePointer.getSons().get(keyIndex+1);
        while (nodeToSwapKey.getSons().size() > 0) {   // Suche nach nächstgrößerem Element (vom zu löschenden Key aus betrachtet)
            nodeToSwapKey = nodeToSwapKey.getSons().get(0);
        }
        nodePointer.removeKeyAtIndex(keyIndex);   // zu löschender Key wird mit nächstgrößerem Element vertauscht und anschließend gelöscht
        nodePointer.insertKeyAtIndex(keyIndex, nodeToSwapKey.getKeys().get(0));
        nodeToSwapKey.removeKeyAtIndex(0);
        nodePointer = nodeToSwapKey;

        return nodePointer;
    }

    public static void merge(Node nodePointer, int nodePointerIndex) {
        if (nodePointerIndex > 0) {
            DeleteMethods.mergeWithLeft(nodePointer, nodePointerIndex);
        } else if (nodePointerIndex < nodePointer.getParent().getSons().size()-1) {  // Merge mit rechtem Nachbar, falls kein linker Nachbar vorhanden ist
            DeleteMethods.mergeWithRight(nodePointer, nodePointerIndex);
        }
    }

    public static void mergeWithLeft(Node nodePointer, int nodePointerIndex) {
        Node neighbor = nodePointer.getParent().getSons().get(nodePointerIndex-1);
        for (int neighborKey : neighbor.getKeys()) {  // Alle Keys vom Nachbar zu Underflow-Knoten hinzufügen
            nodePointer.insertKey(neighborKey);
        }
        nodePointer.insertKey(nodePointer.getParent().getKeys().get(nodePointerIndex-1));  // Key aus parent in Underflow-Knoten hinzufügen und aus Parent entfernen
        nodePointer.getParent().removeKeyAtIndex(nodePointerIndex-1);

        if (nodePointer.getSons().size() != 0) {
            for (int i = neighbor.getSons().size()-1; i>=0; i--) {   // Falls vorhanden, Söhne von Nachbar zu Underflow-Knoten hinzufügen
                nodePointer.setSon(0, neighbor.getSons().get(i));
                neighbor.getSons().get(i).setParent(nodePointer);
            }
        }
        nodePointer.getParent().removeSon(nodePointerIndex-1);   // Referenz auf Nachbar von Parent entfernen
    }

    public static void mergeWithRight(Node nodePointer, int nodePointerIndex) {  // Ablauf vergleichbar mit mergeWithLeft()
        Node neighbor = nodePointer.getParent().getSons().get(nodePointerIndex+1);
        for (int neighborKey : neighbor.getKeys()) {
            nodePointer.insertKey(neighborKey);
        }
        nodePointer.insertKey(nodePointer.getParent().getKeys().get(nodePointerIndex));
        nodePointer.getParent().removeKeyAtIndex(nodePointerIndex);

        if (nodePointer.getSons().size() != 0) {
            for (int i = 0; i<neighbor.getSons().size(); i++) {
                nodePointer.setSon(neighbor.getSons().get(i));
                neighbor.getSons().get(i).setParent(nodePointer);
            }
        }
        nodePointer.getParent().removeSon(nodePointerIndex+1);
    }
}
