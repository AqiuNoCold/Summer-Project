package Pages.Pages.Library;

import Pages.MainApp;
import Pages.Utils.ClientUtils;
import Pages.Utils.IconUtils;
import vCampus.Entity.Books.Book;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Queue;
import java.util.concurrent.ExecutionException;

public class HomePage extends JPanel {
    private static HomePage instance; // 唯一实例
    private JPanel bookDisplayPanel;
    private JPanel paginationPanel;
    private JButton prevButton;
    private JButton nextButton;
    private JTextField pageNumberField;
    private JDialog loadingDialog;
    private SearchBar searchBar; // 添加 SearchBar 实例
    private int currentPage = 1; // 当前页码
    private int totalBooks = 30859865; // 总书籍数
    private Map<Integer, List<Book>> bookCache = new HashMap<>(); // 缓存每页的书籍列表
    private Queue<BookLoader> bookLoaderQueue = new LinkedList<>();

    // 定义页面状态
    private enum PageStatus {
        NOT_LOADED,
        LOADING,
        LOADED
    }

    // 页面状态缓存
    private Map<Integer, PageStatus> pageStatusCache = new HashMap<>();

    private HomePage() {
        // 私有构造函数，防止实例化
        setLayout(new BorderLayout());

        // 创建图书展示区域
        bookDisplayPanel = new JPanel(new GridLayout(2, 4, 10, 10)); // 2行4列，间距为10
        bookDisplayPanel.setPreferredSize(new Dimension(800, 400)); // 设置宽度为800，高度为400

        // 创建搜索栏
        searchBar = SearchBar.getInstance();
        add(searchBar, BorderLayout.NORTH); // 将搜索栏添加到布局中

        // 创建分页面板
        paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        prevButton = IconUtils.createImageButton("/imgs/caret-left.svg", 800, 600);
        pageNumberField = new JTextField(5);
        pageNumberField.setHorizontalAlignment(JTextField.CENTER); // 设置水平对齐方式为居中
        pageNumberField.setPreferredSize(new Dimension(50, 30)); // 调整宽度
        nextButton = IconUtils.createImageButton("/imgs/caret-right.svg", 800, 600);

        paginationPanel.add(prevButton);
        paginationPanel.add(pageNumberField);
        paginationPanel.add(nextButton);

        // 添加页码输入框的事件监听器
        pageNumberField.addActionListener(e -> handlePageNumberInput());
        pageNumberField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                handlePageNumberInput();
            }
        });

        // 添加窗口大小监听器
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeBookImages();
            }
        });

        add(bookDisplayPanel, BorderLayout.CENTER);
        add(paginationPanel, BorderLayout.SOUTH);

        // 异步获取并显示8本随机书籍的封面
        loadBooksForPage(currentPage);

        // 预加载下一页的书籍
        preloadNextPages(currentPage + 1, 2); // 预加载当前页之后的2页

        // 添加鼠标事件监听器
        addPaginationButtonListeners();
    }

    public static synchronized HomePage getInstance() {
        if (instance == null) {
            instance = new HomePage();
        }
        return instance;
    }

    // 处理页码输入框的输入
    private void handlePageNumberInput() {
        String input = pageNumberField.getText();
        try {
            int page = Integer.parseInt(input);
            if (page > 0 && page <= (totalBooks + 7) / 8) { // 假设每页显示8本书
                switchPage(page);
            } else {
                pageNumberField.setText(String.valueOf(currentPage)); // 重置为当前页码
            }
        } catch (NumberFormatException e) {
            pageNumberField.setText(String.valueOf(currentPage)); // 重置为当前页码
        }
    }

    // 修改切换页面的方法，更新前后两页的状态
    private void switchPage(int newPage) {
        currentPage = newPage;
        updatePageStatus(newPage);
        loadBooksForPage(newPage);
    }

    public void updateBookDisplay(List<Book> books, int currentPage) {
        this.currentPage = currentPage;

        bookDisplayPanel.removeAll(); // 清空当前展示的书籍

        Random random = new Random();
        for (Book book : books) {
            JPanel bookPanel = new JPanel(new BorderLayout());
            JLabel bookLabel = new JLabel();
            String imagePath = book.getCachedImagePath();
            if (imagePath == null) {
                int randomIndex = random.nextInt(8) + 1; // 生成1到8之间的随机数
                imagePath = "src/imgs/default_book_cover/" + randomIndex + ".jpg";
            }
            ImageIcon bookIcon = new ImageIcon(imagePath);
            if (bookIcon.getIconWidth() == -1) {
                System.out.println("Failed to load image: " + imagePath); // 调试信息
            } else {
                Image image = bookIcon.getImage();
                int panelWidth = bookDisplayPanel.getWidth() / 4 - 20; // 4列，每列之间有10的间距
                int panelHeight = bookDisplayPanel.getHeight() / 2 - 20;
                int imageWidth = image.getWidth(null);
                int imageHeight = image.getHeight(null);

                double panelAspectRatio = (double) panelWidth / panelHeight;
                double imageAspectRatio = (double) imageWidth / imageHeight;

                int newWidth, newHeight;

                if (panelAspectRatio > imageAspectRatio) {
                    // 按高度对齐
                    newHeight = panelHeight;
                    newWidth = (int) (imageWidth * ((double) newHeight / imageHeight));
                } else {
                    // 按宽度对齐
                    newWidth = panelWidth;
                    newHeight = (int) (imageHeight * ((double) newWidth / imageWidth));
                }
                Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                scaledIcon.setDescription(imagePath); // 设置描述以便后续使用
                bookLabel.setIcon(scaledIcon);
                bookLabel.setHorizontalAlignment(JLabel.CENTER);
                bookLabel.setVerticalAlignment(JLabel.CENTER);
            }
            bookLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // 添加灰色边框
            JLabel titleLabel = new JLabel(book.getTitle(), JLabel.CENTER);
            bookPanel.add(bookLabel, BorderLayout.CENTER);
            bookPanel.add(titleLabel, BorderLayout.SOUTH);

            // 添加点击事件监听器
            bookPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    BookDetailPage.getInstance().showBookDetails(book);
                }
            });

            bookDisplayPanel.add(bookPanel);
        }

        bookDisplayPanel.revalidate();
        bookDisplayPanel.repaint();

        // 更新分页按钮状态
        updatePaginationButtons();

        // 更新页码框的数字
        pageNumberField.setText(String.valueOf(currentPage));
    }

    private void resizeBookImages() {
        for (Component component : bookDisplayPanel.getComponents()) {
            if (component instanceof JLabel) {
                JLabel bookLabel = (JLabel) component;
                ImageIcon icon = (ImageIcon) bookLabel.getIcon();
                if (icon != null) {
                    String imagePath = icon.getDescription();
                    ImageIcon newIcon = new ImageIcon(imagePath);
                    Image image = newIcon.getImage();
                    int panelWidth = bookLabel.getWidth();
                    int panelHeight = bookLabel.getHeight();
                    int imageWidth = image.getWidth(null);
                    int imageHeight = image.getHeight(null);

                    double panelAspectRatio = (double) panelWidth / panelHeight;
                    double imageAspectRatio = (double) imageWidth / imageHeight;

                    int newWidth, newHeight;

                    if (panelAspectRatio > imageAspectRatio) {
                        // 按高度对齐
                        newHeight = panelHeight;
                        newWidth = (int) (imageWidth * ((double) newHeight / imageHeight));
                    } else {
                        // 按宽度对齐
                        newWidth = panelWidth;
                        newHeight = (int) (imageHeight * ((double) newWidth / imageWidth));
                    }
                    Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(scaledImage);
                    scaledIcon.setDescription(imagePath); // 设置描述以便后续使用
                    bookLabel.setIcon(scaledIcon);
                }
            }
        }
    }

    // 更新当前页和前后两页的状态
    private void updatePageStatus(int page) {
        for (int i = page - 2; i <= page + 2; i++) {
            if (!pageStatusCache.containsKey(i)) {
                pageStatusCache.put(i, PageStatus.NOT_LOADED);
            }
        }
    }

    // 修改 loadBooksForPage 方法，检查页面状态并执行相应操作
    private void loadBooksForPage(int page) {
        PageStatus status = pageStatusCache.getOrDefault(page, PageStatus.NOT_LOADED);

        switch (status) {
            case NOT_LOADED:
                pageStatusCache.put(page, PageStatus.LOADING);
                bookLoaderQueue.add(new BookLoader(page, true));
                if (bookLoaderQueue.size() == 1) {
                    bookLoaderQueue.peek().execute();
                }
                break;
            case LOADING:
                // 显示加载框
                showLoadingDialog();
                break;
            case LOADED:
                List<Book> books = bookCache.get(page);
                updateBookDisplay(books, page);
                break;
        }
    }

    // 修改 preloadNextPages 方法
    private void preloadNextPages(int startPage, int numberOfPages) {
        for (int i = startPage; i < startPage + numberOfPages; i++) {
            if (!bookCache.containsKey(i)) {
                bookLoaderQueue.add(new BookLoader(i, false));
                if (bookLoaderQueue.size() == 1) {
                    bookLoaderQueue.peek().execute();
                }
            }
        }
    }

    // 添加 startNextBookLoader 方法
    private void startNextBookLoader() {
        bookLoaderQueue.poll(); // 移除已完成的任务
        if (!bookLoaderQueue.isEmpty()) {
            bookLoaderQueue.peek().execute(); // 启动下一个任务
        }
    }

    // 修改 BookLoader 类，添加一个标志来判断是否显示加载框
    private class BookLoader extends SwingWorker<List<Book>, Void> {
        private int page;
        private boolean showLoadingDialog;

        public BookLoader(int page, boolean showLoadingDialog) {
            this.page = page;
            this.showLoadingDialog = showLoadingDialog;
        }

        @Override
        protected List<Book> doInBackground() throws Exception {
            if (showLoadingDialog) {
                // 显示加载对话框
                SwingUtilities.invokeLater(() -> showLoadingDialog());
            }
            return ClientUtils.getRandomBooksAndInitialize(8);
        }

        @Override
        protected void done() {
            try {
                List<Book> books = get();
                if (books != null) {
                    bookCache.put(page, books); // 将获取的书籍列表存入缓存
                    pageStatusCache.put(page, PageStatus.LOADED); // 更新页面状态为已加载
                    if (page == currentPage) {
                        updateBookDisplay(books, page);
                    }
                } else {
                    // 处理 books 为 null 的情况
                    pageStatusCache.put(page, PageStatus.NOT_LOADED); // 重置页面状态
                    if (page == currentPage) {
                        JOptionPane.showMessageDialog(HomePage.this, "无法加载图书信息，请稍后重试。", "加载失败",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                pageStatusCache.put(page, PageStatus.NOT_LOADED); // 重置页面状态
                if (page == currentPage) {
                    JOptionPane.showMessageDialog(HomePage.this, "无法加载图书信息，请稍后重试。", "加载失败", JOptionPane.ERROR_MESSAGE);
                }
            } finally {
                if (showLoadingDialog) {
                    // 关闭加载对话框
                    SwingUtilities.invokeLater(() -> hideLoadingDialog());
                }
                // 启动下一个任务
                startNextBookLoader();
            }
        }
    }

    private void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new JDialog(SwingUtilities.getWindowAncestor(bookDisplayPanel), "加载中",
                    Dialog.ModalityType.APPLICATION_MODAL);
            JLabel loadingLabel = new JLabel("图书信息正在加载中，请稍候...", JLabel.CENTER);
            loadingDialog.add(loadingLabel);
            loadingDialog.setSize(300, 150);
            loadingDialog.setLocationRelativeTo(bookDisplayPanel);
        }
        loadingDialog.setVisible(true);
    }

    private void hideLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dispose();
        }
    }

    private void addPaginationButtonListeners() {
        prevButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                prevButton.setIcon(IconUtils.loadSVGImage("/imgs/caret-left-fill.svg", 800, 600));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                prevButton.setIcon(IconUtils.loadSVGImage("/imgs/caret-left.svg", 800, 600));
                if (currentPage > 1) {
                    currentPage--;
                    loadBooksForPage(currentPage); // 重新加载书籍
                }
            }
        });

        nextButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                nextButton.setIcon(IconUtils.loadSVGImage("/imgs/caret-right-fill.svg", 800, 600));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                nextButton.setIcon(IconUtils.loadSVGImage("/imgs/caret-right.svg", 800, 600));
                currentPage++;
                loadBooksForPage(currentPage); // 重新加载书籍
            }
        });

        // 根据是否需要翻页来设置按钮的状态和图标
        updatePaginationButtons();
    }

    private void updatePaginationButtons() {
        boolean needsPagination = totalBooks > 8; // 假设每页显示8本书

        prevButton.setEnabled(currentPage > 1);
        nextButton.setEnabled(currentPage * 8 < totalBooks);
        pageNumberField.setEnabled(needsPagination); // 启用或禁用翻页框

        if (!needsPagination) {
            prevButton.setIcon(IconUtils.loadSVGImage("/imgs/caret-left-fill.svg", 800, 600));
            nextButton.setIcon(IconUtils.loadSVGImage("/imgs/caret-right-fill.svg", 800, 600));
        } else {
            prevButton.setIcon(IconUtils.loadSVGImage("/imgs/caret-left.svg", 800, 600));
            nextButton.setIcon(IconUtils.loadSVGImage("/imgs/caret-right.svg", 800, 600));
        }
    }

    public static void main(String[] args) {
        // 初始化Socket
        MainApp.initializeSocket();

        // 创建并显示HomePage
        JFrame frame = new JFrame("Library Home Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(HomePage.getInstance());
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MainApp.close_source();
            }
        });
    }
}