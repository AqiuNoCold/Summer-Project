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
    private BookUser bookUser;
    private JPanel contentPanel;
    private JPanel menuPanel;
    private JButton homeButton;
    private JButton bookshelvesButton;
    private JButton exploreButton;
    private JButton profileButton;

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

        add(menuPanel, BorderLayout.SOUTH);

        // 按钮点击事件
        homeButton.addActionListener(e -> switchPage("home"));
        bookshelvesButton.addActionListener(e -> switchPage("bookshelves"));
        exploreButton.addActionListener(e -> switchPage("explore"));
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

    private JButton createImageButton(String text, String imagePath, int windowWidth, int windowHeight) {
        double scaleFactor = Math.min(windowWidth / 1920.0, windowHeight / 1080.0);
        int iconSize = (int) (64 * scaleFactor); // 根据缩放比例调整图标大小
        ImageIcon icon = SVGImageLoader.loadSVGImage(imagePath, iconSize, iconSize);

        if (icon == null || icon.getIconWidth() == -1) { // 判断图片是否加载成功
            System.err.println("Error: Could not load image at " + imagePath);
        }

        JButton button = new JButton(text, icon);
        button.setHorizontalTextPosition(SwingConstants.CENTER); // 文字在图片中心
        button.setVerticalTextPosition(SwingConstants.BOTTOM); // 文字在图片下方
        button.setFont(new Font("微软雅黑", Font.BOLD, (int) (14 * scaleFactor))); // 根据缩放比例调整字体大小
        button.setBackground(new Color(255, 255, 255)); // 设置背景颜色
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder((int) (10 * scaleFactor), (int) (10 * scaleFactor),
                (int) (10 * scaleFactor), (int) (10 * scaleFactor))); // 根据缩放比例调整按钮内边距
        return button;
    }

    private void updateButtonIcons(int windowWidth, int windowHeight) {
        double scaleFactor = Math.min(windowWidth / 1920.0, windowHeight / 1080.0);
        int iconSize = (int) (64 * scaleFactor);

        homeButton.setIcon(SVGImageLoader.loadSVGImage("/imgs/home.svg", iconSize, iconSize));
        bookshelvesButton.setIcon(SVGImageLoader.loadSVGImage("/imgs/bookshelves.svg", iconSize, iconSize));
        exploreButton.setIcon(SVGImageLoader.loadSVGImage("/imgs/explore.svg", iconSize, iconSize));
        profileButton.setIcon(SVGImageLoader.loadSVGImage("/imgs/person.svg", iconSize, iconSize));

        homeButton.setFont(new Font("微软雅黑", Font.BOLD, (int) (14 * scaleFactor)));
        bookshelvesButton.setFont(new Font("微软雅黑", Font.BOLD, (int) (14 * scaleFactor)));
        exploreButton.setFont(new Font("微软雅黑", Font.BOLD, (int) (14 * scaleFactor)));
        profileButton.setFont(new Font("微软雅黑", Font.BOLD, (int) (14 * scaleFactor)));

        homeButton.setBorder(BorderFactory.createEmptyBorder((int) (10 * scaleFactor), (int) (10 * scaleFactor),
                (int) (10 * scaleFactor), (int) (10 * scaleFactor)));
        bookshelvesButton.setBorder(BorderFactory.createEmptyBorder((int) (10 * scaleFactor), (int) (10 * scaleFactor),
                (int) (10 * scaleFactor), (int) (10 * scaleFactor)));
        exploreButton.setBorder(BorderFactory.createEmptyBorder((int) (10 * scaleFactor), (int) (10 * scaleFactor),
                (int) (10 * scaleFactor), (int) (10 * scaleFactor)));
        profileButton.setBorder(BorderFactory.createEmptyBorder((int) (10 * scaleFactor), (int) (10 * scaleFactor),
                (int) (10 * scaleFactor), (int) (10 * scaleFactor)));
    }

    private void navigateBack() {
        new NavigationPage().setVisible(true);
        dispose(); // 关闭当前页面
    }
}