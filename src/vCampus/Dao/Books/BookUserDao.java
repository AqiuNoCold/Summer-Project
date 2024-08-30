package vCampus.Dao.Books;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import vCampus.Dao.BaseDao;
import vCampus.Db.DbConnection;
import vCampus.Entity.Books.Book;
import vCampus.Entity.Books.BookShelf;
import vCampus.Entity.Books.BorrowRecord;
import vCampus.Entity.Books.BookUser;

public class BookUserDao implements BaseDao<BookUser> {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    @Override
    public boolean add(BookUser bookUser) {
        boolean isAdded = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "INSERT INTO tblBookUsers (id, default_bookshelf, all_bookshelves) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookUser.getCard());
            pstmt.setLong(2, bookUser.getDefaultBookShelf().getId());
            pstmt.setString(3, bookUser.getAllBookShelvesAsString());
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
    public boolean update(BookUser bookUser) {
        boolean isUpdated = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "UPDATE tblBookUsers SET default_bookshelf = ?, all_bookshelves = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, bookUser.getDefaultBookShelf().getId());
            pstmt.setString(2, bookUser.getAllBookShelvesAsString());
            pstmt.setString(3, bookUser.getCard());
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
            String sql = "DELETE FROM tblBookUsers WHERE id = ?";
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
    public BookUser find(String id) {
        BookUser bookUser = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT * FROM tblBookUsers WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                BookShelf defaultBookShelf = new BookShelf(rs.getLong("default_bookshelf"));
                bookUser = new BookUser(rs.getString("id"));
                bookUser.setDefaultBookShelf(defaultBookShelf);
                bookUser.setAllBookShelvesFromString(rs.getString("all_bookshelves"));
                // bookUser.setBorrowRecords(getBorrowRecords(id));
                // bookUser.setBookShelves(getBookShelves(id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return bookUser;
    }
}