package Pages.Pages.ECard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Objects;

public class transactionPage extends JFrame {
    private LinkedList<String> history=new LinkedList<>();

    public transactionPage(String transactionHistory) {

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(400, 550);
        setTitle("账单");

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("", Font.BOLD, 17));
        if(transactionHistory.length()>0) {
        String[] items = transactionHistory.split(";");

        for (String item : items)
            if(!Objects.equals(item, ""))
                history.add(item);

        for (String str : history)
            textArea.append(str + "\n");
        }
        else {
            textArea.append("目前没有账单明细");
        }
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
    }
}
