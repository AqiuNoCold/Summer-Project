package vCampus.Entity.Books;

import java.util.ArrayList;
import java.util.List;

import vCampus.Entity.UserInfo;

public class BookUser {
    private UserInfo userInfo; // 用户信息
    private List<BorrowRecord> borrowRecords; // 借阅记录列表
    private BookShelf defaultBookShelf; // 默认书架
    private List<BookShelf> bookShelves; // 所有书架

    public BookUser(UserInfo userInfo) {
        this.userInfo = userInfo;
        this.borrowRecords = new ArrayList<>();
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

    // 获取用户的所有借阅记录
    public List<BorrowRecord> getBorrowRecords() {
        return borrowRecords;
    }

    // 获取用户信息
    public UserInfo getUserInfo() {
        return userInfo;
    }

    // 其他方法，例如：
    // - 判断用户是否逾期
    // - 获取用户借阅的图书总数
    // - ...
}