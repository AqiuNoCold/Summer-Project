package Pages.Pages;

import vCampus.Entity.User;
import Pages.MainApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TeacherMainPage extends JFrame {
    private JButton DNDXButton;
    private JButton infoButton;
    private JButton infoAllButton;
    private JButton gradeButton;

    User user = MainApp.getCurrentUser();
    // 创建一个 User 对象
    User userTest = new User(
            "user123",  // id
            "password1", // pw/
            25,          // age
            true,        // gender (true = male, false = female)
            "ST",        // role (ST, TC, or AD)
            "user@example.com", // email
            "123456789", // card
            false        // lost (false = not frozen, true = frozen)
    );

    public TeacherMainPage() {
        setTitle("学生学籍系统（教师端）");
        setSize(800, 600); // 调整窗口大小
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示窗口

        // 设置主面板
        JPanel mainPanel = new JPanel(new GridLayout(2, 2, 10, 10)); // 2行3列的网格布局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 增加边距
        mainPanel.setBackground(new Color(255, 255, 255)); // 设置背景颜色
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(144, 238, 144), 5)); // 浅绿色边框，宽度为5像素

        // 初始化带图片的按钮
        infoButton = createImageButton("一卡通号："+userTest.getCard(),  "src/imgs/Tea3.jpg",90,110,16,new Color(76, 138, 77));
        infoAllButton = createImageButton("学生信息管理", "src/imgs/t11.jpg",80,80,16,new Color(0, 0, 0));
//        JLabel imageLabel = createImageLabel("src/imgs/logoDNDX.png");
        DNDXButton = createImageButton("学籍管理系统", "src/imgs/logoDNDX.png",250,80,16,new Color(101, 93, 93));
        gradeButton = createImageButton("学生成绩录入", "src/imgs/t2.jpg",80,80,16,new Color(0, 0, 0));
        JButton backButton = new JButton("返回");
        add(backButton, BorderLayout.SOUTH);

        // 添加按钮到主面板
        mainPanel.add(DNDXButton,gbc);
        mainPanel.add(infoButton,gbc);
        mainPanel.add(infoAllButton,gbc);
        mainPanel.add(gradeButton);

//        mainPanel.add(libraryButton);
//        mainPanel.add(courseButton);

        add(mainPanel); // 将主面板添加到框架中

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigateBack();
            }
        });
        // Add action listener to the infoAllButton
        infoAllButton.addActionListener(e -> openTeacherInfoMS());
        gradeButton.addActionListener(e -> openTeacherGrade());

    }

    // 创建带图片和文字的按钮
    private JButton createImageButton(String text, String imagePath,int weight,int height,int size,Color color) {
        ImageIcon icon = new ImageIcon(imagePath);

        // 调整图片大小
        Image img = icon.getImage().getScaledInstance(weight, height, Image.SCALE_SMOOTH); // 调整图片为64x64像素
        icon = new ImageIcon(img);

        if (icon.getIconWidth() == -1) { // 判断图片是否加载成功
            System.err.println("Error: Could not load image at " + imagePath);
        }

        JButton button = new JButton(text, icon);
        button.setHorizontalTextPosition(SwingConstants.CENTER); // 文字在图片中心
        button.setVerticalTextPosition(SwingConstants.BOTTOM); // 文字在图片下方
        button.setFont(new Font("微软雅黑", Font.BOLD, size)); // 设置字体
        button.setForeground(color); // 设置字体颜色
        button.setBackground(new Color(255, 255, 255)); // 设置背景颜色
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 增加按钮内边距
        return button;
    }

    // 创建带图片和文字的按钮
    private JButton createImageButton2(String text, String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);

        // 调整图片大小
        Image img = icon.getImage().getScaledInstance(250, 80, Image.SCALE_SMOOTH); // 调整图片为64x64像素
        icon = new ImageIcon(img);

        if (icon.getIconWidth() == -1) { // 判断图片是否加载成功
            System.err.println("Error: Could not load image at " + imagePath);
        }

        JButton button = new JButton(text, icon);
        button.setHorizontalTextPosition(SwingConstants.CENTER); // 文字在图片中心
        button.setVerticalTextPosition(SwingConstants.BOTTOM); // 文字在图片下方
        button.setFont(new Font("微软雅黑", Font.BOLD, 16)); // 设置字体
        button.setBackground(new Color(255, 255, 255)); // 设置背景颜色
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 增加按钮内边距
        return button;
    }

    private static JLabel createImageLabel(String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);

        // 调整图片大小
        Image img = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH); // 调整图片为200x150像素
        icon = new ImageIcon(img);

        if (icon.getIconWidth() == -1) { // 判断图片是否加载成功
            System.err.println("Error: Could not load image at " + imagePath);
        }

        // 创建 JLabel 并设置图标
        JLabel label = new JLabel(icon);
        return label;
    }

    public static JLabel createTextLabel(String text1, String text2) {
        // 使用HTML格式来设置标签的内容，调整字体大小、行间距和字体颜色
        String htmlText = "<html><div style='text-align: center;'>"
                + "<span style='font-size: 18px; color: rgb(0, 0, 255);'>" // 设置字体大小和颜色
                + text1 + "</span><br>"
                + "<span style='font-size: 18px; color: rgb(0, 0, 255);'>" // 设置字体大小和颜色
                + text2 + "</span>"
                + "<br><span style='font-size: 10px; line-height: 20px;'>"
                + "</span></div></html>";

        // 创建 JLabel 并设置文本
        JLabel label = new JLabel(htmlText, SwingConstants.CENTER);

        // 设置标签为居中对齐
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);

        return label;
    }

    private void openPage(JFrame page) {
        page.setVisible(true);
        dispose(); // 关闭导航页面
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TeacherMainPage navPage = new TeacherMainPage();
            navPage.setVisible(true);
        });
    }
    private void navigateBack() {
        new NavigationPage().setVisible(true);
        dispose(); // 关闭当前页面
    }
    private void openTeacherInfoMS() {
        // Instantiate and display TeacherInfoMS
        SwingUtilities.invokeLater(() -> {
            TeacherInfoMS.main(new String[0]);
        });
        dispose(); // Close current page
    }
    private void openTeacherGrade() {
        // Instantiate and display TeacherInfoMS
        SwingUtilities.invokeLater(() -> {
            TeacherGrade.main(new String[0]);
        });
        dispose(); // Close current page
    }

}
