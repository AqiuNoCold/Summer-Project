package Pages.Pages.ECard;

import Pages.MainApp;
import vCampus.Entity.ECard.ECard;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class LimitedLengthDocument extends PlainDocument {
    private final int maxLength;

    public LimitedLengthDocument(int maxLength) {
        this.maxLength = maxLength;
    }

    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str == null) {
            return; // 如果输入的是 null，则不做任何处理
        }
        if ((getLength() + str.length()) <= maxLength) {
            super.insertString(offs, str, a); // 允许插入
        } else {
            Toolkit.getDefaultToolkit().beep(); // 超过最大长度，发出提示音
        }
    }
}

public class chargePage extends JDialog {

    private String ecardNumber;
    private JButton chargeButton;
    private JTextField amountField;
    private JLabel messageLabel;

    public void setAmountField() {
        amountField.setText("");
    }

    public void setMessageLabel() {
        messageLabel.setText("");
    }

    public chargePage(String newecard) {
        ecardNumber = newecard;
        setModal(true);

        ObjectInputStream in = MainApp.getIn();
        ObjectOutputStream out = MainApp.getOut();
        setTitle("请输入充值金额");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(200, 150);
        setLocationRelativeTo(null);
        // 创建文本字段
        setLayout(new GridLayout(3,1));

        amountField = new JTextField(8);
        amountField.setDocument(new LimitedLengthDocument(3));
        // 添加键盘监听器，限制输入为数字
        amountField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                // 只允许输入数字（0-9）和控制字符（如退格）
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume(); // 取消事件，防止输入
                }
            }
        });
        messageLabel = new JLabel("");
        chargeButton = new JButton("充值");

        add(amountField);
        add(messageLabel);
        add(chargeButton, BorderLayout.SOUTH);


        chargeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (amountField.getText().equals("")) {
                    messageLabel.setText("充值金额不能为空");
                    messageLabel.setForeground(Color.RED);
                } else if (Float.parseFloat(amountField.getText()) == 0f) {
                    messageLabel.setText("充值金额不能为零");
                    messageLabel.setForeground(Color.RED);
                } else {
                    try {
                        dispose();
                        out.writeObject("3");
                        out.writeObject("Charge");
                        out.writeObject(ecardNumber);
                        out.writeObject(Float.parseFloat(amountField.getText()));
                        out.flush();
                        messageWindow();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.out.println(ex.getMessage());
                    }
                }
            }
        });
    }

    private void messageWindow() {
        JDialog frame = new JDialog(this,"充值结果",true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 150);

        JLabel label = new JLabel("充值成功", SwingConstants.CENTER);
        label.setForeground(Color.BLACK);
        frame.setLocationRelativeTo(null);
        // 设置字体样式和大小
        label.setFont(new Font("微软雅黑", Font.BOLD, 20));
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
