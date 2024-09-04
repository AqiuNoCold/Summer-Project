package Pages.Pages.ECard;

import vCampus.Entity.User;

import javax.swing.*;

public class PayPage extends JFrame {
    private float amount;
    private String reason;

    private User user;

    private JButton payButton;
    private JLabel ammountLabel;
    private JLabel reasonLabel;
    private JTextField passwordField;

    public PayPage(float amountEntered, String reasonEntered) {
        amount = amountEntered;
        reason = reasonEntered;
    }
}
