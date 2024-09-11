package Pages.Pages.Library;

import Pages.MainApp;
import vCampus.Entity.Books.BorrowRecord;
import vCampus.Dao.Criteria.BorrowRecordSearchCriteria;
import vCampus.Dao.Criteria.BorrowRecordSortCriteria;
import vCampus.Dao.Criteria.SortCriteria;
import vCampus.Service.SearchResult;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
    private Map<String, List<BorrowRecord>> borrowRecordCache = new HashMap<>(); // 缓存借阅记录

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
                showBorrowRecords();
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
            synchronized (MainApp.class) {
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
                    out.writeObject("4");
                    out.writeObject("getTotalBorrowRecords");
                    out.writeObject(userId);
                    out.flush();

                    int totalBorrowCount = (int) in.readObject();
                    int currentBorrowCount = (int) in.readObject();
                    int bookshelfCount = (int) in.readObject();

                    // 更新UI
                    SwingUtilities.invokeLater(() -> {
                        totalBorrowLabel.setText(String.valueOf(totalBorrowCount));
                        currentBorrowLabel.setText(String.valueOf(currentBorrowCount));
                        bookshelfCountLabel.setText(String.valueOf(bookshelfCount));
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showBorrowRecords() {
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
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 加载下一页借阅记录
            }
        });

        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        // 加载第一页借阅记录
        loadBorrowRecords(1, table);
    }

    private void loadBorrowRecords(int page, JTable table) {
        new Thread(() -> {
            try {
                ObjectOutputStream out = MainApp.getOut();
                ObjectInputStream in = MainApp.getIn();

                // 请求服务器获取借阅记录
                BorrowRecordSearchCriteria searchCriteria = new BorrowRecordSearchCriteria();
                searchCriteria.addCriteria("user_id", MainApp.getCurrentUser().getId(), "and");
                BorrowRecordSortCriteria sortCriteria = new BorrowRecordSortCriteria();
                sortCriteria.addCriteria("borrow_date", SortCriteria.SortOrder.DESC);

                out.writeObject("searchBorrowRecords");
                out.writeObject(searchCriteria);
                out.writeObject(sortCriteria);
                out.writeObject(page);
                out.writeObject(10); // 每页10条记录
                out.flush();

                SearchResult<BorrowRecord> result = (SearchResult<BorrowRecord>) in.readObject();

                // 更新UI
                SwingUtilities.invokeLater(() -> {
                    List<BorrowRecord> records = result.getResult();
                    for (int i = 0; i < records.size(); i++) {
                        BorrowRecord record = records.get(i);
                        table.setValueAt(record.getBorrowDate(), i, 0);
                        table.setValueAt(record.getReturnDate(), i, 1);
                        table.setValueAt(record.getBook().getTitle(), i, 2);
                        table.setValueAt(record.getStatus().toString(), i, 3);
                    }
                });

                // 缓存结果
                borrowRecordCache.put("page_" + page, result.getResult());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}