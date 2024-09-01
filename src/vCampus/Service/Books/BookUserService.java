package vCampus.Service.Books;

import java.util.ArrayList;
import java.util.List;

import vCampus.Entity.User;
import vCampus.Entity.Books.BookShelf;
import vCampus.Entity.Books.BookUser;
import vCampus.Dao.Books.BookUserDao;
import vCampus.Dao.UserDao;

public class BookUserService extends User {
    private BookShelfService defaultBookShelf; // 默认书架
    private BookShelfService currentBookShelf; // 当前书架
    private List<BookShelfService> bookShelves; // 所有书架
    private Boolean firstLogin = false; // 首次登录标志

    // 构造函数，只接受id
    public BookUserService(String id) {
        super(new UserDao().find(id));
        BookUserDao bookUserDao = new BookUserDao();
        BookUserService bookUser = bookUserDao.find(id);
        if (bookUser == null) {
            this.defaultBookShelf = new BookShelfService(id);
            this.bookShelves = new ArrayList<>();
            // 将新用户添加到数据库
            bookUserDao.add(bookUser);
            this.firstLogin = true;
        }
    }

    // 构造函数，接受id，borrowRecordIds，defaultBookShelf，和shelfIds
    public BookUserService(String id, BookShelfService defaultBookShelf, List<BookShelfService> bookShelves) {
        super(new UserDao().find(id));
        this.bookShelves = new ArrayList<>();
        this.defaultBookShelf = defaultBookShelf;
        this.bookShelves = bookShelves;
    }

    // 根据 BookUser 实体类创建 BookUserService 服务类的构造方法
    public BookUserService(BookUser bookUser) {
        super(bookUser);
        this.defaultBookShelf = new BookShelfService(bookUser.getDefaultBookShelf());
        this.currentBookShelf = new BookShelfService(bookUser.getCurrentBookShelf());
        this.bookShelves = new ArrayList<>();
        for (BookShelf shelf : bookUser.getBookShelves()) {
            this.bookShelves.add(new BookShelfService(shelf));
        }
        this.firstLogin = false; // 默认设置为 false
    }

    // 获取默认书架
    public BookShelfService getDefaultBookShelf() {
        return defaultBookShelf;
    }

    // 设置默认书架
    public void setDefaultBookShelf(BookShelfService defaultBookShelf) {
        this.defaultBookShelf = defaultBookShelf;
    }

    // 获取当前书架
    public BookShelfService getCurrentBookShelf() {
        return currentBookShelf;
    }

    // 设置当前书架
    public void setCurrentBookShelf(BookShelfService currentBookShelf) {
        this.currentBookShelf = currentBookShelf;
    }

    // 设置所有书架
    public void setBookShelves(List<BookShelfService> bookShelves) {
        this.bookShelves = bookShelves;
    }

    // 获取所有书架
    public List<BookShelfService> getBookShelves() {
        return bookShelves;
    }

    // 添加书架
    public void addBookShelf(BookShelfService bookShelf) {
        if (!bookShelves.contains(bookShelf)) {
            bookShelves.add(bookShelf);
        }
    }

    // 移除书架
    public boolean removeBookShelf(BookShelfService bookShelf) {
        return bookShelves.remove(bookShelf);
    }

    // 获取书架ID列表
    public List<String> getShelfIds() {
        List<String> shelfIds = new ArrayList<>();
        for (BookShelfService shelf : bookShelves) {
            shelfIds.add(String.valueOf(shelf.getId()));
        }
        return shelfIds;
    }

    // 获取首次登录标志
    public Boolean isFirstLogin() {
        return firstLogin;
    }
}