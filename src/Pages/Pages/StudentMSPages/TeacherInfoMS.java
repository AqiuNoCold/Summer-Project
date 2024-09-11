package Pages.Pages.StudentMSPages;

import Pages.MainApp;
import vCampus.Entity.Grade;
import vCampus.Entity.Student;
import vCampus.Entity.User;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TeacherInfoMS {

    private static final int ROWS_PER_PAGE = 9; // 每页显示的行数
    private static int currentPage = 0;
    private static List<Object[]> data; // 表格数据
    private static List<Object[]> filteredData; // 过滤后的数据
    private static JTable table;
    private static DefaultTableModel tableModel;
    private static JTextField pageNumberField;
    private static JTextField searchField; // 搜索框
    private static JFrame frame;


    public static void main(String[] args) {
        TeacherInfoMS infoPage = new TeacherInfoMS();
        SwingUtilities.invokeLater(infoPage::createAndShowGUIInfo);
    }

    public void createAndShowGUIInfo() {
        // 创建主面板，使用 GridBagLayout 布局
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(10, 10, 10, 10); // 设置内边距

        // 创建包含图片和文字的面板
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        ImageIcon icon = new ImageIcon(getClass().getResource("/imgs/logoDNDX.png"));
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(260, 86, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImg);

        JLabel imageLabel = new JLabel(icon);
        JLabel textLabel = new JLabel("        学生学籍管理");

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

        // 创建表格模型，添加三个额外的按钮列
        tableModel = new DefaultTableModel(new Object[]{"一卡通号", "姓名", "性别", "详情", "修改", "删除"}, 0);
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
        // 设置单元格内容水平居中
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);



        // 应用渲染器到所有列
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        // 为按钮列设置渲染器和编辑器
        for (int i = 3; i < tableModel.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new ButtonRenderer());
            table.getColumnModel().getColumn(i).setCellEditor(new ButtonEditor(new JCheckBox()));
        }

        // 初始化数据
        User user = MainApp.getCurrentUser();
        ObjectInputStream in = MainApp.getIn();
        ObjectOutputStream out = MainApp.getOut();
        List<Student> students = new ArrayList<>();
        try {
            out.writeObject("6");
            out.writeObject("teacherFindAllInfo");
            out.flush();
            students = (List<Student>) in.readObject();
            System.out.println(students.size());
            for (Student student : students) {
                System.out.println(student);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        data = new ArrayList<>();
        for (Student student : students) {
            data.add(new Object[]{
                    student.getCardId(),
                    student.getName(),
                    student.getGender(),
                    "Action 1",
                    "Action 2",
                    "Action 3",
                    student.getBirth(),
                    student.getCollege(),
                    student.getGrade(),
                    student.getMajor(),
                    student.getEmail(),
                    student.getStage(),
                    student.getHonor(),
                    student.getPunish(),
                    student.getStuCode()

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

        // 填充第一页数据
        updateTableData();

        // 自动调整列宽以填充视口
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // 创建分页控件
        JPanel paginationPanel = new JPanel();
        JButton previousButton = new JButton("上一页");
        JButton nextButton = new JButton("下一页");
        JButton goButton = new JButton("跳转");
        JButton searchButton = new JButton("搜索");
        pageNumberField = new JTextField(3); // 输入框用于输入页码
        pageNumberField.setText(String.valueOf(currentPage + 1));
        searchField = new JTextField(8); // 搜索框
        // 创建“新建”按钮
        JButton newButton = new JButton("新建");
        // 设置按钮的大小
        newButton.setPreferredSize(new Dimension(80, 25));

        // 设置按钮的大小相同
        Dimension buttonSize = new Dimension(60, 25); // 设置按钮的大小
        Dimension buttonSize2 = new Dimension(80, 25); // 设置按钮的大小
        previousButton.setPreferredSize(buttonSize2);
        nextButton.setPreferredSize(buttonSize2);
        goButton.setPreferredSize(buttonSize);
        searchButton.setPreferredSize(buttonSize);

        // 添加按钮动作监听器
        previousButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                updateTableData();
                pageNumberField.setText(String.valueOf(currentPage + 1));
            }
        });

        nextButton.addActionListener(e -> {
            if ((currentPage + 1) * ROWS_PER_PAGE < filteredData.size()) {
                currentPage++;
                updateTableData();
                pageNumberField.setText(String.valueOf(currentPage + 1));
            }
        });

        goButton.addActionListener(e -> {
            try {
                int page = Integer.parseInt(pageNumberField.getText()) - 1;
                if (page >= 0 && page < (filteredData.size() + ROWS_PER_PAGE - 1) / ROWS_PER_PAGE) {
                    currentPage = page;
                    updateTableData();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid page number", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        searchButton.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            if (!searchText.isEmpty()) {
                try {
                    filteredData = data.stream()
                            .filter(row -> row[0].equals(searchText))
                            .collect(Collectors.toList());
                    currentPage = 0; // 重置到第一页
                    updateTableData();
                    pageNumberField.setText(null);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                filteredData = new ArrayList<>(data); // 如果搜索框为空，则显示所有数据
                updateTableData();
                pageNumberField.setText(String.valueOf(currentPage + 1));
            }
        });
        // 添加“新建”按钮的动作监听器，调用 NewStudent 方法
        newButton.addActionListener(e -> NewStudent());

        paginationPanel.add(newButton); // 添加“新建”按钮
        paginationPanel.add(previousButton);
        paginationPanel.add(nextButton);
        paginationPanel.add(new JLabel("Page:"));
        paginationPanel.add(pageNumberField);
        paginationPanel.add(goButton);
        paginationPanel.add(new JLabel("一卡通号："));
        paginationPanel.add(searchField);
        paginationPanel.add(searchButton);

        // 创建底部面板，包含返回按钮
        JPanel footerPanel = new JPanel();
        JButton backButton = new JButton("返回");
        backButton.setPreferredSize(new Dimension(100, 50)); // 设置按钮的高度为50，宽度不固定

        // 使用 GridBagConstraints 来确保按钮横跨整个宽度
        footerPanel.setLayout(new GridBagLayout());
        GridBagConstraints footerConstraints = new GridBagConstraints();
        footerConstraints.gridx = 0;
        footerConstraints.gridy = 0;
        footerConstraints.weightx = 1.0; // 使按钮横跨整个宽度
        footerConstraints.fill = GridBagConstraints.HORIZONTAL; // 按钮填充整个宽度

        footerPanel.add(backButton, footerConstraints);

        // 添加返回按钮的动作监听器
        backButton.addActionListener(e -> {
            new TeacherMainPage().setVisible(true); // 打开TeacherMainPage
            frame.dispose(); // 关闭当前页面
        });

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

        // 将底部面板放在 GridBagLayout 的最底部
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(footerPanel, gbc);

        // 创建 JFrame 并设置其属性
        frame = new JFrame("学生信息管理");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // 调整窗口大小以适应新的表格和底部面板
        frame.add(mainPanel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
    private void NewStudent() {

            // 弹出对话框以输入新数据
            JPanel inputPanel = new JPanel(new GridLayout(12, 2));
            JTextField cardIdField = new JTextField();
            JTextField nameField = new JTextField();
            JTextField genderField = new JTextField();
            JTextField birthField = new JTextField();
            JTextField collegeField = new JTextField();
            JTextField gradeField = new JTextField();
            JTextField majorField = new JTextField();
            JTextField emailField = new JTextField();
            JTextField stageField = new JTextField();
            JTextField honorField = new JTextField();
            JTextField punishField = new JTextField();
            JTextField stuCodeField = new JTextField();

            // 添加标签和字段到面板
            inputPanel.add(new JLabel("一卡通号:"));
            inputPanel.add(cardIdField);
            inputPanel.add(new JLabel("姓名:"));
            inputPanel.add(nameField);
            inputPanel.add(new JLabel("性别:"));
            inputPanel.add(genderField);
            inputPanel.add(new JLabel("出生日期:"));
            inputPanel.add(birthField);
            inputPanel.add(new JLabel("所属学院:"));
            inputPanel.add(collegeField);
            inputPanel.add(new JLabel("年级:"));
            inputPanel.add(gradeField);
            inputPanel.add(new JLabel("专业:"));
            inputPanel.add(majorField);
            inputPanel.add(new JLabel("邮箱:"));
            inputPanel.add(emailField);
            inputPanel.add(new JLabel("培养阶段:"));
            inputPanel.add(stageField);
            inputPanel.add(new JLabel("荣誉奖项:"));
            inputPanel.add(honorField);
            inputPanel.add(new JLabel("奖惩信息:"));
            inputPanel.add(punishField);
            inputPanel.add(new JLabel("学籍:"));
            inputPanel.add(stuCodeField);
        while (true) {
            // 初始化字段内容（如果需要填充之前的数据）
            int result = JOptionPane.showConfirmDialog(frame, inputPanel, "新建记录", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String cardId = cardIdField.getText().trim();
                String name = nameField.getText().trim();
                String gender = genderField.getText().trim();
                String birth = birthField.getText().trim();
                String college = collegeField.getText().trim();
                String grade = gradeField.getText().trim();
                String major = majorField.getText().trim();
                String email = emailField.getText().trim();
                String stage = stageField.getText().trim();
                String honor = honorField.getText().trim();
                String punish = punishField.getText().trim();
                String stuCode = stuCodeField.getText().trim();

                // 检查字段的有效性
                if (cardId.isEmpty() || name.isEmpty() || gender.isEmpty() || birth.isEmpty() || college.isEmpty() ||
                        grade.isEmpty() || major.isEmpty() || email.isEmpty() || stage.isEmpty() || honor.isEmpty() ||
                        punish.isEmpty() || stuCode.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "所有字段都必须填写", "错误", JOptionPane.ERROR_MESSAGE);
                    continue; // 继续循环以保持对话框打开
                }

                // 验证出生日期格式
                try {
                    Date.valueOf(birth);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, "出生日期格式错误，应为 YYYY-MM-DD", "错误", JOptionPane.ERROR_MESSAGE);
                    continue; // 继续循环以保持对话框打开
                }

                // 验证邮箱格式
                if (!email.matches("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$")) {
                    JOptionPane.showMessageDialog(frame, "邮箱格式错误", "错误", JOptionPane.ERROR_MESSAGE);
                    continue; // 继续循环以保持对话框打开
                }

                boolean success;
                try {
                    ObjectInputStream in = MainApp.getIn();
                    ObjectOutputStream out = MainApp.getOut();
                    out.writeObject("6");
                    out.writeObject("teacherAddInfo");
                    Student student = new Student();
                    student.setId("12");
                    student.setCardId(cardId);
                    student.setName(name);
                    student.setGender(gender);
                    student.setBirth(Date.valueOf(birth));
                    student.setCollege(college);
                    student.setGrade(grade);
                    student.setMajor(major);
                    student.setEmail(email);
                    student.setStage(stage);
                    student.setHonor(honor);
                    student.setPunish(punish);
                    student.setStuCode(stuCode);
                    out.writeObject(student);
                    out.flush();
                    success = (boolean) in.readObject();
                    System.out.println(success);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                if (success) {
                    // 添加新数据到 data 和 filteredData 中
                    Object[] newRow1 = {cardId, name, gender, "Action 1", "Action 2", "Action 3", birth, college, grade, major, email, stage, honor, punish, stuCode};
                    data.add(newRow1);
                    Object[] newRow2 = {cardId, name, gender, "Action 1", "Action 2", "Action 3"};
                    filteredData.add(newRow2); // 确保 filteredData 包含新数据

                    // 更新表格数据
                    updateTableData();

                    JOptionPane.showMessageDialog(frame, "新建成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                    break; // 退出循环，完成录入
                } else {
                    JOptionPane.showMessageDialog(frame, "新建失败", "错误", JOptionPane.ERROR_MESSAGE);
                    continue; // 继续循环以保持对话框打开
                }
            } else {
                // 用户点击了取消
                break;
            }
        }
    }



    private static void updateTableData() {
        // 清空表格模型
        tableModel.setRowCount(0);

        // 计算分页数据
        int start = currentPage * ROWS_PER_PAGE;
        int end = Math.min(start + ROWS_PER_PAGE, filteredData.size());

        // 将数据添加到表格模型中
        for (int i = start; i < end; i++) {
            tableModel.addRow(filteredData.get(i));
        }

        // 更新表格的视口大小以适应新的行数
        int tableHeight = ROWS_PER_PAGE * table.getRowHeight(); // 根据行高和每页的行数计算表格高度
        table.setPreferredScrollableViewportSize(new Dimension(900, tableHeight)); // 高度调整
        table.revalidate(); // 重新验证表格
    }

    // 自定义按钮渲染器
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(Color.WHITE); // 设置按钮背景颜色为白色
            setHorizontalAlignment(SwingConstants.CENTER); // 设置水平对齐方式为居中
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(Color.WHITE); // 确保背景颜色是白色
                setForeground(table.getForeground());
                setHorizontalAlignment(SwingConstants.CENTER);
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
                    int dataIndex = getDataIndex(row);
                    if (dataIndex != -1 && dataIndex < data.size()) {
                        Object[] rowData = data.get(dataIndex);
                        JOptionPane.showMessageDialog(button,
                                "详细信息:\n一卡通号: " + rowData[0] +
                                "\n姓名: "+ rowData[1] +
                                  "\n性别: "+ rowData[2] +
                                "\n出生日期: " + rowData[6] +
                                "\n所属学院: " + rowData[7] + "\n年级: " + rowData[8] +
                                "\n专业: " + rowData[9] + "\n邮箱: " + rowData[10] + "\n培养阶段: " +
                                rowData[11]+"\n荣誉情况: " + rowData[12]
                                +"\n奖惩信息: " + rowData[13] + "\n学籍号: " + rowData[14]
                        );
                    } else {
                        JOptionPane.showMessageDialog(button, "未找到详细信息", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (column == 4) {
                    // 修改按钮操作
                    int dataIndex = getDataIndex(row);
                    if (dataIndex != -1) {
                        Object[] rowData = data.get(dataIndex);

                        JTextField nameField = new JTextField(rowData[1].toString());
                        JTextField genderField = new JTextField(rowData[2].toString());
                        JTextField birthField = new JTextField(rowData[6].toString());
                        JTextField collegeField = new JTextField(rowData[7].toString());
                        JTextField gradeField = new JTextField(rowData[8].toString());
                        JTextField majorField = new JTextField(rowData[9].toString());
                        JTextField emailField = new JTextField(rowData[10].toString());
                        JTextField stageField = new JTextField(rowData[11].toString());
                        JTextField honorField = new JTextField(rowData[12].toString());
                        JTextField punishField = new JTextField(rowData[13].toString());
                        JTextField stuCodeField = new JTextField(rowData[14].toString());

                        JPanel panel = new JPanel(new GridLayout(12, 2));
                        panel.add(new JLabel("姓名:"));
                        panel.add(nameField);
                        panel.add(new JLabel("性别:"));
                        panel.add(genderField);
                        panel.add(new JLabel("出生日期:"));
                        panel.add(birthField);
                        panel.add(new JLabel("所属学院:"));
                        panel.add(collegeField);
                        panel.add(new JLabel("年级:"));
                        panel.add(gradeField);
                        panel.add(new JLabel("专业:"));
                        panel.add(majorField);
                        panel.add(new JLabel("邮箱:"));
                        panel.add(emailField);
                        panel.add(new JLabel("培养阶段:"));
                        panel.add(stageField);
                        panel.add(new JLabel("荣誉奖项:"));
                        panel.add(honorField);
                        panel.add(new JLabel("奖惩信息:"));
                        panel.add(punishField);
                        panel.add(new JLabel("学籍号:"));
                        panel.add(stuCodeField);

                        int result = JOptionPane.showConfirmDialog(button, panel, "修改成绩", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                        boolean success = false;
                        if (result == JOptionPane.OK_OPTION) {
                            ObjectInputStream in = MainApp.getIn();
                            ObjectOutputStream out = MainApp.getOut();
                            try {
                                out.writeObject("6");
                                out.writeObject("teaccherModifyInfo");
                                Student student = new Student();
                                student.setCardId(String.valueOf(rowData[0]));
                                student.setName(nameField.getText().trim());
                                student.setGender(genderField.getText().trim());
                                student.setBirth(Date.valueOf(birthField.getText().trim()));
                                student.setCollege(collegeField.getText().trim());
                                student.setGrade(gradeField.getText().trim());
                                student.setMajor(majorField.getText().trim());
                                student.setEmail(emailField.getText().trim());
                                student.setStage(stageField.getText().trim());
                                student.setHonor(honorField.getText().trim());
                                student.setPunish(punishField.getText().trim());
                                student.setStuCode(stuCodeField.getText().trim());
                                out.writeObject(student);
                                out.flush();
                                success = (boolean) in.readObject();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            } catch (ClassNotFoundException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        if (success && result == JOptionPane.OK_OPTION) {
                            try {
                                // 更新表格数据
                                rowData[1] = nameField.getText().trim();
                                rowData[2] = genderField.getText().trim();
                                rowData[6] = birthField.getText().trim();
                                rowData[7] = collegeField.getText().trim();
                                rowData[8] = gradeField.getText().trim();
                                rowData[9] = majorField.getText().trim();
                                rowData[10] = emailField.getText().trim();
                                rowData[11] = stageField.getText().trim();
                                rowData[12] = honorField.getText().trim();
                                rowData[13] = punishField.getText().trim();
                                rowData[14] = stuCodeField.getText().trim();
                                data.set(dataIndex, rowData);
                                updateFilteredData();
                                updateTableData();
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(button, "请输入有效的数字", "错误", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        if (success) {
                            JOptionPane.showMessageDialog(button, "修改成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                        } else {
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
                            out.writeObject("teacherDeleteInfo");
                            out.writeObject(rowData[0]);
                            out.flush();
                            success = (boolean) in.readObject();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                        if (success) {
                            JOptionPane.showMessageDialog(button, "删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                        } else {
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
        private int getDataIndex(int filteredRowIndex) {
            // 查找 filteredData 在原始数据中的索引
            Object[] filteredRow = filteredData.get(filteredRowIndex);
            for (int i = 0; i < data.size(); i++) {
                Object[] row = data.get(i);
                if (java.util.Arrays.equals(filteredRow, java.util.Arrays.copyOfRange(row, 0, 6))) {
                    return i+currentPage*ROWS_PER_PAGE;
                }
            }
            return -1; // 如果没有找到匹配项，则返回 -1
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

