package Pages.Pages.Library;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Pages.Utils.IconUtils;
import vCampus.Dao.Criteria.BookSearchCriteria;
import vCampus.Dao.Criteria.BookSortCriteria;
import vCampus.Entity.Books.Book;
import vCampus.Service.SearchResult;

import Pages.MainApp;

public class SearchBar extends JPanel {
    private JTextField searchField;
    private JButton settingsButton;
    private JButton searchButton;
    private JDialog advancedSearchDialog;
    private JPanel advancedSearchPanel;
    private boolean isAdvancedSearchVisible = false;
    private List<AdvancedSearchRow> rows = new ArrayList<>();
    private int totalResults;
    private int currentPage = 1;
    private List<Book> books;
    private JDialog searchingDialog;

    // 显示字段名到实际字段名的映射
    private static final Map<String, String> fieldMap = new HashMap<>();
    static {
        fieldMap.put("标题", "title");
        fieldMap.put("作者", "authors");
        fieldMap.put("简介", "synopsis");
        fieldMap.put("关键词", "subjects");
        fieldMap.put("ISBN", "isbn");
        fieldMap.put("ISBN13", "isbn13");
    }

    public SearchBar() {
        setLayout(new BorderLayout());

        // 创建搜索栏
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchField = new JTextField(20);
        settingsButton = IconUtils.createImageButton("/imgs/setting.svg", 800, 600);
        settingsButton.addActionListener(e -> toggleAdvancedSearch());
        searchButton = IconUtils.createImageButton("/imgs/search.svg", 800, 600);
        searchButton.addActionListener(e -> performSearch());
        searchPanel.add(settingsButton);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.CENTER);

        // 创建高级搜索对话框
        advancedSearchDialog = new JDialog((Frame) null, "高级搜索", false); // 设置为非模态
        advancedSearchDialog.setUndecorated(true); // 移除标题栏和关闭按钮
        advancedSearchPanel = new JPanel();
        advancedSearchPanel.setLayout(new BoxLayout(advancedSearchPanel, BoxLayout.Y_AXIS));
        advancedSearchDialog.add(new JScrollPane(advancedSearchPanel));
        advancedSearchDialog.setSize(400, 300);
        addAdvancedSearchRow(); // 初始添加一行

