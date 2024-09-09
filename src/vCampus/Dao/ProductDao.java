package vCampus.Dao;

import vCampus.Db.DbConnection;
import vCampus.Entity.Shop.*;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDao implements BaseDao<Product> {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    @Override
    public boolean add(Product product) {
        boolean isAdded = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "INSERT INTO tblProduct (id, name, price, numbers, owner, discount, time, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, product.getId());
            pstmt.setString(2, product.getName());
            pstmt.setFloat(3, product.getPrice());
            pstmt.setInt(4, product.getNumbers());
            pstmt.setString(5, product.getOwner());
            pstmt.setFloat(6, product.getDiscount());
            pstmt.setTimestamp(7, new Timestamp(product.getTime().getTime()));
            // Handle the image as Blob
            byte[] imageBytes = product.getImage();
            if (imageBytes != null) {
                Blob blob = conn.createBlob();
                blob.setBytes(1, imageBytes);
                pstmt.setBlob(8, blob);
            } else {
                pstmt.setNull(8, Types.BLOB); // Set NULL if image is not present
            }
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
            String sql = "UPDATE tblProduct SET name = ?, price = ?, numbers = ?, owner = ?, discount = ?, time = ?,image = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, product.getName());
            pstmt.setFloat(2, product.getPrice());
            pstmt.setInt(3, product.getNumbers());
            pstmt.setString(4, product.getOwner());
            pstmt.setFloat(5, product.getDiscount());
            pstmt.setTimestamp(6, new Timestamp(product.getTime().getTime()));
            byte[] imageBytes = product.getImage();
            if (imageBytes != null) {
                Blob blob = conn.createBlob();
                blob.setBytes(1, imageBytes);
                pstmt.setBlob(7, blob);
            } else {
                pstmt.setNull(7, Types.BLOB); // Set NULL if image is not present
            }
            pstmt.setString(8, product.getId());
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
                Blob blob = rs.getBlob("image");
                product.setImage(blob);
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


    public String searchProduct(String productName) {
        List<String> Ids = new ArrayList<>();
        try {
            conn = DbConnection.getConnection();
            // 3. 创建查询语句
            String sql = "SELECT id FROM tblProduct WHERE name LIKE ?";

            // 4. 使用PreparedStatement来防止SQL注入
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                // 设置LIKE查询的参数
                statement.setString(1, "%" + productName + "%");

                // 5. 执行查询
                try {
                    rs = statement.executeQuery();
                    // 6. 处理结果
                    while (rs.next()) {
                        int productId = rs.getInt("id");
                        Ids.add(productId+"");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return String.join(",", Ids);
    }
}
