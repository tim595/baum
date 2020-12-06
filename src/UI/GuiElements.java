package UI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.WindowEvent;
import java.util.List;

public class GuiElements {
    public static int showWelcomeDialogue(List<Integer> csvKeys) throws InterruptedException {
        Box box = new Box(1);
        box.setSize(100, 250);
        box.add(new JLabel("Welcome to the b-tree simulator!"));
        JLabel orderLabel = new JLabel("First, enter the order of the tree (minimum: 3):");
        orderLabel.setBorder(new EmptyBorder(15, 0, 10, 0));
        box.add(orderLabel);
        JTextField order = new JTextField();
        box.add(order);
        JLabel question = new JLabel("Do you want to build the tree automatically via a csv file,");
        question.setBorder(new EmptyBorder(20, 0, 0, 0));
        box.add(question);
        box.add(new JLabel("or do you want to build it manually?"));

        String orderString = "";
        Object[] o = {"Automatic input via csv",
                "Manual input"};

        int optionVal = JOptionPane.NO_OPTION;
        while (orderString.equals("") && optionVal != JOptionPane.CLOSED_OPTION) {
            optionVal = JOptionPane.showOptionDialog(null, box, "Welcome", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    o, o[0]);
            orderString = order.getText();
        }
        if (optionVal == JOptionPane.YES_OPTION) GuiMethods.pickCSV(csvKeys);

        int orderInt;
        try {
            orderInt = Integer.parseInt(orderString.trim());
            if (orderInt < 3) orderInt = 3;
        } catch (NumberFormatException e) {  // per default wird m auf 5 gesetzt
            System.out.println("Order input is not a number!");
            orderInt = 5;
        }
        return orderInt;
    }

    public static int showChangeOrderDialog(String newOrder, List<Integer> keysToInsert, List<Integer> keysToDelete, List<Integer> csvKeys, JFrame f) {
        int changedOrder = 5;
        try {
            changedOrder = Integer.parseInt(newOrder.trim());
            if (changedOrder < 3) changedOrder = 3;
            keysToInsert.clear();
            keysToDelete.clear();
            csvKeys.clear();
            f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));

            /*
            Box box = new Box(1);
            box.setSize(100, 250);
            box.add(new JLabel("Do you want to build the new tree automatically via a csv file,"));
            box.add(new JLabel("or do you want to build it manually?"));

            Object[] o = {"Automatic input via csv",
                    "Manual input"};

            int newOptionVal = JOptionPane.NO_OPTION;
            newOptionVal = JOptionPane.showOptionDialog(null, box, "", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    o, o[0]);

            if (newOptionVal == JOptionPane.YES_OPTION) {
                GuiMethods.pickCSV(csvKeys);
                return changedOrder;
            }
             */
        } catch (NumberFormatException nanException) {
            System.out.println("New order input is not a number");
        }
        return changedOrder;
    }
}