        // 添加焦点监听器
        advancedSearchDialog.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                if (isAdvancedSearchVisible) {
                    advancedSearchDialog.requestFocus();
                    advancedSearchDialog.setVisible(false);
                    advancedSearchDialog.setVisible(true);
                }
            }
        });

        // 创建“正在搜索”对话框
        searchingDialog = new JDialog((Frame) null, "正在搜索", true);
        searchingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        searchingDialog.setUndecorated(true); // 移除标题栏
        searchingDialog.setSize(200, 100);
        searchingDialog.setLocationRelativeTo(this);
        searchingDialog.add(new JLabel("正在搜索，请稍候...", SwingConstants.CENTER));

        // 添加父窗口位置监听器
        addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                Window window = SwingUtilities.getWindowAncestor(SearchBar.this);
                if (window != null) {
                    window.addComponentListener(new ComponentAdapter() {
                        @Override
                        public void componentMoved(ComponentEvent e) {
                            if (isAdvancedSearchVisible) {
                                updateAdvancedSearchDialogLocation();
                            }
                        }

                        @Override
                        public void componentResized(ComponentEvent e) {
                            if (isAdvancedSearchVisible) {
                                updateAdvancedSearchDialogLocation();
                            }
                        }
                    });
                }
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
            }
        });
    }

    private void toggleAdvancedSearch() {
        isAdvancedSearchVisible = !isAdvancedSearchVisible;
        if (isAdvancedSearchVisible) {
            updateAdvancedSearchDialogLocation();
            advancedSearchDialog.setVisible(true);
            searchField.setEditable(false);
            searchField.setText(""); // 清空普通搜索框
            for (AdvancedSearchRow row : rows) {
                row.searchField.setEditable(true); // 确保高级搜索框的文本输入部分可用
            }
        } else {
            advancedSearchDialog.setVisible(false);
            searchField.setEditable(true);
            searchField.setText(""); // 清空普通搜索框
        }
    }

    private void updateAdvancedSearchDialogLocation() {
        // 获取 settingsButton 的位置和大小
        Point location = settingsButton.getLocationOnScreen();
        int x = location.x;
        int y = location.y + settingsButton.getHeight();

        // 设置高级搜索对话框的位置
        advancedSearchDialog.setLocation(x, y);
    }

    private void addAdvancedSearchRow() {
        AdvancedSearchRow row = new AdvancedSearchRow();
        rows.add(row);
        advancedSearchPanel.add(row);
        advancedSearchPanel.revalidate();
        advancedSearchDialog.pack(); // 调整窗口的大小
    }

    private void performSearch() {
        // 显示“正在搜索”对话框
        SwingUtilities.invokeLater(() -> searchingDialog.setVisible(true));

        // 创建一个新的线程来执行搜索操作
        new Thread(() -> {
            BookSearchCriteria searchCriteria = new BookSearchCriteria();
            BookSortCriteria sortCriteria = new BookSortCriteria();

            if (isAdvancedSearchVisible) {
                // 高级搜索逻辑
                for (AdvancedSearchRow row : rows) {
                    String logic = (String) row.logicComboBox.getSelectedItem();
                    String displayField = (String) row.fieldComboBox.getSelectedItem();
                    String actualField = fieldMap.get(displayField); // 获取实际字段名
                    String value = row.searchField.getText();
                    searchCriteria.addCriteria(actualField, value, logic);
                }
                searchField.setText(searchCriteria.toString()); // 在搜索框中显示完整搜索语句
            } else {
                // 普通搜索逻辑
                String query = searchField.getText();
                searchCriteria.addCriteria("title", query, "OR");
                searchCriteria.addCriteria("authors", query, "OR");
                searchCriteria.addCriteria("subjects", query, "OR");
                searchCriteria.addCriteria("synopsis", query, "OR");
            }

            ObjectOutputStream out = MainApp.getOut();
            ObjectInputStream in = MainApp.getIn();

            try {
                out.writeObject("4");
                out.writeObject("searchBooks");
                out.writeObject(searchCriteria);
                out.writeObject(sortCriteria);
                out.writeObject(currentPage);
                out.writeObject(8); // 每页显示 8 本书
                out.flush();

                // 接收搜索结果
                SearchResult<Book> searchResult = (SearchResult<Book>) in.readObject();
                books = searchResult.getResult();
                totalResults = searchResult.getTotal();
                books = searchResult.getResult();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 输出搜索结果
            System.out.println("符合条件的书籍总数：" + totalResults);
            for (Book book : books) {
                System.out.println(book);
            }

            // 隐藏“正在搜索”对话框
            SwingUtilities.invokeLater(() -> searchingDialog.setVisible(false));
        }).start();
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<Book> getBooks() {
        return books;
    }

    // 高级搜索行类
    private class AdvancedSearchRow extends JPanel {
        private JComboBox<String> logicComboBox;
        private JTextField searchField;
        private JComboBox<String> fieldComboBox;
        private JButton addButton;
        private JButton removeButton;

        public AdvancedSearchRow() {
            setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
            setBorder(new EmptyBorder(5, 0, 5, 0));

            logicComboBox = new JComboBox<>(new String[] { "AND", "OR" });
            searchField = new JTextField(10);
            fieldComboBox = new JComboBox<>(new String[] { "标题", "作者", "简介", "关键词", "ISBN", "ISBN13" });

            addButton = IconUtils.createImageButton("/imgs/add.svg", 800, 600);
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addAdvancedSearchRow();
                }
            });
            removeButton = IconUtils.createImageButton("/imgs/minus.svg", 800, 600);
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (rows.size() > 1) {
                        advancedSearchPanel.remove(AdvancedSearchRow.this);
                        rows.remove(AdvancedSearchRow.this);
                        advancedSearchPanel.revalidate();
                        advancedSearchDialog.pack(); // 调整窗口的大小
                    }
                }
            });

            add(logicComboBox);
            add(searchField);
            add(fieldComboBox);
            add(addButton);
            add(removeButton);
        }
    }

    public static void main(String[] args) {
        // 初始化Socket或其他必要的设置
        MainApp.initializeSocket();

        // 创建并显示搜索框
        JFrame frame = new JFrame("搜索框");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new SearchBar());
        frame.pack();
        frame.setVisible(true);

        // 添加窗口监听器，在窗口关闭时调用 MainApp.close_source()
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MainApp.close_source();
            }
        });
    }
}