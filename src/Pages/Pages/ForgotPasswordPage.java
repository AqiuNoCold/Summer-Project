package Pages.Pages;

import Pages.MainApp;
import vCampus.Entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ForgotPasswordPage extends JFrame{
    private JTextField idField;
    private JTextField emailField;
    private JButton submitButton;
    private JLabel messageLabel;

    public ForgotPasswordPage() {
        ObjectInputStream in = MainApp.getIn();
        ObjectOutputStream out = MainApp.getOut();

        setTitle("找回密码");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // ID标签和输入框
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("用户ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        idField = new JTextField(15);
        add(idField, gbc);

        // 邮箱标签和输入框
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("注册邮箱:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        emailField = new JTextField(15);
        add(emailField, gbc);

        // 提交按钮
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        submitButton = new JButton("提交");
        add(submitButton, gbc);

        // 消息标签
        gbc.gridy = 3;
        messageLabel = new JLabel("");
        add(messageLabel, gbc);

        // 提交按钮事件
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = idField.getText();
                String email = emailField.getText();
                User finduser = null;
                try {
                    out.writeObject("1");
                    out.writeObject("Forget");
                    out.writeObject(userId);
                    out.writeObject(email);
                    finduser = (User) in.readObject();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                // 假设有一个方法来处理找回密码的逻辑
                if (finduser != null) {
                    MainApp.setCurrentUser(finduser);
                    new ForgotPasswordPage.ResetPasswordPage(userId).setVisible(true);
                    dispose(); // 关闭找回密码页面
                } else {
                    messageLabel.setText("用户ID或邮箱错误。");
                }
            }
        });
    }

    // ResetPasswordPage 类
    public class ResetPasswordPage extends JFrame {
        private JPasswordField newPasswordField;
        private JPasswordField confirmPasswordField;
        private JButton backToLoginButton;
        private JButton resetButton;
        private JLabel messageLabel;
        private String userId;
        public ResetPasswordPage(String userId) {
            ObjectInputStream in = MainApp.getIn();
            ObjectOutputStream out = MainApp.getOut();
            this.userId = userId;
            setTitle("重置密码");
            setSize(800, 400);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);

            // 新密码标签和输入框
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.EAST;
            add(new JLabel("新密码:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;
            newPasswordField = new JPasswordField(15);
            add(newPasswordField, gbc);

            // 确认密码标签和输入框
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.EAST;
            add(new JLabel("确认密码:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.WEST;
            confirmPasswordField = new JPasswordField(15);
            add(confirmPasswordField, gbc);

            // 重置按钮
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            resetButton = new JButton("重置");
            add(resetButton, gbc);

            // 消息标签
            gbc.gridy = 3;
            messageLabel = new JLabel("");
            add(messageLabel, gbc);

            gbc.gridy = 4;
            backToLoginButton = new JButton("返回登录");
            add(backToLoginButton, gbc);

            // 重置按钮事件
            resetButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newPassword = new String(newPasswordField.getPassword());
                    String confirmPassword = new String(confirmPasswordField.getPassword());
                    boolean success = false;
                    if (newPassword.length() > 16 | newPassword.length() < 6) {
                        messageLabel.setText("密码必须是6到16个字符");
                        newPasswordField.setText("");
                        confirmPasswordField.setText("");
                        return;
                    }
                    if (newPassword.equals(confirmPassword)) {
                        try {
                            out.writeObject("1");
                            out.writeObject("Reset");
                            out.writeObject(userId);
                            out.writeObject(newPassword);
                            success = (boolean) in.readObject();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        if (success) {
                            messageLabel.setText("密码重置成功。");
                            newPasswordField.setText("");
                            confirmPasswordField.setText("");
                        } else {
                            messageLabel.setText("密码重置失败。");
                        }
                    } else {
                        messageLabel.setText("两次输入的密码不一致。");
                    }
                }
            });

            backToLoginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    LoginPage loginpage = new LoginPage();
                    loginpage.setVisible(true);
                    dispose();
                }
            });
        }
    }
}
