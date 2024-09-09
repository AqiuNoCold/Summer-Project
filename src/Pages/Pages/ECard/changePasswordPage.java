package Pages.Pages.ECard;

import Pages.MainApp;
import vCampus.Entity.ECard.ECard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;

public class changePasswordPage extends JDialog {

    private final String ecardNumber;
    private final JButton compareButton;
    private final JTextField passwordField;
    private final JLabel messageLabel;
    private int status;

    public changePasswordPage(String newecard) {
        setModal(true);
        ecardNumber = newecard;
        ObjectInputStream in = MainApp.getIn();
        ObjectOutputStream out = MainApp.getOut();
        setTitle("请输入旧的支付密码");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(200, 150);
        setLocationRelativeTo(null);

        passwordField = new JTextField(8);
        passwordField.setDocument(new LimitedLengthDocument(6));
        // 添加键盘监听器，限制输入为数字
        passwordField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                // 只允许输入数字（0-9）和控制字符（如退格）
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume(); // 取消事件，防止输入
                }
            }
        });


        setLayout(new GridLayout(3,1));


        add(passwordField);

        messageLabel = new JLabel("");
        add(messageLabel);

        compareButton = new JButton("确定");
        add(compareButton, BorderLayout.SOUTH);

        compareButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                setmessageLabel();
                if (passwordField.getText().isEmpty()) {
                    messageLabel.setText("输入不能为空");
                    messageLabel.setForeground(Color.RED);
                    setAmountField();
                } else if (passwordField.getText().length() < 6) {
                    messageLabel.setText("输入不少于6位数");
                    messageLabel.setForeground(Color.RED);
                    setAmountField();
                } else {
                    try {
                        if (status == 0) {
                            out.writeObject("3");
                            out.writeObject("comparePassword");
                            out.writeObject(ecardNumber);
                            out.writeObject(Integer.parseInt(passwordField.getText()));
                            out.flush();
                            boolean result=(boolean) in.readObject();
                            if (!result) {
                                messageLabel.setForeground(Color.RED);
                                messageLabel.setText("密码不正确");
                                setAmountField();
                            } else {
                                setTitle("请输入新的支付密码");
                                setAmountField();
                                setmessageLabel();
                                status = 1;
                            }
                        } else if (status == 1) {
                            out.writeObject("3");
                            out.writeObject("newPassword");
                            Integer newPassword = Integer.parseInt(passwordField.getText());
                            out.writeObject(ecardNumber);
                            out.writeObject(newPassword);
                            out.flush();
                            messageWindow();
                            dispose();
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    public void setAmountField() {
        passwordField.setText("");
    }

    public void setmessageLabel() {
        messageLabel.setText("");
    }

    public void setStatus() {
        status = 0;
    }

    private void messageWindow() {
        JFrame frame = new JFrame("密码修改结果");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 150);

        JLabel label = new JLabel("密码修改成功！", SwingConstants.CENTER);
        label.setFont(new Font("微软雅黑", Font.BOLD, 20));
        label.setForeground(Color.BLACK);

        frame.setLocationRelativeTo(null);
        // 设置字体样式和大小
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
