package Pages.Pages.ECard;

import Pages.MainApp;
import Pages.Pages.NavigationPage;
import vCampus.Entity.ECard.ECardDTO;
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


    private JDialog message_failed;

    private ECard ecard;

    private JButton billsButton;
    private JButton changePasswordButton;
    private JButton chargeButton;
    private JButton lostButton;
    private JButton statusButton;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ECardPage(ECard response) {
        in=MainApp.getIn();
        out=MainApp.getOut();

        ecard=response;
        setTitle("一卡通页面");
        setSize(800, 600);

        message_failed=messageWindow_failed();



        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示窗口
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridLayout(2, 3, 10, 10)); // 2行3列的网格布局
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 增加边距
        mainPanel.setBackground(new Color(245, 245, 245)); // 设置背景颜色
        mainPanel.setBorder(new LineBorder(new Color(144, 238, 144), 5));

        billsButton=createImageButton("账单","/imgs/ecard/bills.png");
        changePasswordButton=createImageButton("修改密码","/imgs/ecard/changePassword.png");
        chargeButton=createImageButton("充值","/imgs/ecard/charge.png");
        lostButton=createImageButton("","/imgs/ecard/lost.png");
        updateLostButton();
        statusButton=createImageButton("卡片状态","/imgs/ecard/status.png");

        billsButton.setFont(new Font("微软雅黑", Font.BOLD, 20));
        changePasswordButton.setFont(new Font("微软雅黑", Font.BOLD, 20));
        chargeButton.setFont(new Font("微软雅黑", Font.BOLD, 20));
        lostButton.setFont(new Font("微软雅黑", Font.BOLD, 20));
        statusButton.setFont(new Font("微软雅黑", Font.BOLD, 20));


        mainPanel.add(chargeButton);
        mainPanel.add(billsButton);
        mainPanel.add(changePasswordButton);
        mainPanel.add(lostButton);
        mainPanel.add(statusButton);

        add(mainPanel);

        chargeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(ecard.getLost())
                    message_failed.setVisible(true);
                else{
                try{
                    new chargePage(ecard.getCard()).setVisible(true);
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
                    message_failed.setVisible(true);
                else{
                try{
                    new changePasswordPage(ecard.getCard()).setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }}
        });

        lostButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    out.writeObject("3");
                    out.writeObject("LostSettings");
                    out.writeObject(ecard.getId());
                    out.writeObject(ecard.getLost());
                    out.flush();
                    ecard.setLost(!ecard.getLost());
                    messageWindow_lostsettings(ecard.getLost()).setVisible(true);
                    updateLostButton();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        statusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    out.writeObject("3");
                    out.writeObject("Status");
                    out.writeObject(ecard.getCard());
                    out.flush();
                    ECardDTO cardStatus = (ECardDTO) in.readObject();
                    statusWindow(cardStatus).setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                navigateBack();
            }
        });
    }


    private JButton createImageButton(String text, String imagePath) {
//        ImageIcon icon = new ImageIcon(imagePath);
        Image icon1 = Toolkit.getDefaultToolkit().getImage(getClass().getResource(imagePath));


        // 调整图片大小
        Image img = icon1.getScaledInstance(64, 64, Image.SCALE_SMOOTH); // 调整图片为64x64像素
        ImageIcon icon = new ImageIcon(img);

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

    private void updateLostButton(){
        if(!ecard.getLost()) {
            lostButton.setText("冻结卡片");
            lostButton.setForeground(Color.RED);
        }
        else
        {lostButton.setText("解冻卡片");
        lostButton.setForeground(new Color(98, 211, 98));}
    }

    private JDialog messageWindow_failed() {
        JDialog frame = new JDialog(this,"打开服务失败",true);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        frame.setSize(500, 150);

        JLabel label = new JLabel("卡片已被冻结，无法使用该服务", SwingConstants.CENTER);

        label.setFont(new Font("", Font.BOLD, 20));
        label.setForeground(Color.RED);

        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(label, BorderLayout.CENTER);
        return frame;
    }

    private JDialog messageWindow_lostsettings(boolean islost) {
        JDialog frame = new JDialog(this,"打开服务失败",true);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        frame.setSize(500, 150);
        frame.setLocationRelativeTo(null);

        JLabel label = new JLabel("", SwingConstants.CENTER);
        if(islost) {
            frame.setTitle("冻结成功！");
            label.setText("冻结成功！");
        }else{
            frame.setTitle("解冻成功！");
            label.setText("解冻成功！");
        }


        label.setFont(new Font("", Font.BOLD, 20));

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(label, BorderLayout.CENTER);
        return frame;
    }

    private JDialog statusWindow(ECardDTO status){
        JDialog modalDialog = new JDialog(this, "卡片状态", true);

        modalDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        modalDialog.setSize(300, 150);
        modalDialog.setLocationRelativeTo(null);

        JLabel cardLabel = new JLabel("卡号："+status.getCard());
        JLabel remainLabel=new JLabel("余额："+status.getRemain());
        JLabel lostLabel=new JLabel();
        if(ecard.getLost()){
            lostLabel.setText("冻结中");
            lostLabel.setForeground(Color.RED);
        }
        else{
            lostLabel.setText("正常");
            lostLabel.setForeground(new Color(98, 211, 98));
        }


        cardLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        remainLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        lostLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(cardLabel);
        panel.add(Box.createRigidArea(new java.awt.Dimension(0, 10)));
        panel.add(remainLabel);
        panel.add(Box.createRigidArea(new java.awt.Dimension(0, 10)));
        panel.add(lostLabel);

        modalDialog.add(panel);

        return modalDialog;
    }
}

