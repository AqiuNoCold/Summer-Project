package Pages.Pages.support;

/**
 * @author JCSTARS
 * 基于JLayeredPane的课表，放置于教务系统首页，需要与ClassBlock配合使用
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.*;
import javax.swing.*;
import vCampus.Entity.Course;
import vCampus.Entity.User;
import java.awt.Image;

public class Timetable extends JLayeredPane {

    ImageIcon background = new ImageIcon(getClass().getResource("/imgs/course/bg.png"));
    static int X = 988, Y = 555;
    static int offsetX = 70, offsetY = 30, spaceX = 183, spaceY = 40;

    Course[] classes = null;
    int state;
    public User person;

    public Timetable(Course[] classesPara, int statePara, User p) {
        super();
        this.classes = classesPara;
        this.state = statePara;
        this.person = p;
        this.setBounds(50, 47, X, Y);

        render();
    }

    public Course[] getClasses() {
        return classes;
    }

    public void render() {
        this.removeAll();
        this.repaint();

        // [start], layered pane
        Image img = background.getImage();
        img = img.getScaledInstance(X, Y, Image.SCALE_SMOOTH);
        background = new ImageIcon(img);
        JLabel label = new JLabel(background);
        JPanel bgpanel = new JPanel();
        bgpanel.setBounds(0, -5, X, Y); // -5是玄学offset
        bgpanel.add(label);
        this.add(bgpanel, JLayeredPane.DEFAULT_LAYER);
        // [end]

        // [start], content panel
        JPanel content = new JPanel(null);
        content.setBounds(0, 0, X, Y);

        String[] weekdays = { "星期一", "星期二", "星期三", "星期四", "星期五" };
        for (int i = 0; i < weekdays.length; i++) {
            JLabel weekdayLabel = new JLabel(weekdays[i], JLabel.CENTER);
            weekdayLabel.setBounds(offsetX + i * spaceX, 0, spaceX, offsetY); // 调整表头位置
            weekdayLabel.setBorder(new LineBorder(Color.GRAY));
            weekdayLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            content.add(weekdayLabel);
        }
        // 添加节次的表头
        for (int i = 0; i < 13; i++) { // 假设一天12节课
            JLabel timeLabel = new JLabel("第" + (i + 1) + "节", JLabel.CENTER);
            timeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            timeLabel.setBounds(0, offsetY + i * spaceY, offsetX, spaceY); // 调整表头位置
            timeLabel.setBorder(new LineBorder(Color.GRAY));
            content.add(timeLabel);
        }
        for (int i = 0; i < classes.length; ++i) {
            Pages.Pages.support.ClassBlock classBlock = new Pages.Pages.support.ClassBlock(classes[i], this, state);
            classBlock.setBounds(offsetX + (classes[i].getTime() - 1) * spaceX,
                    offsetY + (classes[i].getStartTime() - 1) * spaceY, spaceX,
                    spaceY * (classes[i].getEndTime() - classes[i].getStartTime() + 1));
            content.add(classBlock);
        }
        // [end]

        content.setBounds(0, 0, X, Y);
        content.setOpaque(false);
        this.add(content, JLayeredPane.MODAL_LAYER);
    }

    public void setClasses(Course[] newClasses) {
        this.classes = newClasses.clone();
        render();
    }

    public void addClassToTimetable(Course course) {
        Course[] newClasses = new Course[classes.length + 1];
        for (int i = 0; i < classes.length; ++i)
            newClasses[i] = classes[i];
        newClasses[classes.length] = course;
        this.setClasses(newClasses);
    }

    public void removeClassFromTimetable(Course course) { // 只需要一个课程号，我就把所有课程都删除
        List<Course> arrList = Arrays.asList(this.classes);
        List<Course> classList = new ArrayList(arrList);
        for (int i = 0; i < classList.size(); i++) {
            if (classList.get(i).getCourseId().equals(course.getCourseId())) {
                classList.remove(i--);
            }
        }
        Course[] newClasses = classList.toArray(new Course[classList.size()]);
        this.setClasses(newClasses);
    }

}
