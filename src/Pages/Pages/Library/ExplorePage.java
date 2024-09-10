package Pages.Pages.Library;

import Pages.MainApp;
import Pages.Utils.IconUtils;
import vCampus.Entity.Books.Book;
import vCampus.Service.SearchResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ExplorePage extends JPanel {
    private static ExplorePage instance; // 唯一实例
    private JPanel bookDisplayPanel;
    private JPanel paginationPanel;
    private JButton prevButton;
    private JButton nextButton;
    private JTextField pageNumberField;
    private JDialog loadingDialog;
    private SearchBar searchBar; // 添加 SearchBar 实例
    private JLabel totalBooksLabel; // 添加总书籍数标签
    private int currentPage = 1; // 当前页码
    private int totalBooks = 0; // 总书籍数
    private Map<Integer, List<Book>> bookCache = new HashMap<>(); // 缓存每页的书籍列表
    private boolean isSearchMode = false; // 是否为搜索模式
    private JPanel centerPanel; // 中间面板

    private ExplorePage() {
        // 私有构造函数，防止实例化
        setLayout(new BorderLayout());

        // 创建图书展示区域
        bookDisplayPanel = new JPanel(new GridLayout(2, 4, 10, 10)); // 2行4列，间距为10
        bookDisplayPanel.setPreferredSize(new Dimension(800, 400)); // 设置宽度为800，高度为400

        // 创建搜索栏
        searchBar = SearchBar.getInstance();
        searchBar.setExplorePage(this); // 设置搜索结果页面

        // 创建中间面板
        centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(searchBar, new GridBagConstraints());
        add(centerPanel, BorderLayout.CENTER);

        // 创建分页面板
        paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        prevButton = IconUtils.createImageButton("/imgs/caret-left.svg", 800, 600);
        pageNumberField = new JTextField(5);
        nextButton = IconUtils.createImageButton("/imgs/caret-right.svg", 800, 600);

        paginationPanel.add(prevButton);
        paginationPanel.add(pageNumberField);
        paginationPanel.add(nextButton);

        // 初始化总书籍数标签
        totalBooksLabel = new JLabel("符合条件的书籍数: " + totalBooks, JLabel.CENTER);

        // 添加窗口大小监听器
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeBookImages();
            }
        });

        // 添加鼠标事件监听器
        addPaginationButtonListeners();
    }

    public static synchronized ExplorePage getInstance() {
        if (instance == null) {
            instance = new ExplorePage();
        }
        return instance;
    }

    public void updateBookDisplay(List<Book> books, int currentPage, int totalBooks) {
        this.currentPage = currentPage;
        this.totalBooks = totalBooks;

        bookDisplayPanel.removeAll(); // 清空当前展示的书籍

        for (Book book : books) {
            JLabel bookLabel = new JLabel();
            String imagePath = book.getCachedImagePath();
            if (imagePath == null) {
                imagePath = "src/imgs/default_book_cover/1.jpg"; // 默认封面
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
            bookDisplayPanel.add(bookLabel);
        }

        bookDisplayPanel.revalidate();
        bookDisplayPanel.repaint();

        // 更新分页按钮状态
        updatePaginationButtons();

        // 更新总书籍数标签
        totalBooksLabel.setText("符合条件的书籍数: " + totalBooks);
    }

    public void switchToSearchMode() {
        if (!isSearchMode) {
            remove(totalBooksLabel);
            remove(centerPanel);
            add(totalBooksLabel, BorderLayout.NORTH);
            add(bookDisplayPanel, BorderLayout.CENTER);
            add(paginationPanel, BorderLayout.SOUTH);
            revalidate();
            repaint();
            isSearchMode = true;
        }
    }

    public void switchToDefaultMode() {
        if (isSearchMode) {
            remove(totalBooksLabel);
            remove(bookDisplayPanel);
            remove(paginationPanel);
            add(centerPanel, BorderLayout.CENTER); // 将中间面板添加到布局中
            revalidate();
            repaint();
            isSearchMode = false;
        }
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

    private void loadBooksForPage(int page) {
        if (bookCache.containsKey(page)) {
            // 如果缓存中有该页的书籍列表，则直接使用缓存
            List<Book> books = bookCache.get(page);
            updateBookDisplay(books, page, totalBooks);
        } else {
            // 否则异步获取书籍列表
            new BookLoader(page).execute();
        }
    }

    private class BookLoader extends SwingWorker<List<Book>, Void> {
        private int page;

        public BookLoader(int page) {
            this.page = page;
        }

        @Override
        protected List<Book> doInBackground() throws Exception {
            // 显示加载对话框
            SwingUtilities.invokeLater(() -> showLoadingDialog());

            ObjectOutputStream out = MainApp.getOut();
            ObjectInputStream in = MainApp.getIn();

            try {
                out.writeObject("4");
                out.writeObject("searchBooks");
                out.writeObject(searchBar.getSearchCriteria());
                out.writeObject(searchBar.getSortCriteria());
                out.writeObject(page);
                out.writeObject(8); // 每页显示 8 本书
                out.flush();

                // 接收搜索结果
                SearchResult<Book> searchResult = (SearchResult<Book>) in.readObject();
                totalBooks = searchResult.getTotal();
                return searchResult.getResult();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void done() {
            try {
                List<Book> books = get();
                bookCache.put(page, books); // 将获取的书籍列表存入缓存
                updateBookDisplay(books, page, totalBooks);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } finally {
                // 关闭加载对话框
                SwingUtilities.invokeLater(() -> hideLoadingDialog());
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

        // 创建并显示ExplorePage
        JFrame frame = new JFrame("Library Explore Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(ExplorePage.getInstance());
        ExplorePage.getInstance().switchToDefaultMode();
        frame.setVisible(true);

        // 关闭资源
        Runtime.getRuntime().addShutdownHook(new Thread(() -> MainApp.close_source()));
    }
}