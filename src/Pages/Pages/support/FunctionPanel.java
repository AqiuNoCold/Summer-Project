package Pages.Pages.support;

import java.awt.Color;
import javax.swing.JPanel;

/**
 * @author JCSTARS
 * @version 1.0
 * 为选项卡布局定制的功能面板
 */

public class FunctionPanel extends JPanel {
    static int X = 1200, Y = 650;

    BackgroundTabbedFrame jf;
    String description;

    public FunctionPanel(BackgroundTabbedFrame frame, String desc) {
        super(null);
        this.jf = frame;
        this.description = desc;
        this.setBounds(0, 0, X, Y);
        this.setBackground(Color.WHITE);
        this.setOpaque(true);
    }

}
