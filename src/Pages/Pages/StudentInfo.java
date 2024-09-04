package Pages.Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentInfo extends JFrame {
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
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示窗口
        setLayout(new GridLayout(6, 2, 10, 10));

        // 创建组件
        JLabel idLabel = new JLabel("学号: ");
        JLabel idValue = new JLabel(student.getId());

        JLabel nameLabel = new JLabel("姓名: ");
        JLabel nameValue = new JLabel(student.getName());

        JLabel genderLabel = new JLabel("性别: ");
        JLabel genderValue = new JLabel(student.getGender());

        JLabel ageLabel = new JLabel("年龄: ");
        JLabel ageValue = new JLabel(String.valueOf(student.getAge()));

        JLabel classLabel = new JLabel("班级: ");
        JLabel classValue = new JLabel(student.getClassName());

        JButton modifyButton = new JButton("修改");
        JButton backButton = new JButton("返回");

        // 添加组件到窗口
        add(idLabel);
        add(idValue);
        add(nameLabel);
        add(nameValue);
        add(genderLabel);
        add(genderValue);
        add(ageLabel);
        add(ageValue);
        add(classLabel);
        add(classValue);
        add(modifyButton);
        add(backButton);

        // 按钮事件监听
        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 修改操作的实现
                JOptionPane.showMessageDialog(StudentInfo.this, "修改功能尚未实现");
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // 关闭当前页面
            }
        });
    }
}
