package Pages.Pages.support;

import Pages.MainApp;
import vCampus.Entity.Course;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import vCampus.Entity.User;

public class CCButton extends JLabel{
    static int X = 1200, Y = 650;
    public User person;
    BackgroundTabbedFrame jf;
    /**
     * JCButton的构造函数
     *
     * @param s     按钮显示名称
     * @param icon  按钮显示的图标
     * @param frame 被添加到哪个frame上，便于后面进行选项卡添加操作
     * @param per   Person对象
     */
    public CCButton(String s, TextIcon icon, BackgroundTabbedFrame frame, User per) {
        super(s, icon, JLabel.CENTER);
        this.person = per;
        this.jf = frame;
        //this.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        this.setOpaque(true);

        this.setBackground(Color.WHITE);
        this.setVerticalTextPosition(javax.swing.JButton.BOTTOM);
        this.setHorizontalTextPosition(javax.swing.JButton.CENTER);
        this.setBorder(new ShadowBorder());
        this.setOpaque(true);
        this.setBounds(600,400,180,70);
        //this.setBounds(100, 100, 200, 50);
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //System.out.println("Mouse clicked");
                listenerStudentJwc();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //System.out.println("Mouse Pressed");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //System.out.println("Mouse Released");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //System.out.println("Mouse Entered");
                setBackground(MyType.defaultColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //System.out.println("Mouse Exited");
                setBackground(Color.WHITE);
            }
        });
    }

    public JLabel getVcampus() {
        ImageIcon vcampusIcon = new ImageIcon(getClass().getResource("/imgs/course/seulog.jpeg"));
        JLabel vcampus = new JLabel(vcampusIcon);
        vcampus.setBounds(40, Y - 120, 180, 65);
        return vcampus;
    }

    // [start], 教务系统
    /**
     * 学生教务系统主界面
     */
    public void listenerStudentJwc() {
        // panels
        FunctionPanel jp = new FunctionPanel(jf, "学生课表");

        ObjectInputStream in = MainApp.getIn();
        ObjectOutputStream out = MainApp.getOut();

        //if (in == null || out == null) {
        //    System.out.println("*****");
        //    return;
        //}
        // [start], title & exit
        JLabel title = new JLabel("学生课表");
        title.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        title.setBounds(20, 10, 210, 30);

        ImageIcon imi = new ImageIcon(getClass().getResource("/imgs/course/return.jpg"));
        Image ii = imi.getImage().getScaledInstance(80,80,java.awt.Image.SCALE_SMOOTH);
        JLabel exit = new JLabel(new ImageIcon(ii));
        exit.setBounds(1040, 483, 80, 80);
        exit.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jf.tabbedPane.remove(jf.tabbedPane.getSelectedIndex());
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                exit.setIcon(new ImageIcon(ii));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exit.setIcon(new ImageIcon(ii));
            }
        });
        // [end]
        // [start], timetable
        //ArrayList<Course> classesArray = passdata.all_course(person.getCard());

        ArrayList<Course> classesArray = new ArrayList<Course>();
        // 接口，目前学生已经选择的全部课程
        try{
            out.writeObject("2");
            out.writeObject("all_course");
            out.writeObject(person.getCard());
            classesArray = (ArrayList<Course>) in.readObject();
        } catch(Exception e){
            e.printStackTrace();
        }
        Course[] classes = classesArray.toArray(new Course[classesArray.size()]);
        Timetable timetable = new Timetable(classes, ClassBlock.STUDENT, person);
        JLabel refreshLabel = new JLabel("<html>刷<br>新<br>课<br>表</html>");
        refreshLabel.setHorizontalAlignment(SwingConstants.CENTER); // 居中对齐
        refreshLabel.setPreferredSize(new Dimension(40, 120)); // 设置大小
        refreshLabel.setOpaque(true);  // 设置背景色生效
        refreshLabel.setBackground(new Color(173, 216, 230)); // 设置背景色
        refreshLabel.setForeground(new Color(0,20,255)); // 设置字体颜色
        refreshLabel.setFont(new Font("微软雅黑", Font.PLAIN, 20)); // 设置字体
        refreshLabel.setBounds(1100,120,40,120);
        refreshLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //ArrayList<Course> classesArray = passdata.all_course(person.account_number);
                ArrayList<Course> classesArray = new ArrayList<Course>();
                // 接口，目前学生已经选择的全部课程
                try{
                    out.writeObject("2");
                    out.writeObject("all_course");
                    out.writeObject(person.getCard());
                    classesArray = (ArrayList<Course>) in.readObject();
                } catch(Exception e2){
                    e2.printStackTrace();
                }
                Course[] classes = classesArray.toArray(new Course[classesArray.size()]);
                timetable.setClasses(classes);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // 设置悬停效果
                refreshLabel.setBackground(new Color(135, 186, 250));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                refreshLabel.setForeground(new Color(173, 216, 230));
            }});
        // [end]

        // [start], choose classes
        JLabel chooseClass = new JLabel("<html>课<br>程<br>页<br>面</html>");
        chooseClass.setHorizontalAlignment(SwingConstants.CENTER); // 居中对齐
        chooseClass.setPreferredSize(new Dimension(40, 120)); // 设置大小
        chooseClass.setOpaque(true);  // 设置背景色生效
        chooseClass.setBackground(new Color(173, 216, 230)); // 设置背景色
        chooseClass.setForeground(new Color(0,20,255)); // 设置字体颜色
        chooseClass.setFont(new Font("微软雅黑", Font.PLAIN, 20)); // 设置字体
        chooseClass.setBounds(1100,300,40,120);
        chooseClass.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //System.out.println("进入学生选课界面");
                listenerStudentChooseClass(timetable);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                chooseClass.setBackground(new Color(135, 186, 250));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                chooseClass.setBackground(new Color(173, 216, 230)); // 鼠标移出时还原
            }
        });

        // [end]

        jp.add(title);
        jp.add(exit);
        jp.add(chooseClass);
        jp.add(timetable);
        jp.add(refreshLabel);

        jf.addFunctionPanel(jp, "学生课表");
    }
    /**
     * 学生选课界面
     *
     * @param timetable 指向课表的引用，用来同步更新课表内容
     */
    public void listenerStudentChooseClass(Timetable timetable) {
        // panels
        FunctionPanel jp = new FunctionPanel(jf, "选课界面");

        ObjectInputStream in = MainApp.getIn();
        ObjectOutputStream out = MainApp.getOut();
        // course lists
        //ArrayList<Course> courseListArray = passdata.search_course("null", person.account_number, 0, 0, 0);
        ArrayList<Course> courseListArray = new ArrayList<Course>();
        // 接口，目前学生已经选择的全部课程
        try{
            out.writeObject("2");
            out.writeObject("all_course");
            out.writeObject(person.getCard());
            courseListArray = (ArrayList<Course>) in.readObject();
        } catch(Exception e3){
            e3.printStackTrace();
        }
        Course[] courseList = courseListArray.toArray(new Course[courseListArray.size()]);
        // 接口，返回给我所有课程
        CourseScollContent courseInfo = new CourseScollContent(courseList, person, timetable, 0);

        // [start], panel in middle
        JScrollPane courseScroll = new JScrollPane(courseInfo);
        courseScroll.getViewport().setOpaque(false);
        courseScroll.setBorder(new RoundBorder(0, Color.WHITE, 1));

        // size & location
        courseScroll.setBounds(50, 80, 1060, 500);

        // add it
        jp.add(courseScroll);

        // [end]

        // [start], top side
        String[] courseTypeContent = { "--课程性质--", "必修", "选修", };
        JComboBox courseType = new JComboBox(courseTypeContent);
        String[] courseFullContent = { "--是否已满--", "未选满", "已选满", };
        JComboBox courseFull = new JComboBox(courseFullContent);
        JTextField searchContent = new JTextField("  请输入搜索关键词");
        String[] courseConflictContent = { "--是否冲突--", "不冲突", "冲突", };
        JComboBox courseConflict = new JComboBox(courseConflictContent);
        JLabel searchIt = new JLabel("搜索");
        JLabel vcampusLogo = getVcampus();

        // format
        searchContent.setBorder(new RoundBorder(0, MyType.blueColor, 2));
        searchContent.setAlignmentX((float) 0.5);
        searchIt.setBackground(MyType.blueColor);
        searchIt.setOpaque(true);
        searchIt.setFont(new Font("微软雅黑", Font.BOLD, 15));
        searchIt.setForeground(Color.WHITE);
        searchIt.setHorizontalAlignment(JLabel.CENTER);

        // size & location
        vcampusLogo.setBounds(20, 5, 180, 65);
        courseType.setBounds(240, 35, 115, 30);
        courseFull.setBounds(380, 35, 115, 30);
        courseConflict.setBounds(520, 35, 115, 30);
        searchContent.setBounds(650, 35, 430, 30);
        searchIt.setBounds(1080, 35, 70, 30);

        // function
        searchIt.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //ArrayList<Course> courseArray = passdata.search_course(
                //        (searchContent.getText().length() == 0 || searchContent.getText().contains("  请输入搜索关键词"))
                //                ? "null"
                //                : searchContent.getText(),
                //        person.account_number, courseType.getSelectedIndex(), courseFull.getSelectedIndex(),
                //        courseConflict.getSelectedIndex());
                ArrayList<Course> courseArray = null;
                if ((searchContent.getText().length()==0) || (searchContent.getText().contains("  请输入搜索关键词")))
                    courseArray = null;
                else{
                    try{
                        out.writeObject("2");
                        out.writeObject("search_course");
                        out.writeObject(searchContent.getText());
                        out.writeObject(person.getCard());
                        out.writeObject(courseType.getSelectedIndex());
                        out.writeObject(courseFull.getSelectedIndex());
                        out.writeObject(courseConflict.getSelectedIndex());
                        courseArray = (ArrayList<Course>) in.readObject();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                Course[] courseListNew = courseArray.toArray(new Course[courseArray.size()]);
                // 搜索
                // 刷新列表
                courseScroll.setViewportView(new CourseScollContent(courseListNew, person, timetable, courseConflict.getSelectedIndex()));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                searchContent.setBorder(new RoundBorder(0, MyType.purpleColor, 2));
                searchIt.setBackground(MyType.purpleColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                searchContent.setBorder(new RoundBorder(0, MyType.blueColor, 2));
                searchIt.setBackground(MyType.blueColor);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        // add them
        jp.add(vcampusLogo);
        jp.add(courseType);
        jp.add(courseFull);
        jp.add(courseConflict);
        jp.add(searchContent);
        jp.add(searchIt);
        // [end]

        // [start], exit
        ImageIcon imi = new ImageIcon(getClass().getResource("/imgs/course/return.jpg"));
        Image ii = imi.getImage().getScaledInstance(40,40,java.awt.Image.SCALE_SMOOTH);
        JLabel exit = new JLabel(new ImageIcon(ii));
        exit.setBounds(1120, 483, 40, 40);
        exit.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jf.tabbedPane.remove(jf.tabbedPane.getSelectedIndex());
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                exit.setIcon(new ImageIcon(ii));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exit.setIcon(new ImageIcon(ii));
            }
        });
        jp.add(exit);
        // [end]
        jf.addFunctionPanel(jp, "选课界面");
    }
}
