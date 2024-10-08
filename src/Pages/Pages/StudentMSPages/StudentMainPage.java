package Pages.Pages.StudentMSPages;

import Pages.Pages.NavigationPage;
import vCampus.Entity.User;
import Pages.MainApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentMainPage extends JFrame {
    private JButton DNDXButton;
    private JButton personButton;
    private JButton infoAllButton;
    private JButton gradeButton;
    private JButton libraryButton;
    private JButton courseButton;

    User user = MainApp.getCurrentUser();
    // 创建一个 User 对象
    User userTest = new User(
            "user123",  // id
            "password1", // pwd
            25,          // age
            true,        // gender (true = male, false = female)
            "ST",        // role (ST, TC, or AD)
            "user@example.com", // email
            "123456789", // card
            false        // lost (false = not frozen, true = frozen)
    );

    public StudentMainPage() {
        setTitle("学生学籍系统（学生端）");
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
        personButton = createImageButton("一卡通号："+user.getCard(),  "/imgs/man.png",90,110,16,new Color(76, 138, 77));
        infoAllButton = createImageButton("个人信息查询", "/imgs/info.png",80,80,16,new Color(0, 0, 0));
        DNDXButton = createImageButton("学生基本信息", "/imgs/logoDNDX.png",250,80,16,new Color(101, 93, 93));
        gradeButton = createImageButton("个人成绩查询", "/imgs/grade.png",80,80,16,new Color(0, 0, 0));
        JButton backButton = new JButton("返回");
        add(backButton, BorderLayout.SOUTH);
        // 添加按钮到主面板
        mainPanel.add(DNDXButton,gbc);
        mainPanel.add(personButton,gbc);
        mainPanel.add(infoAllButton,gbc);
        mainPanel.add(gradeButton);
//        mainPanel.add(backButton);

        add(mainPanel); // 将主面板添加到框架中

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigateBack();
            }
        });
        infoAllButton.addActionListener(e -> openStudentInfoMS());
        gradeButton.addActionListener(e -> openStudentGrade());
    }

    private void openStudentGrade() {
        // Instantiate and display TeacherInfoMS
        SwingUtilities.invokeLater(() -> {
            StudentGrade.main(new String[0]);
        });
        dispose(); // Close current page
    }

    private void openStudentInfoMS() {
        // Instantiate and display TeacherInfoMS
        SwingUtilities.invokeLater(() -> {
            StudentInfo.main(new String[0]);
        });
        dispose(); // Close current page
    }

    // 创建带图片和文字的按钮
    private JButton createImageButton(String text, String imagePath,int weight,int height,int size,Color color) {
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));

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

    private void navigateBack() {
        new NavigationPage().setVisible(true);
        dispose(); // 关闭当前页面
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentMainPage navPage = new StudentMainPage();
            navPage.setVisible(true);
        });
    }
}
