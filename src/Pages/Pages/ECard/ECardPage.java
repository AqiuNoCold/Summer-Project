package Pages.Pages.ECard;

import Pages.MainApp;
import Pages.Pages.NavigationPage;
import Pages.Pages.ECard.chargePage;
import vCampus.Entity.User;
import vCampus.Entity.ECard.ECard;


import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ECardPage extends JFrame {
    private chargePage charge;

    private ECard ecard;
    private JButton billsButton;
    private JButton changePasswordButton;
    private JButton chargeButton;
    private JButton lostButton;
    private JButton statusButton;
    private JButton backButton = new JButton("返回");

    public ECardPage(ECard response) {
        ObjectInputStream in=MainApp.getIn();
        ObjectOutputStream out=MainApp.getOut();

        ecard=response;
        setTitle("一卡通页面");
        setSize(800, 600);

        charge=new chargePage(ecard);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示窗口
        setLayout(new BorderLayout());
        add(backButton, BorderLayout.SOUTH);

        JPanel mainPanel = new JPanel(new GridLayout(2, 3, 10, 10)); // 2行3列的网格布局
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 增加边距
        mainPanel.setBackground(new Color(245, 245, 245)); // 设置背景颜色
        mainPanel.setBorder(new LineBorder(new Color(144, 238, 144), 5));

        billsButton=createImageButton("账单","src/imgs/ecard/bills.png");
        changePasswordButton=createImageButton("修改密码","src/imgs/ecard/changePassword.png");
        chargeButton=createImageButton("充值","src/imgs/ecard/charge.png");
        lostButton=createImageButton("挂失/解挂","src/imgs/ecard/lost.png");
        statusButton=createImageButton("卡片状态","src/imgs/ecard/status.png");

        mainPanel.add(chargeButton);
        mainPanel.add(billsButton);
        mainPanel.add(changePasswordButton);
        mainPanel.add(lostButton);
        mainPanel.add(statusButton);

        add(mainPanel);

        chargeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    charge.setAmountField();
                    charge.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigateBack();
            }
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
    }


    public static void LostSettings(User user) {
    }

    public static void showStatus(User user) {
        // 请求服务端showStatusSSrv后展示服务端返回的数据,假设为currentBalance
        float currentBalance = 200f;
        System.out.println("Current Balance: " + currentBalance);
    }

    public static void getTransactionHistory(User user) {
        // 请求服务端getTransactionHistorySSrv后展示返回值.,假设为currentHistory

    }

    public static void comparePassword(User user) {

        boolean ServerResult = false;
        if (!ServerResult)
            System.out.println("Doesn't match the old password");
    }

    public static void newPassword(User user) {

        boolean ServerResult = true;
        if (ServerResult)
            System.out.println("Password changed successfully!");
    }

    public static boolean payClientSrv(User user) {
        comparePassword(user);
        return true;
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

}

