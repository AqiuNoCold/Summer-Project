package vCampus.Service;

import vCampus.Dao.Books.BookShelfDao;
import vCampus.Entity.Books.Book;
import vCampus.Entity.Books.BookReview;
import vCampus.Entity.Books.BookShelf;

import java.util.List;
import java.time.LocalDateTime;

public class BookShelfService {
    private BookShelfDao bookShelfDao;

    public BookShelfService() {
        this.bookShelfDao = new BookShelfDao();
    }

    // 创建书架
    public BookShelf createBookShelf(String name, Long userId, List<Book> books, List<BookReview> reviews) {
        BookShelf bookShelf = new BookShelf(null, name, userId, LocalDateTime.now(), LocalDateTime.now(), null,
                null);
        bookShelf.setBooks(books);
        bookShelf.setReviews(reviews);
        bookShelfDao.save(bookShelf);
        return bookShelf;
    }

    // 更新书架
    public void updateBookShelf(BookShelf bookShelf) {
        bookShelf.setUpdateTime(LocalDateTime.now());
        bookShelfDao.update(bookShelf);
    }

    // 删除书架
    public void deleteBookShelf(Long id) {
        bookShelfDao.delete(String.valueOf(id));
    }

    // 根据ID查找书架
    public BookShelf findBookShelfById(Long id) {
        return bookShelfDao.find(String.valueOf(id));
    }

    // 分页获取书架
    public List<BookShelf> getBookShelvesByPage(int page, int pageSize) {
        return bookShelfDao.findByPage(page, pageSize);
    }
}