package com.scrumsquad.taskmaster.lib.swing;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class GradientRoundedPanel extends JPanel {

    private Color color1;
    private Color color2;
    private int borderRadious;
    private Color borderColor;

    public GradientRoundedPanel(Color color1, Color color2, Color borderColor, int borderRadius) {
        this.borderRadious = borderRadius;
        this.color1 = color1;
        this.color2 = color2;
        this.borderColor = borderColor;
        setOpaque(false); // para que funcione bien el pintado personalizado
    }

    @Override
    protected void paintComponent(Graphics g) {
        int w = getWidth();
        int h = getHeight();

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Degradado horizontal
        GradientPaint gradient = new GradientPaint(0, 0, color1, w, 0, color2);
        g2.setPaint(gradient);

        Shape capsule = new RoundRectangle2D.Double(0, 0, w, h, borderRadious, borderRadious);
        g2.fill(capsule);

        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(3f));
        Shape borderShape = new RoundRectangle2D.Double(1, 1, w - 2.5, h - 2.5, borderRadious, borderRadious);
        g2.draw(borderShape);

        g2.dispose();
        super.paintComponent(g);
    }

    public void setColor(Color color1, Color color2){
        this.color1 = color1;
        this.color2 = color2;
    }
}
