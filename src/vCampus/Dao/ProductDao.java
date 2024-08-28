package vCampus.Dao;

import vCampus.Db.DbConnection;
import vCampus.Entity.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ProductDao implements BaseDao<Product> {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    @Override
    public boolean add(Product product) {
        boolean isAdded = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "INSERT INTO tblProduct (id, name, price, numbers, owner, discount, time) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, product.getId());
            pstmt.setString(2, product.getName());
            pstmt.setFloat(3, product.getPrice());
            pstmt.setInt(4, product.getNumbers());
            pstmt.setString(5, product.getOwner());
            pstmt.setFloat(6, product.getDiscount());
            pstmt.setTimestamp(7, new Timestamp(product.getTime().getTime()));
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
    public boolean update(Product product) {
        boolean isUpdated = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "UPDATE tblProduct SET name = ?, price = ?, numbers = ?, owner = ?, discount = ?, time = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, product.getName());
            pstmt.setFloat(2, product.getPrice());
            pstmt.setInt(3, product.getNumbers());
            pstmt.setString(4, product.getOwner());
            pstmt.setFloat(5, product.getDiscount());
            pstmt.setTimestamp(6, new Timestamp(product.getTime().getTime()));
            pstmt.setString(7, product.getId());
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
            String sql = "DELETE FROM tblProduct WHERE id = ?";
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
    public Product find(String id) {
        Product product = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT * FROM tblProduct WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                product = new Product(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getFloat("price"),
                        rs.getInt("numbers"),
                        rs.getString("owner")
                );
                product.setDiscount(rs.getFloat("discount"));
                product.setTime(rs.getTimestamp("time"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return product;
    }
}
