package Pages.Pages;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherInfoMS {

    private static final int ROWS_PER_PAGE = 5; // 每页显示的行数
    private static int currentPage = 0;
    private static List<Object[]> data; // 表格数据
    private static JTable table;
    private static DefaultTableModel tableModel;
    private static JTextField pageNumberField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TeacherInfoMS::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        // 创建主面板，使用 GridBagLayout 布局
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // 设置内边距

        // 创建包含图片和文字的面板
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        JLabel imageLabel = new JLabel(new ImageIcon("src/imgs/logoDNDX.png")); // 替换为你的图片路径
        JLabel textLabel = new JLabel("            学生学籍管理");

        // 设置字体大小为 24
        Font font = new Font("Serif", Font.BOLD, 24); // 字体名称，样式和大小
        // 设置字体颜色为蓝色
        textLabel.setForeground(new Color(3, 81, 32));
        textLabel.setFont(font);

        // 设置文字水平居中
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(imageLabel);
        headerPanel.add(textLabel);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 创建表格模型，添加两个额外的按钮列
        tableModel = new DefaultTableModel(new Object[]{"CardId", "Name", "Gender", "详情", "修改", "删除"}, 0);
        table = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex >= 3) {
                    return JButton.class; // 最后三列的类是 JButton
                }
                return super.getColumnClass(columnIndex);
            }
        };

        // 设置每一行的高度
        table.setRowHeight(40); // 设置每一行的高度为40像素

        // 设置表格的首选视口大小，以调整表格的高度
        int tableHeight = ROWS_PER_PAGE * table.getRowHeight(); // 根据行高和每页的行数计算表格高度
        table.setPreferredScrollableViewportSize(new Dimension(900, tableHeight)); // 宽度900，高度为计算得到的值

        // 设置表格在视口中填充满整个可视区域
        table.setFillsViewportHeight(true);

        // 为按钮列设置渲染器和编辑器
        for (int i = 3; i < tableModel.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new ButtonRenderer());
            table.getColumnModel().getColumn(i).setCellEditor(new ButtonEditor(new JCheckBox()));
        }

        // 初始化数据
        data = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            data.add(new Object[]{i, "Person " + i, 20 + i, "Action 1", "Action 2", "Action 3"});
        }

        // 填充第一页数据
        updateTableData();

        // 自动调整列宽以填充视口
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // 创建分页控件
        JPanel paginationPanel = new JPanel();
        JButton previousButton = new JButton("Previous");
        JButton nextButton = new JButton("Next");
        JButton goButton = new JButton("Go");
        pageNumberField = new JTextField(5); // 输入框用于输入页码

        // 设置按钮的大小相同
        Dimension buttonSize = new Dimension(100, 30); // 设置按钮的大小
        previousButton.setPreferredSize(buttonSize);
        nextButton.setPreferredSize(buttonSize);
        goButton.setPreferredSize(buttonSize);

        // 添加按钮动作监听器
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
                    JOptionPane.showMessageDialog(null, "Invalid page number", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        paginationPanel.add(previousButton);
        paginationPanel.add(nextButton);
        paginationPanel.add(new JLabel("Page:"));
        paginationPanel.add(pageNumberField);
        paginationPanel.add(goButton);

        // 将表格放入 JScrollPane 中
        JScrollPane scrollPane = new JScrollPane(table);

        // 设置 GridBagConstraints 来使表格和标题居中
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(headerPanel, gbc); // 添加标题面板

        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollPane, gbc); // 添加表格

        // 将分页控件放在 GridBagLayout 的底部
        gbc.gridy = 2;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(paginationPanel, gbc);

        // 创建 JFrame 并设置其属性
        JFrame frame = new JFrame("Paginated Table Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600); // 调整窗口大小以适应新的表格大小
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static void updateTableData() {
        // 清空表格模型
        tableModel.setRowCount(0);

        // 计算分页数据
        int start = currentPage * ROWS_PER_PAGE;
        int end = Math.min(start + ROWS_PER_PAGE, data.size());

        // 将数据添加到表格模型中
        for (int i = start; i < end; i++) {
            tableModel.addRow(data.get(i));
        }

        // 更新表格的视口大小以适应新的行数
        int tableHeight = ROWS_PER_PAGE * table.getRowHeight(); // 根据行高和每页的行数计算表格高度
        table.setPreferredScrollableViewportSize(new Dimension(900, tableHeight));
    }

    // 自定义按钮渲染器
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(Color.WHITE); // 设置按钮背景颜色为白色
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(Color.WHITE); // 确保背景颜色是白色
                setForeground(table.getForeground());
            }

            // 设置按钮文本
            switch (column) {
                case 3: setText("详情"); break;
                case 4: setText("修改"); break;
                case 5: setText("删除"); break;
                default: setText("");
            }

            return this;
        }
    }

    // 自定义按钮编辑器
    static class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(Color.WHITE); // 设置按钮背景颜色为白色

            button.addActionListener(e -> {
                fireEditingStopped(); // Stops editing and notifies listeners
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                if (column == 3) {
                    // 详情按钮操作
                    JOptionPane.showMessageDialog(button, "详情按钮点击，行 " + row);
                } else if (column == 4) {
                    // 修改按钮操作
                    JOptionPane.showMessageDialog(button, "修改按钮点击，行 " + row);
                } else if (column == 5) {
                    // 删除按钮操作
                    JOptionPane.showMessageDialog(button, "删除按钮点击，行 " + row);
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
                button.setBackground(Color.WHITE); // 确保背景颜色是白色
            }

            // 设置按钮文本
            switch (column) {
                case 3: label = "详情"; break;
                case 4: label = "修改"; break;
                case 5: label = "删除"; break;
                default: label = "";
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
    }
}
