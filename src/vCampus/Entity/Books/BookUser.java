package vCampus.Entity.Books;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import vCampus.Entity.User;
import vCampus.Service.Books.BookUserService;
import vCampus.Service.Books.BookShelfService;

public class BookUser extends User implements Serializable {
    private BookShelf defaultBookShelf; // 默认书架
    private BookShelf currentBookShelf; // 当前书架
    private List<BookShelf> bookShelves; // 所有书架
    private Boolean firstLogin = false; // 首次登录标志
    private static BookUser currentUser; // 当前用户

    // 拷贝构造函数
    public BookUser(BookUser other) {
        super(other);
        this.defaultBookShelf = other.defaultBookShelf;
        this.currentBookShelf = other.currentBookShelf;
        this.bookShelves = new ArrayList<>(other.bookShelves);
        this.firstLogin = other.firstLogin;
    }

    // 通过BookUserService对象的get方法建立的构造函数
    public BookUser(BookUserService service) {
        super(service);
        this.defaultBookShelf = new BookShelf(service.getDefaultBookShelf());
        this.currentBookShelf = new BookShelf(service.getCurrentBookShelf());
        this.bookShelves = new ArrayList<>();
        for (BookShelfService shelfService : service.getBookShelves()) {
            this.bookShelves.add(new BookShelf(shelfService));
        }
        this.firstLogin = service.isFirstLogin();
    }

    // Getter和Setter方法
    public BookShelf getDefaultBookShelf() {
        return defaultBookShelf;
    }

    public void setDefaultBookShelf(BookShelf defaultBookShelf) {
        this.defaultBookShelf = defaultBookShelf;
    }

    public BookShelf getCurrentBookShelf() {
        return currentBookShelf;
    }

    public void setCurrentBookShelf(BookShelf currentBookShelf) {
        this.currentBookShelf = currentBookShelf;
    }

    public List<BookShelf> getBookShelves() {
        return bookShelves;
    }

    public void setBookShelves(List<BookShelf> bookShelves) {
        this.bookShelves = bookShelves;
    }

    // 获取首次登录标志
    public Boolean isFirstLogin() {
        return firstLogin;
    }

    // 设置当前用户
    public static void setCurrentUser(BookUser user) {
        currentUser = user;
    }

    // 获取当前用户
    public static BookUser getCurrentUser() {
        return currentUser;
    }
}