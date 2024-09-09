package Pages.Pages.Library;

import Pages.Utils.IconUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JPanel {
    private JTextField searchField;
    private JPanel advancedSearchPanel;
    private boolean isAdvancedSearchVisible = false;
    private JButton settingsButton;
    private JButton searchButton;
    private JPanel bookDisplayPanel;
    private JPanel paginationPanel;

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
        add(bookDisplayPanel, BorderLayout.CENTER);

        // 添加示例图书封面
        for (int i = 0; i < 8; i++) {
            bookDisplayPanel.add(new JLabel(new ImageIcon("/imgs/book_cover" + i + ".jpg")));
        }

        // 创建底部翻页栏目
        paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton prevButton = new JButton("向左");
        JTextField pageNumberField = new JTextField(5);
        JButton nextButton = new JButton("向右");

        paginationPanel.add(prevButton);
        paginationPanel.add(pageNumberField);
        paginationPanel.add(nextButton);

        add(paginationPanel, BorderLayout.SOUTH);

        // 创建高级搜索面板
        advancedSearchPanel = new JPanel();
        advancedSearchPanel.setLayout(new BoxLayout(advancedSearchPanel, BoxLayout.Y_AXIS));
        advancedSearchPanel.setVisible(false);
        advancedSearchPanel.setPreferredSize(new Dimension(topPanel.getWidth(), 200)); // 固定高度
        add(advancedSearchPanel, BorderLayout.CENTER); // 添加到布局中

        // 添加初始的高级搜索行
        addAdvancedSearchRow();
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
        rowPanel.add(addButton, BorderLayout.SOUTH);

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

    public void updateButtonIcons(int windowWidth, int windowHeight) {
        IconUtils.updateButtonIcon(settingsButton, "/imgs/setting.svg", windowWidth, windowHeight);
        IconUtils.updateButtonIcon(searchButton, "/imgs/search.svg", windowWidth, windowHeight);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Library Home Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new HomePage());
        frame.setVisible(true);
    }
}