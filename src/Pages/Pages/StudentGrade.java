package Pages.Pages;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentGrade {
    private static final int ROWS_PER_PAGE = 8; // 每页显示的行数
    private static int currentPage = 0;
    private static List<Object[]> data; // 表格数据
    private static List<Object[]> filteredData; // 过滤后的数据
    private static JTable table;
    private static DefaultTableModel tableModel;
    private static JTextField pageNumberField;
    private static JTextField searchField;
    private static JComboBox<String> searchComboBox;
    private static JFrame frame;

    public static void main(String[] args) {
        StudentGrade infoPage = new StudentGrade();
        SwingUtilities.invokeLater(infoPage::createAndShowGUIGrade);
    }

    public void createAndShowGUIGrade() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        // 创建 ImageIcon 并调整大小
        ImageIcon icon = new ImageIcon("src/imgs/logoDNDX.png");
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(260, 86, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImg);

        JLabel imageLabel = new JLabel(icon);
        JLabel textLabel = new JLabel("        学生成绩信息");

        Font font = new Font("Serif", Font.BOLD, 27); // 字体名称，样式和大小
        textLabel.setForeground(new Color(3, 81, 32));
        textLabel.setFont(font);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(imageLabel);
        headerPanel.add(textLabel);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        tableModel = new DefaultTableModel(new Object[]{"课程编号", "课程名称", "学分", "最终成绩", "详情"}, 0);
        table = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) {
                    return JButton.class; // 最后一列的类是 JButton
                }
                return super.getColumnClass(columnIndex);
            }
        };

        table.setRowHeight(40);
        int tableHeight = ROWS_PER_PAGE * table.getRowHeight();
        table.setPreferredScrollableViewportSize(new Dimension(1000, tableHeight));
        table.setFillsViewportHeight(true);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), table));

        data = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            // 添加课程编号、课程名称、学分和最终成绩的示例数据
            data.add(new Object[]{i, "课程 " + i, i + 1, i + 2, "Details"});
        }
        filteredData = new ArrayList<>(data); // 初始化为全数据

        updateTableData();

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JPanel paginationPanel = new JPanel();
        JButton previousButton = new JButton("上一页");
        JButton nextButton = new JButton("下一页");
        JButton goButton = new JButton("跳转");
        pageNumberField = new JTextField(5);
        pageNumberField.setText(String.valueOf(currentPage + 1));

        Dimension buttonSize = new Dimension(60, 25); // 设置按钮的大小
        Dimension buttonSize2 = new Dimension(80, 25); // 设置按钮的大小
        previousButton.setPreferredSize(buttonSize2);
        nextButton.setPreferredSize(buttonSize2);
        goButton.setPreferredSize(buttonSize);

        previousButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                updateTableData();
                pageNumberField.setText(String.valueOf(currentPage + 1));
            }
        });

        nextButton.addActionListener(e -> {
            if ((currentPage + 1) * ROWS_PER_PAGE < data.size()) {
                currentPage++;
                updateTableData();
                pageNumberField.setText(String.valueOf(currentPage + 1));
            }
        });

        goButton.addActionListener(e -> {
            try {
                int page = Integer.parseInt(pageNumberField.getText()) - 1;
                if (page >= 0 && page < (data.size() + ROWS_PER_PAGE - 1) / ROWS_PER_PAGE) {
                    currentPage = page;
                    updateTableData();
                } else {
                    JOptionPane.showMessageDialog(null, "无效的页码", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "请输入有效的页码", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        paginationPanel.add(previousButton);
        paginationPanel.add(nextButton);
        paginationPanel.add(new JLabel("页码:"));
        paginationPanel.add(pageNumberField);
        paginationPanel.add(goButton);

        JPanel footerPanel = new JPanel();
        JButton backButton = new JButton("返回");
        backButton.setPreferredSize(new Dimension(100, 50));

        footerPanel.setLayout(new GridBagLayout());
        GridBagConstraints footerConstraints = new GridBagConstraints();
        footerConstraints.gridx = 0;
        footerConstraints.gridy = 0;
        footerConstraints.weightx = 1.0;
        footerConstraints.fill = GridBagConstraints.HORIZONTAL;

        footerPanel.add(backButton, footerConstraints);

        backButton.addActionListener(e -> {
            new StudentMainPage().setVisible(true);
            frame.dispose();
        });

        JScrollPane scrollPane = new JScrollPane(table);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(headerPanel, gbc);

        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollPane, gbc);

        gbc.gridy = 3;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(paginationPanel, gbc);

        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(footerPanel, gbc);

        // 创建搜索面板
        JPanel searchPanel = new JPanel();
        searchComboBox = new JComboBox<>(new String[]{"课程编号"});
        searchField = new JTextField(15);
        JButton searchButton = new JButton("搜索");

        searchButton.addActionListener(e -> performSearch());

        searchPanel.add(new JLabel("搜索方式:"));
        searchPanel.add(searchComboBox);
        searchPanel.add(new JLabel("关键词:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        gbc.gridy = 1;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(searchPanel, gbc);

        frame = new JFrame("学生成绩信息");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(mainPanel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private static void updateTableData() {
        tableModel.setRowCount(0);
        int start = currentPage * ROWS_PER_PAGE;
        int end = Math.min(start + ROWS_PER_PAGE, filteredData.size());

        for (int i = start; i < end; i++) {
            tableModel.addRow(filteredData.get(i));
        }

        int tableHeight = ROWS_PER_PAGE * table.getRowHeight();
        table.setPreferredScrollableViewportSize(new Dimension(1000, tableHeight));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.revalidate();
    }

    private static void performSearch() {
        String searchType = (String) searchComboBox.getSelectedItem();
        String searchTerm = searchField.getText().trim();
        if (searchType != null && !searchTerm.isEmpty()) {
            try {
                if (searchType.equals("课程编号")) {
                    int courseNumber = Integer.parseInt(searchTerm);
                    filteredData = data.stream()
                            .filter(row -> row[0].equals(courseNumber))
                            .collect(Collectors.toList());
                }
                currentPage = 0;
                updateTableData();
                pageNumberField.setText(String.valueOf(currentPage + 1));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "课程编号必须是数字", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            filteredData = new ArrayList<>(data);
            currentPage = 0;
            updateTableData();
            pageNumberField.setText(String.valueOf(currentPage + 1));
        }
    }

    // 自定义按钮渲染器
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(Color.WHITE);
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(Color.WHITE);
                setForeground(table.getForeground());
            }

            if (column == 4) {
                setText("详情");
            } else {
                setText("");
            }

            return this;
        }
    }

    // 自定义按钮编辑器
    static class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private JTable table;

        public ButtonEditor(JCheckBox checkBox, JTable table) {
            super(checkBox);
            this.table = table;
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(Color.WHITE);

            button.addActionListener(e -> {
                fireEditingStopped();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                if (column == 4) {
                    // 详情按钮操作
                    Object[] rowData = getRowData(row);
                    JOptionPane.showMessageDialog(button, "详细信息:\n课程编号: " + rowData[0] + "\n课程名称: " + rowData[1] + "\n学分: " + rowData[2] + "\n最终成绩: " + rowData[3]);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(Color.WHITE);
            }

            if (column == 4) {
                label = "详情";
            } else {
                label = "";
            }

            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Do something when button is pressed, if needed
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }

        private Object[] getRowData(int row) {
            return new Object[]{
                    table.getValueAt(row, 0),
                    table.getValueAt(row, 1),
                    table.getValueAt(row, 2),
                    table.getValueAt(row, 3)
            };
        }
    }
}
