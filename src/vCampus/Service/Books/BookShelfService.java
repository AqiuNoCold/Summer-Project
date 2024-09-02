package vCampus.Service.Books;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import vCampus.Dao.Books.BookDao;
import vCampus.Dao.Books.BookReviewDao;
import vCampus.Dao.Books.BookShelfDao;
import vCampus.Entity.Books.Book;
import vCampus.Entity.Books.BookReview;
import vCampus.Entity.Books.BookShelf;

public class BookShelfService {
    private Long id; // 书架ID
    private String name; // 书架名称
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
    private String userId; // 用户ID
    private List<BookService> books; // 书架上的图书
    private List<BookReviewService> reviews; // 书架上的书评
    private List<String> bookIds; // 图书ID列表
    private List<String> reviewIds; // 书评ID列表
    private Boolean isPublic; // 书架是否公开
    private Integer subscribeCount; // 订阅数
    private Integer favoriteCount; // 收藏数
    private Boolean isLoaded; // 标志位，说明是否加载完成

    // 键为书籍的id，值为书籍对象
    Map<String, BookService> bookMap = new HashMap<>();

    // 原有的构造方法，用于从数据库中直接获取数据
    public BookShelfService(Long id, String name, String userId, LocalDateTime createTime, LocalDateTime updateTime,
            List<String> bookIds, List<String> reviewIds, Boolean isPublic, Integer subscribeCount,
            Integer favoriteCount) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.books = new ArrayList<>();
        this.reviews = new ArrayList<>();
        this.bookIds = bookIds != null ? new ArrayList<>(bookIds) : new ArrayList<>();
        this.reviewIds = reviewIds != null ? new ArrayList<>(reviewIds) : new ArrayList<>();
        this.isPublic = isPublic;
        this.subscribeCount = subscribeCount;
        this.favoriteCount = favoriteCount;
        this.isLoaded = false;
    }

    // 用于首次登录时创建默认书架
    public BookShelfService(String userId) {
        this("我的书架", userId);
    }

    // 用于首次登录时创建指定名称的书架
    public BookShelfService(String name, String userId) {
        this.name = (name == null || name.isEmpty()) ? "我的书架" : name;
        this.userId = userId;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.books = new ArrayList<>();
        this.reviews = new ArrayList<>();
        this.bookIds = new ArrayList<>();
        this.reviewIds = new ArrayList<>();
        this.isPublic = false;
        this.subscribeCount = 0;
        this.favoriteCount = 0;
        this.isLoaded = false;

        // 保存到数据库并获取自动分配的ID
        BookShelfDao bookShelfDao = new BookShelfDao();
        this.id = bookShelfDao.save(this);
    }

    // 拷贝构造函数，用于用户尝试收藏别人的书架
    public BookShelfService(BookShelfService other) {
        other.subscribeCount++;
        new BookShelfDao().update(other);

        this.id = other.id;
        this.name = other.name;
        this.userId = other.userId;
        this.createTime = other.createTime;
        this.updateTime = other.updateTime;
        this.books = new ArrayList<>(other.books);
        this.reviews = new ArrayList<>(other.reviews);
        this.bookIds = new ArrayList<>(other.bookIds);
        this.reviewIds = new ArrayList<>(other.reviewIds);
        this.isPublic = other.isPublic;
        this.subscribeCount = other.subscribeCount;
        this.favoriteCount = other.favoriteCount;
        this.isLoaded = other.isLoaded;
    }

    // 克隆构造函数，用于用户尝试克隆别人的书架
    public BookShelfService(BookShelfService other, String newUserId) {
        other.favoriteCount++;
        new BookShelfDao().update(other);

        this.name = other.name;
        this.userId = newUserId; // 使用提供的userId
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.books = new ArrayList<>(other.books);
        this.reviews = new ArrayList<>(other.reviews);
        this.bookIds = new ArrayList<>(other.bookIds);
        this.reviewIds = new ArrayList<>(other.reviewIds);
        this.isPublic = other.isPublic;
        this.subscribeCount = 0;
        this.favoriteCount = 0;
        this.isLoaded = other.isLoaded;

        // 保存到数据库并获取自动分配的ID
        BookShelfDao bookShelfDao = new BookShelfDao();
        this.id = bookShelfDao.save(this);
    }

    // 仅提供id的构造函数
    public BookShelfService(Long id) {
        BookShelfDao bookShelfDao = new BookShelfDao();
        BookShelfService bookShelf = bookShelfDao.find(String.valueOf(id));
        if (bookShelf != null) {
            this.id = bookShelf.getId();
            this.name = bookShelf.getName();
            this.userId = bookShelf.getUserId();
            this.createTime = bookShelf.getCreateTime();
            this.updateTime = bookShelf.getUpdateTime();
            this.bookIds = bookShelf.getBookIds();
            this.reviewIds = bookShelf.getReviewIds();
            this.isPublic = bookShelf.getIsPublic();
            this.subscribeCount = bookShelf.getSubscribeCount();
            this.favoriteCount = bookShelf.getFavoriteCount();
            this.isLoaded = false;
        } else {
            throw new IllegalArgumentException("BookShelf with id " + id + " not found.");
        }
    }

    // 根据BookShelf实体类建立服务类的构造方法
    public BookShelfService(BookShelf bookShelf) {
        this.id = bookShelf.getId();
        this.name = bookShelf.getName();
        this.createTime = bookShelf.getCreateTime();
        this.updateTime = bookShelf.getUpdateTime();
        this.userId = bookShelf.getUserId();
        this.books = new ArrayList<>();
        for (Book book : bookShelf.getBooks()) {
            this.books.add(new BookService(book));
        }
        this.reviews = new ArrayList<>();
        for (BookReview review : bookShelf.getReviews()) {
            this.reviews.add(new BookReviewService(review));
        }
        this.isPublic = bookShelf.getIsPublic();
        this.subscribeCount = bookShelf.getSubscribeCount();
        this.favoriteCount = bookShelf.getFavoriteCount();
        this.bookIds = new ArrayList<>(bookShelf.getBookIds());
        this.reviewIds = new ArrayList<>(bookShelf.getReviewIds());
        this.isLoaded = bookShelf.getIsLoaded();
    }

    // 加载图书和书评
    public void loadBooksAndReviews() {
        if (!isLoaded) {
            BookDao bookDao = new BookDao();
            for (String bookId : bookIds) {
                BookService book = bookDao.find(bookId);
                if (book != null) {
                    books.add(book);
                }
            }

            boolean fastLoad = false;
            if (bookMap.isEmpty() && !books.isEmpty()) {
                for (BookService book : books) {
                    bookMap.put(book.getId(), book);
                }
                fastLoad = true;
            }

            BookReviewDao reviewDao = new BookReviewDao();
            for (String reviewId : reviewIds) {
                BookReviewService review = reviewDao.find(reviewId);
                if (review != null) {
                    String reviewBookId = review.getBookId();
                    if (fastLoad) {
                        review.setBook(bookMap.get(reviewBookId));
                    } else {
                        review.setBook(new BookService(reviewBookId));
                    }
                    reviews.add(review);
                }
            }
            this.isLoaded = true;
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
        if (updateTime == null) {
            updateTime = createTime;
        }
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

    public List<String> getBookIds() {
        if (bookIds.isEmpty() && !books.isEmpty()) {
            bookIds = new ArrayList<>();
            for (BookService book : books) {
                bookIds.add(book.getId());
            }
        }
        return bookIds;
    }

    public void setBookIds(List<String> bookIds) {
        this.bookIds = bookIds;
        this.books.clear();
        this.isLoaded = false;
    }

    // 获取图书列表
    public List<BookService> getBooks() {
        if (!isLoaded) {
            loadBooksAndReviews();
        }
        return books;
    }

    public void setBooks(List<BookService> books) {
        this.books = books;
    }

    public List<String> getReviewIds() {
        if (reviewIds.isEmpty() && !reviews.isEmpty()) {
            reviewIds = new ArrayList<>();
            for (BookReviewService review : reviews) {
                reviewIds.add(String.valueOf(review.getId()));
            }
        }
        return reviewIds;
    }

    public void setReviewIds(List<String> reviewIds) {
        this.reviewIds = reviewIds;
    }

    // 获取书评列表
    public List<BookReviewService> getReviews() {
        if (!isLoaded) {
            loadBooksAndReviews();
        }
        return reviews;
    }

    public void setReviews(List<BookReviewService> reviews) {
        this.reviews = reviews;
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

    // 添加图书
    public void addBook(BookService book) {
        if (isLoaded) {
            if (!books.contains(book)) {
                books.add(book);
            }
        } else {
            if (!bookIds.contains(book.getId())) {
                bookIds.add(book.getId());
            }
        }
    }

    // 添加图书ID的方法
    public void addBookById(String bookId) {
        if (!isLoaded) {
            if (!bookIds.contains(bookId)) {
                bookIds.add(bookId);
            }
        } else {
            BookDao bookDao = new BookDao();
            BookService book = bookDao.find(bookId);
            if (book != null && !books.contains(book)) {
                books.add(book);
            }
        }
    }

    // 移除图书
    public boolean removeBook(BookService book) {
        if (books.remove(book)) {
            return true;
        }
        return false;
    }

    // 添加书评
    public void addReview(BookReviewService review) {
        if (!reviews.contains(review)) {
            reviews.add(review);
        }
    }
}