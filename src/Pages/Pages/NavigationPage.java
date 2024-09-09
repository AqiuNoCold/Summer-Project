package Pages.Pages;

import Pages.Pages.ECard.ECardPage;
import Pages.Pages.StudentMSPages.StudentMainPage;
import Pages.Pages.StudentMSPages.TeacherMainPage;
import vCampus.Entity.User;
import vCampus.Entity.ECard.ECard;
import Pages.MainApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

public class NavigationPage extends JFrame {
    User user=MainApp.getCurrentUser();

    private JButton storeButton;
    private JButton eCardButton;
    private JButton studentRecordButton;
    private JButton libraryButton;
    private JButton courseButton;

    public NavigationPage() {

        setTitle("导航页面");
        setSize(800, 600); // 调整窗口大小
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示窗口

        // 设置主面板
        JPanel mainPanel = new JPanel(new GridLayout(2, 3, 10, 10)); // 2行3列的网格布局
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 增加边距
        mainPanel.setBackground(new Color(245, 245, 245)); // 设置背景颜色
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(144, 238, 144), 5)); // 浅绿色边框，宽度为5像素
        // 初始化带图片的按钮
        storeButton = createImageButton("商店页面", "/imgs/store_icon.png");
        eCardButton = createImageButton("一卡通页面", "/imgs/ecard_icon.png");
        studentRecordButton = createImageButton("学籍管理页面", "/imgs/student_record_icon.png");
        libraryButton = createImageButton("图书馆页面", "/imgs/library_icon.png");
        courseButton = createImageButton("选课页面", "/imgs/course_icon.png");

        // 添加按钮到主面板
        mainPanel.add(storeButton);
        mainPanel.add(eCardButton);
        mainPanel.add(studentRecordButton);
        mainPanel.add(libraryButton);
        mainPanel.add(courseButton);

        add(mainPanel); // 将主面板添加到框架中

        // 按钮点击事件
        storeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPage(new StorePage());
            }
        });
        eCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ObjectInputStream in = MainApp.getIn();
                ObjectOutputStream out = MainApp.getOut();
                try {
                    out.writeObject("3");
                    out.writeObject("cardIni");
                    out.writeObject(user.getId());
                    out.flush();
                    ECard response=(ECard) in.readObject();
                    user= response;
                    MainApp.setCurrentUser(user);
                    openPage(new ECardPage(response));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        studentRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("studentRecordButton");
                // 创建 User 实例
//                User user = new User(
//                        "user123",        // id
//                        "password",       // pwd
//                        25,               // age
//                        true,             // gender
//                        "ST",        // role
//                        "user123@example.com", // email
//                        "123456789",      // card
//                        false             // lost
//                );
                User user=MainApp.getCurrentUser();
                System.out.println(user.getRole());
                if (user.getRole().equals("ST")) {
                    openPage(new StudentMainPage());

                } else if (user.getRole().equals("TC")) {
                    openPage(new TeacherMainPage());
                }
            }
        });
        libraryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                openPage(new LibraryPage());
            }
        });
        courseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPage(new CoursePage());
            }
        });
    }

    // 创建带图片和文字的按钮
    private JButton createImageButton(String text, String imagePath) {
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));

        // 调整图片大小
        Image img = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH); // 调整图片为64x64像素
        icon = new ImageIcon(img);

        if (icon.getIconWidth() == -1) { // 判断图片是否加载成功
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

    private void openPage(JFrame page) {
        page.setVisible(true);
        dispose(); // 关闭导航页面
    }
    public static void main(String[] args) {
        new NavigationPage().setVisible(true);
    }
}
