package Pages.Pages;

import Pages.MainApp;
import Pages.Utils.*;
import Pages.Pages.Library.*;
import vCampus.Entity.Books.BookUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LibraryPage extends JFrame {
    private static LibraryPage instance; // 唯一实例
    private static BookUser bookUser;
    private static JPanel contentPanel;
    private static JPanel menuPanel;
    private static JButton homeButton;
    private static JButton bookshelvesButton;
    private static JButton exploreButton;
    private static JButton profileButton;

    private LibraryPage() {
        // 私有构造函数，防止实例化
    }

    public static LibraryPage init() {
        if (instance == null) {
            instance = new LibraryPage();
            instance.setTitle("图书馆页面");
            instance.setSize(800, 600);
            instance.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            instance.setLocationRelativeTo(null); // 居中显示窗口
            instance.setLayout(new BorderLayout());

            // 调用login方法并存储返回的BookUser对象
            login();

            // 创建内容面板
            contentPanel = new JPanel(new CardLayout());
            contentPanel.add(MainApp.getHomePage(), "home");
            contentPanel.add(new BookshelvesPage(), "bookshelves");
            contentPanel.add(new ExplorePage(), "explore");
            contentPanel.add(new ProfilePage(), "profile");

            instance.add(contentPanel, BorderLayout.CENTER);

            // 创建底部菜单栏
            menuPanel = new JPanel(new GridLayout(1, 4));
            menuPanel.setBorder(BorderFactory.createLineBorder(new Color(144, 238, 144), 5)); // 浅绿色边框，宽度为5像素

            homeButton = createImageButton("首页", "/imgs/home.svg", 800, 600);
            bookshelvesButton = createImageButton("书架", "/imgs/bookshelves.svg", 800, 600);
            exploreButton = createImageButton("探索", "/imgs/explore.svg", 800, 600);
            profileButton = createImageButton("个人主页", "/imgs/person.svg", 800, 600);

            menuPanel.add(homeButton);
            menuPanel.add(bookshelvesButton);
            menuPanel.add(exploreButton);
            menuPanel.add(profileButton);

            instance.add(menuPanel, BorderLayout.SOUTH);

            // 按钮点击事件
            homeButton.addActionListener(e -> switchPage("home"));
            bookshelvesButton.addActionListener(e -> switchPage("bookshelves"));
            exploreButton.addActionListener(e -> switchPage("explore"));
            profileButton.addActionListener(e -> switchPage("profile"));

            // 添加窗口关闭事件监听器
            instance.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    navigateBack();
                }
            });

            // 添加窗口大小改变事件监听器
            instance.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    Dimension newSize = instance.getSize();
                    updateButtonIcons(newSize.width, newSize.height);
                }
            });
        }
        return instance;
    }

    private static void login() {
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

    private static void switchPage(String pageName) {
        CardLayout cl = (CardLayout) (contentPanel.getLayout());
        cl.show(contentPanel, pageName);
    }

    private static JButton createImageButton(String text, String imagePath, int windowWidth, int windowHeight) {
        ImageIcon icon = IconUtils.loadSVGImage(imagePath, windowWidth, windowHeight);
        JButton button = new JButton(text, icon);
        button.setHorizontalTextPosition(SwingConstants.CENTER); // 文字在图片中心
        button.setVerticalTextPosition(SwingConstants.BOTTOM); // 文字在图片下方
        button.setBackground(new Color(255, 255, 255)); // 设置背景颜色
        button.setFocusPainted(false);
        double scaleFactor = Math.min(windowWidth / 1920.0, windowHeight / 1080.0);
        button.setFont(new Font("微软雅黑", Font.BOLD, (int) (14 * scaleFactor))); // 根据缩放比例调整字体大小
        IconUtils.setButtonBorder(button, scaleFactor); // 设置按钮边框
        return button;
    }

    private static void updateButtonIcons(int windowWidth, int windowHeight) {
        IconUtils.updateButtonIcon(homeButton, "/imgs/home.svg", windowWidth, windowHeight);
        IconUtils.updateButtonIcon(bookshelvesButton, "/imgs/bookshelves.svg", windowWidth, windowHeight);
        IconUtils.updateButtonIcon(exploreButton, "/imgs/explore.svg", windowWidth, windowHeight);
        IconUtils.updateButtonIcon(profileButton, "/imgs/person.svg", windowWidth, windowHeight);

        double scaleFactor = Math.min(windowWidth / 1920.0, windowHeight / 1080.0);
        homeButton.setFont(new Font("微软雅黑", Font.BOLD, (int) (14 * scaleFactor)));
        bookshelvesButton.setFont(new Font("微软雅黑", Font.BOLD, (int) (14 * scaleFactor)));
        exploreButton.setFont(new Font("微软雅黑", Font.BOLD, (int) (14 * scaleFactor)));
        profileButton.setFont(new Font("微软雅黑", Font.BOLD, (int) (14 * scaleFactor)));

        IconUtils.setButtonBorder(homeButton, scaleFactor);
        IconUtils.setButtonBorder(bookshelvesButton, scaleFactor);
        IconUtils.setButtonBorder(exploreButton, scaleFactor);
        IconUtils.setButtonBorder(profileButton, scaleFactor);
    }

    private static void navigateBack() {
        new NavigationPage().setVisible(true);
    }
}