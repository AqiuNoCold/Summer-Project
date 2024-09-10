package Pages.Utils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import vCampus.Entity.Books.Book;
import Pages.MainApp;

public class ClientUtils {
    // 获取随机书籍并初始化
    public static List<Book> getRandomBooksAndInitialize(int count) {
        List<Book> books = null;
        try {
            ObjectOutputStream out = MainApp.getOut();
            ObjectInputStream in = MainApp.getIn();

            // 发送请求
            out.writeObject("4"); // 4 对应 LibraryPage
            out.writeObject("getRandomBooks");
            out.writeObject(count);
            out.flush();

            // 接收响应
            books = (List<Book>) in.readObject();

            // 初始化书籍
            BookUtils.initializeBooks(books);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    public static void main(String[] args) {
        // 初始化Socket
        MainApp.initializeSocket();

        // 获取8本随机书籍并初始化
        List<Book> books = getRandomBooksAndInitialize(8);
        for (Book book : books) {
            System.out.println(book);
        }

        // 关闭资源
        MainApp.close_source();
    }
}