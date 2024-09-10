package vCampus.Course;

import java.util.ArrayList;
import java.io.Serializable;

import vCampus.User.IUserServerSrv;
import vCampus.Entity.Course;
import vCampus.Course.CourseServerSrv;

public class CourseCientSrv implements Serializable{
    public CourseServerSrv aCss;

    public CourseCientSrv() {aCss = new CourseServerSrv();}

    /**
     * 按课程名称搜索
     *
     * @param name
     * @param state    必修选修 0无约束 1必修 2选修
     * @param full     是否已满 0无约束 1未满 2已满
     * @param conflict 是否冲突 0无约束 1不冲 2冲突
     * @return
     */
    public ArrayList<Course> search_course(String name, String s_id, int state, int full, int conflict){
        ArrayList<Course> C = new ArrayList<Course>();
        ArrayList<ArrayList<String>> B = new ArrayList<ArrayList<String>>();

        String s = state + "";
        String f = full + "";
        // 寻找某课程名name的所有课程
        //B = (ArrayList<ArrayList<String>>) a.request(524, true, name + "\n" + s + "\n" + f);
        B = (ArrayList<ArrayList<String>>) aCss.searchOneUser(name, s, f);
        System.out.println(B);
        String id = "";
        String temp_name;
        String teacher;
        String classroom;
        int num = 0;
        int date = 0, date2 = 0, start = 0, start2 = 0, end = 0, end2 = 0;
        int now_num;
        int max_num;
        String status;

        for (int i = 0; i < B.size(); i++) {

            id = B.get(i).get(0);
            num = Integer.parseInt(B.get(i).get(10));
            teacher = B.get(i).get(2);
            temp_name = B.get(i).get(1);
            // date,date2,start1,start2,end1,end2;

            date = Integer.parseInt(B.get(i).get(3));
            start = Integer.parseInt(B.get(i).get(4));
            end = Integer.parseInt(B.get(i).get(5));
            if (num == 2) {
                System.out.println(temp_name + "有第二节课");
                date2 = Integer.parseInt(B.get(++i).get(3));
                start2 = Integer.parseInt(B.get(i).get(4));
                end2 = Integer.parseInt(B.get(i).get(5));
            } else {
                System.out.println(temp_name + "没有第二节课");
                date2 = 0;
                start2 = 0;
                end2 = 0;
            }
            //status = Integer.parseInt(B.get(i).get(6));
            status = B.get(i).get(6);
            now_num = Integer.parseInt(B.get(i).get(7));
            max_num = Integer.parseInt(B.get(i).get(8));
            classroom = B.get(i).get(9);


            Course course = new Course(temp_name, id, teacher, date, start, end, status, max_num,
                    now_num, classroom, num);
            C.add(course);
        }
        // 根据学生ID 拿到课程id
        //ArrayList<ArrayList<String>> X = (ArrayList<ArrayList<String>>) a.request(523, true, "id" + "\n" + s_id);
        ArrayList<ArrayList<String>> X = (ArrayList<ArrayList<String>>) aCss.searchOneUser("card_id", s_id);
        String temp = "";
        String[] Y = new String[X.size()];
        for (int i = 0; i < X.size(); i++) {
            Y[i] = X.get(i).get(1);
            if (i < X.size() - 1) {
                temp = temp + Y[i] + "\n";
            } else {
                temp = temp + Y[i];
            }
        }
        // 根据课程id 拿到学生课程表-》周几 开始时间 结束时间
        //X = (ArrayList<ArrayList<String>>) a.request(522, true, temp);
        String[] tmp = temp.split("\n");
        X = (ArrayList<ArrayList<String>>)aCss.searchCourse(tmp);
        ArrayList<ArrayList<String>> D = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < X.size(); i++) {
            ArrayList<String> t = new ArrayList<String>();
            String d = X.get(i).get(3);// 周几
            String st = X.get(i).get(4);// 开始时间
            String et = X.get(i).get(5);// 结束时间
            t.add(d);
            t.add(st);
            t.add(et);
            D.add(t);
        }

