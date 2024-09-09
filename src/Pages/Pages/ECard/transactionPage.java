package Pages.Pages.ECard;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Objects;

public class transactionPage extends JDialog {
    private final LinkedList<String> history = new LinkedList<>();
    JScrollPane scrollPane;
    JTextArea textArea = new JTextArea();
    public transactionPage(String transactionHistory) {
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(400, 550);
        setTitle("账单");
        textArea.setEditable(false);
        textArea.setFont(new Font("微软雅黑", Font.BOLD, 18));
        if (!transactionHistory.isEmpty()) {
            String[] items = transactionHistory.split(";");

            for (String item : items)
                if (!Objects.equals(item, ""))
                    history.add(item);

            for (String str : history)
                textArea.append(str + "\n");
        }
        else
            textArea.append("目前没有账单明细");

        scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setHistory(String transactionHistory) {
        history.clear();
        textArea.setText("");
        if (!transactionHistory.isEmpty()) {
            String[] items = transactionHistory.split(";");
            for (String item : items)
                if (!Objects.equals(item, ""))
                    history.add(item);
            for (String str : history)
                textArea.append(str + "\n");
        }
        else
            textArea.append("目前没有账单明细");
        scrollPane.repaint();
    }

}
