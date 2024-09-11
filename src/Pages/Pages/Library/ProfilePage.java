package Pages.Pages.Library;

import Pages.MainApp;
import vCampus.Entity.Books.BorrowRecord;
import vCampus.Entity.Books.BookUser;
import vCampus.Dao.Criteria.BorrowRecordSearchCriteria;
import vCampus.Dao.Criteria.BorrowRecordSortCriteria;
import vCampus.Dao.Criteria.SortCriteria;
import vCampus.Service.SearchResult;
import vCampus.Service.Books.BorrowRecordService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ProfilePage extends JPanel {
    private static ProfilePage instance; // 单实例
    private JLabel nameLabel;
    private JLabel genderLabel;
    private JLabel ageLabel;
    private JLabel roleLabel;
    private JLabel totalBorrowLabel;
    private JLabel currentBorrowLabel;
    private JLabel bookshelfCountLabel;
    private JLabel reviewCountLabel;
    private Map<Integer, List<BorrowRecord>> allBorrowRecordsCache = new HashMap<>();
    private Map<Integer, List<BorrowRecord>> currentBorrowRecordsCache = new HashMap<>();

    private ProfilePage() {
        setLayout(new BorderLayout());

        // 创建个人信息面板
        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createTitledBorder("个人信息"));

        nameLabel = new JLabel("姓名: ");
        genderLabel = new JLabel("性别: ");
        ageLabel = new JLabel("年龄: ");
        roleLabel = new JLabel("角色: ");
        totalBorrowLabel = new JLabel("累计借阅数量: ");
        currentBorrowLabel = new JLabel("当前待还书数量: ");
        bookshelfCountLabel = new JLabel("书架数量: ");
        reviewCountLabel = new JLabel("书评数量: 0");

        infoPanel.add(new JLabel("姓名: "));
        infoPanel.add(nameLabel);
        infoPanel.add(new JLabel("性别: "));
        infoPanel.add(genderLabel);
        infoPanel.add(new JLabel("年龄: "));
        infoPanel.add(ageLabel);
        infoPanel.add(new JLabel("角色: "));
        infoPanel.add(roleLabel);
        infoPanel.add(new JLabel("累计借阅数量: "));
        infoPanel.add(totalBorrowLabel);
        infoPanel.add(new JLabel("当前待还书数量: "));
        infoPanel.add(currentBorrowLabel);
        infoPanel.add(new JLabel("书架数量: "));
        infoPanel.add(bookshelfCountLabel);
        infoPanel.add(new JLabel("书评数量: "));
        infoPanel.add(reviewCountLabel);

        add(infoPanel, BorderLayout.NORTH);

        // 添加累计借阅数量点击事件
        totalBorrowLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showBorrowRecords("total");
            }
        });

        // 添加当前待还书数量点击事件
        currentBorrowLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showBorrowRecords("current");
            }
        });

        // 获取并显示用户信息
        fetchUserInfo();
    }

    public static synchronized ProfilePage getInstance() {
        if (instance == null) {
            instance = new ProfilePage();
        }
        return instance;
    }

    private void fetchUserInfo() {
        new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("线程 " + threadName + " 正在执行获取用户信息功能");

            synchronized (MainApp.class) {
                System.out.println("线程 " + threadName + " 获取了同步锁");

                try {
                    ObjectOutputStream out = MainApp.getOut();
                    ObjectInputStream in = MainApp.getIn();

                    // 获取当前用户ID
                    String userId = MainApp.getCurrentUser().getId();

                    // 请求服务器获取用户姓名
                    out.writeObject("2");
                    out.writeObject("getNameFromId");
                    out.writeObject(userId);
                    out.flush();

                    final String name; // 将name声明为final
                    String tempName = (String) in.readObject();
                    if (tempName == null) {
                        name = "新用户";
                    } else {
                        name = tempName;
                    }

                    // 更新UI
                    SwingUtilities.invokeLater(() -> {
                        nameLabel.setText(name);
                        genderLabel.setText(MainApp.getCurrentUser().getGender() ? "男" : "女");
                        ageLabel.setText(String.valueOf(MainApp.getCurrentUser().getAge()));
                        roleLabel.setText(MainApp.getCurrentUser().getRole());
                    });

                    // 请求服务器获取借阅记录数量
                    BorrowRecordSearchCriteria searchCriteria = new BorrowRecordSearchCriteria();
                    searchCriteria.addCriteria("user_id", userId, "AND");

                    BorrowRecordSortCriteria sortCriteria = new BorrowRecordSortCriteria();
                    sortCriteria.addCriteria("borrow_date", SortCriteria.SortOrder.DESC);

                    out.writeObject("4");
                    out.writeObject("searchBorrowRecords");
                    out.writeObject(searchCriteria);
                    out.writeObject(sortCriteria);
                    out.writeObject(1);
                    out.writeObject(10); // 每页10条记录
                    out.flush();

                    SearchResult<BorrowRecord> searchResult = (SearchResult<BorrowRecord>) in.readObject();
                    List<BorrowRecord> allBorrowRecords = searchResult.getResult();
                    int totalBorrowCount = searchResult.getTotal();

                    // 按状态筛选当前借阅记录
                    searchCriteria.addCriteria("status", BorrowRecordService.BorrowStatus.BORROWING.name(), "AND");

                    BorrowRecordSortCriteria currentSortCriteria = new BorrowRecordSortCriteria();
                    currentSortCriteria.addCriteria("return_date", SortCriteria.SortOrder.ASC);

                    out.writeObject("4");
                    out.writeObject("searchBorrowRecords");
                    out.writeObject(searchCriteria);
                    out.writeObject(currentSortCriteria);
                    out.writeObject(1);
                    out.writeObject(10); // 每页10条记录
                    out.flush();

                    searchResult = (SearchResult<BorrowRecord>) in.readObject();
                    List<BorrowRecord> currentBorrowRecords = searchResult.getResult();
                    int currentBorrowCount = searchResult.getTotal();

                    // 获取书架数量
                    int bookshelfCount = BookUser.getCurrentUser().getBookShelves().size();

                    // 更新UI
                    SwingUtilities.invokeLater(() -> {
                        totalBorrowLabel.setText(String.valueOf(totalBorrowCount));
                        currentBorrowLabel.setText(String.valueOf(currentBorrowCount));
                        bookshelfCountLabel.setText(String.valueOf(bookshelfCount));
                    });

                    // 缓存借阅记录
                    allBorrowRecordsCache.put(1, allBorrowRecords);
                    currentBorrowRecordsCache.put(1, currentBorrowRecords);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("线程 " + threadName + " 释放了同步锁");
                }
            }
        }).start();
    }

    private void showBorrowRecords(String type) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "借阅记录", true);
        dialog.setLayout(new BorderLayout());

        // 创建表格
        String[] columnNames = { "借阅日期", "归还日期", "图书名称", "状态" };
        Object[][] data = new Object[10][4];
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // 创建分页按钮
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton prevButton = new JButton("上一页");
        JButton nextButton = new JButton("下一页");
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // 添加分页按钮事件
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 加载上一页借阅记录
                loadBorrowRecords(type, table, -1);
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 加载下一页借阅记录
                loadBorrowRecords(type, table, 1);
            }
        });

        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        // 加载第一页借阅记录
        loadBorrowRecords(type, table, 0);
    }

    private void loadBorrowRecords(String type, JTable table, int pageOffset) {
        new Thread(() -> {
            try {
                ObjectOutputStream out = MainApp.getOut();
                ObjectInputStream in = MainApp.getIn();

                // 请求服务器获取借阅记录
                BorrowRecordSearchCriteria searchCriteria = new BorrowRecordSearchCriteria();
                searchCriteria.addCriteria("user_id", MainApp.getCurrentUser().getId(), "AND");
                BorrowRecordSortCriteria sortCriteria = new BorrowRecordSortCriteria();

                Map<Integer, List<BorrowRecord>> cache;
                if (type.equals("total")) {
                    sortCriteria.addCriteria("borrow_date", SortCriteria.SortOrder.DESC);
                    cache = allBorrowRecordsCache;
                } else {
                    searchCriteria.addCriteria("status", BorrowRecordService.BorrowStatus.BORROWING.name(), "AND");
                    sortCriteria.addCriteria("return_date", SortCriteria.SortOrder.ASC);
                    cache = currentBorrowRecordsCache;
                }

                int currentPage = cache.isEmpty() ? 1 : cache.keySet().stream().max(Integer::compareTo).orElse(1);
                currentPage += pageOffset;
                if (currentPage < 1)
                    currentPage = 1;

                if (!cache.containsKey(currentPage)) {
                    out.writeObject("4");
                    out.writeObject("searchBorrowRecords");
                    out.writeObject(searchCriteria);
                    out.writeObject(sortCriteria);
                    out.writeObject(currentPage);
                    out.writeObject(10); // 每页10条记录
                    out.flush();

                    SearchResult<BorrowRecord> result = (SearchResult<BorrowRecord>) in.readObject();
                    cache.put(currentPage, result.getResult());
                }

                List<BorrowRecord> records = cache.get(currentPage);

                // 更新UI
                SwingUtilities.invokeLater(() -> {
                    for (int i = 0; i < records.size(); i++) {
                        BorrowRecord record = records.get(i);
                        table.setValueAt(record.getBorrowDate(), i, 0);
                        table.setValueAt(record.getReturnDate(), i, 1);
                        table.setValueAt(record.getBook().getTitle(), i, 2);
                        table.setValueAt(record.getStatus().toString(), i, 3);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}