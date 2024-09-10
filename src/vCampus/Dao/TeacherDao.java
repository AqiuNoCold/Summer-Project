package vCampus.Dao;

import vCampus.Db.DbConnection;
import vCampus.Entity.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherDao implements BaseDao<Teacher> {

    // Add a teacher
    public boolean add(Teacher teacher) {
        String sql = "INSERT INTO tblTeacher (id, course) VALUES (?, ?)";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, teacher.getId());
            pstmt.setString(2, teacher.getCourseId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Find a teacher by course_ID
    public Teacher find(String course_id) {
        String sql = "SELECT * FROM tblTeacher WHERE course = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course_id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Teacher(
                        rs.getString("id"),
                        rs.getString("course")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Update a teacher's information
    public boolean update(Teacher teacher) {
        String sql = "UPDATE tblTeacher SET course = ? WHERE id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, teacher.getCourseId());
            pstmt.setString(2, teacher.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a teacher by ID
    public boolean delete(String id) {
        String sql = "DELETE FROM tblTeacher WHERE id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a course from a teacher by course_ID
    public boolean deleteCourse(String cou_id) {
        String sql = "DELETE FROM tblTeacher WHERE course = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cou_id);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all teachers
    public List<Teacher> findAll() {
        String sql = "SELECT * FROM tblTeacher";
        List<Teacher> teachers = new ArrayList<>();

        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Teacher teacher = new Teacher(
                        rs.getString("id"),
                        rs.getString("course")
                );
                teachers.add(teacher);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teachers;
    }
}