        // 根据拿到的课程列表 拿到当前即将展示课程的周几 开始时间 结束时间 已选人数 课程最大容量
        // B = (ArrayList<ArrayList<String>>) a.request(521, true, "course_id" + "\n" +
        // c_id);
        ArrayList<ArrayList<String>> E = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < B.size(); i++) {
            ArrayList<String> t = new ArrayList<String>();
            String da = B.get(i).get(3);
            String st = B.get(i).get(4);
            String en = B.get(i).get(5);
            String na = B.get(i).get(0);
            // String now_number=B.get(i).get(7);
            // String max_num=B.get(i).get(8);
            t.add(da);
            t.add(st);
            t.add(en);
            t.add(na);
            // t.add(now_number);
            // t.add(max_num);
            E.add(t);
        }

        if (conflict == 1) {
            // 判断时间是否冲突以及当前课程是否选满 若不冲突 可以显示
            for (int i = 0; i < E.size(); i++) {
                for (int j = 0; j < D.size(); j++) {
                    if (E.get(i).get(0) == D.get(j).get(0)) {
                        int a = Integer.parseInt(E.get(i).get(1));// 预选课程开始时间
                        int b = Integer.parseInt(E.get(i).get(2));// 预选课程结束时间
                        int c = Integer.parseInt(D.get(j).get(1));// 学生已选课程开始时间
                        int d = Integer.parseInt(D.get(j).get(2));// 学生已选课程结束时间
                        // 冲突 则删除
                        if ((a > c && a < d) || (b < d && b > c)) {
                            for (int k = 0; k < C.size(); k++) {
                                if (C.get(i).getCourseId() == E.get(i).get(3)) {
                                    C.remove(i);
                                }
                            }
                        }
                    }
                }
            }
        } else if (conflict == 2) {
            for (int i = 0; i < E.size(); i++) {
                for (int j = 0; j < D.size(); j++) {
                    if (E.get(i).get(0) == D.get(j).get(0)) {
                        int a = Integer.parseInt(E.get(i).get(1));// 预选课程开始时间
                        int b = Integer.parseInt(E.get(i).get(2));// 预选课程结束时间
                        int c = Integer.parseInt(D.get(j).get(1));// 学生已选课程开始时间
                        int d = Integer.parseInt(D.get(j).get(2));// 学生已选课程结束时间
                        // 不冲突 则删除
                        if (!((a > c && a < d) || (b < d && b > c))) {
                            for (int k = 0; k < C.size(); k++) {
                                if (C.get(i).getCourseId() == E.get(i).get(3)) {
                                    C.remove(i);
                                }
                            }
                        }
                    }
                }
            }

        }
        return C;
    }

    /**
     * 查看当前学生的所选课程
     *
     * @param s_id 学生id
     * @return 当前学生所选课程的列表
     */
    public ArrayList<Course> all_course(String s_id) {
        ArrayList<ArrayList<String>> B = (ArrayList<ArrayList<String>>)aCss.searchPrivateCourse("card_id", s_id);
        //System.out.println(B);
        String course_list = "";
        for (int i = 0; i < B.size(); i++) {
            if (i < B.size() - 1) {
                course_list = course_list + B.get(i).get(3) + "\n";
            } else {
                course_list = course_list + B.get(i).get(3);
            }
        }
        //System.out.println(course_list);

        //B = (ArrayList<ArrayList<String>>) a.request(522, true, course_list);
        String[] cl = course_list.split("\n");
        B = (ArrayList<ArrayList<String>>)aCss.searchCourse(cl);
        //System.out.println(B);

        ArrayList<Course> C = new ArrayList<Course>();
        String id = "";
        String temp_name = "";
        String teacher;
        String classroom;
        int num = 0;
        int date = 0, date2 = 0, start = 0, start2 = 0, end = 0, end2 = 0;
        int now_num;
        int max_num;
        //int status;
        String status;
        for (int i = 0; i < B.size(); i++) {
            String temp = id;
            id = B.get(i).get(0);
            num = Integer.parseInt(B.get(i).get(10));
            teacher = B.get(i).get(2);
            temp_name = B.get(i).get(1);

            date = Integer.parseInt(B.get(i).get(3));
            start = Integer.parseInt(B.get(i).get(4));
            end = Integer.parseInt(B.get(i).get(5));
            if (num == 2) {
                date2 = Integer.parseInt(B.get(++i).get(3));
                start2 = Integer.parseInt(B.get(i).get(4));
                end2 = Integer.parseInt(B.get(i).get(5));
            }
            //status = Integer.parseInt(B.get(i).get(6));
            status = B.get(i).get(6);
            now_num = Integer.parseInt(B.get(i).get(7));
            max_num = Integer.parseInt(B.get(i).get(8));
            classroom = B.get(i).get(9);

            Course course = new Course(id, temp_name, teacher, date, start, end, status,
                    now_num, max_num, classroom, 1);
            C.add(course);

        }
        return C;
    }

    /**
     * 选择课程
     *
     * @param c_id 课程号
     * @param s_id 学生一卡通号
     * 返回1表示选课成功，-1表示发生冲突
     */
    public int choose_course(String c_id, String s_id) {
        // 根据学生ID 拿到已选课程id
        // ArrayList<ArrayList<String>> B = (ArrayList<ArrayList<String>>) a.request(523, true, "id" + "\n" + s_id);
        // ArrayList<ArrayList<String>> C=new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> B = (ArrayList<ArrayList<String>>) aCss.searchPrivateCourse("card_id", s_id);
        String temp = "";
        String[] C = new String[B.size()];
        for (int i = 0; i < B.size(); i++) {
            C[i] = B.get(i).get(3);
            if (i < B.size() - 1) {
                temp = temp + C[i] + "\n";
            } else {
                temp = temp + C[i];
            }
        }
        //System.out.println(temp);

        // 根据课程id 拿到学生课程表-》周几 开始时间 结束时间
        // B = (ArrayList<ArrayList<String>>) a.request(522, true, temp);
        B = (ArrayList<ArrayList<String>>)aCss.searchCourse(temp.split("\n"));
        //System.out.println(B);
        ArrayList<ArrayList<String>> D = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < B.size(); i++) {
            ArrayList<String> t = new ArrayList<String>();
            String date = B.get(i).get(3);
            String start = B.get(i).get(4);
            String end = B.get(i).get(5);
            t.add(date);
            t.add(start);
            t.add(end);
            D.add(t);
        }

        // 根据输入的课程ID 拿到当前欲选课程的周几 开始时间 结束时间 已选人数 课程最大容量
        B = (ArrayList<ArrayList<String>>) aCss.searchOneUser("course_ID" , c_id);
        ArrayList<ArrayList<String>> E = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < B.size(); i++) {
            ArrayList<String> t = new ArrayList<String>();
            String date = B.get(i).get(3);
            String start = B.get(i).get(4);
            String end = B.get(i).get(5);
            // String now_number=B.get(i).get(7);
            // String max_num=B.get(i).get(8);
            t.add(date);
            t.add(start);
            t.add(end);
            // t.add(now_number);
            // t.add(max_num);
            E.add(t);
        }
        // E: 待选课程  D: 已选课程
        System.out.println(E);
        System.out.println(D);
        // 判断时间是否冲突以及当前课程是否选满 若不冲突 可以选课 修改课表以及当前课容量
        boolean flag = true;
        for (int i = 0; i < E.size(); i++) {
            for (int j = 0; j < D.size(); j++) {
                System.out.println(E.get(i).get(0) + D.get(j).get(0));
                if (E.get(i).get(0).equals(D.get(j).get(0))) {
                    int a = Integer.parseInt(E.get(i).get(1));// 预选课程开始时间
                    int b = Integer.parseInt(E.get(i).get(2));// 预选课程结束时间
                    int c = Integer.parseInt(D.get(j).get(1));// 学生已选课程开始时间
                    int d = Integer.parseInt(D.get(j).get(2));// 学生已选课程结束时间
                    if ((a >= c && a <= d) || (b <= d && b >= c)) {
                        flag = false;
                    }
                }
            }
        }

        if (flag == true) {
            return aCss.addCourse(s_id, c_id);
        }
        // 若冲突 返回错误信息
        else {
            return -1;
        }
    }

    /**
     * 学生退课
     *
     * @param account_number
     * @param courseID
     * @return
     */
    public int quit_course(String account_number, String courseID) {
        return Integer.parseInt(aCss.deleteCourse(account_number, courseID).toString());
    }

    /**
     * 查看选择当前课程的人名单
     *
     * @param c_id
     * @return
     */
    public ArrayList<String> show_list(String c_id) {

        // 根据课程号拿到一卡通号
        //ArrayList<ArrayList<String>> B = (ArrayList<ArrayList<String>>) a.request(523, true, "course_id" + "\n" + c_id);
        ArrayList<ArrayList<String>> B = (ArrayList<ArrayList<String>>)
                aCss.searchPrivateCourse( "course_id", c_id);
        System.out.println(B);
        //Object[][] C = new Object[B.size()][2];

        // 所有人的 id
        ArrayList<String> D = new ArrayList<String>();
        for (int i = 0; i < B.size(); i++) {
            String id = B.get(i).get(1);
            D.add(id);
        }
        return D;
    }

    public static void main(String[] args) {
        CourseCientSrv ccs = new CourseCientSrv();
        //System.out.println(ccs.all_course("213222801"));
        //System.out.println(ccs.choose_course("CS144", "213222801"));
        //System.out.println(ccs.quit_course("213222801", "CS144"));
        //System.out.println(ccs.show_list("CS101"));
        System.out.println(ccs.show_list("CS101"));
    }

}
