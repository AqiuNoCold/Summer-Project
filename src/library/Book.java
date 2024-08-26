package library;

public class Book {
    private String name;      // 书名
    private String author;    // 作者
    private int price;        // 价格
    private String type;      // 类型
    private int availableCopies; // 剩余可用书的数量

    public Book(String name, String author, int price, String type, int availableCopies) {
        this.name = name;
        this.author = author;
        this.price = price;
        this.type = type;
        this.availableCopies = availableCopies;
    }

    // Getter and setter methods for the attributes (省略了详细代码)
    // ...

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", type='" + type + '\'' +
                ", availableCopies=" + availableCopies +
                '}';
    }
}