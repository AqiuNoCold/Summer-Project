package Pages.Utils;

import java.util.List;

import vCampus.Entity.Books.Book;

public class BookUtils {
    // 批量初始化方法
    public static void initializeBooks(List<Book> books) {
        for (Book book : books) {
            book.initialize();
        }
    }
}
