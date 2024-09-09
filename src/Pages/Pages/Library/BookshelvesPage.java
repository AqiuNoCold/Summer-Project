package Pages.Pages.Library;

import javax.swing.*;
import java.awt.*;

public class BookshelvesPage extends JPanel {
    public BookshelvesPage() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("书架页面内容", SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }
}