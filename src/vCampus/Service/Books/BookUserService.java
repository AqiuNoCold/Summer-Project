package vCampus.Service.Books;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            // 如果用户不存在，则创建新用户
            this.defaultBookShelf = new BookShelfService(id);
            this.bookShelves = new ArrayList<>();
            this.bookShelves.add(this.defaultBookShelf); // 将默认书架添加到书架列表中
            // 将新用户添加到数据库
            bookUser = new BookUserService(id, this.defaultBookShelf,
                    this.bookShelves);
            bookUserDao.add(bookUser);
            this.firstLogin = true;
            System.out.println("首次登录，注册图书馆用户");
        }
    }

    // 构造函数，接受id，borrowRecordIds，defaultBookShelf，和bookShelves
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
        if (!bookShelves.contains(defaultBookShelf)) {
            throw new IllegalArgumentException("书架不存在于用户的书架列表中");
        }
        this.defaultBookShelf = defaultBookShelf;
        new BookUserDao().update(this);
    }

    // 获取当前书架
    public BookShelfService getCurrentBookShelf() {
        if (currentBookShelf == null) {
            currentBookShelf = defaultBookShelf;
        }
        return currentBookShelf;
    }

    // 设置当前书架
    public void setCurrentBookShelf(BookShelfService currentBookShelf) {
        if (!bookShelves.contains(currentBookShelf)) {
            throw new IllegalArgumentException("书架不存在于用户的书架列表中");
        }
        if (!currentBookShelf.getIsLoaded()) {
            currentBookShelf.loadBooksAndReviews();
            // 更新书架列表中的书架
            for (int i = 0; i < bookShelves.size(); i++) {
                if (bookShelves.get(i).getId().equals(currentBookShelf.getId())) {
                    bookShelves.set(i, currentBookShelf);
                    break;
                }
            }
        }
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
            new BookUserDao().update(this);
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

    // 更新书架信息
    public void updateBookShelf(BookShelfService updatedShelf) {
        // 检查是否是默认书架或当前书架
        if (defaultBookShelf != null && defaultBookShelf.getId().equals(updatedShelf.getId())) {
            defaultBookShelf = updatedShelf;
        }
        if (currentBookShelf != null && currentBookShelf.getId().equals(updatedShelf.getId())) {
            currentBookShelf = updatedShelf;
        }

        // 在书架列表中找到对应的书架并更新
        for (int i = 0; i < bookShelves.size(); i++) {
            if (bookShelves.get(i).getId().equals(updatedShelf.getId())) {
                bookShelves.set(i, updatedShelf);
                break;
            }
        }
    }
}