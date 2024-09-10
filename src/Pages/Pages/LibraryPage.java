package Pages.Pages;

import Pages.MainApp;
import Pages.Utils.*;
import Pages.Pages.Library.*;
import vCampus.Entity.User;
import vCampus.Entity.Books.BookUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LibraryPage extends JFrame {
    private static LibraryPage instance; // 唯一实例
    private BookUser bookUser;
    private JPanel contentPanel;
    private JPanel menuPanel;
    private JButton homeButton;
    private JButton bookshelvesButton;
    private JButton exploreButton;
    private JButton profileButton;

    private LibraryPage() {
        // 私有构造函数，防止实例化
        setTitle("图书馆页面");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示窗口
        setLayout(new BorderLayout());

        // 调用login方法并存储返回的BookUser对象
        login();

        // 创建内容面板
        contentPanel = new JPanel(new CardLayout());
        contentPanel.add(HomePage.getInstance(), "home");
        contentPanel.add(new BookshelvesPage(), "bookshelves");
        contentPanel.add(ExplorePage.getInstance(), "explore");
        contentPanel.add(new ProfilePage(), "profile");

        add(contentPanel, BorderLayout.CENTER);

        // 创建底部菜单栏
        menuPanel = new JPanel(new GridLayout(1, 4));
        menuPanel.setBorder(BorderFactory.createLineBorder(new Color(144, 238, 144), 5)); // 浅绿色边框，宽度为5像素

        homeButton = createImageButton("首页", "/imgs/home.svg", 800, 600);
        exploreButton = createImageButton("探索", "/imgs/explore.svg", 800, 600);
        bookshelvesButton = createImageButton("书架", "/imgs/bookshelves.svg", 800, 600);
        profileButton = createImageButton("个人主页", "/imgs/person.svg", 800, 600);

        menuPanel.add(homeButton);
        menuPanel.add(exploreButton);
        menuPanel.add(bookshelvesButton);
        menuPanel.add(profileButton);

        add(menuPanel, BorderLayout.SOUTH);

        // 按钮点击事件
        homeButton.addActionListener(e -> switchPage("home"));
        bookshelvesButton.addActionListener(e -> switchPage("bookshelves"));
        exploreButton.addActionListener(e -> {
            ExplorePage.getInstance().switchToDefaultMode();
            switchPage("explore");
        });
        profileButton.addActionListener(e -> switchPage("profile"));

        // 添加窗口关闭事件监听器
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                navigateBack();
            }
        });

        // 添加窗口大小改变事件监听器
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension newSize = getSize();
                updateButtonIcons(newSize.width, newSize.height);
            }
        });
    }

    public static synchronized LibraryPage getInstance() {
        if (instance == null) {
            instance = new LibraryPage();
        }
        return instance;
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

            BookUser.setCurrentUser(bookUser);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchPage(String pageName) {
        CardLayout cl = (CardLayout) (contentPanel.getLayout());
        cl.show(contentPanel, pageName);
    }

    private JButton createImageButton(String text, String imagePath, int windowWidth, int windowHeight) {
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

    private void updateButtonIcons(int windowWidth, int windowHeight) {
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

    private void navigateBack() {
        new NavigationPage().setVisible(true);
    }

    public static void main(String[] args) {
        // 设置当前用户
        MainApp.setCurrentUser(
                new User("213221715", "123456", 20, true, "ST", "213221715@seu.edu.cn", "213221715", false));

        // 初始化Socket
        MainApp.initializeSocket();

        // 显示LibraryPage窗口
        SwingUtilities.invokeLater(() -> {
            LibraryPage libraryPage = LibraryPage.getInstance();
            libraryPage.setVisible(true);
            // 添加窗口监听器，在窗口关闭时调用 MainApp.close_source()
            libraryPage.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    MainApp.close_source();
                }
            });
        });
    }
}