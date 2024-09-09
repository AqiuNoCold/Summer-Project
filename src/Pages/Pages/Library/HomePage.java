package Pages.Pages.Library;

import Pages.MainApp;
import Pages.Utils.ClientUtils;
import Pages.Utils.IconUtils;
import vCampus.Entity.Books.Book;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class HomePage extends JPanel {
    private JTextField searchField;
    private JPanel advancedSearchPanel;
    private boolean isAdvancedSearchVisible = false;
    private JButton settingsButton;
    private JButton searchButton;
    private JPanel bookDisplayPanel;
    private JPanel paginationPanel;
    private JButton prevButton;
    private JButton nextButton;

    public HomePage() {
        setLayout(new BorderLayout());

        // 创建顶部搜索栏面板
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(800, 50)); // 设置宽度为800，高度为50
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 创建设置按钮
        settingsButton = createImageButton("/imgs/setting.svg", 800, 600);
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleAdvancedSearch();
            }
        });
        topPanel.add(settingsButton, BorderLayout.WEST);

        // 创建搜索输入框
        searchField = new JTextField();
        topPanel.add(searchField, BorderLayout.CENTER);

        // 创建搜索按钮
        searchButton = createImageButton("/imgs/search.svg", 800, 600);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });
        topPanel.add(searchButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // 创建图书展示区域
        bookDisplayPanel = new JPanel(new GridLayout(2, 4, 10, 10)); // 2行4列，间距为10
        bookDisplayPanel.setPreferredSize(new Dimension(800, 400)); // 设置宽度为800，高度为400
        add(bookDisplayPanel, BorderLayout.CENTER);

        // 异步获取并显示8本随机书籍的封面
        new BookLoader().execute();

        paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        prevButton = createImageButton("/imgs/caret-left.svg", 800, 600);
        JTextField pageNumberField = new JTextField(5);
        nextButton = createImageButton("/imgs/caret-right.svg", 800, 600);

        paginationPanel.add(prevButton);
        paginationPanel.add(pageNumberField);
        paginationPanel.add(nextButton);

        add(paginationPanel, BorderLayout.SOUTH);

        // 添加鼠标事件监听器
        addPaginationButtonListeners();

        // 创建并隐藏高级搜索面板
        advancedSearchPanel = new JPanel();
        advancedSearchPanel.setLayout(new BoxLayout(advancedSearchPanel, BoxLayout.Y_AXIS));
        advancedSearchPanel.setVisible(false);
        advancedSearchPanel.setPreferredSize(new Dimension(topPanel.getWidth(), 200)); // 固定高度
        add(advancedSearchPanel, BorderLayout.SOUTH); // 添加到布局中

        // 添加初始的高级搜索行
        addAdvancedSearchRow();

        // 添加窗口大小监听器
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeBookImages();
            }
        });
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
                    int newWidth = bookLabel.getWidth();
                    int newHeight = (int) (image.getHeight(null) * ((double) newWidth / image.getWidth(null)));
                    Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(scaledImage);
                    scaledIcon.setDescription(imagePath); // 设置描述以便后续使用
                    bookLabel.setIcon(scaledIcon);
                }
            }
        }
    }

    private class BookLoader extends SwingWorker<List<Book>, Void> {
        @Override
        protected List<Book> doInBackground() throws Exception {
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
                        int newWidth = bookDisplayPanel.getWidth() / 4 - 20; // 4列，每列之间有10的间距
                        int newHeight = (int) (image.getHeight(null) * ((double) newWidth / image.getWidth(null)));
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
            }
        }
    }

    private void toggleAdvancedSearch() {
        isAdvancedSearchVisible = !isAdvancedSearchVisible;
        advancedSearchPanel.setVisible(isAdvancedSearchVisible);
        searchField.setEditable(!isAdvancedSearchVisible);
    }

    private void addAdvancedSearchRow() {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // 创建逻辑运算符下拉框
        JComboBox<String> logicComboBox = new JComboBox<>(new String[] { "AND", "OR" });
        rowPanel.add(logicComboBox, BorderLayout.WEST);

        // 创建搜索输入框
        JTextField advancedSearchField = new JTextField();
        rowPanel.add(advancedSearchField, BorderLayout.CENTER);

        // 创建字段选择下拉框
        JComboBox<String> fieldComboBox = new JComboBox<>(new String[] { "标题", "作者", "简介", "关键词", "ISBN", "ISBN13" });
        rowPanel.add(fieldComboBox, BorderLayout.EAST);

        // 创建增加按钮
        JButton addButton = createImageButton("/imgs/add.svg", 800, 600);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAdvancedSearchRow();
            }
        });
        rowPanel.add(addButton, BorderLayout.EAST); // 将按钮放在最右侧

        advancedSearchPanel.add(rowPanel);
        advancedSearchPanel.revalidate();
        advancedSearchPanel.repaint();
    }

    private void performSearch() {
        // 实现搜索逻辑
        String searchText = searchField.getText();
        System.out.println("搜索内容: " + searchText);

        // 检查高级搜索字段
        for (Component component : advancedSearchPanel.getComponents()) {
            if (component instanceof JPanel) {
                JPanel rowPanel = (JPanel) component;
                JComboBox<String> fieldComboBox = (JComboBox<String>) rowPanel.getComponent(2);
                JTextField advancedSearchField = (JTextField) rowPanel.getComponent(1);

                String selectedField = (String) fieldComboBox.getSelectedItem();
                String fieldValue = advancedSearchField.getText();

                if ("ISBN".equals(selectedField) && fieldValue.length() != 10) {
                    JOptionPane.showMessageDialog(this, "ISBN长度必须为10位", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if ("ISBN13".equals(selectedField) && fieldValue.length() != 13) {
                    JOptionPane.showMessageDialog(this, "ISBN13长度必须为13位", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 构建搜索准则
                System.out.println("高级搜索字段: " + selectedField + " 值: " + fieldValue);
            }
        }

        // 创建排序对象
        // 默认排序准则
        String sortOrder = "无";
        System.out.println("排序准则: " + sortOrder);
    }

    private JButton createImageButton(String imagePath, int windowWidth, int windowHeight) {
        ImageIcon icon = IconUtils.loadSVGImage(imagePath, windowWidth, windowHeight);
        JButton button = new JButton(icon);
        button.setBackground(new Color(255, 255, 255)); // 设置背景颜色
        button.setFocusPainted(false);
        double scaleFactor = Math.min(windowWidth / 1920.0, windowHeight / 1080.0);
        IconUtils.setButtonBorder(button, scaleFactor); // 设置按钮边框
        return button;
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
            }
        });

        // 根据是否需要翻页来设置按钮的状态和图标
        updatePaginationButtons();
    }

    private void updatePaginationButtons() {
        boolean needsPagination = bookDisplayPanel.getComponentCount() > 8; // 假设每页显示8本书

        prevButton.setEnabled(needsPagination);
        nextButton.setEnabled(needsPagination);

        if (!needsPagination) {
            prevButton.setIcon(IconUtils.loadSVGImage("/imgs/caret-left-fill.svg", 800, 600));
            nextButton.setIcon(IconUtils.loadSVGImage("/imgs/caret-right-fill.svg", 800, 600));
        } else {
            prevButton.setIcon(IconUtils.loadSVGImage("/imgs/caret-left.svg", 800, 600));
            nextButton.setIcon(IconUtils.loadSVGImage("/imgs/caret-right.svg", 800, 600));
        }
    }

    public void updateButtonIcons(int windowWidth, int windowHeight) {
        IconUtils.updateButtonIcon(settingsButton, "/imgs/setting.svg", windowWidth, windowHeight);
        IconUtils.updateButtonIcon(searchButton, "/imgs/search.svg", windowWidth, windowHeight);
    }

    public static void main(String[] args) {
        // 初始化Socket
        MainApp.initializeSocket();

        // 创建并显示HomePage
        JFrame frame = new JFrame("Library Home Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new HomePage());
        frame.setVisible(true);

        // 关闭资源
        Runtime.getRuntime().addShutdownHook(new Thread(() -> MainApp.close_source()));
    }
}