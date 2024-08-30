package vCampus.Entity.Books;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import vCampus.Dao.Books.BookDao;
import vCampus.Dao.Books.BookReviewDao;

public class BookShelf {
    private Long id; // 书架ID
    private String name; // 书架名称
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
    private Long userId; // 用户ID
    private List<Book> books; // 书架上的图书
    private List<BookReview> reviews; // 书架上的书评
    private List<String> bookIds; // 图书ID列表
    private List<String> reviewIds; // 书评ID列表
    private boolean isDirty = false; // 脏数据标志

    // 构造方法
    public BookShelf(Long id, String name, Long userId, LocalDateTime createTime, LocalDateTime updateTime,
            List<String> bookIds, List<String> reviewIds) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.books = new ArrayList<>();
        this.reviews = new ArrayList<>();
        this.bookIds = bookIds != null ? new ArrayList<>(bookIds) : new ArrayList<>();
        this.reviewIds = reviewIds != null ? new ArrayList<>(reviewIds) : new ArrayList<>();
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
        this.isDirty = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.isDirty = true;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        this.isDirty = true;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        this.isDirty = true;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
        this.isDirty = true;
    }

    public List<String> getBookIds() {
        if (bookIds.isEmpty() && !books.isEmpty()) {
            bookIds = new ArrayList<>();
            for (Book book : books) {
                bookIds.add(book.getId());
            }
        }
        return bookIds;
    }

    public void setBookIds(List<String> bookIds) {
        this.bookIds = bookIds;
        this.books.clear();
        this.isDirty = true;
    }

    // 获取图书列表
    public List<Book> getBooks() {
        if (books.isEmpty() && !bookIds.isEmpty()) {
            BookDao bookDao = new BookDao();
            for (String bookId : bookIds) {
                Book book = bookDao.find(bookId);
                if (book != null) {
                    books.add(book);
                }
            }
        }
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        this.isDirty = true;
    }

    public List<String> getReviewIds() {
        if (reviewIds.isEmpty() && !reviews.isEmpty()) {
            reviewIds = new ArrayList<>();
            for (BookReview review : reviews) {
                reviewIds.add(String.valueOf(review.getId()));
            }
        }
        return reviewIds;
    }

    public void setReviewIds(List<String> reviewIds) {
        this.reviewIds = reviewIds;
        this.reviews.clear();
        this.isDirty = true;
    }

    // 获取书评列表
    public List<BookReview> getReviews() {
        if (reviews.isEmpty() && !reviewIds.isEmpty()) {
            BookReviewDao reviewDao = new BookReviewDao();
            for (String reviewId : reviewIds) {
                BookReview review = reviewDao.find(reviewId);
                if (review != null) {
                    reviews.add(review);
                }
            }
        }
        return reviews;
    }

    public void setReviews(List<BookReview> reviews) {
        this.reviews = reviews;
        this.isDirty = true;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty(boolean isDirty) {
        this.isDirty = isDirty;
    }

    // 添加图书
    public boolean addBook(Book book) {
        if (!books.contains(book)) {
            books.add(book);
            this.isDirty = true;
            return true;
        }
        return false;
    }

    // 移除图书
    public boolean removeBook(Book book) {
        if (books.remove(book)) {
            this.isDirty = true;
            return true;
        }
        return false;
    }

    // 添加书评
    public void addReview(BookReview review) {
        if (!reviews.contains(review)) {
            reviews.add(review);
            this.isDirty = true;
        }
    }

    // 复制书架
    public BookShelf copy() {
        BookShelf copy = new BookShelf(
                null,
                this.name,
                this.userId,
                LocalDateTime.now(),
                LocalDateTime.now(),
                this.bookIds,
                this.reviewIds);
        copy.setBooks(new ArrayList<>(this.books));
        copy.setReviews(new ArrayList<>(this.reviews));
        return copy;
    }
}