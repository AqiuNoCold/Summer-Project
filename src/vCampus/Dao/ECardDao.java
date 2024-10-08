package vCampus.Dao;

import vCampus.Db.DbConnection;
import vCampus.Entity.ECard.ECardDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ECardDao {
    private Connection conn = null;
    private PreparedStatement pstmt = null;

    public boolean add(ECardDTO cardInfo) {
        boolean isAdded = false;
        String sql = "INSERT INTO tblECard (remain, password, card) VALUES (?, ?, ?)";
        try {
            conn = DbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setFloat(1, cardInfo.getRemain());
            pstmt.setInt(2, cardInfo.getPassword());
            pstmt.setString(3, cardInfo.getCard());
            int rowsAffected = pstmt.executeUpdate();
            isAdded = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return isAdded;
    }
    public boolean update(ECardDTO cardInfo) {
        boolean isUpdated = false;
        String sql = "UPDATE tblECard SET remain = ?, password = ? WHERE card = ?";
        try {
            conn = DbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setFloat(1, cardInfo.getRemain());
            pstmt.setInt(2, cardInfo.getPassword());
            pstmt.setString(3, cardInfo.getCard());
            int rowsAffected = pstmt.executeUpdate();
            isUpdated = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return isUpdated;
    }

    public boolean updatePassword(Integer password, String card) {
        boolean isUpdated = false;
        String sql = "UPDATE tblECard SET password = ? WHERE card = ?";
        try {
            conn = DbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, password);
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

    public boolean updateRemain(Float remain, String card)  {
        boolean isUpdated = false;
        String sql = "UPDATE tblECard SET remain = ? WHERE card = ?";
        try {
            conn = DbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setFloat(1, remain);
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
    // 删除
    public boolean delete(String card) {
        boolean isDeleted = false;
        String sql = "DELETE FROM tblECard WHERE card = ?";

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

    // 查找
    public ECardDTO find(String card) {
        ECardDTO cardInfo = new ECardDTO(card);
        String sql = "SELECT * FROM tblECard WHERE card = ?";
        ResultSet rs = null;
        try {
            conn = DbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, card);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                cardInfo = new ECardDTO(
                        rs.getFloat("remain"),
                        rs.getInt("password"),
                        rs.getString("card")
                );
            }
            else{
                add(cardInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return cardInfo;
    }

//    public static void main(String[] args) {
//        ECardDao dao = new ECardDao();
//        dao.updateRemain(2000f,"111111111");
//    }
}
