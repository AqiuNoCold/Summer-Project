package Pages.Pages;

import Pages.MainApp;
import Pages.Utils.*;
import Pages.Pages.Library.*;
import vCampus.Entity.Books.BookUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LibraryPage extends JFrame {
    private BookUser bookUser;
    private JPanel contentPanel;

    public LibraryPage() {
        setTitle("图书馆页面");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示窗口
        setLayout(new BorderLayout());

        // 调用login方法并存储返回的BookUser对象
        login();

        // 创建内容面板
        contentPanel = new JPanel(new CardLayout());
        contentPanel.add(new HomePage(), "home");
        contentPanel.add(new BookshelvesPage(), "bookshelves");
        contentPanel.add(new ExplorePage(), "explore");
        contentPanel.add(new ProfilePage(), "profile");

        add(contentPanel, BorderLayout.CENTER);

        // 创建底部菜单栏
        JPanel menuPanel = new JPanel(new GridLayout(1, 4));
        menuPanel.setBorder(BorderFactory.createLineBorder(new Color(144, 238, 144), 5)); // 浅绿色边框，宽度为5像素

        JButton homeButton = createImageButton("首页", "/imgs/home.svg");
        JButton bookshelvesButton = createImageButton("书架", "/imgs/bookshelves.svg");
        JButton exploreButton = createImageButton("探索", "/imgs/explore.svg");
        JButton profileButton = createImageButton("个人主页", "/imgs/person.svg");

        menuPanel.add(homeButton);
        menuPanel.add(bookshelvesButton);
        menuPanel.add(exploreButton);
        menuPanel.add(profileButton);

        add(menuPanel, BorderLayout.SOUTH);

        // 按钮点击事件
        homeButton.addActionListener(e -> switchPage("home"));
        bookshelvesButton.addActionListener(e -> switchPage("bookshelves"));
        exploreButton.addActionListener(e -> switchPage("explore"));
        profileButton.addActionListener(e -> switchPage("profile"));
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

    private void switchPage(String pageName) {
        CardLayout cl = (CardLayout) (contentPanel.getLayout());
        cl.show(contentPanel, pageName);
    }

    private JButton createImageButton(String text, String imagePath) {
        ImageIcon icon = SVGImageLoader.loadSVGImage(imagePath, 64, 64);

        if (icon == null || icon.getIconWidth() == -1) { // 判断图片是否加载成功
            System.err.println("Error: Could not load image at " + imagePath);
        }

        JButton button = new JButton(text, icon);
        button.setHorizontalTextPosition(SwingConstants.CENTER); // 文字在图片中心
        button.setVerticalTextPosition(SwingConstants.BOTTOM); // 文字在图片下方
        button.setFont(new Font("微软雅黑", Font.BOLD, 14)); // 设置字体
        button.setBackground(new Color(255, 255, 255)); // 设置背景颜色
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 增加按钮内边距
        return button;
    }

    private void navigateBack() {
        new NavigationPage().setVisible(true);
        dispose(); // 关闭当前页面
    }
}