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
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class HomePage extends JPanel {
    private static HomePage instance; // 唯一实例
    private static JPanel bookDisplayPanel;
    private static JPanel paginationPanel;
    private static JButton prevButton;
    private static JButton nextButton;
    private static JTextField pageNumberField;
    private static JDialog loadingDialog;
    private static SearchBar searchBar; // 添加 SearchBar 实例
    private static int currentPage = 1; // 当前页码

    private HomePage() {
        // 私有构造函数，防止实例化
    }

    public static HomePage init() {
        if (instance == null) {
            instance = new HomePage();
            instance.setLayout(new BorderLayout());

            // 创建图书展示区域
            bookDisplayPanel = new JPanel(new GridLayout(2, 4, 10, 10)); // 2行4列，间距为10
            bookDisplayPanel.setPreferredSize(new Dimension(800, 400)); // 设置宽度为800，高度为400

            // 创建搜索栏
            searchBar = new SearchBar();
            instance.add(searchBar, BorderLayout.NORTH); // 将搜索栏添加到布局中

            // 创建分页面板
            paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            prevButton = IconUtils.createImageButton("/imgs/caret-left.svg", 800, 600);
            pageNumberField = new JTextField(5);
            nextButton = IconUtils.createImageButton("/imgs/caret-right.svg", 800, 600);

            paginationPanel.add(prevButton);
            paginationPanel.add(pageNumberField);
            paginationPanel.add(nextButton);

            // 添加窗口大小监听器
            instance.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    instance.resizeBookImages();
                }
            });

            instance.add(bookDisplayPanel, BorderLayout.CENTER);
            instance.add(paginationPanel, BorderLayout.SOUTH);

            // 异步获取并显示8本随机书籍的封面
            new BookLoader().execute();

            // 添加鼠标事件监听器
            instance.addPaginationButtonListeners();
        }
        return instance;
    }

    public static HomePage getInstance() {
        if (instance == null) {
            return init();
        }
        return instance;
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

    private static class BookLoader extends SwingWorker<List<Book>, Void> {
        @Override
        protected List<Book> doInBackground() throws Exception {
            // 显示加载对话框
            SwingUtilities.invokeLater(() -> showLoadingDialog());
            return ClientUtils.getRandomBooksAndInitialize(8);
        }

        @Override
        protected void done() {
            try {
                List<Book> books = get();
                Random random = new Random();
                for (Book book : books) {
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
                        bookLabel.setIcon(scaledIcon);
                        bookLabel.setHorizontalAlignment(JLabel.CENTER);
                        bookLabel.setVerticalAlignment(JLabel.CENTER);
                    }
                    bookLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // 添加灰色边框
                    bookDisplayPanel.add(bookLabel);
                }
                bookDisplayPanel.revalidate();
                bookDisplayPanel.repaint();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } finally {
                // 关闭加载对话框
                SwingUtilities.invokeLater(() -> hideLoadingDialog());
            }
        }
    }

    private static void showLoadingDialog() {
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

    private static void hideLoadingDialog() {
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
                    new BookLoader().execute(); // 重新加载书籍
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
                new BookLoader().execute(); // 重新加载书籍
            }
        });

        // 根据是否需要翻页来设置按钮的状态和图标
        updatePaginationButtons();
    }

    private void updatePaginationButtons() {
        boolean needsPagination = bookDisplayPanel.getComponentCount() > 8; // 假设每页显示8本书

        prevButton.setEnabled(needsPagination);
        nextButton.setEnabled(needsPagination);
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

        // 关闭资源
        Runtime.getRuntime().addShutdownHook(new Thread(() -> MainApp.close_source()));
    }
}