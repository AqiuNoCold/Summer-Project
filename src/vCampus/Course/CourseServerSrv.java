package vCampus.Course;

import vCampus.Entity.Course;
import vCampus.Dao.CourseDao;
import vCampus.Entity.Teacher;
import vCampus.Dao.TeacherDao;
import vCampus.Dao.GradeDao;
import vCampus.Entity.Grade;

import java.sql.*;
import java.util.ArrayList;

public class CourseServerSrv {
    /**
     * 用于在系统数据库中添加一条课程信息
     * @param args 课程的所有信息(0：课程id，1：课程名称，2：老师名字，3：星期几，4：起始时间，5：结束时间，6：状态，7：现选人数，8：最大人数，9：教室，10：几节课)；
     *             其中，在状态中，1代表必修，2代表选修；
     * @return 若返回1则代表添加课程信息成功，若返回-1或0代表添加课程失败
     */
    public Integer addUser(String[] args) {
        /*
        for (String i : args) {
            System.out.print(i);
            System.out.print(",");
        }
        System.out.println(" ");
        */
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

        try {
            Course newC = new Course(course_id, course_name, teacher_name,
                    time, start_time, end_time, status, now_num, max_num, classroom, num);
            CourseDao newDao = new CourseDao();
            if (newDao.add(newC))
                return 1;
            else
                return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据CourseID删除指定课程
     * @param course_id 删除的课程编号
     * @return 若返回1则代表删除该用户成功，若返回-1/0则代表删除该用户失败
     */
    public Integer deleteUser(String course_id) {
        Statement statement = null;
        try {
            CourseDao newDao = new CourseDao();
            if (newDao.delete(course_id))
                return 1;
            else
                return 0;
        }catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    /**
     * 用于修改用户再数据库中的信息
     *
     * @param args 课程的所有信息(0：课程id，1：课程名称，2：老师名字，3：星期几，4：起始时间，5：结束时间，6：状态，7：现选人数，8：最大人数，9：教室，10：几节课)；
     *             其中，在状态中，1代表必修，2代表选修；
     * @return 若返回1则代表修改用户信息成功，若返回-1代表修改信息失败
     */
    public Integer updateUser(String[] args) {
        /*
        for (String i : args) {
            System.out.print(i);
            System.out.print(",");
        }
        System.out.println(" ");
        */
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

        try {
            Course newC = new Course(course_id, course_name, teacher_name,
                    time, start_time, end_time, status, now_num, max_num, classroom, num);
            CourseDao newDao = new CourseDao();
            if (newDao.update(newC))
                return 1;
            else
                return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    /**
     * 用于查找符合要求的所有信息
     *
     * @param condition_name 需要查询的索引
     * @param condition      索引值
     * @return 返回为符合该索引值的所有信息，包括课程id，课程名字等等， 服务端根据客户端的需求自行选择信息返回(注意：下标从0开始！！)；
     * 每一行的信息为（0：课程id，1：课程名称，2：老师名字，3：星期几，4：起始时间，5：结束时间，6：状态，7：现选人数，8：最大人数，9：教室，10：几节课）
     */
    public ArrayList<ArrayList<String>> searchOneUser(String condition_name, String condition) {
        //System.out.print(condition_name + "," + condition);
        //System.out.println(" ");
        try {
            CourseDao newDao = new CourseDao();

            return newDao.find_by_condition(condition_name, condition);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据指定的要求查找课程信息
     *
     * @param cou_name 课程名字
     * @param status_  必/选修 （1为必修，2为选修），若为0则为无约束
     * @param isMax    该课程是否已满 （1为未满，2为已满），若为0则为无约束
     * @return 返回为符合该索引值的所有信息，包括课程id，课程名字等等， 服务端根据客户端的需求自行选择信息返回(注意：下标从0开始！！)；
     * 每一行的信息为（0：课程id，1：课程名称，2：老师名字，3：星期几，4：起始时间，5：结束时间，6：状态，7：现选人数，8：最大人数，9：教室，10：几节课）
     */
    public ArrayList<ArrayList<String>> searchOneUser(String cou_name, String status_, String isMax) {
        try {
            CourseDao newDao = new CourseDao();
            return newDao.find_by_course(cou_name, status_, isMax);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 用于查找指定列的所有课程信息
     *
     * @param column 用于指定列
     * @return 返回所有用户的指定列信息(如：course_ID, course_name ...)
     */
    public ArrayList<String> searchAllUser(String column) {
        try {
            CourseDao newDao = new CourseDao();
            return newDao.find_all_course(column);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 用于学生添加课程
     *
     * @param stu_id 学生一卡通
     * @param cou_id 课程id
     * @return 如果添加成功则返回1，否则返回-1(课程已满)或0(其他原因)
     */
    public synchronized Integer addCourse(String stu_id, String cou_id) {
        String id, course_id;
        id = stu_id;
        course_id = cou_id;

        ArrayList<String> result = null;
        try {
            result = searchOneUser("course_id", course_id).get(0);
            Integer now_temp = Integer.parseInt(result.get(7)) + 1;
            Integer max_temp = Integer.parseInt(result.get(8));
            if (now_temp.intValue() >= max_temp.intValue() + 1) {
                return -1;
            } else {
                String[] temp = new String[11];
                for (int i = 0; i < 11; i++) {
                    temp[i] = result.get(i);
                }
                temp[7] = now_temp.toString();
                updateUser(temp); // 修改课程已选人数
                GradeDao newDao = new GradeDao();
                String couName = result.get(1);
                Grade gd = new Grade(null,id,couName,course_id,0,0,0,0,0,false,null);
                newDao.add(gd);
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("This record is existing in the table!");
            return -1;
        }
    }

    /**
     * 用于学生删除课程
     *
     * @param stu_id 学生的一卡通号
     * @param cou_id 课程id
     * @return 若删除成功则返回1，否则返回-1
     */
    public Integer deleteCourse(String stu_id, String cou_id) {
        String id, course_id;
        id = stu_id;
        course_id = cou_id;

        ArrayList<String> result = null;
        try {
            result = searchOneUser("course_id", course_id).get(0);
            Integer now_temp = Integer.parseInt(result.get(7)) - 1;

            String[] temp = new String[11];
            for (int i = 0; i <= 10; i++) {
                temp[i] = result.get(i);
            }
            temp[7] = now_temp.toString();
            updateUser(temp); // 修改课程已选人数
            GradeDao newDao = new GradeDao(); // 修改成绩单
            newDao.delete_course(id, course_id);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 用于老师添加课程
     *
     * @param id 老师一卡通
     * @param cou_id 课程id
     * @return 如果添加成功则返回1，否则返回-1
     */
    public synchronized Integer addTeacherCourse(String id, String cou_id) {
        try {
            TeacherDao td = new TeacherDao();
            Teacher tc = new Teacher(id,cou_id);
            td.add(tc);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 用于老师删除课程
     *
     * @param cou_id 课程id
     * @return 如果添加成功则返回1，否则返回-1
     */
    public synchronized Integer deleteTeacherCourse(String cou_id) {
        try {
            TeacherDao td = new TeacherDao();
            td.deleteCourse(cou_id);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 用于搜索一个学生所选的所有课程
     *
     * @param n 学生所选所有课程的id号数组
     * @return 该学生所有课程的二维数组
     */
    public ArrayList<ArrayList<String>> searchCourse(String[] n) {
        try {
            CourseDao newDao = new CourseDao();
            return newDao.searchCourse(n);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 得到个人的所有课程 : condition_name = card_id
     * 或者选择该课程的所有人 : condition_name = course_id
     *
     * @param condition_name
     * @param condition
     * @return
     */
    public ArrayList<ArrayList<String>> searchPrivateCourse(String condition_name, String condition) {
        try {
            GradeDao newDao = new GradeDao();
            return newDao.searchPrivateCourse(condition_name, condition);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**/
    public static void main(String[] args) {
        CourseServerSrv cSS= new CourseServerSrv();
        String[] courseInfo = {
                "CS101",              // course_id
                "Introduction to Programming",  // course_name
                "Dr. John Doe",       // teacher_name
                "3",                  // time (e.g., duration in hours)
                "9",                  // start_time (e.g., 9 AM)
                "12",                 // end_time (e.g., 12 PM)
                "1",             // status
                "25",                 // now_num (current number of students enrolled)
                "50",                 // max_num (maximum number of students allowed)
                "Room 101",           // classroom
                "1"               // num (possibly a unique identifier or a section number)
        };
        String[] courseInfo2 = {
                "CS101",              // course_id
                "Introduction",  // course_name
                "Dr. Tao",       // teacher_name
                "3",                  // time (e.g., duration in hours)
                "9",                  // start_time (e.g., 9 AM)
                "12",                 // end_time (e.g., 12 PM)
                "1",             // status
                "25",                 // now_num (current number of students enrolled)
                "50",                 // max_num (maximum number of students allowed)
                "Room 204",           // classroom
                "1"               // num (possibly a unique identifier or a section number)
        };
        String[] courseInfo3 = {
                "CS192",              // course_id
                "Machine",  // course_name
                "Dr. Yu",       // teacher_name
                "3",                  // time (e.g., duration in hours)
                "9",                  // start_time (e.g., 9 AM)
                "12",                 // end_time (e.g., 12 PM)
                "1",             // status
                "25",                 // now_num (current number of students enrolled)
                "50",                 // max_num (maximum number of students allowed)
                "Room 408",           // classroom
                "1"               // num (possibly a unique identifier or a section number)
        };
        cSS.deleteUser(courseInfo2[0]);
        cSS.deleteUser(courseInfo3[0]);
        cSS.addUser(courseInfo2);
        cSS.addUser(courseInfo3);

        //cSS.updateUser(courseInfo2);
        //System.out.println(cSS.searchOneUser("course_ID", courseInfo2[0]));
        //System.out.println(cSS.searchOneUser(courseInfo2[1], "0","0"));
        //System.out.println(cSS.searchAllUser("course_ID"));
        //cSS.addCourse("213222801", courseInfo2[0]);
        //cSS.deleteCourse("213222801", courseInfo2[0]);
        //cSS.addTeacherCourse("9999999", courseInfo2[0]);
        //cSS.addUser(courseInfo3);
        //cSS.addCourse("213222801", "CS101");
        //cSS.addCourse("213222801", "CS192");
        //String[] idsOfCou = {"CS101", "CS192"};
        //System.out.println(cSS.searchCourse(idsOfCou));
        //System.out.println(cSS.searchPrivateCourse("card_id", "213222801"));
    }

}
