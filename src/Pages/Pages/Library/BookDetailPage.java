package Pages.Pages.Library;

import vCampus.Entity.Books.Book;
import Pages.Utils.IconUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.math.BigDecimal;
import java.util.Random;

public class BookDetailPage extends JPanel {
    private JLabel bookCoverLabel;
    private JLabel titleLabel;
    private JLabel isbnLabel;
    private JLabel authorLabel;
    private JLabel publisherLabel;
    private JLabel averageRatingLabel;
    private JLabel reviewCountLabel;
    private JLabel favoriteCountLabel;
    private JLabel borrowCountLabel;
    private JTextArea synopsisTextArea;
    private JButton favoriteButton;
    private JButton borrowButton;
    private JButton viewReviewsButton;
    private JButton addReviewButton;
    private String imagePath; // 保存图书封面路径
    private Book currentBook; // 当前显示的图书对象

    public BookDetailPage() {
        setLayout(new BorderLayout());

        // 上半部分
        JPanel topPanel = new JPanel(new BorderLayout());
        bookCoverLabel = new JLabel();
        bookCoverLabel.setHorizontalAlignment(JLabel.CENTER); // 居中对齐
        topPanel.add(bookCoverLabel, BorderLayout.WEST);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        titleLabel = new JLabel();
        isbnLabel = new JLabel();
        authorLabel = new JLabel();
        publisherLabel = new JLabel();
        averageRatingLabel = new JLabel();
        reviewCountLabel = new JLabel();
        favoriteCountLabel = new JLabel();
        borrowCountLabel = new JLabel();

        // 第一行：标题
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.add(titleLabel);
        infoPanel.add(titlePanel);

        // 第二行：ISBN
        JPanel isbnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        isbnPanel.add(isbnLabel);
        infoPanel.add(isbnPanel);

        // 第三行：作者
        JPanel authorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        authorPanel.add(authorLabel);
        infoPanel.add(authorPanel);

        // 第四行：出版社
        JPanel publisherPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        publisherPanel.add(publisherLabel);
        infoPanel.add(publisherPanel);

        // 第五行：评分和评论数
        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ratingPanel.add(new JLabel("平均评分:"));
        ratingPanel.add(averageRatingLabel);
        ratingPanel.add(new JLabel("书评数量:"));
        ratingPanel.add(reviewCountLabel);
        infoPanel.add(ratingPanel);

        // 第六行：收藏数和借阅数
        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        countPanel.add(new JLabel("收藏数量:"));
        countPanel.add(favoriteCountLabel);
        countPanel.add(new JLabel("借阅数量:"));
        countPanel.add(borrowCountLabel);
        infoPanel.add(countPanel);

        topPanel.add(infoPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // 下半部分
        synopsisTextArea = new JTextArea();
        synopsisTextArea.setLineWrap(true);
        synopsisTextArea.setWrapStyleWord(true);
        synopsisTextArea.setEditable(false);
        add(new JScrollPane(synopsisTextArea), BorderLayout.CENTER);

        // 底部按钮
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        favoriteButton = new JButton("收藏");
        borrowButton = new JButton("借阅");
        viewReviewsButton = new JButton("查看书评");
        addReviewButton = new JButton("添加书评");

        buttonPanel.add(favoriteButton);
        buttonPanel.add(borrowButton);
        buttonPanel.add(viewReviewsButton);
        buttonPanel.add(addReviewButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // 添加组件大小调整监听器
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeBookCover();
            }
        });

        // 收藏按钮点击事件
        favoriteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BookshelvesPage bookshelvesPage = new BookshelvesPage();
                bookshelvesPage.addBookToShelf(getCurrentBook());
            }
        });

        // 添加书评按钮点击事件
        addReviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addReview();
            }
        });
    }

    public void showBookDetails(Book book) {
        this.currentBook = book; // 设置当前显示的图书对象

        // 设置图书封面路径
        imagePath = book.getCachedImagePath();
        if (imagePath == null) {
            Random random = new Random();
            int randomIndex = random.nextInt(8) + 1; // 生成1到8之间的随机数
            imagePath = "src/imgs/default_book_cover/" + randomIndex + ".jpg";
        }
        ImageIcon bookIcon = new ImageIcon(imagePath);
        if (bookIcon.getIconWidth() == -1) {
            imagePath = "/imgs/default-book-cover.jpg";
            bookIcon = new ImageIcon(imagePath);
        }
        bookCoverLabel.setIcon(bookIcon);

        // 设置图书信息
        String title = book.getTitleLong();
        if (book.getEdition() != null && !book.getEdition().isEmpty()) {
            title += " (" + book.getEdition() + ")";
        }
        titleLabel.setText(title);
        isbnLabel.setText(book.getIsbn() + " / " + book.getIsbn13());
        authorLabel.setText(book.getAuthors());
        publisherLabel.setText(book.getPublisher());
        averageRatingLabel.setText(String.valueOf(book.getAverageRating()));
        reviewCountLabel.setText(String.valueOf(book.getReviewCount()));
        favoriteCountLabel.setText(String.valueOf(book.getFavoriteCount()));
        borrowCountLabel.setText(String.valueOf(book.getBorrowCount()));
        synopsisTextArea.setText(book.getSynopsis());

        // 显示图书详情页
        JFrame frame = new JFrame("图书详情");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 800);
        frame.add(this);
        frame.setVisible(true);

        // 调整图书封面大小
        resizeBookCover();
    }

    private void resizeBookCover() {
        int width = getWidth() / 3; // 将宽度限制为页面宽度的1/3
        int height = (int) (width * 1.5); // 假设图书封面的宽高比为2:3

        if (imagePath != null) {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            bookCoverLabel.setIcon(new ImageIcon(scaledImg));
        }
    }

    private Book getCurrentBook() {
        return currentBook;
    }

    private void addReview() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "添加书评", true);
        dialog.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("评分："));
        JTextField ratingField = new JTextField();
        inputPanel.add(ratingField);
        inputPanel.add(new JLabel("书评："));
        JTextArea reviewArea = new JTextArea();
        inputPanel.add(new JScrollPane(reviewArea));
        dialog.add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        JButton saveButton = new JButton("完成");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BigDecimal newRating = new BigDecimal(ratingField.getText());
                    int newReviewCount = currentBook.getReviewCount() + 1;
                    BigDecimal newAverageRating = (currentBook.getAverageRating()
                            .multiply(BigDecimal.valueOf(currentBook.getReviewCount())).add(newRating))
                            .divide(BigDecimal.valueOf(newReviewCount), 2, BigDecimal.ROUND_HALF_UP);
                    currentBook.setAverageRating(newAverageRating);
                    currentBook.setReviewCount(newReviewCount);
                    // 这里可以添加保存书评的逻辑
                    dialog.dispose();
                    showBookDetails(currentBook);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}