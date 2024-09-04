package Pages.Pages.Library;

import Pages.Utils.SVGImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JPanel {
    private JTextField searchField;
    private JPanel advancedSearchPanel;
    private boolean isAdvancedSearchVisible = false;

    public HomePage() {
        setLayout(new BorderLayout());

        // 创建顶部搜索栏面板
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 创建设置按钮
        JButton settingsButton = createImageButton("/imgs/setting.svg", 800, 600);
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
        JButton searchButton = createImageButton("/imgs/search.svg", 800, 600);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });
        topPanel.add(searchButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // 创建高级搜索面板
        advancedSearchPanel = new JPanel();
        advancedSearchPanel.setLayout(new BoxLayout(advancedSearchPanel, BoxLayout.Y_AXIS));
        advancedSearchPanel.setVisible(false);
        add(advancedSearchPanel, BorderLayout.CENTER);

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
        JComboBox<String> fieldComboBox = new JComboBox<>(new String[] { "标题", "作者", "简介", "关键词" });
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
    }

    private JButton createImageButton(String imagePath, int windowWidth, int windowHeight) {
        double scaleFactor = Math.min(windowWidth / 1920.0, windowHeight / 1080.0);
        int iconSize = (int) (64 * scaleFactor); // 根据缩放比例调整图标大小
        ImageIcon icon = SVGImageLoader.loadSVGImage(imagePath, iconSize, iconSize);

        if (icon == null || icon.getIconWidth() == -1) { // 判断图片是否加载成功
            System.err.println("Error: Could not load image at " + imagePath);
        }

        JButton button = new JButton(icon);
        button.setBackground(new Color(255, 255, 255)); // 设置背景颜色
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder((int) (10 * scaleFactor), (int) (10 * scaleFactor),
                (int) (10 * scaleFactor), (int) (10 * scaleFactor))); // 根据缩放比例调整按钮内边距
        return button;
    }
}