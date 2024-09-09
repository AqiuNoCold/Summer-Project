package Pages.Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
//        setLayout(new GridLayout(6, 2, 10, 10));

        JPanel mainPanel = new JPanel(new GridLayout(6, 4, 10, 10)); // 2行3列的网格布局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 增加边距
        mainPanel.setBackground(new Color(255, 255, 255)); // 设置背景颜色
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(144, 238, 144), 5)); // 浅绿色边框，宽度为5像素

        // 创建组件
        JLabel nameLabel = new JLabel("姓名:          "+student.getName());
        nameLabel.setFont(new Font("Serif", Font.PLAIN, 20)); // 选择字体、样式和大小
//        JLabel nameValue = new JLabel(student.getName());

        JLabel cardIdLabel = new JLabel("一卡通号:"+"1234567890");
        cardIdLabel.setFont(new Font("Serif", Font.PLAIN, 20)); // 选择字体、样式和大小
//        JLabel cardIdValue = new JLabel("1234567890");

        JLabel genderLabel = new JLabel("性别:        "+student.getGender());
        genderLabel.setFont(new Font("Serif", Font.PLAIN, 20));
//        JLabel genderValue = new JLabel(student.getGender());

        JLabel birthLabel = new JLabel("出生日期:"+student.getAge() + "");
        birthLabel.setFont(new Font("Serif", Font.PLAIN, 20));
//        JLabel birthValue = new JLabel(student.getAge() + "");

        JLabel collegeLabel = new JLabel("所属学院:"+"计算机科学与技术学院");
        collegeLabel.setFont(new Font("Serif", Font.PLAIN, 20));
//        JLabel collegeValue = new JLabel("计算机科学与技术学院");

        JLabel gradeLabel = new JLabel("年级:        "+"2020");
        gradeLabel.setFont(new Font("Serif", Font.PLAIN, 20));
//        JLabel gradeValue = new JLabel("2020");

        JLabel majorLabel = new JLabel("专业:        "+"计算机科学与技术");
        majorLabel.setFont(new Font("Serif", Font.PLAIN, 20));
//        JLabel majorValue = new JLabel("计算机科学与技术");

        JLabel emailLabel = new JLabel("邮箱: "+"1234567890@qq.com");
        emailLabel.setFont(new Font("Serif", Font.PLAIN, 20));
//        JLabel emailValue = new JLabel("1234567890@qq.com");

        JLabel stageLabel = new JLabel("入学阶段: "+"本科");
        stageLabel.setFont(new Font("Serif", Font.PLAIN, 20));
//        JLabel stageValue = new JLabel("本科");

        JLabel honorLabel = new JLabel("荣誉信息:"+"无");
        honorLabel.setFont(new Font("Serif", Font.PLAIN, 20));
//        JLabel honorValue = new JLabel("无");

        JLabel punishLabel = new JLabel("处分信息:"+"无");
        punishLabel.setFont(new Font("Serif", Font.PLAIN, 20));
//        JLabel punishValue = new JLabel("无");

        JLabel stuCodeLabel = new JLabel("学籍号:"+"2020");
        stuCodeLabel.setFont(new Font("Serif", Font.PLAIN, 20));
//        JLabel stuCodeValue = new JLabel("2020");

        JButton backButton = new JButton("返回");
        add(backButton, BorderLayout.SOUTH);

        // 添加组件到窗口
        mainPanel.add(nameLabel);
//        mainPanel.add(nameValue);
        mainPanel.add(cardIdLabel);
//        mainPanel.add(cardIdValue);
        mainPanel.add(genderLabel);
//        mainPanel.add(genderValue);
        mainPanel.add(birthLabel);
//        mainPanel.add(birthValue);
        mainPanel.add(collegeLabel);
//        mainPanel.add(collegeValue);
        mainPanel.add(gradeLabel);
//        mainPanel.add(gradeValue);
        mainPanel.add(majorLabel);
//        mainPanel.add(majorValue);
        mainPanel.add(emailLabel);
//        mainPanel.add(emailValue);
        mainPanel.add(stageLabel);
//        mainPanel.add(stageValue);
        mainPanel.add(stuCodeLabel);
//        mainPanel.add(stuCodeValue);
        mainPanel.add(honorLabel);
//        mainPanel.add(honorValue);
        mainPanel.add(punishLabel);
//        mainPanel.add(punishValue);




        add(mainPanel);

        backButton.addActionListener(e -> {
            new StudentMainPage().setVisible(true);
            dispose();
        });
    }
}
