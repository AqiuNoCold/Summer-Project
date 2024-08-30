package vCampus.Entity.Books;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import vCampus.Entity.UserInfo;

public class BookUser {
    private UserInfo userInfo; // 用户信息
    private List<BorrowRecord> borrowRecords; // 借阅记录列表
    private BookShelf defaultBookShelf; // 默认书架
    private List<BookShelf> bookShelves; // 所有书架

    public BookUser(UserInfo userInfo) {
        this.userInfo = userInfo;
        this.borrowRecords = new ArrayList<>();
        this.bookShelves = new ArrayList<>();
    }

    // 新增的构造函数，只接受id
    public BookUser(String id) {
        this.userInfo = new UserInfo(id);
        this.borrowRecords = new ArrayList<>();
        this.bookShelves = new ArrayList<>();
    }

    // 添加借阅记录，返回布尔值表示是否成功
    public boolean addBorrowRecord(BorrowRecord borrowRecord) {
        if (userInfo.isLost()) {
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
        return borrowRecords;
    }

    // 获取用户信息
    public UserInfo getUserInfo() {
        return userInfo;
    }

    public String getCard() {
        return userInfo.getCard();
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

    // 设置所有书架的ID，从逗号分隔的字符串解析
    public void setAllBookShelvesFromString(String shelvesString) {
        String[] shelfIds = shelvesString.split(",");
        this.bookShelves = new ArrayList<>();
        for (String id : shelfIds) {
            this.bookShelves.add(new BookShelf(Long.parseLong(id)));
        }
    }

    // 获取所有书架
    public List<BookShelf> getBookShelves() {
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
        if (bookShelves.remove(bookShelf)) {
            return true;
        }
        return false;
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

    // 获取所有书架的ID并以逗号分隔的字符串形式返回
    public String getAllBookShelvesAsString() {
        return bookShelves.stream()
                .map(shelf -> String.valueOf(shelf.getId()))
                .collect(Collectors.joining(","));
    }
}