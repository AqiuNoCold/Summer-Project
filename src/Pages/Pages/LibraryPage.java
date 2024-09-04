package Pages.Pages;

import vCampus.Entity.Books.BookUser;
import Pages.MainApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LibraryPage extends JFrame {
    private BookUser bookUser;

    public LibraryPage() {
        setTitle("图书馆页面");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示窗口
        setLayout(new BorderLayout());

        // 调用login方法并存储返回的BookUser对象
        login();

        JLabel label = new JLabel("图书馆页面内容", SwingConstants.CENTER);
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

    private void login() {
        try {
            ObjectOutputStream out = MainApp.getOut();
            ObjectInputStream in = MainApp.getIn();

            // 发送请求
            out.writeObject("4"); // 图书馆模块
            out.writeObject("login");
            out.writeObject(MainApp.getCurrentUser().getId());
            out.flush();

            // 接收响应
            bookUser = (BookUser) in.readObject();

            // 打印当前用户信息到命令行
            System.out.println("当前图书馆用户: " + bookUser);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateBack() {
        new NavigationPage().setVisible(true);
        dispose(); // 关闭当前页面
    }
}