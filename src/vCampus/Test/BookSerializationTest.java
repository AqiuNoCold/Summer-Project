package vCampus.Test;

import java.io.*;
import java.math.BigDecimal;

import vCampus.Entity.Books.Book;

public class BookSerializationTest {
    public static void main(String[] args) {
        // 创建一个 Book 对象
        Book book = new Book("1234567890", new BigDecimal("29.99"), "http://example.com/image.jpg", 300, "Example Book",
                "1234567890123", "John Doe", "Paperback", "1st", "Related Book", "English", "Fiction",
                "This is a synopsis.",
                "Example Publisher", "8.5 x 11 inches", "Example Book: A Comprehensive Guide", "2023-01-01", 10, 5,
                new BigDecimal("4.5"), 100, 50, true, false);

        // 序列化 Book 对象
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("book.ser"))) {
            oos.writeObject(book);
            System.out.println("Book 对象已序列化");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 反序列化 Book 对象
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("book.ser"))) {
            Book deserializedBook = (Book) ois.readObject();
            System.out.println("Book 对象已反序列化");
            System.out.println(deserializedBook);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}