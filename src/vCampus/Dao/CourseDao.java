package vCampus.Dao;

import vCampus.Db.DbConnection;
import vCampus.Entity.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class CourseDao implements BaseDao<Course> {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    @Override
    public boolean add(Course course) {
        boolean isAdded = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "INSERT INTO tblCourse (course_ID, course_name, teacher_name, time, start_time, end_time, status, now_num, max_num, classroom, num) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, course.getCourseId());
            pstmt.setString(2, course.getCourseName());
            pstmt.setString(3, course.getTeacherName());
            pstmt.setInt(4, course.getTime());
            pstmt.setInt(5, course.getStartTime());
            pstmt.setInt(6, course.getEndTime());
            pstmt.setString(7, course.getStatus());
            pstmt.setInt(8, course.getNowNum());
            pstmt.setInt(9, course.getMaxNum());
            pstmt.setString(10, course.getClassroom());
            pstmt.setInt(11, course.getNum());
            int rowsAffected = pstmt.executeUpdate();
            isAdded = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return isAdded;
    }

    @Override
    public boolean update(Course course) {
        boolean isUpdated = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "UPDATE tblCourse SET course_name = ?, teacher_name = ?, time = ?, start_time = ?, end_time = ?, status = ?, now_num = ?, max_num = ?, classroom = ?, num = ? WHERE course_ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, course.getCourseName());
            pstmt.setString(2, course.getTeacherName());
            pstmt.setInt(3, course.getTime());
            pstmt.setInt(4, course.getStartTime());
            pstmt.setInt(5, course.getEndTime());
            pstmt.setString(6, course.getStatus());
            pstmt.setInt(7, course.getNowNum());
            pstmt.setInt(8, course.getMaxNum());
            pstmt.setString(9, course.getClassroom());
            pstmt.setInt(10, course.getNum());
            pstmt.setString(11, course.getCourseId());
            int rowsAffected = pstmt.executeUpdate();
            isUpdated = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return isUpdated;
    }

    @Override
    public boolean delete(String courseId) {
        boolean isDeleted = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "DELETE FROM tblCourse WHERE course_ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, courseId);
            int rowsAffected = pstmt.executeUpdate();
            isDeleted = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            //System.out.println("Cannot delete course. It is referenced by other records.");
        } finally {
            DbConnection.closeConnection(conn);
        }
        return isDeleted;
    }

    @Override
    public Course find(String courseId) {
        Course course = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT * FROM tblCourse WHERE course_ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, courseId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                course = new Course(
                        rs.getString("course_ID"),
                        rs.getString("course_name"),
                        rs.getString("teacher_name"),
                        rs.getInt("time"),
                        rs.getInt("start_time"),
                        rs.getInt("end_time"),
                        rs.getString("status"),
                        rs.getInt("now_num"),
                        rs.getInt("max_num"),
                        rs.getString("classroom"),
                        rs.getInt("num")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return course;
    }

    public ArrayList<ArrayList<String>> find_by_condition(String condition_name, String condition) {

        HashMap<String, String> status = new HashMap<String, String>();
        status.put("必修", "1");
        status.put("选修", "2");
        Statement statement = null;
        ResultSetMetaData rsmd = null;
        ArrayList<ArrayList<String>> al = new ArrayList<ArrayList<String>>();
        ArrayList<String> temp = null;
        boolean isInt = false;

        try {
            conn = DbConnection.getConnection();
            String sql = null;
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (condition_name.equals("null") && condition.equals("null")) {
                sql = "SELECT* FROM " + "tblCourse";
            } else { // 如果两个参数不为空
                if (condition_name.equals("time") || condition_name.equals("now_num") || condition_name.equals("max_num")
                        || condition_name.equals("num")) {
                    isInt = true;
                }

                if (isInt)
                    sql = "SELECT* FROM " + "tblCourse" + " WHERE " + condition_name + "=" + condition;
                else
                    sql = "SELECT* FROM " + "tblCourse" + " WHERE " + condition_name + " LIKE" + "'%" + condition + "%'";
            }
            rs = statement.executeQuery(sql);
            rsmd = rs.getMetaData();
            while (rs.next()) {
                temp = new ArrayList<String>();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    if (i == 7)
                        temp.add(status.get(rs.getString(i)));
                    else
                        temp.add(rs.getString(i));
                }
                al.add(temp);
            }
            return al;
        } catch (Exception e) {
            e.printStackTrace();
            return al;
        } finally {
            DbConnection.closeConnection(conn);
        }
    }

    public ArrayList<ArrayList<String>> find_by_course(String cou_name, String status_, String isMax) {
        HashMap<String, String> status = new HashMap<String, String>();
        status.put("必修", "1");
        status.put("选修", "2");
        Statement statement = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        ArrayList<ArrayList<String>> al = new ArrayList<ArrayList<String>>();
        ArrayList<String> temp = null;

        String restrainSql = " WHERE num > 0";
        if (isMax.equals("2"))
            restrainSql += " and now_num=max_num";
        else
            restrainSql += " and now_num < max_num";
        if (!cou_name.equals("null"))
            restrainSql += " and course_name LIKE" + "'%" + cou_name + "%'";
        if (status_.equals("1") || status_.equals("2"))
            restrainSql += " and status=" + status_;
        try {
            conn = DbConnection.getConnection();
            String sql = null;
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            sql = "SELECT* FROM " + "tblCourse" + restrainSql;
            rs = statement.executeQuery(sql);
            rsmd = rs.getMetaData();
            while (rs.next()) {
                temp = new ArrayList<String>();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    if (i == 7)
                        temp.add(status.get(rs.getString(i)));
                    else
                        temp.add(rs.getString(i));
                }
                al.add(temp);
            }
            return al;
        } catch (Exception e) {
            e.printStackTrace();
            return al;
        } finally {
            DbConnection.closeConnection(conn);
        }
    }

    public ArrayList<String> find_all_course(String column) {
        Statement statement = null;
        ResultSet rs = null;
        ArrayList<String> al = new ArrayList<String>();
        try {
            conn = DbConnection.getConnection();
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT* FROM " + "tblCourse";
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                al.add(rs.getString(column));
            }
            return al;
        } catch (SQLException e) {
            System.out.println("该列不存在");
            e.printStackTrace();
            return al;
        } catch (Exception e) {
            e.printStackTrace();
            return al;
        } finally {
            DbConnection.closeConnection(conn);
        }
    }

    public ArrayList<ArrayList<String>> searchCourse(String[] n) {
        HashMap<String, String> status = new HashMap<String, String>();
        status.put("必修", "1");
        status.put("选修", "2");
        Statement statement = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        ArrayList<ArrayList<String>> al = new ArrayList<ArrayList<String>>();
        ArrayList<String> temp = null;

        try {
            conn = DbConnection.getConnection();
            String subsql = null, sql = null;
            for (String data : n) {
                subsql += "'" + data + "',";
            }
            subsql = subsql.substring(4, subsql.length() - 1);
            System.out.println(subsql);

            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (subsql != null)
                sql = "SELECT* FROM " + "tblCourse" + " WHERE course_id IN(" + subsql + ")";
            else
                return null;
            rs = statement.executeQuery(sql);
            rsmd = rs.getMetaData();
            while (rs.next()) {
                temp = new ArrayList<String>();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    if (i == 7)
                        temp.add(status.get(rs.getString(i)));
                    else
                        temp.add(rs.getString(i));
                }
                al.add(temp);
            }
            return al;
        } catch (Exception e) {
            e.printStackTrace();
            return al;
        } finally {
            DbConnection.closeConnection(conn);
        }
    }
}
