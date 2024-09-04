package vCampus.Test;

import vCampus.Dao.Books.BookUserDao;
import vCampus.Service.Books.BookUserService;
import vCampus.Service.Books.BookShelfService;

import java.util.Collections;

public class BookUserDaoTest {
    public static void main(String[] args) {
        // 创建BookUserDao对象
        BookUserDao bookUserDao = new BookUserDao();

        // 创建BookShelfService对象
        BookShelfService defaultBookShelf = new BookShelfService(1L);

        // 创建BookUserService对象
        BookUserService bookUser = new BookUserService("213221715", defaultBookShelf,
                Collections.singletonList(defaultBookShelf));

        // 测试添加操作
        boolean isAdded = bookUserDao.add(bookUser);
        System.out.println("添加操作成功: " + isAdded);

        // 测试查找操作
        BookUserService foundBookUser = bookUserDao.find("213221715");
        System.out.println("查找操作结果: " + foundBookUser);

        // 验证查找结果
        if (foundBookUser != null) {
            System.out.println("ID: " + foundBookUser.getId());
            System.out.println("默认书架ID: " + foundBookUser.getDefaultBookShelf().getId());
            System.out.println("书架列表: " + foundBookUser.getShelfIds());
        } else {
            System.out.println("未找到ID为213221715的用户");
        }
    }
}