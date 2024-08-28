package vCampus.Dao;

import vCampus.Db.DbConnection;
import vCampus.Entity.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StuDao implements BaseDao<Student> {

    // 添加学生信息
    public boolean add(Student student) {
        String sql = "INSERT INTO tblStu (id, card_id, name, gender, birth, college, grade, major, email, stage, honor, punish, stu_code) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getId());
            pstmt.setString(2, student.getCardId());
            pstmt.setString(3, student.getName());
            pstmt.setString(4, student.getGender());
            pstmt.setDate(5, student.getBirth());
            pstmt.setString(6, student.getCollege());
            pstmt.setString(7, student.getGrade());
            pstmt.setString(8, student.getMajor());
            pstmt.setString(9, student.getEmail());
            pstmt.setString(10, student.getStage());
            pstmt.setString(11, student.getHonor());
            pstmt.setString(12, student.getPunish());
            pstmt.setString(13, student.getStuCode());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 查找学生信息
    public Student find(String cardId) {
        String sql = "SELECT * FROM tblStu WHERE card_id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cardId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Student(
                        rs.getString("id"),
                        rs.getString("card_id"),
                        rs.getString("name"),
                        rs.getString("gender"),
                        rs.getDate("birth"),
                        rs.getString("college"),
                        rs.getString("grade"),
                        rs.getString("major"),
                        rs.getString("email"),
                        rs.getString("stage"),
                        rs.getString("honor"),
                        rs.getString("punish"),
                        rs.getString("stu_code")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 更新学生信息
    public boolean update(Student student) {
        String sql = "UPDATE tblStu SET name = ?, gender = ?, birth = ?, college = ?, grade = ?, major = ?, email = ?, stage = ?, honor = ?, punish = ?, stu_code = ? WHERE card_id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getGender());
            pstmt.setDate(3, student.getBirth());
            pstmt.setString(4, student.getCollege());
            pstmt.setString(5, student.getGrade());
            pstmt.setString(6, student.getMajor());
            pstmt.setString(7, student.getEmail());
            pstmt.setString(8, student.getStage());
            pstmt.setString(9, student.getHonor());
            pstmt.setString(10, student.getPunish());
            pstmt.setString(11, student.getStuCode());
            pstmt.setString(12, student.getCardId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除学生信息
    public boolean delete(String cardId) {
        String sql = "DELETE FROM tblStu WHERE card_id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cardId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 获取所有学生信息
    public List<Student> findAll() {
        String sql = "SELECT * FROM tblStu";
        List<Student> students = new ArrayList<>();

        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Student student = new Student(
                        rs.getString("id"),
                        rs.getString("card_id"),
                        rs.getString("name"),
                        rs.getString("gender"),
                        rs.getDate("birth"),
                        rs.getString("college"),
                        rs.getString("grade"),
                        rs.getString("major"),
                        rs.getString("email"),
                        rs.getString("stage"),
                        rs.getString("honor"),
                        rs.getString("punish"),
                        rs.getString("stu_code")
                );
                students.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }
}
