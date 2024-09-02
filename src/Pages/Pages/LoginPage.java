package Pages.Pages;

import Pages.MainApp;
import vCampus.Entity.User;
import vCampus.User.IUserServerSrv;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton forgotPasswordButton;
    private JLabel messageLabel;

    public LoginPage() {
        ObjectInputStream in = MainApp.getIn();
        ObjectOutputStream out = MainApp.getOut();

        setTitle("登录页面");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示窗口
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // 添加间距

        // 用户名标签和输入框
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("用户名:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        usernameField = new JTextField(15);
        add(usernameField, gbc);

        // 密码标签和输入框
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("密码:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(15);
        add(passwordField, gbc);

        // 登录按钮
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // 跨越两列
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton = new JButton("登录");
        add(loginButton, gbc);

        // 找回密码按钮
        gbc.gridy = 3;
        forgotPasswordButton = new JButton("找回密码");
        add(forgotPasswordButton, gbc);

        // 消息标签
        gbc.gridy = 4;
        messageLabel = new JLabel("");
        add(messageLabel, gbc);

        // 登录按钮事件
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    out.writeObject("1");
                    out.writeObject("Login");
                    out.writeObject(usernameField.getText());
                    out.writeObject(new String(passwordField.getPassword()));
                    out.flush();

                    User response = (User) in.readObject();
                    MainApp.setCurrentUser(response);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(ex.getMessage());
                }

                if (MainApp.getCurrentUser()!=null) {
                    new NavigationPage().setVisible(true);
                    dispose(); // 关闭登录页面
                } else {
                    messageLabel.setText("用户名或密码错误");
                }
            }
        });
    }
}