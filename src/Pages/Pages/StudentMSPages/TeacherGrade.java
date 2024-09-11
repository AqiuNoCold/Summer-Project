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
//修改为滚动条
public class TeacherGrade {
    private static List<Object[]> data; // 表格数据
    private static List<Object[]> filteredData; // 过滤后的数据
    private static JTable table;
    private static DefaultTableModel tableModel;
    private static JTextField searchField;
    private static JComboBox<String> searchComboBox;
    private static JFrame frame;

    public static void main(String[] args) {
        TeacherGrade infoPage = new TeacherGrade();
        SwingUtilities.invokeLater(infoPage::createAndShowGUIGrade);
    }

    public void createAndShowGUIGrade() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        ImageIcon icon = new ImageIcon(getClass().getResource("/imgs/logoDNDX.png"));
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(260, 86, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImg);

        JLabel imageLabel = new JLabel(icon);
        JLabel textLabel = new JLabel("        学生成绩管理");

        Font font = new Font("Serif", Font.BOLD, 27);
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
                    return JButton.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };

        table.setRowHeight(40);
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

        filteredData = new ArrayList<>(data);
        updateTableData();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(900, 600));

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

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(headerPanel, gbc);

        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollPane, gbc);

        gbc.gridy = 2;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(searchPanel, gbc);

        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(footerPanel, gbc);

        frame = new JFrame("学生成绩管理");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(mainPanel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private static void updateTableData() {
        tableModel.setRowCount(0);
        for (Object[] row : filteredData) {
            tableModel.addRow(row);
        }
        table.revalidate();
    }

    private static void performSearch() {
        String searchType = (String) searchComboBox.getSelectedItem();
        String searchTerm = searchField.getText().trim();
        if (searchType != null && !searchTerm.isEmpty()) {
            try {
                if (searchType.equals("一卡通号")) {
                    filteredData = data.stream()
                            .filter(row -> row[1].equals(searchTerm))
                            .collect(Collectors.toList());
                } else if (searchType.equals("课程编号")) {
                    filteredData = data.stream()
                            .filter(row -> row[0].equals(searchTerm))
                            .collect(Collectors.toList());
                }
                updateTableData();
                searchField.setText(null);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "请输入正确的搜索条件", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            filteredData = new ArrayList<>(data);
            updateTableData();
            searchField.setText(null);
        }
    }

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
                if (column == 3) {
                    int dataIndex = getDataIndex(row);
                    if (dataIndex != -1 && dataIndex < data.size()) {
                        Object[] rowData = data.get(dataIndex);
                        JOptionPane.showMessageDialog(button, "详细信息:\n课程编号: " + rowData[0] +
                                "\n一卡通号: " + rowData[1] + "\n最终成绩: " + rowData[2]);
                    }
                } else if (column == 4) {
                    // Handle modification action
                } else if (column == 5) {
                    // Handle deletion action
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.table = table;
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
                // Add logic for button action
            }
            isPushed = false;
            return new String(label);
        }

        private int getDataIndex(int row) {
            if (row >= 0 && row < table.getRowCount()) {
                return table.convertRowIndexToModel(row);
            }
            return -1;
        }
    }
}
