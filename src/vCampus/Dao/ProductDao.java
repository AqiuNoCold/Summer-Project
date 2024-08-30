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

    public int getRecordCount(String tableName) {
        int count = 0;
        try {
            conn = DbConnection.getConnection();
            String query = "SELECT COUNT(*) FROM " + tableName;
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1); // 获取计数结果
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return count;
    }

    /*
    public String searchProduct(String keyword) {
        try {
            conn = DbConnection.getConnection();
            String query = "SELECT COUNT(*) FROM " + tableName;
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1); // 获取计数结果
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }


        // JDBC连接、查询和结果处理
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // SQL查询，使用LIKE进行模糊匹配
            String sql = "SELECT * FROM products WHERE product_name LIKE ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                // 设置查询参数
                statement.setString(1, "%" + keyword + "%");

                // 执行查询
                try (ResultSet resultSet = statement.executeQuery()) {
                    // 处理结果
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String productName = resultSet.getString("product_name");
                        double price = resultSet.getDouble("price");

                        System.out.println("ID: " + id);
                        System.out.println("Product Name: " + productName);
                        System.out.println("Price: " + price);
                        System.out.println("--------------------");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    */
}
