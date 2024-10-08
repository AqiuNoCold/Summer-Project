package vCampus.Service;

import vCampus.Dao.Books.BookDao;
import vCampus.Dao.Books.BorrowRecordDao;
import vCampus.Dao.Criteria.BookSearchCriteria;
import vCampus.Dao.Criteria.BookSortCriteria;
import vCampus.Dao.Criteria.BorrowRecordSearchCriteria;
import vCampus.Dao.Criteria.BorrowRecordSortCriteria;
import vCampus.Entity.Books.Book;
import vCampus.Entity.Books.BookUser;
import vCampus.Entity.Books.BookShelf;
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

    // 搜索并获取借阅记录的方法
    public SearchResult<BorrowRecord> searchBorrowRecords(BorrowRecordSearchCriteria searchCriteria,
            BorrowRecordSortCriteria sortCriteria,
            int page, int pageSize) {
        int totalRecords = borrowRecordDao.getTotalRecords(searchCriteria);
        List<BorrowRecordService> recordServices = borrowRecordDao.findAllByPage(searchCriteria, sortCriteria, page,
                pageSize);
        List<BorrowRecord> records = new ArrayList<>();

        for (BorrowRecordService recordService : recordServices) {
            records.add(new BorrowRecord(recordService));
        }

        return new SearchResult<BorrowRecord>(totalRecords, page, pageSize, records);
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
        bookUserService.setCurrentBookShelf(newShelf);

        // 将更新后的 BookUserService 对象转换回 BookUser 对象并返回
        return new BookUser(bookUserService);
    }

    // 设置当前书架的方法
    public BookUser setCurrentBookShelf(BookUser bookUser, BookShelf bookShelf) {
        // 将 BookUser 对象转换为 BookUserService 对象
        BookUserService bookUserService = new BookUserService(bookUser);

        // 将 BookShelf 对象转换为 BookShelfService 对象
        BookShelfService bookShelfService = new BookShelfService(bookShelf);

        // 设置当前书架
        bookUserService.setCurrentBookShelf(bookShelfService);

        // 将更新后的 BookUserService 对象转换回 BookUser 对象并返回
        return new BookUser(bookUserService);
    }

    // 通过图书ID添加图书到书架的方法
    public BookUser addBookToShelfById(BookUser bookUser, Long shelfId, String bookId) {
        BookUserService bookUserService = new BookUserService(bookUser);
        BookShelfService bookShelfService = new BookShelfService(shelfId);

        // 检查书架是否属于该用户
        if (!bookShelfService.getUserId().equals(bookUserService.getId())) {
            throw new IllegalArgumentException("书架不属于该用户");
        }

        // 添加图书到书架
        bookShelfService.addBookById(bookId);

        // 更新 BookUser 中对应书架的信息
        bookUserService.updateBookShelf(bookShelfService);

        // 返回更新后的 BookUser 对象
        return new BookUser(bookUserService);
    }

    // 通过图书对象添加图书到书架的方法
    public BookUser addBookToShelfByObject(BookUser bookUser, Long shelfId, Book book) {
        BookUserService bookUserService = new BookUserService(bookUser);
        BookShelfService bookShelfService = new BookShelfService(shelfId);
        BookService bookService = new BookService(book);

        // 检查书架是否属于该用户
        if (!bookShelfService.getUserId().equals(bookUserService.getId())) {
            throw new IllegalArgumentException("书架不属于该用户");
        }

        // 添加图书到书架
        bookShelfService.addBookByObject(bookService);

        // 更新 BookUser 中对应书架的信息
        bookUserService.updateBookShelf(bookShelfService);

        // 返回更新后的 BookUser 对象
        return new BookUser(bookUserService);
    }

    // 通过图书ID从书架中删除图书的方法
    public BookUser removeBookFromShelfById(BookUser bookUser, Long shelfId, String bookId) {
        BookUserService bookUserService = new BookUserService(bookUser);
        BookShelfService bookShelfService = new BookShelfService(shelfId);

        // 检查书架是否属于该用户
        if (!bookShelfService.getUserId().equals(bookUserService.getId())) {
            throw new IllegalArgumentException("书架不属于该用户");
        }

        // 从书架中删除图书
        bookShelfService.removeBookById(bookId);

        // 更新 BookUser 中对应书架的信息
        bookUserService.updateBookShelf(bookShelfService);

        // 返回更新后的 BookUser 对象
        return new BookUser(bookUserService);
    }

    // 随机获取指定数量的书籍
    public List<Book> getRandomBooks(int count) {
        List<String> bookIds = bookDao.findRandomBooks(count);
        List<Book> books = new ArrayList<>();

        for (String bookId : bookIds) {
            Book book = new Book(bookDao.find(bookId));
            if (book != null) {
                books.add(book);
            }
        }

        return books;
    }
}