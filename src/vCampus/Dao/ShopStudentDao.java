package vCampus.Dao;

import vCampus.Db.DbConnection;
import vCampus.Entity.Shop.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ShopStudentDao{
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    public boolean add(ShopStudent shopStudent) {
        boolean isAdded = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "INSERT INTO tblShopStudent (id, card, favorites, belongs, bill) " +
                    "VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, shopStudent.getId());
            pstmt.setString(2, shopStudent.getCard());
            pstmt.setString(3, shopStudent.getFavoritesId());
            pstmt.setString(4, shopStudent.getBelongsId());
            pstmt.setString(5, shopStudent.getBillId());
            int rowsAffected = pstmt.executeUpdate();
            isAdded = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return isAdded;
    }

    public boolean update(ShopStudent shopStudent) {
        boolean isUpdated = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "UPDATE tblShopStudent SET card = ?, favorites = ?, belongs = ?, bill = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, shopStudent.getCard());
            pstmt.setString(2, shopStudent.getFavoritesId());
            pstmt.setString(3, shopStudent.getBelongsId());
            pstmt.setString(4, shopStudent.getBillId());
            pstmt.setString(5, shopStudent.getId());
            int rowsAffected = pstmt.executeUpdate();
            isUpdated = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return isUpdated;
    }

    public boolean delete(String id) {
        boolean isDeleted = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "DELETE FROM tblShopStudent WHERE id = ?";
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

    public ShopStudentData find(String id) {
        ShopStudentData data = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT favorites, belongs, bill FROM tblShopStudent WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                data = new ShopStudentData(
                        rs.getString("favorites"),
                        rs.getString("belongs"),
                        rs.getString("bill")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return data;
    }

    // 静态内部类用于传递数据
    public static class ShopStudentData {
        private String favorites;
        private String belongs;
        private String bill;

        public ShopStudentData(String favorites, String belongs, String bill) {
            this.favorites = favorites;
            this.belongs = belongs;
            this.bill = bill;
        }

        public String getFavorites() {
            return favorites;
        }

        public void setFavorites(String favorites) {
            this.favorites = favorites;
        }

        public String getBelongs() {
            return belongs;
        }

        public void setBelongs(String belongs) {
            this.belongs = belongs;
        }

        public String getBill() {
            return bill;
        }

        public void setBill(String bill) {
            this.bill = bill;
        }

        @Override
        public String toString() {
            return "ShopStudentData{" +
                    "favorites='" + favorites + '\'' +
                    ", belongs='" + belongs + '\'' +
                    ", bill='" + bill + '\'' +
                    '}';
        }
    }
}
