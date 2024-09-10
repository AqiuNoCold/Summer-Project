package Pages.Pages.StudentMSPages;

import Pages.MainApp;
import vCampus.Entity.Grade;
import vCampus.Entity.User;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TeacherGrade {
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
    // 在 StudentGrade 类中
    private static final Map<Integer, Object[]> rowDataMap = new HashMap<>();

    public static void main(String[] args) {
        TeacherGrade infoPage = new TeacherGrade();
        SwingUtilities.invokeLater(infoPage::createAndShowGUIGrade);
    }

    public void createAndShowGUIGrade() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(10, 10, 10, 10); // 设置内边距

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
//        JLabel imageLabel = new JLabel(new ImageIcon("src/imgs/logoDNDX.png")); // 替换为你的图片路径
        // 创建 ImageIcon 并调整大小
        ImageIcon icon = new ImageIcon(getClass().getResource("/imgs/logoDNDX.png"));
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(260, 86, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImg);

        JLabel imageLabel = new JLabel(icon);
        JLabel textLabel = new JLabel("        学生成绩管理");

        Font font = new Font("Serif", Font.BOLD, 27); // 字体名称，样式和大小
        textLabel.setForeground(new Color(3, 81, 32));
        textLabel.setFont(font);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(imageLabel);
        headerPanel.add(textLabel);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        tableModel = new DefaultTableModel(new Object[]{"课程编号", "一卡通号", "最终成绩", "详情", "修改", "删除"}, 0);
        table = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex >= 3) {
                    return JButton.class; // 最后三列的类是 JButton
                }
                return super.getColumnClass(columnIndex);
            }
        };

        table.setRowHeight(40);
        int tableHeight = ROWS_PER_PAGE * table.getRowHeight();
        table.setPreferredScrollableViewportSize(new Dimension(900, tableHeight));
        table.setFillsViewportHeight(true);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        for (int i = 3; i < tableModel.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new ButtonRenderer());
            table.getColumnModel().getColumn(i).setCellEditor(new ButtonEditor(new JCheckBox(), table));
        }

        User user = MainApp.getCurrentUser();
        ObjectInputStream in = MainApp.getIn();
        ObjectOutputStream out = MainApp.getOut();
        List<Grade> grades = new ArrayList<>();
        try {
            out.writeObject("6");
            out.writeObject("teacherFindAllGrade");
            out.flush();
            grades = (List<Grade>) in.readObject();
            System.out.println(grades.size());
            for (Grade grade : grades) {
                System.out.println(grade);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        data = new ArrayList<>();
        for (Grade grade : grades) {
            data.add(new Object[]{
                    grade.getCourseId(),
                    grade.getCardId(),
                    grade.getTotal()==0?"未录入":grade.getTotal(),
                    "Action 1",
                    "Action 2",
                    "Action 3",
                    grade.getPoint(),
                    grade.getUsual(),
                    grade.getMid(),
                    grade.getFinal(),
                    grade.isFirst(),
                    grade.getCourseName(),
                    grade.getTerm()
            });
        }

        // 创建 filteredData 列表，只包含 data 的前6列数据
        filteredData = new ArrayList<>();
        for (Object[] row : data) {
            if (row.length >= 6) {  // 确保每行至少有6列数据
                Object[] filteredRow = new Object[6];
                System.arraycopy(row, 0, filteredRow, 0, 6);
                filteredData.add(filteredRow);
            }
        }

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
            new TeacherMainPage().setVisible(true);
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
        searchComboBox = new JComboBox<>(new String[]{"一卡通号", "课程编号"});
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


        frame = new JFrame("学生成绩管理");
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
        table.setPreferredScrollableViewportSize(new Dimension(900, tableHeight));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.revalidate();
    }

    private static void performSearch() {
        String searchType = (String) searchComboBox.getSelectedItem();
        String searchTerm = searchField.getText().trim();
        if (searchType != null && !searchTerm.isEmpty()) {
            try {if (searchType.equals("一卡通号")) {
                filteredData = data.stream()
                        .filter(row -> row[1].equals(searchTerm))
                        .collect(Collectors.toList());
                currentPage = 0; // 重置到第一页
                updateTableData();
                pageNumberField.setText(null);
                }
                else if (searchType.equals("课程编号")) {
//                System.out.println("search by courseId222");
//                System.out.println(searchTerm);
//                System.out.println(searchType);
                filteredData = data.stream()
                            .filter(row -> row[0].equals(searchTerm))
                            .collect(Collectors.toList());
                    currentPage = 0; // 重置到第一页
                    updateTableData();
                    pageNumberField.setText(null);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "请输入正确的搜索条件", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

        } else {
            // 如果没有搜索条件，显示所有数据
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

            switch (column) {
                case 3: setText("详情"); break;
                case 4: setText("修改"); break;
                case 5: setText("删除"); break;
                default: setText("");
            }

            return this;
        }
    }

    static class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private JTable table; // 添加 JTable 实例变量

        public ButtonEditor(JCheckBox checkBox, JTable table) {
            super(checkBox);
            this.table = table; // 初始化 JTable 实例变量
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(Color.WHITE);

            button.addActionListener(e -> {
                fireEditingStopped();
                int row = table.getSelectedRow(); // 使用传递的 table 实例
                int column = table.getSelectedColumn();
                if (column == 3) {
                    // 详情按钮操作
                    int dataIndex = getDataIndex(row);
                    if (dataIndex != -1 && dataIndex < data.size()) {
                        Object[] rowData = data.get(dataIndex);
                        JOptionPane.showMessageDialog(button, "详细信息:\n课程编号: " + rowData[0] +
                                "\n课程名称: "+ rowData[11] +"\n学期: "+ rowData[12] +
                                "\n一卡通号: " + rowData[1] +"\n最终成绩: " + rowData[2] + "\n学分: " + rowData[6] +
                                "\n平时成绩: " + rowData[7] + "\n期中成绩: " + rowData[8] + "\n期末成绩: " +
                                rowData[9]+"\n是否为首修: " + (rowData[10].equals(false)?"是":"否"));
                    } else {
                        JOptionPane.showMessageDialog(button, "未找到详细信息", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (column == 4) {
                    // 修改按钮操作
                    int dataIndex = getDataIndex(row);
                    if (dataIndex != -1) {
                        Object[] rowData = data.get(dataIndex);
                        JTextField totalField = new JTextField(rowData[2].toString());
                        JTextField usualField = new JTextField(rowData[7].toString());
                        JTextField midField = new JTextField(rowData[8].toString());
                        JTextField finalField = new JTextField(rowData[9].toString());

                        JPanel panel = new JPanel(new GridLayout(4, 2));
                        panel.add(new JLabel("最终成绩:"));
                        panel.add(totalField);
                        panel.add(new JLabel("平时成绩:"));
                        panel.add(usualField);
                        panel.add(new JLabel("期中成绩:"));
                        panel.add(midField);
                        panel.add(new JLabel("期末成绩:"));
                        panel.add(finalField);

                        int result = JOptionPane.showConfirmDialog(button, panel, "修改成绩", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                        boolean success = false;
                        if (result == JOptionPane.OK_OPTION) {
                            ObjectInputStream in = MainApp.getIn();
                            ObjectOutputStream out = MainApp.getOut();
                            try {
                                out.writeObject("6");
                                out.writeObject("teacherModifyGrade");
                                Grade grade = new Grade(
                                        "1",
                                        rowData[1].toString(),
                                        rowData[11].toString(),
                                        rowData[0].toString(),
                                        Double.parseDouble(usualField.getText()),
                                        Double.parseDouble(midField.getText()),
                                        Double.parseDouble(finalField.getText()),
                                        Double.parseDouble(totalField.getText()),
                                        (Double) rowData[6],
                                        rowData[10].equals(true),
                                        rowData[12].toString()
                                );
                                out.writeObject(grade);
                                success = (boolean) in.readObject();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            } catch (ClassNotFoundException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        if (success && result == JOptionPane.OK_OPTION) {
                            try {
                                rowData[2] = Double.parseDouble(totalField.getText());
                                rowData[7] = Double.parseDouble(usualField.getText());
                                rowData[8] = Double.parseDouble(midField.getText());
                                rowData[9] = Double.parseDouble(finalField.getText());
                                data.set(dataIndex, rowData);
                                updateFilteredData();
                                updateTableData();
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(button, "请输入有效的数字", "错误", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        if(!success){
                            JOptionPane.showMessageDialog(button, "修改失败", "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    }else {
                        JOptionPane.showMessageDialog(button, "未找到修改数据", "错误", JOptionPane.ERROR_MESSAGE);
                    }

                } else if (column == 5) {
                    // 删除按钮操作
                    int dataIndex = getDataIndex(row);
                    Object[] rowData = data.get(dataIndex);
                    int result = JOptionPane.showConfirmDialog(button, "确定要删除这一行吗？", "确认", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        ObjectInputStream in = MainApp.getIn();
                        ObjectOutputStream out = MainApp.getOut();
                        boolean success;
                        try {
                            out.writeObject("6");
                            out.writeObject("teacherDeleteGrade");
                            out.writeObject(rowData[1].toString());
                            out.writeObject(rowData[0].toString());
                            out.writeObject(rowData[10]);
                            success = (boolean) in.readObject();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                        if (!success) {
                            JOptionPane.showMessageDialog(button, "删除失败", "错误", JOptionPane.ERROR_MESSAGE);
                        }
                        if (success && dataIndex != -1) {
                            // 从 data 和 filteredData 中删除对应的行
                            data.remove(dataIndex);
                            filteredData.remove(row); // 同步更新 filteredData
                            updateTableData(); // 刷新表格
                        }
                    }
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

        private int getDataIndex(int filteredRowIndex) {
            // 查找 filteredData 在原始数据中的索引
            Object[] filteredRow = filteredData.get(filteredRowIndex);
            for (int i = 0; i < data.size(); i++) {
                Object[] row = data.get(i);
                if (java.util.Arrays.equals(filteredRow, java.util.Arrays.copyOfRange(row, 0, 6))) {
                    return i;
                }
            }
            return -1; // 如果没有找到匹配项，则返回 -1
        }

        private Object[] getRowData(int row) {
            return new Object[]{
                    table.getValueAt(row, 0),
                    table.getValueAt(row, 1),
                    table.getValueAt(row, 2),
                    table.getValueAt(row, 3),
                    table.getValueAt(row, 4),
                    table.getValueAt(row, 5)
            };
        }

        private void updateFilteredData() {
            filteredData = new ArrayList<>();
            for (Object[] row : data) {
                if (row.length >= 6) {
                    Object[] filteredRow = new Object[6];
                    System.arraycopy(row, 0, filteredRow, 0, 6);
                    filteredData.add(filteredRow);
                }
            }
        }

    }

}
