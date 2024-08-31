package Pages.Pages;

import vCampus.Entity.User;
import Pages.MainApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

public class NavigationPage extends JFrame {
    private User user;
    private Socket socket;

    private JButton storeButton;
    private JButton eCardButton;
    private JButton studentRecordButton;
    private JButton libraryButton;
    private JButton courseButton;

    public NavigationPage() {
        this.user = MainApp.getCurrentUser();
        this.socket = MainApp.getSocket();
        setTitle("导航页面");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1));

        storeButton = new JButton("商店页面");
        eCardButton = new JButton("一卡通页面");
        studentRecordButton = new JButton("学籍管理页面");
        libraryButton = new JButton("图书馆页面");
        courseButton = new JButton("选课页面");

        add(storeButton);
        add(eCardButton);
        add(studentRecordButton);
        add(libraryButton);
        add(courseButton);

        storeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPage(new StorePage());
            }
        });
        eCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPage(new ECardPage());
            }
        });
        studentRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPage(new StudentRecordPage());
            }
        });
        libraryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPage(new LibraryPage());
            }
        });
        courseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPage(new CoursePage());
            }
        });
    }

    private void openPage(JFrame page) {
        page.setVisible(true);
        dispose(); // 关闭导航页面
    }
}
