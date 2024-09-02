package vCampus.Entity.Books;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import vCampus.Service.Books.BookShelfService;
import vCampus.Service.Books.BookService;
import vCampus.Service.Books.BookReviewService;

public class BookShelf implements Serializable {
    private Long id; // 书架ID
    private String name; // 书架名称
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
    private String userId; // 用户ID
    private List<Book> books; // 书架上的图书
    private List<BookReview> reviews; // 书架上的书评
    private List<String> bookIds; // 图书ID列表
    private List<String> reviewIds; // 书评ID列表
    private Boolean isPublic; // 书架是否公开
    private Integer subscribeCount; // 订阅数
    private Integer favoriteCount; // 收藏数
    private Boolean isLoaded; // 标志位，说明是否加载完成

    // 拷贝构造函数
    public BookShelf(BookShelf other) {
        this.id = other.id;
        this.name = other.name;
        this.createTime = other.createTime;
        this.updateTime = other.updateTime;
        this.userId = other.userId;
        this.books = new ArrayList<>(other.books);
        this.reviews = new ArrayList<>(other.reviews);
        this.bookIds = new ArrayList<>(other.bookIds);
        this.reviewIds = new ArrayList<>(other.reviewIds);
        this.isPublic = other.isPublic;
        this.subscribeCount = other.subscribeCount;
        this.favoriteCount = other.favoriteCount;
        this.isLoaded = other.isLoaded;
    }

    // 通过BookShelfService对象的get方法建立的构造函数
    public BookShelf(BookShelfService service, boolean lazyLoad) {
        this.id = service.getId();
        this.name = service.getName();
        this.createTime = service.getCreateTime();
        this.updateTime = service.getUpdateTime();
        this.userId = service.getUserId();
        this.isPublic = service.getIsPublic();
        this.subscribeCount = service.getSubscribeCount();
        this.favoriteCount = service.getFavoriteCount();
        this.isLoaded = !lazyLoad;
        this.bookIds = new ArrayList<>(service.getBookIds());
        this.reviewIds = new ArrayList<>(service.getReviewIds());

        if (!lazyLoad) {
            this.books = new ArrayList<>();
            for (BookService bookService : service.getBooks()) {
                this.books.add(new Book(bookService));
            }
            this.reviews = new ArrayList<>();
            for (BookReviewService reviewService : service.getReviews()) {
                this.reviews.add(new BookReview(reviewService));
            }
        } else {
            this.books = new ArrayList<>();
            this.reviews = new ArrayList<>();
        }
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public List<BookReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<BookReview> reviews) {
        this.reviews = reviews;
    }

    public List<String> getBookIds() {
        return bookIds;
    }

    public void setBookIds(List<String> bookIds) {
        this.bookIds = bookIds;
    }

    public List<String> getReviewIds() {
        return reviewIds;
    }

    public void setReviewIds(List<String> reviewIds) {
        this.reviewIds = reviewIds;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Integer getSubscribeCount() {
        return subscribeCount;
    }

    public void setSubscribeCount(Integer subscribeCount) {
        this.subscribeCount = subscribeCount;
    }

    public Integer getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Integer favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public Boolean getIsLoaded() {
        return isLoaded;
    }

    public void setIsLoaded(Boolean isLoaded) {
        this.isLoaded = isLoaded;
    }
}