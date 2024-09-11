package vCampus.Dao;

import vCampus.Db.DbConnection;
import vCampus.Entity.Grade;

import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public boolean delete_course(String card_id, String course_id) {
        boolean isDeleted = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "DELETE FROM tblGrade WHERE card_id = ? AND course_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, card_id);
            pstmt.setString(2, course_id);
            System.out.println(pstmt.toString());
            int rowsAffected = pstmt.executeUpdate();
            isDeleted = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return isDeleted;
    }

    public ArrayList<ArrayList<String>> searchPrivateCourse(String condition_name, String condition) {
        Statement statement = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        ArrayList<ArrayList<String>> al = new ArrayList<ArrayList<String>>();
        ArrayList<String> temp = null;
        boolean isInt = false;

        try {
            conn = DbConnection.getConnection();
            String sql = null;
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (condition_name.equals("null") && condition.equals("null")) {
                sql = "SELECT* FROM tblGrade";
            } else { // 如果两个参数不为空
                if (condition_name.equals("card_id")) {
                    isInt = true;
                }

                if (isInt)
                    sql = "SELECT* FROM tblGrade" + " WHERE " + condition_name + "=" + condition;
                else
                    sql = "SELECT* FROM tblGrade" + " WHERE " + condition_name + " LIKE" + "'%" + condition + "%'";
            }
            System.out.println(sql);
            rs = statement.executeQuery(sql);
            rsmd = rs.getMetaData();
            while (rs.next()) {
                temp = new ArrayList<String>();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
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

    public List<Grade> findAllByCardId(String cardId) {
        List<Grade> grades = new ArrayList<>();
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT * FROM tblGrade WHERE card_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cardId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Grade grade = new Grade(
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
                grades.add(grade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return grades;
    }

    public List<Grade> findAll() {
        List<Grade> grades = new ArrayList<>();
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT * FROM tblGrade";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Grade grade = new Grade(
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
                grades.add(grade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return grades;
    }
    public Grade findFromCardandCourse(String card_id, String course_id) {
        Grade grade = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT * FROM tblGrade WHERE card_id = ? AND course_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, card_id);
            pstmt.setString(2, course_id);
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

    public List<Grade> findAllByCourseId(String courseId) {
        List<Grade> grades = new ArrayList<>();
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT * FROM tblGrade WHERE course_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, courseId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Grade grade = new Grade(
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
                grades.add(grade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return grades;
    }

    public List<Grade> findGrade(String cardId, String courseId) {
        List<Grade> grades = new ArrayList<>();
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT * FROM tblGrade WHERE card_id = ? AND course_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cardId);
            pstmt.setString(2, courseId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Grade grade = new Grade(
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
                grades.add(grade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return grades;
    }

    public Grade findGradeIsFirst(Grade grade) {
        Grade gradeResult = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT * FROM tblGrade WHERE card_id = ? AND course_id = ? AND is_first = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, grade.getCardId());
            pstmt.setString(2, grade.getCourseId());
            pstmt.setBoolean(3, grade.isFirst());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                gradeResult = new Grade(
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
        return gradeResult;
    }

    public boolean setGrade(Grade grade) {
        boolean isUpdated = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "UPDATE tblGrade SET course_name = ?,  usual = ?, mid = ?, final = ?, total = ?, point = ?, id = ?,term = ? WHERE course_id = ?AND card_id = ?AND is_first = ?";
            pstmt = conn.prepareStatement(sql);
            // Set parameters based on the Grade object
            pstmt.setString(1, grade.getCourseName());
            pstmt.setDouble(2, grade.getUsual());
            pstmt.setDouble(3, grade.getMid());
            pstmt.setDouble(4, grade.getFinal());
            pstmt.setDouble(5, grade.getTotal());
            pstmt.setDouble(6, grade.getPoint());
            pstmt.setString(7, grade.getId());
            pstmt.setString(8, grade.getTerm());
            pstmt.setString(9, grade.getCourseId());
            pstmt.setString(10, grade.getCardId());
            pstmt.setBoolean(11, grade.isFirst());

            int rowsAffected = pstmt.executeUpdate();
            isUpdated = rowsAffected > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return isUpdated;
    }
    
    public boolean deleteGrade(String cardId, String courseId, boolean isFirst) {
        boolean isDeleted = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "DELETE FROM tblGrade WHERE course_id = ?AND card_id = ?AND is_first = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, courseId);
            pstmt.setString(2, cardId);
            pstmt.setBoolean(3, isFirst);
            int rowsAffected = pstmt.executeUpdate();
            isDeleted = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return isDeleted;
    }
}
