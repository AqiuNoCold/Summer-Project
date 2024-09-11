package Pages.Pages.Library;

import vCampus.Entity.Books.Book;
import vCampus.Entity.Books.BookShelf;
import vCampus.Entity.Books.BookUser;
import Pages.MainApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.Random;

public class BookshelvesPage extends JPanel {
    private static BookshelvesPage instance; // 单实例
    private JComboBox<String> bookshelfComboBox;
    private JButton deleteBookshelfButton;
    private JButton addBookshelfButton;
    private JPanel booksPanel;
    private BookUser currentUser;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private BookshelvesPage() {
        setLayout(new BorderLayout());

        currentUser = BookUser.getCurrentUser();
        out = MainApp.getOut();
        in = MainApp.getIn();

        // 顶部面板
        JPanel topPanel = new JPanel(new BorderLayout());

        // 左上角：书架下拉框
        bookshelfComboBox = new JComboBox<>(
                currentUser.getBookShelves().stream().map(BookShelf::getName).toArray(String[]::new));
        bookshelfComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedShelfName = (String) bookshelfComboBox.getSelectedItem();
                if (selectedShelfName != null) {
                    BookShelf selectedShelf = currentUser.getBookShelves().stream()
                            .filter(shelf -> shelf.getName().equals(selectedShelfName))
                            .findFirst()
                            .orElse(null);
                    if (selectedShelf != null) {
                        setCurrentBookshelf(selectedShelf);
                    }
                }
            }
        });
        topPanel.add(bookshelfComboBox, BorderLayout.WEST);

        // 右上角：删除书架和添加书架按钮
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        deleteBookshelfButton = new JButton("删除书架");
        addBookshelfButton = new JButton("添加书架");
        addBookshelfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBookshelf();
            }
        });
        buttonPanel.add(deleteBookshelfButton);
        buttonPanel.add(addBookshelfButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // 主体面板：书架内容
        booksPanel = new JPanel();
        booksPanel.setLayout(new BoxLayout(booksPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(booksPanel);
        add(scrollPane, BorderLayout.CENTER);

        // 显示当前书架内容
        displayBooks(currentUser.getCurrentBookShelf());
    }

    public static synchronized BookshelvesPage getInstance() {
        if (instance == null) {
            instance = new BookshelvesPage();
        }
        return instance;
    }

    private void setCurrentBookshelf(BookShelf bookshelf) {
        try {
            out.writeObject("setCurrentBookShelf");
            out.writeObject(bookshelf);
            out.flush();
            currentUser.setCurrentBookShelf(bookshelf);
            displayBooks(bookshelf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addBookshelf() {
        String name = JOptionPane.showInputDialog(this, "请输入书架名称：", "添加书架", JOptionPane.PLAIN_MESSAGE);
        if (name != null && !name.trim().isEmpty()) {
            boolean nameExists = currentUser.getBookShelves().stream().anyMatch(shelf -> shelf.getName().equals(name));
            if (nameExists) {
                JOptionPane.showMessageDialog(this, "书架名称已存在，请选择其他名称。", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 显示加载框
            JDialog loadingDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "加载中", true);
            JLabel loadingLabel = new JLabel("正在创建书架，请稍候...");
            loadingDialog.add(loadingLabel);
            loadingDialog.setSize(200, 100);
            loadingDialog.setLocationRelativeTo(this);
            loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            SwingUtilities.invokeLater(() -> loadingDialog.setVisible(true));

            new Thread(() -> {
                try {
                    out.writeObject("createBookShelf");
                    out.writeObject(name);
                    out.flush();
                    BookShelf newShelf = (BookShelf) in.readObject();
                    currentUser.getBookShelves().add(newShelf);
                    SwingUtilities.invokeLater(() -> {
                        bookshelfComboBox.addItem(newShelf.getName());
                        loadingDialog.dispose();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(() -> {
                        loadingDialog.dispose();
                        JOptionPane.showMessageDialog(this, "创建书架失败，请重试。", "错误", JOptionPane.ERROR_MESSAGE);
                    });
                }
            }).start();
        }
    }

    private void displayBooks(BookShelf bookshelf) {
        booksPanel.removeAll();
        for (Book book : bookshelf.getBooks()) {
            JPanel bookPanel = new JPanel(new BorderLayout());
            bookPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            bookPanel.setPreferredSize(new Dimension(0, 150));

            // 左侧：书籍封面和标题
            JPanel leftPanel = new JPanel(new BorderLayout());
            JLabel coverLabel = new JLabel();
            coverLabel.setPreferredSize(new Dimension(100, 150));
            if (book.getCachedImagePath() != null) {
                coverLabel.setIcon(new ImageIcon(book.getCachedImagePath()));
            } else {
                Random random = new Random();
                int randomIndex = random.nextInt(8) + 1;
                coverLabel.setIcon(new ImageIcon("src/imgs/default_book_cover/" + randomIndex + ".jpg"));
            }
            leftPanel.add(coverLabel, BorderLayout.CENTER);
            leftPanel.add(new JLabel(book.getTitle(), SwingConstants.CENTER), BorderLayout.SOUTH);
            bookPanel.add(leftPanel, BorderLayout.WEST);

            // 中间：书评或简介
            JPanel centerPanel = new JPanel(new BorderLayout());
            if (book.getReviewCount() > 0) {
                centerPanel.add(new JLabel("评分：" + book.getAverageRating()), BorderLayout.NORTH);
                centerPanel.add(new JLabel("书评：" + book.getSynopsis()), BorderLayout.CENTER);
            } else {
                centerPanel.add(new JLabel("简介：" + book.getSynopsis()), BorderLayout.CENTER);
            }
            bookPanel.add(centerPanel, BorderLayout.CENTER);

            // 右侧：编辑、复制、移动和删除按钮
            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton editButton = new JButton("编辑");
            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    editBook(book);
                }
            });
            JButton copyButton = new JButton("复制");
            copyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    copyBook(book);
                }
            });
            JButton moveButton = new JButton("移动");
            moveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveBook(book);
                }
            });
            JButton deleteButton = new JButton("删除");
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteBook(book);
                }
            });
            rightPanel.add(editButton);
            rightPanel.add(copyButton);
            rightPanel.add(moveButton);
            rightPanel.add(deleteButton);
            bookPanel.add(rightPanel, BorderLayout.EAST);

            booksPanel.add(bookPanel);
        }
        booksPanel.revalidate();
        booksPanel.repaint();
    }

    private void editBook(Book book) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "编辑书籍", true);
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
                    int newReviewCount = book.getReviewCount() + 1;
                    BigDecimal newAverageRating = (book.getAverageRating()
                            .multiply(BigDecimal.valueOf(book.getReviewCount())).add(newRating))
                            .divide(BigDecimal.valueOf(newReviewCount), 2, BigDecimal.ROUND_HALF_UP);
                    book.setAverageRating(newAverageRating);
                    book.setReviewCount(newReviewCount);
                    // 这里可以添加保存书评的逻辑
                    dialog.dispose();
                    displayBooks(currentUser.getCurrentBookShelf());
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

    private void copyBook(Book book) {
        BookShelf targetShelf = (BookShelf) JOptionPane.showInputDialog(this, "选择目标书架：", "复制书籍",
                JOptionPane.PLAIN_MESSAGE, null, currentUser.getBookShelves().toArray(), null);
        if (targetShelf != null) {
            try {
                out.writeObject("addBookToShelfByObject");
                out.writeObject(targetShelf);
                out.writeObject(book);
                out.flush();
                targetShelf.getBooks().add(book);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void moveBook(Book book) {
        BookShelf targetShelf = (BookShelf) JOptionPane.showInputDialog(this, "选择目标书架：", "移动书籍",
                JOptionPane.PLAIN_MESSAGE, null, currentUser.getBookShelves().toArray(), null);
        if (targetShelf != null) {
            try {
                out.writeObject("removeBookFromShelfById");
                out.writeObject(currentUser.getCurrentBookShelf());
                out.writeObject(book.getId());
                out.flush();
                currentUser.getCurrentBookShelf().getBooks().remove(book);

                out.writeObject("addBookToShelfByObject");
                out.writeObject(targetShelf);
                out.writeObject(book);
                out.flush();
                targetShelf.getBooks().add(book);

                displayBooks(currentUser.getCurrentBookShelf());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteBook(Book book) {
        try {
            out.writeObject("removeBookFromShelfById");
            out.writeObject(currentUser.getCurrentBookShelf());
            out.writeObject(book.getId());
            out.flush();
            currentUser.getCurrentBookShelf().getBooks().remove(book);
            displayBooks(currentUser.getCurrentBookShelf());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addBookToShelf(Book book) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "添加图书", true);
        dialog.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("选择书架："));
        JComboBox<String> shelfComboBox = new JComboBox<>(
                currentUser.getBookShelves().stream().map(BookShelf::getName).toArray(String[]::new));
        shelfComboBox.setSelectedItem(currentUser.getCurrentBookShelf().getName());
        inputPanel.add(shelfComboBox);
        inputPanel.add(new JLabel("评分："));
        JTextField ratingField = new JTextField();
        inputPanel.add(ratingField);
        dialog.add(inputPanel, BorderLayout.NORTH);

        JTextArea reviewArea = new JTextArea();
        dialog.add(new JScrollPane(reviewArea), BorderLayout.CENTER);

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
                    String selectedShelfName = (String) shelfComboBox.getSelectedItem();
                    BookShelf selectedShelf = currentUser.getBookShelves().stream()
                            .filter(shelf -> shelf.getName().equals(selectedShelfName))
                            .findFirst()
                            .orElse(null);
                    if (selectedShelf != null) {
                        BigDecimal newRating = new BigDecimal(ratingField.getText());
                        int newReviewCount = book.getReviewCount() + 1;
                        BigDecimal newAverageRating = (book.getAverageRating()
                                .multiply(BigDecimal.valueOf(book.getReviewCount())).add(newRating))
                                .divide(BigDecimal.valueOf(newReviewCount), 2, BigDecimal.ROUND_HALF_UP);
                        book.setAverageRating(newAverageRating);
                        book.setReviewCount(newReviewCount);
                        // 这里可以添加保存书评的逻辑
                        out.writeObject("addBookToShelfByObject");
                        out.writeObject(selectedShelf);
                        out.writeObject(book);
                        out.flush();
                        selectedShelf.getBooks().add(book);
                        dialog.dispose();
                        displayBooks(currentUser.getCurrentBookShelf());
                    }
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