package vCampus.Entity.Books;

import java.util.ArrayList;
import java.util.List;

import vCampus.Entity.User;
import vCampus.Dao.Books.BorrowRecordDao;
import vCampus.Dao.Books.BookShelfDao;

public class BookUser extends User {
    private List<BorrowRecord> borrowRecords; // 借阅记录列表
    private BookShelf defaultBookShelf; // 默认书架
    private List<BookShelf> bookShelves; // 所有书架
    private List<String> borrowRecordIds; // 借阅记录ID列表
    private List<String> shelfIds; // 书架ID列表

    // 构造函数，只接受id
    public BookUser(String id) {
        super(id);
        this.borrowRecords = new ArrayList<>();
        this.bookShelves = new ArrayList<>();
        this.borrowRecordIds = new ArrayList<>();
        this.shelfIds = new ArrayList<>();
    }

    // 构造函数，接受id，borrowRecordIds，defaultBookShelf，和shelfIds
    public BookUser(String id, List<String> borrowRecordIds, BookShelf defaultBookShelf, List<String> shelfIds) {
        super(id);
        this.borrowRecords = new ArrayList<>();
        this.bookShelves = new ArrayList<>();
        this.borrowRecordIds = borrowRecordIds;
        this.defaultBookShelf = defaultBookShelf;
        this.shelfIds = shelfIds;
    }

    // 添加借阅记录，返回布尔值表示是否成功
    public boolean addBorrowRecord(BorrowRecord borrowRecord) {
        if (this.getLost()) {
            // 用户账户被冻结，无法添加借阅记录
            return false;
        } else {
            borrowRecords.add(borrowRecord);
            return true;
        }
    }

    // 设置借阅记录
    public void setBorrowRecords(List<BorrowRecord> borrowRecords) {
        this.borrowRecords = borrowRecords;
    }

    // 获取用户的所有借阅记录
    public List<BorrowRecord> getBorrowRecords() {
        if (borrowRecords.isEmpty() && !borrowRecordIds.isEmpty()) {
            BorrowRecordDao borrowRecordDao = new BorrowRecordDao();
            for (String id : borrowRecordIds) {
                borrowRecords.add(borrowRecordDao.find(id));
            }
        }
        return borrowRecords;
    }

    // 获取默认书架
    public BookShelf getDefaultBookShelf() {
        return defaultBookShelf;
    }

    // 设置默认书架
    public void setDefaultBookShelf(BookShelf defaultBookShelf) {
        this.defaultBookShelf = defaultBookShelf;
    }

    // 设置所有书架
    public void setBookShelves(List<BookShelf> bookShelves) {
        this.bookShelves = bookShelves;
    }

    // 获取所有书架
    public List<BookShelf> getBookShelves() {
        if (bookShelves.isEmpty() && !shelfIds.isEmpty()) {
            BookShelfDao bookShelfDao = new BookShelfDao();
            for (String id : shelfIds) {
                bookShelves.add(bookShelfDao.find(id));
            }
        }
        return bookShelves;
    }

    // 添加书架
    public void addBookShelf(BookShelf bookShelf) {
        if (!bookShelves.contains(bookShelf)) {
            bookShelves.add(bookShelf);
        }
    }

    // 移除书架
    public boolean removeBookShelf(BookShelf bookShelf) {
        return bookShelves.remove(bookShelf);
    }

    // 获取用户借阅的图书总数
    public int getTotalBorrowedBooks() {
        return borrowRecords.size();
    }

    // 判断用户是否逾期
    public boolean isOverdue() {
        for (BorrowRecord record : borrowRecords) {
            if (record.isOverdue()) {
                return true;
            }
        }
        return false;
    }

    // 获取借阅记录ID列表
    public List<String> getBorrowRecordIds() {
        return borrowRecordIds;
    }

    // 设置书架ID列表
    public void setShelfIds(List<String> shelfIds) {
        this.shelfIds = shelfIds;
    }

    // 获取书架ID列表
    public List<String> getShelfIds() {
        return shelfIds;
    }
}