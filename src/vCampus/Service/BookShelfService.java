package vCampus.Service;

import vCampus.Dao.Books.BookShelfDAO;
import vCampus.Entity.Books.Book;
import vCampus.Entity.Books.BookReview;
import vCampus.Entity.Books.BookShelf;

import java.util.List;

public class BookShelfService {
    private BookShelfDAO bookShelfDAO;

    public BookShelfService() {
        this.bookShelfDAO = new BookShelfDAO();
    }

    // 创建书架
    public BookShelf createBookShelf(String name, Long userId, List<Book> books, List<BookReview> reviews) {
        BookShelf bookShelf = new BookShelf(null, name, userId, LocalDateTime.now(), LocalDateTime.now());
        bookShelf.setBooks(books);
        bookShelf.setReviews(reviews);
        bookShelfDAO.save(bookShelf);
        return bookShelf;
    }

    // 更新书架
    public void updateBookShelf(BookShelf bookShelf) {
        bookShelf.setUpdateTime(LocalDateTime.now());
        bookShelfDAO.update(bookShelf);
    }

    // 删除书架
    public void deleteBookShelf(Long id) {
        bookShelfDAO.delete(id);
    }

    // 根据ID查找书架
    public BookShelf findBookShelfById(Long id) {
        return bookShelfDAO.findById(id);
    }

    // 获取所有书架
    public List<BookShelf> findAllBookShelves() {
        return bookShelfDAO.findAll();
    }

    // 搜索书架
    public List<BookShelf> searchBookShelves(String keyword) {
        return bookShelfDAO.search(keyword);
    }

    // 排序书架
    public List<BookShelf> sortBookShelves(String sortBy) {
        return bookShelfDAO.sort(sortBy);
    }

    // 分页获取书架
    public List<BookShelf> getBookShelvesByPage(int page, int pageSize) {
        return bookShelfDAO.findByPage(page, pageSize);
    }
}