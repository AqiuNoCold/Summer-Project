package Pages.Pages.ECard;

import Pages.MainApp;
import Pages.Pages.NavigationPage;
import vCampus.Entity.User;
import vCampus.Entity.ECard.ECard;


import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ECardPage extends JFrame {
    private chargePage charge;
    private changePasswordPage changePassword;
    private JFrame message;

    private ECard ecard;
    private JButton billsButton;
    private JButton changePasswordButton;
    private JButton chargeButton;
    private JButton lostButton;
    private JButton statusButton;

    public ECardPage(ECard response) {
        ObjectInputStream in=MainApp.getIn();
        ObjectOutputStream out=MainApp.getOut();

        ecard=response;
        setTitle("一卡通页面");
        setSize(800, 600);

        charge=new chargePage(ecard.getCard());
        changePassword=new changePasswordPage(ecard.getCard());
        message=messageWindow();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示窗口
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridLayout(2, 3, 10, 10)); // 2行3列的网格布局
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 增加边距
        mainPanel.setBackground(new Color(245, 245, 245)); // 设置背景颜色
        mainPanel.setBorder(new LineBorder(new Color(144, 238, 144), 5));

        billsButton=createImageButton("账单","src/imgs/ecard/bills.png");
        changePasswordButton=createImageButton("修改密码","src/imgs/ecard/changePassword.png");
        chargeButton=createImageButton("充值","src/imgs/ecard/charge.png");
        lostButton=createImageButton("冻结/解冻","src/imgs/ecard/lost.png");
        statusButton=createImageButton("卡片状态","src/imgs/ecard/status.png");

        mainPanel.add(chargeButton);
        mainPanel.add(billsButton);
        mainPanel.add(changePasswordButton);
        mainPanel.add(lostButton);
        mainPanel.add(statusButton);

        add(mainPanel);

        chargeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(ecard.getLost())
                    message.setVisible(true);
                else{
                try{
                    charge.setAmountField();
                    charge.setMessageLabel();
                    charge.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }}
        });

        billsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    out.writeObject("3");
                    out.writeObject("History");
                    out.writeObject(ecard.getCard());
                    out.flush();
                    String response = (String) in.readObject();
                    new transactionPage(response).setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        changePasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(ecard.getLost())
                    message.setVisible(true);
                else{
                try{
                    changePassword.setAmountField();
                    changePassword.setmessageLabel();
                    changePassword.setStatus();
                    changePassword.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }}
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                navigateBack();
            }
        });
    }


    private JButton createImageButton(String text, String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);

        // 调整图片大小
        Image img = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH); // 调整图片为64x64像素
        icon = new ImageIcon(img);

        if (icon.getIconWidth() == -1) { // 判断图片是否加载成功
            System.err.println("Error: Could not load image at " + imagePath);
        }

        JButton button = new JButton(text, icon);
        button.setHorizontalTextPosition(SwingConstants.CENTER); // 文字在图片中心
        button.setVerticalTextPosition(SwingConstants.BOTTOM); // 文字在图片下方
        button.setFont(new Font("微软雅黑", Font.BOLD, 14)); // 设置字体
        button.setBackground(new Color(255, 255, 255)); // 设置背景颜色
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 增加按钮内边距
        return button;
    }

    private void navigateBack() {
        new NavigationPage().setVisible(true);
        dispose(); // 关闭当前页面
    }

    private JFrame messageWindow()
    {
        JFrame frame = new JFrame("打开服务失败");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 150);

        JLabel label = new JLabel("充值成功", SwingConstants.CENTER);

        label.setFont(new Font("", Font.BOLD, 20));
        label.setText("卡片已被冻结，无法使用该服务");
        label.setForeground(Color.RED);

        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(label, BorderLayout.CENTER);
        return frame;
    }
}

