package Pages.Pages.ECard;

import Pages.MainApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PayPage extends JDialog {
    private String id;
    private final String ecardNumber;
    private final float amount;
    private final String reason;

    private final JTextField passwordField;
    private final JLabel messageLabel=new JLabel("");


    public PayPage(String newid,String newecard,float newamount, String newreason) {
        setModal(true);
        id=newid;
        ecardNumber = newecard;
        amount = newamount;
        reason = newreason;
        ObjectInputStream in = MainApp.getIn();
        ObjectOutputStream out = MainApp.getOut();
        setTitle("等待支付中...");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1));

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

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout());
        labelPanel.add(new JLabel("卡号："+ecardNumber));
        labelPanel.add(new JLabel("金额："+amount));
        labelPanel.add(new JLabel("原因："+reason));

        JButton compareButton = new JButton("确定");
        setmessageLabel();

        add(labelPanel);
        add(passwordField);
        add(messageLabel);
        add(compareButton);
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
                                out.writeObject("3");
                                out.writeObject("Pay");
                                out.writeObject(id);
                                out.writeObject(ecardNumber);
                                out.writeObject(amount);
                                out.writeObject(reason);
                                out.flush();
                                int payresult=(int)in.readObject();
                                messageWindow(payresult);
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

    private void messageWindow(int result) {
        JDialog frame = new JDialog(this, "支付结果", true);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        frame.setSize(300, 150);
        JLabel label = new JLabel("", SwingConstants.CENTER);
        label.setFont(new Font("微软雅黑", Font.BOLD, 20));
        if (result==0)
            label.setText("支付成功！");
        else if(result==1)
            label.setText("余额不足，支付失败！");
        else if(result==2)
            label.setText("卡片被冻结，支付失败！");
        frame.setLocationRelativeTo(null);
        // 设置字体样式和大小
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
