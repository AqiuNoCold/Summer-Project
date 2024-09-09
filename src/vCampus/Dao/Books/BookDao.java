package vCampus.Dao.Books;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vCampus.Dao.BaseDao;
import vCampus.Db.DbConnection;
import vCampus.Service.Books.BookService;
import vCampus.Dao.Criteria.BookSearchCriteria;
import vCampus.Dao.Criteria.BookSortCriteria;

public class BookDao implements BaseDao<BookService> {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    @Override
    public boolean add(BookService book) {
        boolean isAdded = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "INSERT INTO tblBooks (isbn, msrp, image, pages, title, isbn13, authors, binding, edition, related, language, subjects, synopsis, publisher, dimensions, title_long, date_published, copy_count, review_count, average_rating, favorite_count, borrow_count, is_active, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, book.getIsbn());
            pstmt.setBigDecimal(2, book.getMsrp());
            pstmt.setString(3, book.getImage());
            pstmt.setInt(4, book.getPages());
            pstmt.setString(5, book.getTitle());
            pstmt.setString(6, book.getIsbn13());
            pstmt.setString(7, book.getAuthors());
            pstmt.setString(8, book.getBinding());
            pstmt.setString(9, book.getEdition());
            pstmt.setString(10, book.getRelated());
            pstmt.setString(11, book.getLanguage());
            pstmt.setString(12, book.getSubjects());
            pstmt.setString(13, book.getSynopsis());
            pstmt.setString(14, book.getPublisher());
            pstmt.setString(15, book.getDimensions());
            pstmt.setString(16, book.getTitleLong());
            pstmt.setString(17, book.getDatePublished());
            pstmt.setInt(18, book.getCopyCount());
            pstmt.setInt(19, book.getReviewCount());
            pstmt.setBigDecimal(20, book.getAverageRating());
            pstmt.setInt(21, book.getFavoriteCount());
            pstmt.setInt(22, book.getBorrowCount());
            pstmt.setBoolean(23, book.isActive());
            pstmt.setBoolean(24, book.isDeleted());
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
    public boolean update(BookService book) {
        boolean isUpdated = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "UPDATE tblBooks SET msrp = ?, image = ?, pages = ?, title = ?, authors = ?, binding = ?, edition = ?, related = ?, language = ?, subjects = ?, synopsis = ?, publisher = ?, dimensions = ?, title_long = ?, date_published = ?, copy_count = ?, review_count = ?, average_rating = ?, favorite_count = ?, borrow_count = ?, is_active = ?, is_deleted = ? WHERE isbn = ? AND isbn13 = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1, book.getMsrp());
            pstmt.setString(2, book.getImage());
            pstmt.setInt(3, book.getPages());
            pstmt.setString(4, book.getTitle());
            pstmt.setString(5, book.getAuthors());
            pstmt.setString(6, book.getBinding());
            pstmt.setString(7, book.getEdition());
            pstmt.setString(8, book.getRelated());
            pstmt.setString(9, book.getLanguage());
            pstmt.setString(10, book.getSubjects());
            pstmt.setString(11, book.getSynopsis());
            pstmt.setString(12, book.getPublisher());
            pstmt.setString(13, book.getDimensions());
            pstmt.setString(14, book.getTitleLong());
            pstmt.setString(15, book.getDatePublished());
            pstmt.setInt(16, book.getCopyCount());
            pstmt.setInt(17, book.getReviewCount());
            pstmt.setBigDecimal(18, book.getAverageRating());
            pstmt.setInt(19, book.getFavoriteCount());
            pstmt.setInt(20, book.getBorrowCount());
            pstmt.setBoolean(21, book.isActive());
            pstmt.setBoolean(22, book.isDeleted());
            pstmt.setString(23, book.getIsbn());
            pstmt.setString(24, book.getIsbn13());
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
            String sql = "UPDATE tblBooks SET isDeleted = ? WHERE isbn = ? AND isbn13 = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setBoolean(1, true);
            pstmt.setString(2, isbn);
            pstmt.setString(3, isbn13);
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
    public BookService find(String id) {
        BookService book = null;
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
                book = new BookService(
                        rs.getString("isbn"),
                        rs.getBigDecimal("msrp"),
                        rs.getString("image"),
                        rs.getInt("pages"),
                        rs.getString("title"),
                        rs.getString("isbn13"),
                        rs.getString("authors"),
                        rs.getString("binding"),
                        rs.getString("edition"),
                        rs.getString("related"),
                        rs.getString("language"),
                        rs.getString("subjects"),
                        rs.getString("synopsis"),
                        rs.getString("publisher"),
                        rs.getString("dimensions"),
                        rs.getString("title_long"),
                        rs.getString("date_published"),
                        rs.getInt("copy_count"),
                        rs.getInt("review_count"),
                        rs.getBigDecimal("average_rating"),
                        rs.getInt("favorite_count"),
                        rs.getInt("borrow_count"),
                        rs.getBoolean("is_active"),
                        rs.getBoolean("is_deleted"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return book;
    }

    public int getTotalBooks(BookSearchCriteria searchCriteria) {
        int totalBooks = 0;
        Connection conn = null;
        try {
            conn = DbConnection.getConnection();
            StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM tblBooks WHERE is_deleted = false");
            List<String> params = new ArrayList<>();
            boolean firstCondition = true;

            if (!searchCriteria.getCriteria().isEmpty()) {
                sql.append(" AND (");
                for (Map.Entry<String, String> entry : searchCriteria.getCriteria().entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    String operator = searchCriteria.getOperators().get(key);
                    if (searchCriteria.isValidCriteria(key)) {
                        if (!firstCondition) {
                            sql.append(" ").append(operator).append(" ");
                        } else {
                            firstCondition = false;
                        }
                        switch (key) {
                            case "isbn":
                            case "isbn13":
                                sql.append(key).append(" = ?");
                                params.add(value);
                                break;
                            case "language":
                                sql.append(key).append(" LIKE ?");
                                params.add(value + "%");
                                break;
                            case "title":
                                sql.append("MATCH(title, title_long) AGAINST (? IN BOOLEAN MODE)");
                                params.add("\"" + value + "\""); // 使用引号包裹搜索关键字
                                break;
                            case "authors":
                            case "subjects":
                            case "synopsis":
                            case "publisher":
                                sql.append("MATCH(").append(key).append(") AGAINST (? IN BOOLEAN MODE)");
                                params.add("\"" + value + "\""); // 使用引号包裹搜索关键字
                                break;
                            default:
                                break;
                        }
                    }
                }
                sql.append(")");
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

    public List<String> findBooksByPage(
            BookSearchCriteria searchCriteria,
            BookSortCriteria sortCriteria,
            int page,
            int pageSize) {
        List<String> bookIds = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DbConnection.getConnection();
            StringBuilder sql = new StringBuilder("SELECT isbn, isbn13 FROM tblBooks WHERE is_deleted = false");
            List<String> params = new ArrayList<>();
            boolean firstCondition = true;

            if (!searchCriteria.getCriteria().isEmpty()) {
                sql.append(" AND (");
                for (Map.Entry<String, String> entry : searchCriteria.getCriteria().entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    String operator = searchCriteria.getOperators().get(key);
                    if (searchCriteria.isValidCriteria(key)) {
                        if (!firstCondition) {
                            sql.append(" ").append(operator).append(" ");
                        } else {
                            firstCondition = false;
                        }
                        switch (key) {
                            case "isbn":
                            case "isbn13":
                                sql.append(key).append(" = ?");
                                params.add(value);
                                break;
                            case "language":
                                sql.append(key).append(" LIKE ?");
                                params.add(value + "%");
                                break;
                            case "title":
                                sql.append("MATCH(title, title_long) AGAINST (? IN BOOLEAN MODE)");
                                params.add("\"" + value + "\""); // 使用引号包裹搜索关键字
                                break;
                            case "authors":
                            case "subjects":
                            case "synopsis":
                            case "publisher":
                                sql.append("MATCH(").append(key).append(") AGAINST (? IN BOOLEAN MODE)");
                                params.add("\"" + value + "\""); // 使用引号包裹搜索关键字
                                break;
                            default:
                                break;
                        }
                    }
                }
                sql.append(")");
            }

            if (!sortCriteria.getCriteria().isEmpty()) {
                sql.append(" ORDER BY ");
                for (int i = 0; i < sortCriteria.getCriteria().size(); i++) {
                    String criterion = sortCriteria.getCriteria().get(i);
                    if (sortCriteria.isValidCriteria(criterion)) {
                        sql.append(criterion);
                        if (i < sortCriteria.getCriteria().size() - 1) {
                            sql.append(", ");
                        }
                    }
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

    public List<String> findRandomBooks(int count) {
        List<String> bookIds = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT isbn, isbn13 FROM tblBooks WHERE is_deleted = false ORDER BY RAND() LIMIT ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, count);
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