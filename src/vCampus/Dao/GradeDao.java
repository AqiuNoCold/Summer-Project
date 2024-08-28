package vCampus.Dao;

import vCampus.Db.DbConnection;
import vCampus.Entity.Grade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GradeDao implements BaseDao<Grade> {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    @Override
    public boolean add(Grade grade) {
        boolean isAdded = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "INSERT INTO tblGrade (id, card_id, course_name, course_id, usual, mid, final, total, point, is_first, term) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, grade.getId());
            pstmt.setString(2, grade.getCardId());
            pstmt.setString(3, grade.getCourseName());
            pstmt.setString(4, grade.getCourseId());
            pstmt.setDouble(5, grade.getUsual());
            pstmt.setDouble(6, grade.getMid());
            pstmt.setDouble(7, grade.getFinal());
            pstmt.setDouble(8, grade.getTotal());
            pstmt.setDouble(9, grade.getPoint());
            pstmt.setBoolean(10, grade.isFirst());
            pstmt.setString(11, grade.getTerm());
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
    public boolean update(Grade grade) {
        boolean isUpdated = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "UPDATE tblGrade SET course_name = ?, course_id = ?, usual = ?, mid = ?, final = ?, total = ?, point = ?, is_first = ?, term = ? WHERE id = ? AND card_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, grade.getCourseName());
            pstmt.setString(2, grade.getCourseId());
            pstmt.setDouble(3, grade.getUsual());
            pstmt.setDouble(4, grade.getMid());
            pstmt.setDouble(5, grade.getFinal());
            pstmt.setDouble(6, grade.getTotal());
            pstmt.setDouble(7, grade.getPoint());
            pstmt.setBoolean(8, grade.isFirst());
            pstmt.setString(9, grade.getTerm());
            pstmt.setString(10, grade.getId());
            pstmt.setString(11, grade.getCardId());
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
    public boolean delete(String id) {
        boolean isDeleted = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "DELETE FROM tblGrade WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
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
    public Grade find(String id) {
        Grade grade = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT * FROM tblGrade WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                grade = new Grade(
                        rs.getString("id"),
                        rs.getString("card_id"),
                        rs.getString("course_name"),
                        rs.getString("course_id"),
                        rs.getDouble("usual"),
                        rs.getDouble("mid"),
                        rs.getDouble("final"),
                        rs.getDouble("total"),
                        rs.getDouble("point"),
                        rs.getBoolean("is_first"),
                        rs.getString("term")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return grade;
    }
}
