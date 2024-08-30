package vCampus.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

import vCampus.Db.DbConnection;
import vCampus.Entity.Books.Book;

public class BookDao implements BaseDao<Book> {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    @Override
    public boolean add(Book book) {
        boolean isAdded = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "INSERT INTO tblBooks (isbn, msrp, image, pages, title, isbn13, authors, edition, language, subjects, synopsis, publisher, title_long, date_published) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getMsrp());
            pstmt.setString(3, book.getImage());
            pstmt.setString(4, book.getPages());
            pstmt.setString(5, book.getTitle());
            pstmt.setString(6, book.getIsbn13());
            pstmt.setString(7, book.getAuthors());
            pstmt.setString(8, book.getEdition());
            pstmt.setString(9, book.getLanguage());
            pstmt.setString(10, book.getSubjects());
            pstmt.setString(11, book.getSynopsis());
            pstmt.setString(12, book.getPublisher());
            pstmt.setString(13, book.getTitleLong());
            pstmt.setString(14, book.getDatePublished());
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
            String sql = "UPDATE tblBooks SET msrp = ?, image = ?, pages = ?, title = ?, authors = ?, edition = ?, language = ?, subjects = ?, synopsis = ?, publisher = ?, title_long = ?, date_published = ? WHERE isbn = ? AND isbn13 = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, book.getMsrp());
            pstmt.setString(2, book.getImage());
            pstmt.setString(3, book.getPages());
            pstmt.setString(4, book.getTitle());
            pstmt.setString(5, book.getAuthors());
            pstmt.setString(6, book.getEdition());
            pstmt.setString(7, book.getLanguage());
            pstmt.setString(8, book.getSubjects());
            pstmt.setString(9, book.getSynopsis());
            pstmt.setString(10, book.getPublisher());
            pstmt.setString(11, book.getTitleLong());
            pstmt.setString(12, book.getDatePublished());
            pstmt.setString(13, book.getIsbn());
            pstmt.setString(14, book.getIsbn13());
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
            String[] ids = id.split(",");
            String isbn = ids[0];
            String isbn13 = ids[1];
            String sql = "DELETE FROM tblBooks WHERE isbn = ? AND isbn13 = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, isbn);
            pstmt.setString(2, isbn13);
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
    public Book find(String id) {
        Book book = null;
        try {
            conn = DbConnection.getConnection();
            String[] ids = id.split(",");
            String isbn = ids[0];
            String isbn13 = ids[1];
            String sql = "SELECT * FROM tblBooks WHERE isbn = ? AND isbn13 = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, isbn);
            pstmt.setString(2, isbn13);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                book = new Book(
                        rs.getString("isbn"),
                        rs.getString("msrp"),
                        rs.getString("image"),
                        rs.getString("pages"),
                        rs.getString("title"),
                        rs.getString("isbn13"),
                        rs.getString("authors"),
                        rs.getString("edition"),
                        rs.getString("language"),
                        rs.getString("subjects"),
                        rs.getString("synopsis"),
                        rs.getString("publisher"),
                        rs.getString("title_long"),
                        rs.getString("date_published"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return book;
    }

    public int getTotalBooks(Map<String, String> searchCriteria) {
        int totalBooks = 0;
        try {
            Connection conn = DbConnection.getConnection();
            StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM tblBooks WHERE 1=1");
            List<String> params = new ArrayList<>();

            for (Map.Entry<String, String> entry : searchCriteria.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                switch (key) {
                    case "isbn":
                    case "isbn13":
                        sql.append(" AND ").append(key).append(" = ?");
                        params.add(value);
                        break;
                    case "language":
                        sql.append(" AND ").append(key).append(" LIKE ?");
                        params.add(value + "%");
                        break;
                    case "title":
                    case "authors":
                    case "subjects":
                    case "synopsis":
                    case "publisher":
                    case "title_long":
                        if (value.contains("%")) {
                            sql.append(" AND ").append(key).append(" LIKE ?");
                        } else {
                            sql.append(" AND ").append(key).append(" = ?");
                        }
                        params.add(value);
                        break;
                    default:
                        break;
                }
            }

            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setString(i + 1, params.get(i));
            }

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                totalBooks = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return totalBooks;
    }

    public List<String> findBooksByPage(Map<String, String> searchCriteria, int page, int pageSize) {
        List<String> bookIds = new ArrayList<>();
        try {
            Connection conn = DbConnection.getConnection();
            StringBuilder sql = new StringBuilder("SELECT isbn, isbn13 FROM tblBooks WHERE 1=1");
            List<String> params = new ArrayList<>();

            for (Map.Entry<String, String> entry : searchCriteria.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                switch (key) {
                    case "isbn":
                    case "isbn13":
                        sql.append(" AND ").append(key).append(" = ?");
                        params.add(value);
                        break;
                    case "language":
                        sql.append(" AND ").append(key).append(" LIKE ?");
                        params.add(value + "%");
                        break;
                    case "title":
                        sql.append(
                                " AND (MATCH(title) AGAINST (? IN BOOLEAN MODE) OR MATCH(title_long) AGAINST (? IN BOOLEAN MODE))");
                        params.add(value);
                        params.add(value);
                        break;
                    case "authors":
                    case "subjects":
                    case "synopsis":
                    case "publisher":
                        sql.append(" AND MATCH(").append(key).append(") AGAINST (? IN BOOLEAN MODE)");
                        params.add(value);
                        break;
                    default:
                        break;
                }
            }

            sql.append(" LIMIT ? OFFSET ?");
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setString(i + 1, params.get(i));
            }
            pstmt.setInt(params.size() + 1, pageSize);
            pstmt.setInt(params.size() + 2, (page - 1) * pageSize);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String isbn = rs.getString("isbn");
                String isbn13 = rs.getString("isbn13");
                String bookId = isbn + "," + isbn13;
                bookIds.add(bookId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return bookIds;
    }

    public List<String> findBooksByPage(Map<String, String> searchCriteria, int page, int pageSize) {
        List<String> bookIds = new ArrayList<>();
        try {
            Connection conn = DbConnection.getConnection();
            StringBuilder sql = new StringBuilder("SELECT isbn, isbn13 FROM tblBooks WHERE 1=1");
            List<String> params = new ArrayList<>();

            for (Map.Entry<String, String> entry : searchCriteria.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (value.contains("%")) {
                    sql.append(" AND ").append(key).append(" LIKE ?");
                } else {
                    sql.append(" AND ").append(key).append(" = ?");
                }
                params.add(value);
            }

            sql.append(" LIMIT ? OFFSET ?");
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setString(i + 1, params.get(i));
            }
            pstmt.setInt(params.size() + 1, pageSize);
            pstmt.setInt(params.size() + 2, (page - 1) * pageSize);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String isbn = rs.getString("isbn");
                String isbn13 = rs.getString("isbn13");
                String bookId = isbn + "," + isbn13;
                bookIds.add(bookId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return bookIds;
    }
}