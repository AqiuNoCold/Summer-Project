package Pages.Pages.support;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Dimension.*;
import java.awt.event.MouseMotionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JComponent;

import Pages.MainApp;
import vCampus.Dao.CourseDao;
import vCampus.Entity.Course;
import vCampus.Entity.User;

public class CourseScollContent extends JPanel {
    static int X = 350, Y = 140;
    Course[] courseList = null;
    int state;
    User person = null;
    Timetable timetable = null;
    int expect_confilct = 0;

    /**
     * 构造函数
     * @param courseListPara 所有的课程
     * @param personPara 当前的user
     * @param timetablePara 当前的课表
     * @param confilctPara 1表示希望展示不冲突课程，2表示希望展示冲突课程
     */
    public CourseScollContent(Course[] courseListPara, User personPara, Timetable timetablePara, int confilctPara) {

        ObjectInputStream in = MainApp.getIn();
        ObjectOutputStream out = MainApp.getOut();

        ArrayList<ArrayList<String>> cL = new ArrayList<>();
        try{
            out.writeObject("2");
            out.writeObject("getAllCourse");
            out.writeObject("meiyouyong");
            cL = (ArrayList<ArrayList<String>>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Course[] coul=new Course[cL.size()];
        for(int i=0; i<cL.size(); i++){
            coul[i] = addUser(cL.get(i).toArray(new String[11]));
        }
        this.courseList = coul;
        this.person = personPara;
        this.timetable = timetablePara;
        this.expect_confilct = confilctPara;
        this.setPreferredSize(new Dimension(1040, (courseList.length + 2) / 3 * Y + 50));
        render();
    }

    public Course addUser(String[] args) {
        String course_id, course_name, teacher_name, status, classroom;
        int time, start_time, end_time, now_num, max_num, num;

        course_id = args[0];
        course_name = args[1];
        teacher_name = args[2];
        time = Integer.parseInt(args[3]);
        start_time = Integer.parseInt(args[4]);
        end_time = Integer.parseInt(args[5]);
        status = args[6];
        now_num = Integer.parseInt(args[7]);
        max_num = Integer.parseInt(args[8]);
        classroom = args[9];
        num = Integer.parseInt(args[10]);

        Course newC = new Course(course_id, course_name, teacher_name,
                    time, start_time, end_time, status, now_num, max_num, classroom, num);
        return newC;
    }
    public void render() {
        this.removeAll();
        this.repaint();
        this.setBackground(Color.WHITE);
        int real_count = 0;

        ObjectInputStream in = MainApp.getIn();
        ObjectOutputStream out = MainApp.getOut();

        //System.out.println(courseList.length);
        for (int i = 0; i < courseList.length; i++) {
            int i0 = i;
            JPanel info = new JPanel(null);
            //info.setBounds((real_count % 3) * X, (real_count / 3) * Y, X - 10, Y - 10);
            this.setLayout(new GridLayout(0,3,10,10));
            info.setBackground(Color.WHITE);
            info.setBorder(new RoundBorder(20, Color.LIGHT_GRAY, 1));

            // 课程名称
            JLabel name = new JLabel(courseList[i].getCourseName());
            System.out.println(courseList[i].getCourseName());
            name.setFont(new Font("微软雅黑", Font.PLAIN, 16));
            name.setBounds(10, 5, 150, 30);
            info.add(name);

            // 课程时间
            String[] dayweek = {"一", "二", "三", "四", "五", "六", "日"};
            JLabel time1 = new JLabel("星期" + dayweek[courseList[i].getTime() - 1] + " " +
                    courseList[i].getStartTime() + "-" + courseList[i].getEndTime() + "节");
            time1.setBounds(10, 45, 90, 30);
            info.add(time1);

            // 老师
            JLabel teacher = new JLabel(courseList[i].getTeacherName());
            teacher.setBounds(120, 45, 80, 30);
            info.add(teacher);

            // 教室
            JLabel classroom = new JLabel(courseList[i].getClassroom());
            classroom.setBounds(200, 45, 60, 30);
            info.add(classroom);

            // 判断课程是否已满
            if (courseList[i].getNowNum() >= courseList[i].getMaxNum()) {
                JLabel full = new JLabel("已满");
                full.setHorizontalAlignment(JLabel.CENTER);
                full.setForeground(Color.WHITE);
                full.setBounds(15, 95, 50, 23);
                info.add(full);
            }

            // 判断课程是否已选
            boolean hasChoose = false;
            for (int j = 0; j < timetable.getClasses().length; ++j)
                if (courseList[i0].getCourseId().equals(timetable.getClasses()[j].getCourseId())) {
                    hasChoose = true;
                    break;
                }
            if (hasChoose) { // 已选
                JLabel choosen = new JLabel("已选");
                choosen.setFont(new Font("微软雅黑", Font.BOLD, 13));
                choosen.setHorizontalAlignment(JLabel.CENTER);
                choosen.setForeground(Color.WHITE);
                choosen.setBackground(new Color(81, 200, 230));
                choosen.setOpaque(true);
                choosen.setBounds(145, 95, 50, 23);
                info.add(choosen);
            }
            // 是否冲突
            boolean hasConflict = false;
            for (int j = 0; j < timetable.getClasses().length; ++j) {
                if (timetable.getClasses()[j].getTime() == courseList[i0].getTime()
                        && ((timetable.getClasses()[j].getStartTime() <= courseList[i0].getStartTime()
                        && timetable.getClasses()[j].getEndTime() >= courseList[i0].getStartTime())
                        || (timetable.getClasses()[j].getStartTime() <= courseList[i0].getEndTime()
                        && timetable.getClasses()[j].getEndTime() >= courseList[i0].getEndTime()))) {
                    hasConflict = true;
                    break;
                }
            }
            if (this.expect_confilct == 1 && hasConflict) { // 希望展示未冲突的课程
                continue;
            } else if (this.expect_confilct == 2 && (!hasConflict)) { // 希望展示冲突的课程
                continue;
            }
            if (hasConflict && (!hasChoose)) { // 未选且冲突
                JLabel conflict = new JLabel("冲突");
                conflict.setFont(new Font("微软雅黑", Font.BOLD, 13));
                conflict.setHorizontalAlignment(JLabel.CENTER);
                conflict.setForeground(Color.WHITE);
                conflict.setBackground(MyType.redColor);
                conflict.setOpaque(true);
                conflict.setBounds(80, 95, 50, 23);
                info.add(conflict);
            }

            JLabel compulsory = new JLabel(courseList[i].getStatus().equals("1") ? "必修" : "选修");
            compulsory.setBounds(210, 90, 60, 30);
            info.add(compulsory);

            JLabel choose = new JLabel("已选 " + courseList[i].getNowNum() + "/" + courseList[i].getMaxNum());
            choose.setBounds(255, 90, 100, 30);
            info.add(choose);
            // [end]

            // function
            info.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // [start], chosen
                    boolean hasChooseTemp = false;
                    for (int j = 0; j < timetable.getClasses().length; ++j)
                        if (courseList[i0].getCourseId().equals(timetable.getClasses()[j].getCourseId())) {
                            hasChooseTemp = true;
                            break;
                        }
                    // [end]
                    // [start], conflict
                    boolean hasConflict = false;
                    for (int j = 0; j < timetable.getClasses().length; ++j) {
                        if (timetable.getClasses()[j].getTime() == courseList[i0].getTime()
                                && ((timetable.getClasses()[j].getStartTime() <= courseList[i0].getStartTime()
                                && timetable.getClasses()[j].getEndTime() >= courseList[i0].getStartTime())
                                || (timetable.getClasses()[j].getStartTime() <= courseList[i0].getEndTime()
                                && timetable.getClasses()[j].getEndTime() >= courseList[i0]
                                .getEndTime()))) {
                            hasConflict = true;
                            break;
                        }
                    }
                    if (hasConflict) { // 冲突
                        JLabel conflict = new JLabel("冲突");
                        conflict.setFont(new Font("微软雅黑", Font.BOLD, 13));
                        conflict.setHorizontalAlignment(JLabel.CENTER);
                        conflict.setForeground(Color.WHITE);
                        conflict.setBackground(MyType.redColor);
                        conflict.setOpaque(true);
                        conflict.setBounds(80, 95, 50, 23);
                        info.add(conflict);
                    }
                    // [end]
                    if (courseList[i0].getNowNum() == courseList[i0].getMaxNum() || hasConflict || hasChooseTemp)
                        return;

                    // 我们假设课程只能选不能退，退课在课表页面操作
                    if (JOptionPane.showConfirmDialog(null, "选择该课程？") == 0) {
                        //passdata.choose_course(courseList[i0].getCourseID(), person.account_number);
                        int flag = 0;
                        try {
                            out.writeObject("2");
                            out.writeObject("choose_course");
                            out.writeObject(courseList[i0].getCourseId());
                            out.writeObject(person.getCard());
                            flag = (Integer) in.readObject();
                        } catch (Exception e3) {
                            e3.printStackTrace();
                        }
                        // 选择该课程
                        // 改界面
                        // 1. 改后面的课程表
                        if (flag == 1) {
                            timetable.addClassToTimetable(courseList[i0]);
                            JOptionPane.showMessageDialog(null, "选课成功！");
                            // 2. 刷新选课界面……
                            render();
                        }
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    info.setBorder(new RoundBorder(20, MyType.purpleColor, 2));
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    info.setBorder(new RoundBorder(20, MyType.blueColor, 1));
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    info.setBorder(new RoundBorder(20, MyType.blueColor, 1));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    info.setBorder(new RoundBorder(20, Color.LIGHT_GRAY, 1));
                }

            });
            this.add(info);
            ++real_count;
        }
        this.revalidate();
        this.repaint();
    }

    public static void JlabelSetText(JLabel jLabel, String longString) {
        // throws InterruptedException
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
    }
}
