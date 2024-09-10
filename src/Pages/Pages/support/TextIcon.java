package Pages.Pages.support;

import javax.swing.*;
import java.awt.*;

public class TextIcon implements Icon {
    private String text;
    private int width;
    private int height;
    private Color backgroundColor;
    private Color foregroundColor;
    private Font font;
    private int arcWidth;
    private int arcHeight;

    // 构造函数
    public TextIcon(String text, int width, int height, int arcWidth, int arcHeight) {
        this.text = text;
        this.width = width;
        this.height = height;
        this.backgroundColor = new Color(0,150,255);
        this.foregroundColor = Color.WHITE;
        this.font = new Font("微软雅黑", Font.PLAIN, 28);
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
    }

    // 实现Icon接口的paintIcon方法
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();

        // 开启抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 设置背景色并绘制圆角矩形
        g2d.setColor(backgroundColor);
        g2d.fillRect(x, y, width, height);

        // 设置文字的前景色和字体
        g2d.setColor(foregroundColor);
        g2d.setFont(font);

        // 获取字体指标
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();

        // 计算文字的起始位置
        int textX = x + (width - textWidth) / 2;
        int textY = y + (height + textHeight) / 2;

        // 绘制文字
        g2d.drawString(text, textX, textY);
        g2d.dispose(); // 释放Graphics2D资源
    }

    // 返回Icon的宽度
    @Override
    public int getIconWidth() {
        return width;
    }

    // 返回Icon的高度
    @Override
    public int getIconHeight() {
        return height;
    }

    // 设置文字
    public void setText(String text) {
        this.text = text;
    }

    // 设置前景色
    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    // 设置背景色
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    // 设置字体
    public void setFont(Font font) {
        this.font = font;
    }

    // 设置圆角
    public void setArc(int arcWidth, int arcHeight) {
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
    }
}
