package Pages.Pages.support;

/**
 * @author JCSTARS
 * 课程表上的课程块
 * 目前学生可以查看课程学生名单，查看成绩，退课和取消
 * 添加炫酷鼠标悬停变色特效
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.AbstractBorder;  // 添加这个导入
import Pages.MainApp;
import vCampus.Entity.Course;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ClassBlock extends JPanel {
    public static final int STUDENT = 0, TEACHER = 1;

    ImageIcon vlogoIcon = new ImageIcon(getClass().getResource("/imgs/course/seulog.jpeg"));

    // PassData
    //PassData passdata = new PassData();
    //PassData passData = new PassData();

    static Color[] lightColor = { new Color(255, 255, 200), new Color(220, 220, 255), new Color(255, 220, 220),
            new Color(220, 255, 220), new Color(200, 235, 255), new Color(255, 235, 205), new Color(255, 220, 255),
            new Color(235, 255, 190), };

    private Course course;
    private int state;
    private Timetable timetable;

    public ClassBlock(Course c, Timetable t, int statePara) {
        super(null);
        this.course = c;
        this.state = statePara;
        this.timetable = t;
        this.setOpaque(false);
        this.setBorder(new RoundedRectangleBorder(lightColor[Math.abs(c.getCourseName().hashCode() % lightColor.length)]));

        ObjectInputStream in = MainApp.getIn();
        ObjectOutputStream out = MainApp.getOut();
        // [start], elements
        JLabel className = new JLabel(course.getCourseName());
        //System.out.println(course.getCourseName());
        className.setBounds(10, 18, 180, 20);
        className.setFont(new Font("微软雅黑",Font.BOLD,20));
        if (JlabelSetText(className, course.getCourseName()))
            className.setBounds(12, 20, 180, 40);
        this.add(className);

        JLabel teacher = new JLabel(course.getTeacherName());
        teacher.setBounds(12, 47 + 40 * (c.getEndTime() - c.getStartTime() - 1), 90, 27);
        teacher.setFont(new Font("微软雅黑",Font.PLAIN,16));
        this.add(teacher);

        JLabel classtime = new JLabel("第"+String.valueOf(course.getStartTime())+"-"+String.valueOf(course.getEndTime())+"节");
        classtime.setBounds(12,47,180,40);
        classtime.setFont(new Font("微软雅黑",Font.PLAIN,16));
        this.add(classtime);

        JLabel classroom = new JLabel(course.getClassroom());
        classroom.setBounds(68, 47 + 40 * (c.getEndTime() - c.getStartTime() - 1), 90, 27);
        classroom.setFont(new Font("微软雅黑",Font.PLAIN,16));
        this.add(classroom);
        // [end]

        // function
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (state == STUDENT) {
                    UIManager.put("OptionPane.background", Color.WHITE); // 更改背景颜色
                    UIManager.put("Panel.background", Color.WHITE);      // 更改Panel背景颜色
                    UIManager.put("OptionPane.messageFont", new Font("微软雅黑", Font.BOLD, 16)); // 更改字体
                    UIManager.put("Button.font", new Font("微软雅黑", Font.PLAIN, 16));  // 更改按钮字体
                    UIManager.put("Button.background", new Color(193, 210, 240));     // 按钮背景色
                    UIManager.put("Button.foreground", new Color(70, 130, 180));                 // 按钮前景色（文本颜色）
                    UIManager.put("Button.border", BorderFactory.createLineBorder(Color.BLACK, 1)); // 添加按钮边框

                    UIManager.put("OptionPane.border", BorderFactory.createLineBorder(new Color(70, 130, 180), 2)); // 改变对话框边框颜色

                    Object[] options = new Object[] { "查看班级名单", " 查看成绩 ", " 退课 ", "取消" };
                    JPanel panell = new JPanel();
                    panell.setPreferredSize(new Dimension(400, 200));  // 增大对话框大小
                    panell.setLayout(new BorderLayout());

                    JLabel jl = new JLabel("请选择一个操作：", JLabel.CENTER);
                    jl.setFont(new Font("微软雅黑",Font.PLAIN,20));
                    // 添加占位符
                    JPanel topSpacer = new JPanel();
                    topSpacer.setPreferredSize(new Dimension(400, 50)); // 可以调整这个值来改变占位空间大小
                    topSpacer.setOpaque(false);  // 设置为透明

                    panell.add(jl, BorderLayout.CENTER);
                    int optionSelected = JOptionPane.showOptionDialog(null, panell, "课程选项",
                            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);

                    panell.add(topSpacer, BorderLayout.SOUTH);
                    if (optionSelected == 0) { // 查看班级名单
                        // [start], frame & panel
                        JPanel panel = new JPanel(null);
                        panel.setBackground(Color.WHITE);
                        panel.setOpaque(true);

                        JFrame frame = new JFrame("课程名单");
                        frame.setBounds(650 - 200, 350 - 250, 400, 500);
                        frame.setIconImage(vlogoIcon.getImage());

                        // [start], table & scroll
                        //Object[][] studentListContent = passdata.show_list(course.getCourseID());
                        // 接口，得到班级名单 (所有选课的学生id)
                        ArrayList<String> stuList=null;
                        try{
                            out.writeObject("2");
                            out.writeObject("show_list");
                            out.writeObject(course.getCourseId());
                            stuList= (ArrayList<String>) in.readObject();
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                        Object[][] studentListContent = new Object[stuList.size()][2];
                        for(int j=0; j<stuList.size(); j++){
                            studentListContent[j][0] = stuList.get(j);
                            System.out.println(stuList.get(j));
                            try{
                                out.writeObject("2");
                                out.writeObject("getNameFromId");
                                out.writeObject(stuList.get(j));
                                studentListContent[j][1] = (String)in.readObject();
                            } catch (Exception ee2) {
                                ee2.printStackTrace();
                            }
                        }
						Object[] studentListHead = { "一卡通号", "姓名" };
                        JTable studentList = new JTable(studentListContent, studentListHead);
                        studentList.setSelectionBackground(MyType.focusColor);
                        studentList.setSelectionForeground(Color.WHITE);
                        studentList.setPreferredScrollableViewportSize(new Dimension(386, 400));

                        JScrollPane scroll = new JScrollPane(studentList);
                        scroll.setBounds(0, 0, 386, 400);
                        // [end]

                        // [start], exit
                        JButton exit = new JButton("关闭");
                        exit.setBounds(310, 415, 60, 25);
                        exit.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                frame.dispose();
                            }
                        });
                        // [end]

                        // add elements
                        panel.add(scroll);
                        panel.add(exit);

                        frame.add(panel);
                        frame.setVisible(true);

                    } else if (optionSelected == 1) { // 查看成绩
                        // 接口，学生查看成绩
                        Double scc = 0.0;
                        try{
                            out.writeObject("2");
                            out.writeObject("getGradeFromId");
                            out.writeObject(timetable.person.getCard());
                            out.writeObject(course.getCourseId());
                            scc = (Double) in.readObject();
                        } catch(Exception e3){
                            e3.printStackTrace();
                        }
                        if(scc>0)
                            JOptionPane.showMessageDialog(null,
                                "你的成绩是" + scc +"！");
                        else
                            JOptionPane.showMessageDialog(null,
                                    "这门课还没出成绩");
                    } else if (optionSelected == 2) { // 退课
                        //String score_string = passdata.search_score(timetable.person.account_number, course.getCourseID());
                        //登上成绩之前都可以退课
                        Double scores = -1.0;
                        try{
                            out.writeObject("2");
                            out.writeObject("getGradeFromId");
                            out.writeObject(timetable.person.getCard());
                            out.writeObject(course.getCourseId());
                            scores = (Double)in.readObject();
                        } catch(Exception e1){
                            e1.printStackTrace();
                        }
                        if(scores > 0.1){
                            JOptionPane.showMessageDialog(null, "该门课程已登记成绩（你的成绩为" + scores + "），退课失败！");
                        } else {
                            // 接口，学生退课
                            //int quit_state = passdata.quit_course(timetable.person.account_number, course.getCourseID());
                            int quit_state=0;
                            try{
                                out.writeObject("2");
                                out.writeObject("quit_course");
                                out.writeObject(timetable.person.getCard());
                                out.writeObject(course.getCourseId());
                                quit_state = (Integer) in.readObject();
                            } catch(Exception e2) {
                                e2.printStackTrace();
                            }
                            if (quit_state == 1) {
                                JOptionPane.showMessageDialog(null, "退课成功！");
                                // 在界面把课程删除
                                timetable.removeClassFromTimetable(c);
                            } else {
                                JOptionPane.showMessageDialog(null, "退课失败！");
                            }
                        }
                    }
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                Color temp = lightColor[Math.abs(c.getCourseName().hashCode() % lightColor.length)];
                double gainR = 0.9, gainG = gainR, gainB = gainR;
                if (temp.getRed() >= temp.getBlue() && temp.getBlue() == temp.getGreen()) // 红色一枝独秀
                    gainR = 1;
                else if (temp.getGreen() >= temp.getBlue() && temp.getBlue() == temp.getRed()) // 绿色一枝独秀
                    gainG = 1;
                else if (temp.getBlue() >= temp.getRed() && temp.getRed() == temp.getGreen()) // 蓝色一枝独秀
                    gainB = 1;
                else if (temp.getRed() < temp.getGreen() && temp.getRed() < temp.getBlue()) { // 红色最惨
                    gainB = 1;
                    gainG = 1;
                    gainR = 0.8;
                } else if (temp.getBlue() < temp.getGreen() && temp.getBlue() < temp.getRed()) { // 蓝色最惨
                    gainR = 1;
                    gainG = 1;
                    gainB = 0.8;
                } else if (temp.getGreen() < temp.getBlue() && temp.getGreen() < temp.getRed()) { // 绿色最惨
                    gainR = 1;
                    gainB = 1;
                    gainG = 0.8;
                }

                setBorder(new RoundedRectangleBorder(new Color((int) (temp.getRed() * gainR),
                        (int) (temp.getGreen() * gainG), (int) (temp.getBlue() * gainB))));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(new RoundedRectangleBorder(lightColor[Math.abs(c.getCourseName().hashCode() % lightColor.length)]));
            }
        });

    }
    public static boolean JlabelSetText(JLabel jLabel, String longString) {
        // throws InterruptedException
        boolean ans = false;
        StringBuilder builder = new StringBuilder("<html>");
        char[] chars = longString.toCharArray();
        FontMetrics fontMetrics = jLabel.getFontMetrics(jLabel.getFont());
        int start = 0;
        int len = 0;
        while (start + len < longString.length()) {
            while (true) {
                len++;
                if (start + len > longString.length())
                    break;
                if (fontMetrics.charsWidth(chars, start, len) > jLabel.getWidth()) {
                    ans = true;
                    break;
                }
            }
            builder.append(chars, start, len - 1).append("<br/>");
            start = start + len - 1;
            len = 0;
        }
        builder.append(chars, start, longString.length() - start);
        builder.append("</html>");
        jLabel.setText(builder.toString());
        return ans;
    }

    // 创建带有浅蓝色背景、深蓝色字体和圆角边框的按钮
    private static JButton createCustomButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(new Color(0, 20, 139));  // 深蓝色字体
        button.setBackground(new Color(173, 216, 230));  // 浅蓝色背景
        button.setFont(new Font("Arial", Font.BOLD, 14));  // 设置字体
        button.setFocusPainted(false);  // 去掉焦点虚线
        button.setBorder(new RoundedBorder(15));  // 设置圆角边框
        button.setPreferredSize(new Dimension(150, 40));  // 设置按钮大小
        return button;
    }

    // 自定义圆角边框类
    static class RoundedBorder extends AbstractBorder {
        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(100, 149, 237));  // 设置边框颜色
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);  // 绘制圆角矩形
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(10, 10, 10, 10);  // 设置边框的内边距
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = 10;
            return insets;
        }
    }
}
