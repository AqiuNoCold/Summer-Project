package vCampus.Dao;

import vCampus.Db.DbConnection;
import vCampus.Entity.User;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

public class UserDao implements BaseDao<User> {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    @Override
    public boolean add(User user) {
        boolean isAdded = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "INSERT INTO tblUser (id, pwd, age, gender, role, email, card, remain, password, lost) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";//, courses
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getPwd());
            pstmt.setInt(3, user.getAge());
            pstmt.setBoolean(4, user.getGender());
            pstmt.setString(5, user.getRole());
            pstmt.setString(6, user.getEmail());
            pstmt.setString(7, user.getCard());
            pstmt.setFloat(8, user.getRemain());
            pstmt.setInt(9, user.getPassword());
            pstmt.setBoolean(10, user.getLost());
//            pstmt.setString(11, String.join(",", user.getCourses()));
            int rowsAffected = pstmt.executeUpdate();
            isAdded = rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return isAdded;
    }

    @Override
    public boolean update(User user) {
        boolean isUpdated = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "UPDATE tblUser SET pwd = ?, age = ?, gender = ?, role = ?, email = ?, card = ?, remain = ?, password = ?, lost = ? WHERE id = ?";//, courses = ?
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getPwd());
            pstmt.setInt(2, user.getAge());
            pstmt.setBoolean(3, user.getGender());
            pstmt.setString(4, user.getRole());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getCard());
            pstmt.setFloat(7, user.getRemain());
            pstmt.setInt(8, user.getPassword());
            pstmt.setBoolean(9, user.getLost());
            pstmt.setString(10, user.getId());
//            pstmt.setString(11, String.join(",", user.getCourses()));
            int rowsAffected = pstmt.executeUpdate();
            isUpdated = rowsAffected > 0;
        } catch (Exception e) {
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
            String sql = "DELETE FROM tblUser WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            int rowsAffected = pstmt.executeUpdate();
            isDeleted = rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return isDeleted;
    }

    @Override
    public User find(String id) {
        User user = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT * FROM tblUser WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
//                String coursesStr = rs.getString("courses");
//                ArrayList<String> courses = new ArrayList<>(Arrays.asList(coursesStr.split(",")));
                user = new User(
                        rs.getString("id"),
                        rs.getString("pwd"),
                        rs.getInt("age"),
                        rs.getBoolean("gender"),
                        rs.getString("role"),
                        rs.getString("email"),
                        rs.getString("card"),
                        rs.getFloat("remain"),
                        rs.getInt("password"),
                        rs.getBoolean("lost")
                        //courses
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return user;
    }
}
