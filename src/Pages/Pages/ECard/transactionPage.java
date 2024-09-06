package Pages.Pages.ECard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Objects;

public class transactionPage extends JFrame {
    private LinkedList<String> history=new LinkedList<>();
    private JButton backButton = new JButton("返回");
    public transactionPage(String transactionHistory) {
        add(backButton, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(400, 550);
        setTitle("账单");

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("", Font.BOLD, 16));
        String[] items = transactionHistory.split(";");

        for (String item : items)
            if(!Objects.equals(item, ""))
                history.add(item);

        for (String str : history)
            textArea.append(str + "\n");
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               dispose();
            }
        });
    }
}
