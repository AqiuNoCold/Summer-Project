package Pages.Pages.ECard;

import Pages.MainApp;
import vCampus.Entity.ECard.ECard;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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

public class chargePage extends JFrame {

    private ECard ecard;
    private JButton chargeButton;
    private JTextField amountField;
    private JLabel messageLabel;
    private JButton backButton = new JButton("返回");

    public chargePage(ECard newecard) {
        ecard = newecard;

        ObjectInputStream in = MainApp.getIn();
        ObjectOutputStream out = MainApp.getOut();
        setTitle("请输入充值金额");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);
        // 创建文本字段
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        amountField = new JTextField(8);
        amountField.setDocument(new LimitedLengthDocument(8));
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

        gbc.gridy=0;
        gbc.weighty = 0.5;
        panel.add(new JLabel(""),gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.25;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(amountField, gbc);


        gbc.gridy = 2;
        messageLabel = new JLabel("");
        panel.add(messageLabel, gbc);

        gbc.gridy=3;
        gbc.weighty = 0.5;
        panel.add(new JLabel(""),gbc);


        chargeButton = new JButton("充值");
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(chargeButton, BorderLayout.SOUTH);
        buttonPanel.add(backButton, BorderLayout.SOUTH);

        getContentPane().add(panel);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        chargeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (amountField.getText().equals("")) {
                    messageLabel.setText("充值金额不能为空");
                } else if (Float.parseFloat(amountField.getText()) == 0f) {
                    messageLabel.setText("充值金额不能为零");
                } else {
                    try {
                        dispose();
                        out.writeObject("3");
                        out.writeObject("Charge");
                        out.writeObject(ecard);
                        out.writeObject(Float.parseFloat(amountField.getText()));
                        out.flush();
                        boolean response=(boolean)in.readObject();
                        messageWindow();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.out.println(ex.getMessage());
                    }
                }
            }
        });
    }

    private static void messageWindow()
    {
        JFrame frame = new JFrame("支付结果");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        JLabel label = new JLabel("支付成功", SwingConstants.CENTER);
        // 设置字体样式和大小
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.setVisible(true);
        JButton backButton = new JButton("返回");
        frame.add(backButton, BorderLayout.SOUTH);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }
}
