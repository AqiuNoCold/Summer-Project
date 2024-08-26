package vCampus.Dao;

import vCampus.Db.DbConnection;
import vCampus.Entity.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BooksDao implements BaseDao<Book> {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    @Override
    public boolean add(Book book) {
        boolean isAdded = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "INSERT INTO tblBooks (name, author, price, type, availableCopies) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, book.getName());
            pstmt.setString(2, book.getAuthor());
            pstmt.setInt(3, book.getPrice());
            pstmt.setString(4, book.getType());
            pstmt.setInt(5, book.getAvailableCopies());
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
    public boolean update(Book book) {
        boolean isUpdated = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "UPDATE tblBooks SET author = ?, price = ?, type = ?, availableCopies = ? WHERE name = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, book.getAuthor());
            pstmt.setInt(2, book.getPrice());
            pstmt.setString(3, book.getType());
            pstmt.setInt(4, book.getAvailableCopies());
            pstmt.setString(5, book.getName());
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
    public boolean delete(String name) {
        boolean isDeleted = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "DELETE FROM tblBooks WHERE name = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
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
    public Book find(String name) {
        Book book = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT * FROM tblBooks WHERE name = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                book = new Book(
                        rs.getString("name"),
                        rs.getString("author"),
                        rs.getInt("price"),
                        rs.getString("type"),
                        rs.getInt("availableCopies")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return book;
    }
}
