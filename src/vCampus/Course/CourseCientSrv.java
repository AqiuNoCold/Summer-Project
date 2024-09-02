package vCampus.Course;

import java.util.ArrayList;

import vCampus.Entity.Course;
import vCampus.Course.CourseServerSrv;

public class CourseCientSrv {
    /**
     * 按课程名称搜索
     *
     * @param name
     * @param state    必修选修 0无约束 1必修 2选修
     * @param full     是否已满 0无约束 1未满 2已满
     * @param conflict 是否冲突 0无约束 1不冲 2冲突
     * @return

    public ArrayList<Course> search_course(String name, String s_id, int state, int full, int conflict){
        ArrayList<Course> C = new ArrayList<Course>();
        ArrayList<ArrayList<String>> B = new ArrayList<ArrayList<String>>();

        String s = state + "";
        String f = full + "";
        //B = (ArrayList<ArrayList<String>>) a.request(524, true, name + "\n" + s + "\n" + f);
        //B = (ArrayList<ArrayList<String>>) Sear
        System.out.println(B);
        String id = "";
        String temp_name;
        String teacher;
        String classroom;
        int num = 0;
        int date = 0, date2 = 0, start = 0, start2 = 0, end = 0, end2 = 0;
        int now_num;
        int max_num;
        int status;

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
            status = Integer.parseInt(B.get(i).get(6));
            now_num = Integer.parseInt(B.get(i).get(7));
            max_num = Integer.parseInt(B.get(i).get(8));
            classroom = B.get(i).get(9);

            Course course = new Course(temp_name, id, teacher, date, start, end, date2, start2, end2, status, max_num,
                    now_num, classroom);
            C.add(course);

        }
        // 根据学生ID 拿到课程id
        ArrayList<ArrayList<String>> X = (ArrayList<ArrayList<String>>) a.request(523, true, "id" + "\n" + s_id);
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
        X = (ArrayList<ArrayList<String>>) a.request(522, true, temp);
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
                                if (C.get(i).getCourseID() == E.get(i).get(3)) {
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
                                if (C.get(i).getCourseID() == E.get(i).get(3)) {
                                    C.remove(i);
                                }
                            }
                        }
                    }
                }
            }

        }

        return C;
    }*/

}
