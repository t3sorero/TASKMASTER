package com.scrumsquad.taskmaster.lib.swing;

import com.scrumsquad.taskmaster.lib.SwingUtils;
import com.scrumsquad.taskmaster.views.AppColors;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {

    private int borderRadius;

    public RoundedPanel(int borderRadius) {
        this.borderRadius = Math.max(borderRadius, 0);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        g2d.setColor(getBackground());
        int arc = Math.min(borderRadius, Math.min(w, h));
        g2d.fillRoundRect(0, 0, w, h, arc, arc);
        g2d.dispose();
    }
}
