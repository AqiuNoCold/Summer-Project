package vCampus.Dao;

import vCampus.Db.DbConnection;
import vCampus.Entity.ECard.ECard;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class TransactionDao{
    private Connection conn = null;
    private PreparedStatement pstmt = null;

    // 添加交易记录
    public boolean add(ECard eCard) {
        boolean isAdded = false;
        String sql = "INSERT INTO tblTransaction (transaction, card) VALUES (?, ?)";

        try {
            conn = DbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, String.join(",", eCard.getTransactionHistory()));
            pstmt.setString(2, eCard.getCard());
            int rowsAffected = pstmt.executeUpdate();
            isAdded = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return isAdded;
    }

    // 更新交易记录
    public boolean update(String newHistory,String card) {
        boolean isUpdated = false;
        String sql = "UPDATE tblTransaction SET transaction= ? WHERE card = ?";

        try {
            conn = DbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, String.join(",", newHistory));
            pstmt.setString(2, card);
            int rowsAffected = pstmt.executeUpdate();
            isUpdated = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return isUpdated;
    }

    // 删除交易记录
    public boolean delete(String card) {
        boolean isDeleted = false;
        String sql = "DELETE FROM tblTransaction WHERE card = ?";

        try {
            conn = DbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, card);
            int rowsAffected = pstmt.executeUpdate();
            isDeleted = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return isDeleted;
    }

    // 查找交易记录
    public String find(String card) {
        ECard eCard = null;
        String sql = "SELECT * FROM tblTransaction WHERE card = ?";
        ResultSet rs = null;
        String transaction = null;
        try {
            conn = DbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, card);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                transaction = rs.getString("transactionHistory");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }

        return transaction;
    }
}