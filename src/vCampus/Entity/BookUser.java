package vCampus.Entity;

import java.util.List;

public class BookUser {
    private UserInfo userInfo; // 用户信息
    private List<BorrowRecord> borrowRecords; // 借阅记录列表

    public BookUser(UserInfo userInfo) {
        this.userInfo = userInfo;
        this.borrowRecords = new ArrayList<>();
    }

    // 添加借阅记录
    public void addBorrowRecord(BorrowRecord borrowRecord) {
        borrowRecords.add(borrowRecord);
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