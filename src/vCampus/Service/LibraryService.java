package vCampus.Service;

import vCampus.Dao.Books.BookDao;
import vCampus.Dao.Books.BorrowRecordDao;
import vCampus.Service.Books.BookService;
import vCampus.Service.Books.BookUserService;
import vCampus.Service.Books.BorrowRecordService;
import vCampus.Dao.Books.BookUserDao;

import java.time.LocalDate;

public class LibraryService {
    private BookUserDao bookUserDao;
    private BookDao bookDao;
    private BorrowRecordDao borrowRecordDao;

    public LibraryService() {
        this.bookUserDao = new BookUserDao();
        this.bookDao = new BookDao();
        this.borrowRecordDao = new BorrowRecordDao();
    }

    // 首次登录返回true，否则返回false
    public Boolean login(String id) {
        BookUserService bookUser = bookUserDao.find(id);
        boolean firstLogin = false;
        if (bookUser == null) {
            // 创建一个新的 BookUser 对象
            bookUser = new BookUserService(id);
            BookUserService.setCurrentUser(bookUser);
            firstLogin = true;
        }
        BookUserService.setCurrentUser(bookUser);
        return firstLogin;
    }

    // 借书方法
    public boolean borrowBook(String userId, String bookId) {
        // 获取当前用户
        BookUserService currentUser = new BookUserService(userId);

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
            book.setBorrowCount(book.getBorrowCount() + 1);
            book.setCopyCount(book.getCopyCount() - 1);
            bookDao.update(book);
            return true;
        } else {
            return false;
        }
    }

    // 还书方法
    public boolean returnBook(BorrowRecordService borrowRecord) {
        // 获取当前用户
        BookUserService currentUser = BookUserService.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("当前没有登录的用户");
        }

        // 查找要归还的书籍
        BookService book = borrowRecord.getBook();
        if (book == null) {
            throw new IllegalArgumentException("找不到借阅记录中的书籍");
        }

        // 检查是否逾期
        if (borrowRecord.isOverdue()) {
            // 调用付款函数（目前用打印语句占位）
            System.out.println("借阅记录逾期，请支付罚款！");
            // 这里可以调用实际的付款函数，并检查付款是否成功
            boolean paymentSuccess = false; // 假设付款失败
            if (!paymentSuccess) {
                return false;
            }
        }

        // 更新图书的副本数量
        book.setCopyCount(book.getCopyCount() + 1);
        bookDao.update(book);

        // 更新借阅记录的状态
        borrowRecord.setStatus(BorrowRecordService.BorrowStatus.RETURNED);
        borrowRecordDao.update(borrowRecord);

        return true;
    }
}