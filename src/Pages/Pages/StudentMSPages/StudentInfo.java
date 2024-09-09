package Pages.Pages.StudentMSPages;

import javax.swing.*;
import java.awt.*;

public class StudentInfo extends JFrame {
    public static void main(String[] args) {
        // 创建一个示例 Student 对象
        StudentInfo.Student student = new StudentInfo.Student("2024001", "张三", "男", 20, "计算机科学");

        // 使用 SwingUtilities.invokeLater 确保界面创建在事件调度线程中
        SwingUtilities.invokeLater(() -> {
            // 创建并显示 StudentInfo 窗口
            StudentInfo studentInfoFrame = new StudentInfo(student);
            studentInfoFrame.setVisible(true);
        });
    }

    // 内部类定义 Student
    public static class Student {
        private String id;
        private String name;
        private String gender;
        private int age;
        private String className;

        public Student(String id, String name, String gender, int age, String className) {
            this.id = id;
            this.name = name;
            this.gender = gender;
            this.age = age;
            this.className = className;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getGender() {
            return gender;
        }

        public int getAge() {
            return age;
        }

        public String getClassName() {
            return className;
        }
    }

    public StudentInfo(Student student) {
        setTitle("学生详细信息");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示窗口

        // 创建主面板，使用 BorderLayout 布局
        setLayout(new BorderLayout());

        // 创建 headerPanel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        // 创建 ImageIcon 并调整大小
        ImageIcon icon = new ImageIcon(getClass().getResource("/imgs/logoDNDX.png"));
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(260, 86, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImg);

        JLabel imageLabel = new JLabel(icon);
        JLabel textLabel = new JLabel("        学生成绩信息");

        Font font = new Font("Serif", Font.BOLD, 27); // 字体名称，样式和大小
        textLabel.setForeground(new Color(3, 81, 32));
        textLabel.setFont(font);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(imageLabel);
        headerPanel.add(textLabel);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 创建 mainPanel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(6, 2, 10, 10)); // 6行2列的网格布局
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 增加边距
        mainPanel.setBackground(new Color(255, 255, 255)); // 设置背景颜色
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(144, 238, 144), 1)); // 浅绿色边框，宽度为5像素

        // 创建组件
        JLabel nameLabel = new JLabel("  姓名:          "+student.getName());
        nameLabel.setFont(new Font("Serif", Font.PLAIN, 20));

        JLabel cardIdLabel = new JLabel("    一卡通号:"+"1234567890");
        cardIdLabel.setFont(new Font("Serif", Font.PLAIN, 20));

        JLabel genderLabel = new JLabel("    性别:        "+student.getGender());
        genderLabel.setFont(new Font("Serif", Font.PLAIN, 20));

        JLabel birthLabel = new JLabel("    出生日期:"+student.getAge() + "");
        birthLabel.setFont(new Font("Serif", Font.PLAIN, 20));

        JLabel collegeLabel = new JLabel("    所属学院:"+"计算机科学与技术学院");
        collegeLabel.setFont(new Font("Serif", Font.PLAIN, 20));

        JLabel gradeLabel = new JLabel("    年级:        "+"2020");
        gradeLabel.setFont(new Font("Serif", Font.PLAIN, 20));

        JLabel majorLabel = new JLabel("    专业:        "+"计算机科学与技术");
        majorLabel.setFont(new Font("Serif", Font.PLAIN, 20));

        JLabel emailLabel = new JLabel("    邮箱: "+"1234567890@qq.com");
        emailLabel.setFont(new Font("Serif", Font.PLAIN, 20));

        JLabel stageLabel = new JLabel("    入学阶段: "+"本科");
        stageLabel.setFont(new Font("Serif", Font.PLAIN, 20));

        JLabel honorLabel = new JLabel("    荣誉信息:"+"无");
        honorLabel.setFont(new Font("Serif", Font.PLAIN, 20));

        JLabel punishLabel = new JLabel("    处分信息:"+"无");
        punishLabel.setFont(new Font("Serif", Font.PLAIN, 20));

        JLabel stuCodeLabel = new JLabel("    学籍号:"+"2020");
        stuCodeLabel.setFont(new Font("Serif", Font.PLAIN, 20));

        JButton backButton = new JButton("返回");

        // 添加组件到 mainPanel
        mainPanel.add(nameLabel);
        mainPanel.add(cardIdLabel);
        mainPanel.add(genderLabel);
        mainPanel.add(birthLabel);
        mainPanel.add(collegeLabel);
        mainPanel.add(gradeLabel);
        mainPanel.add(majorLabel);
        mainPanel.add(emailLabel);
        mainPanel.add(stageLabel);
        mainPanel.add(stuCodeLabel);
        mainPanel.add(honorLabel);
        mainPanel.add(punishLabel);

        // 将 headerPanel 和 mainPanel 添加到 JFrame
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);

        // 设置返回按钮的事件处理
        backButton.addActionListener(e -> {
            new StudentMainPage().setVisible(true);
            dispose();
        });
    }
}
