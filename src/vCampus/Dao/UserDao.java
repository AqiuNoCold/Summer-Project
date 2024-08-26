package vCampus.Dao;

import vCampus.Db.DbConnection;
import vCampus.Entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDao implements BaseDao<User> {
    public Connection conn = null;
    public PreparedStatement pstmt = null;
    public ResultSet rs = null;

    @Override
    public boolean add(User user) {
        boolean bool = false;
        conn = DbConnection.getConnection();
        try {
            String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            int rowsAffected = pstmt.executeUpdate();
            bool = rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return bool;
    }

    @Override
    public boolean update(User user) {
        boolean bool = false;
        conn = DbConnection.getConnection();
        try {
            String sql = "UPDATE users SET password = ?, email = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getEmail());
            pstmt.setInt(3, user.getId());
            int rowsAffected = pstmt.executeUpdate();
            bool = rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return bool;
    }

    @Override
    public boolean delete(int id) {
        boolean bool = false;
        conn = DbConnection.getConnection();
        try {
            String sql = "DELETE FROM users WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            bool = rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return bool;
    }

    @Override
    public User find(int id) {
        User user = null;
        conn = DbConnection.getConnection();
        try {
            String sql = "SELECT * FROM users WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return user;
    }
}
