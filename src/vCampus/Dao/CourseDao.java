package vCampus.Dao;

import vCampus.Db.DbConnection;
import vCampus.Entity.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
