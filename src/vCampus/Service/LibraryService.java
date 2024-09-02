package vCampus.Service;

import vCampus.Dao.Books.BookDao;
import vCampus.Dao.Books.BorrowRecordDao;
import vCampus.Dao.Criteria.BookSearchCriteria;
import vCampus.Dao.Criteria.BookSortCriteria;
import vCampus.Entity.Books.Book;
import vCampus.Entity.Books.BookUser;
import vCampus.Entity.Books.BorrowRecord;
import vCampus.Service.Books.BookService;
import vCampus.Service.Books.BookShelfService;
import vCampus.Service.Books.BookUserService;
import vCampus.Service.Books.BorrowRecordService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LibraryService {
    private BookDao bookDao;
    private BorrowRecordDao borrowRecordDao;

    public LibraryService() {
        this.borrowRecordDao = new BorrowRecordDao();
        this.bookDao = new BookDao();
    }

    // 登录
    public BookUser login(String id) {
        BookUserService bookUser = new BookUserService(id);
        return new BookUser(bookUser);
    }

    // 借书方法
    public BorrowRecord borrowBook(BookUser bookUser, String bookId) {
        // 获取当前用户
        BookUserService currentUser = new BookUserService(bookUser);

        // 查找要借阅的书籍
        BookService book = new BookService(bookId);

        // 检查图书的副本数量
        if (book.getCopyCount() <= 0) {
            throw new IllegalStateException("图书 " + bookId + " 的副本数量不足，无法借阅");
        }

        // 创建新的借阅记录
        BorrowRecordService borrowRecord = new BorrowRecordService(
                LocalDate.now(), // 借阅日期为当前日期
                book,
                currentUser);

        // 将借阅记录添加到数据库
        if (borrowRecordDao.add(borrowRecord)) {
            // 更新图书的借阅数量和副本数量
            book.borrowBook();
            borrowRecord.setBook(book);
            return new BorrowRecord(borrowRecord);
        } else {
            return null;
        }
    }

    // 还书方法
    public BorrowRecord returnBook(BorrowRecord borrowRecord) {
        BorrowRecordService borrowRecordService = new BorrowRecordService(borrowRecord);
        // 获取当前用户
        BookUserService currentUser = borrowRecordService.getBookUser();
        if (currentUser == null) {
            throw new IllegalStateException("当前没有登录的用户");
        }

        // 查找要归还的书籍
        BookService book = borrowRecordService.getBook();
        if (book == null) {
            throw new IllegalArgumentException("找不到借阅记录中的书籍");
        }

        // 检查是否逾期
        if (borrowRecordService.isOverdue()) {
            // 调用付款函数（目前用打印语句占位）
            System.out.println("借阅记录逾期，请支付罚款！");
            // 这里可以调用实际的付款函数，并检查付款是否成功
            boolean paymentSuccess = false; // 假设付款失败
            if (!paymentSuccess) {
                throw new IllegalStateException("逾期未付款，无法归还书籍");
            }
        }

        // 更新图书的副本数量
        book.returnBook();
        borrowRecordService.setBook(book);

        // 更新借阅记录的状态
        borrowRecordService.setStatus(BorrowRecordService.BorrowStatus.RETURNED);

        return new BorrowRecord(borrowRecordService);
    }

    // 搜索并获取书籍的方法
    public SearchResult<Book> searchBooks(BookSearchCriteria searchCriteria, BookSortCriteria sortCriteria,
            int page, int pageSize) {
        int totalBooks = bookDao.getTotalBooks(searchCriteria);
        List<String> bookIds = bookDao.findBooksByPage(searchCriteria, sortCriteria, page, pageSize);
        List<Book> books = new ArrayList<>();

        for (String bookId : bookIds) {
            Book book = new Book(bookDao.find(bookId));
            if (book != null) {
                books.add(book);
            }
        }

        return new SearchResult<Book>(totalBooks, page, pageSize, books);
    }

    // 获取图书总数的方法
    public int getTotalBooks(BookSearchCriteria searchCriteria) {
        return bookDao.getTotalBooks(searchCriteria);
    }

    // 创建新的空书架的方法
    public BookUser createBookShelf(BookUser bookUser, String shelfName) {
        // 将 BookUser 对象转换为 BookUserService 对象
        BookUserService bookUserService = new BookUserService(bookUser);

        // 创建新的 BookShelfService 对象
        BookShelfService newShelf = new BookShelfService(shelfName, bookUserService.getId());

        // 将新的书架添加到 BookUserService 对象中
        bookUserService.addBookShelf(newShelf);

        // 将更新后的 BookUserService 对象转换回 BookUser 对象并返回
        return new BookUser(bookUserService);
    }
}