package vCampus.Service.Books;

import vCampus.Dao.Books.BookDao;
import vCampus.Dao.Books.BorrowRecordDao;
import vCampus.Dao.Books.BookUserDao;
import vCampus.Entity.Books.Book;
import vCampus.Entity.Books.BookUser;
import vCampus.Entity.Books.BorrowRecord;
import vCampus.Entity.Books.BookShelf;
import java.time.LocalDate;

public class BookUserService {
    private BookUserDao bookUserDao;
    private BookDao bookDao;
    private BorrowRecordDao borrowRecordDao;

    public BookUserService() {
        this.bookUserDao = new BookUserDao();
        this.bookDao = new BookDao();
        this.borrowRecordDao = new BorrowRecordDao();
    }

    // 首次登录返回false，否则返回true
    public Boolean login(String id) {
        BookUser bookUser = bookUserDao.find(id);
        if (bookUser == null) {
            // 创建一个新的 BookUser 对象
            bookUser = new BookUser(id);
            bookUser.setDefaultBookShelf(new BookShelf(id));
            // 将新用户添加到数据库
            bookUserDao.add(bookUser);
            BookUser.setCurrentUser(bookUser);
            return false;
        }
        return true;
    }

    // 借书方法
    public boolean borrowBook(String bookId) {
        // 获取当前用户
        BookUser currentUser = BookUser.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("当前没有登录的用户");
        }

        // 查找要借阅的书籍
        Book book = bookDao.find(bookId);
        if (book == null) {
            throw new IllegalArgumentException("找不到ID为 " + bookId + " 的书籍");
        }

        // 检查图书的副本数量
        if (book.getCopyCount() <= 0) {
            throw new IllegalStateException("图书 " + bookId + " 的副本数量不足，无法借阅");
        }

        // 创建新的借阅记录
        BorrowRecord borrowRecord = new BorrowRecord(
                LocalDate.now(), // 借阅日期为当前日期
                book,
                currentUser);

        // 将借阅记录添加到数据库
        boolean isAdded = borrowRecordDao.add(borrowRecord);
        if (isAdded) {
            // 更新图书的借阅数量和副本数量
            book.setBorrowCount(book.getBorrowCount() + 1);
            book.setCopyCount(book.getCopyCount() - 1);
            bookDao.update(book);

            // 将借阅记录添加到当前用户的借阅记录列表中
            currentUser.addBorrowRecord(borrowRecord);
            return true;
        } else {
            return false;
        }
    }

    // 还书方法
    public boolean returnBook(BorrowRecord borrowRecord) {
        // 获取当前用户
        BookUser currentUser = BookUser.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("当前没有登录的用户");
        }

        // 查找要归还的书籍
        Book book = borrowRecord.getBook();
        if (book == null) {
            throw new IllegalArgumentException("找不到借阅记录中的书籍");
        }

        // 检查是否逾期
        BorrowRecordService borrowRecordService = new BorrowRecordService();
        if (borrowRecordService.isOverdue(borrowRecord)) {
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
        borrowRecord.setStatus(BorrowRecord.BorrowStatus.RETURNED);
        borrowRecordDao.update(borrowRecord);

        return true;
    }
}