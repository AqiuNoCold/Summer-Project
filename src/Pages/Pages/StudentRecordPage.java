package Pages.Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentRecordPage extends JFrame {
    public StudentRecordPage() {
        setTitle("学籍管理页面");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示窗口
        setLayout(new BorderLayout());

        JLabel label = new JLabel("学籍管理页面内容", SwingConstants.CENTER);
        JButton backButton = new JButton("返回");

        add(label, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigateBack();
            }
        });
    }

    private void navigateBack() {
        new NavigationPage().setVisible(true);
        dispose(); // 关闭当前页面
    }
}
