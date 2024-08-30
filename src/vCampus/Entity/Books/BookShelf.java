package vCampus.Entity.Books;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookShelf {
    private Long id; // 书架ID
    private String name; // 书架名称
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
    private Long userId; // 用户ID
    private List<Book> books; // 书架上的图书
    private List<BookReview> reviews; // 书架上的书评
    private boolean isDirty = false; // 脏数据标志

    // 构造方法
    public BookShelf(Long id, String name, Long userId, LocalDateTime createTime, LocalDateTime updateTime) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.books = new ArrayList<>();
        this.reviews = new ArrayList<>();
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

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        this.isDirty = true;
    }

    public List<BookReview> getReviews() {
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
    public void addBook(Book book) {
        if (!books.contains(book)) {
            books.add(book);
            this.isDirty = true;
        }
    }

    // 移除图书
    public void removeBook(Book book) {
        books.remove(book);
        this.isDirty = true;
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
        BookShelf copy = new BookShelf(null, this.name, this.userId, LocalDateTime.now(), LocalDateTime.now());
        copy.setBooks(new ArrayList<>(this.books));
        copy.setReviews(new ArrayList<>(this.reviews));
        return copy;
    }
}