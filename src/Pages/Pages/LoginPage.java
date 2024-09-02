package Pages.Pages;

import Pages.MainApp;
import vCampus.Entity.User;
import vCampus.User.IUserServerSrv;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton forgotPasswordButton;
    private JLabel messageLabel;

    public LoginPage() {
        setTitle("登录页面");
        setSize(400, 300);
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
                User cUser = IUserServerSrv.login(usernameField.getText(), new String(passwordField.getPassword()));
                if (cUser != null) {
                    MainApp.setCurrentUser(cUser);
                    new NavigationPage().setVisible(true);
                    dispose(); // 关闭登录页面
                } else {
                    messageLabel.setText("用户名或密码错误");
                }
            }
        });

        // 找回密码按钮事件
        forgotPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ForgotPasswordPage().setVisible(true);
            }
        });
    }

    // 找回密码界面
    private class ForgotPasswordPage extends JFrame {
        private JTextField idField;
        private JTextField emailField;
        private JButton submitButton;
        private JLabel messageLabel;

        public ForgotPasswordPage() {
            setTitle("找回密码");
            setSize(400, 200);
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
                    // 这里可以添加找回密码的逻辑
                    String userId = idField.getText();
                    String email = emailField.getText();
                    // 假设有一个方法来处理找回密码的逻辑
                    User finduser = IUserServerSrv.forgetPassword(userId,email);
                    if (finduser != null) {
                        new ResetPasswordPage(userId).setVisible(true);
                        dispose(); // 关闭找回密码页面
                    } else {
                        messageLabel.setText("用户ID或邮箱错误。");
                    }
                }
            });
        }
    }

    // ResetPasswordPage 类
    private class ResetPasswordPage extends JFrame {
        private JPasswordField newPasswordField;
        private JPasswordField confirmPasswordField;
        private JButton resetButton;
        private JLabel messageLabel;
        private String userId;

        public ResetPasswordPage(String userId) {
            this.userId = userId;
            setTitle("重置密码");
            setSize(400, 200);
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

            // 重置按钮事件
            resetButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newPassword = new String(newPasswordField.getPassword());
                    String confirmPassword = new String(confirmPasswordField.getPassword());
                    if (newPassword.equals(confirmPassword)) {
                        boolean success = IUserServerSrv.resetPassword(userId, newPassword);
                        if (success) {
                            messageLabel.setText("密码重置成功。");
                        } else {
                            messageLabel.setText("密码重置失败。");
                        }
                    } else {
                        messageLabel.setText("两次输入的密码不一致。");
                    }
                }
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
    }
}