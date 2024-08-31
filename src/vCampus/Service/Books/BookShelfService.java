package vCampus.Service.Books;

import vCampus.Dao.Books.BookReviewDao;
import vCampus.Dao.Books.BookShelfDao;
import vCampus.Entity.Books.Book;
import vCampus.Entity.Books.BookReview;
import vCampus.Entity.Books.BookShelf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

public class BookShelfService {
    private BookShelfDao bookShelfDao;

    public BookShelfService() {
        this.bookShelfDao = new BookShelfDao();
    }

    // 加载书架的所有内容
    public void loadAllContent(BookShelf bookShelf) {
        // 调用getBooks方法加载书架上的所有书籍
        List<Book> books = bookShelf.getBooks();

        // 将书籍列表转换为Map，键为书籍的id，值为书籍对象
        Map<String, Book> bookMap = new HashMap<>();
        for (Book book : books) {
            bookMap.put(book.getId(), book);
        }

        // 调用getReviews方法加载书架上的所有书评
        List<BookReview> reviews = bookShelf.getReviews();

        // 遍历每个书评，获取其bookId，并通过Map查找对应的书籍
        for (BookReview review : reviews) {
            String reviewBookId = review.getBookId();
            Book book = bookMap.get(reviewBookId);
            if (book != null) {
                // 如果找到匹配的书籍，则初始化书评中的book属性
                review.setBook(book);
            }
        }
    }

    // 更新书架
    public void updateBookShelf(BookShelf bookShelf) {
        bookShelf.setUpdateTime(LocalDateTime.now());
        bookShelfDao.update(bookShelf);

        // 更新书架中对应的每一个书评
        BookReviewDao bookReviewDao = new BookReviewDao();
        for (BookReview review : bookShelf.getReviews()) {
            BookReview existingReview = bookReviewDao.find(String.valueOf(review.getId()));
            if (existingReview != null && !existingReview.equals(review)) {
                review.setUpdateTime(LocalDateTime.now());
                bookReviewDao.update(review);
            }
        }
    }
}