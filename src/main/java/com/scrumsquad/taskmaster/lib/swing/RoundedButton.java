package com.scrumsquad.taskmaster.lib.swing;

import com.scrumsquad.taskmaster.lib.SwingUtils;

import javax.swing.*;
import javax.swing.border.StrokeBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class RoundedButton extends JButton {

    private static final int letterSpacing = 2;
    private static final Dimension minimumEmptySize = new Dimension(2, 2);

    private boolean hover;
    private boolean pressed;
    private boolean focus;

    public RoundedButton(String text) {
        setText(text);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    pressed = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    pressed = false;
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
            }
        });
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                focus = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                focus = false;
            }
        });
        // Set button dimension
        if (text != null && !text.isEmpty()) {
            final Dimension dim = getMinimumDimension(text);
            setMinimumSize(dim);
            setSize(dim);
        } else {
            setMinimumSize(minimumEmptySize);
            setSize(minimumEmptySize);
        }
        setBorder(SwingUtils.emptyBorder(8));
    }

    private Dimension getMinimumDimension(String text) {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        FontMetrics metrics = g.getFontMetrics(getFont());
        int textWidth = metrics.stringWidth(text) + (text.length() - 1) * letterSpacing;
        int textHeight = metrics.getHeight();
        g.dispose();
        return new Dimension(textWidth, textHeight);
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        // Update minimum dimension
        if (text != null && !text.isEmpty()) {
            setMinimumSize(getMinimumDimension(text));
        } else {
            setMinimumSize(minimumEmptySize);
        }
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        // Update minimum dimension
        if (getText() != null && !getText().isEmpty()) {
            setMinimumSize(getMinimumDimension(getText()));
        } else {
            setMinimumSize(minimumEmptySize);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        final int w = getWidth();
        final int h = getHeight();
        Color bg = getBackground();
        Color bgDarker, bgLighter;

        float bgRatio = 0.125f; // 1/8
        final float opacity = 0.8f; // 80%
        if (!isEnabled()) {
            bgDarker = SwingUtils.withAlpha(SwingUtils.blackerColor(bg, bgRatio), opacity);
            bgLighter = SwingUtils.withAlpha(SwingUtils.whiterColor(bg, bgRatio), opacity);
            bg = SwingUtils.withAlpha(bg, opacity);
        } else if (pressed) {
            bgDarker = bg;
            bg = SwingUtils.blackerColor(bg, bgRatio);
            bgLighter = SwingUtils.blackerColor(bg, bgRatio);
        } else if (hover) {
            bgDarker = bg;
            bg = SwingUtils.whiterColor(bg, bgRatio);
            bgLighter = SwingUtils.whiterColor(bg, bgRatio);
        } else {
            bgDarker = SwingUtils.blackerColor(bg, bgRatio);
            bgLighter = SwingUtils.whiterColor(bg, bgRatio);
        }
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int cornersRounding = Math.min(Math.max(w, h) / 2, Math.min(w, h));
        LinearGradientPaint gp = new LinearGradientPaint(0, 0, 0, h,
                new float[]{0f, .4f, .6f, 1f}, new Color[]{bgLighter, bg, bg, bgDarker});
        g2d.setPaint(gp);
        g2d.fillRoundRect(0, 0, w - 1, h - 1, cornersRounding, cornersRounding);
        if (focus) {
            g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.setColor(SwingUtils.whiterColor(getBackground(), 0.75f));
            g2d.drawRoundRect(2, 2, w - 5, h - 5, cornersRounding, cornersRounding);
        }
        g2d.setColor(getForeground());
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawRoundRect(0, 0, w - 1, h - 1, cornersRounding, cornersRounding);
        if (getText() != null && !getText().isEmpty()) {
            if (isEnabled()) {
                g2d.setColor(getForeground());
            } else {
                g2d.setColor(SwingUtils.withAlpha(getForeground(), opacity));
            }
            final String text = getText();
            g2d.setPaint(null);
            g2d.setFont(getFont());
            SwingUtils.drawStringWithLetterSpacing(g2d, text, w - 48, h - 4, letterSpacing, new Point(24, 2));
        }
        g2d.dispose();
    }
}
