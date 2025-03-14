package com.scrumsquad.taskmaster.lib.swing;

import com.scrumsquad.taskmaster.lib.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Rounded3dButton extends JButton {

    private static final Dimension minimumEmptySize = new Dimension(2, 2);
    private static final int buttonSideHeight = 8;

    private final int borderRadius;
    private boolean hover;
    private boolean pressed;
    private boolean focus;

    public int getLetterSpacing() {
        return letterSpacing;
    }

    public void setLetterSpacing(int letterSpacing) {
        this.letterSpacing = letterSpacing;
    }

    private int letterSpacing = 2;

    public Rounded3dButton(String text) {
        this(text, 8);
    }

    public Rounded3dButton(String text, int borderRadius) {
        this.borderRadius = borderRadius;
        setText(text);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setContentAreaFilled(false);
        setFocusPainted(false);
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
        setBorder(SwingUtils.emptyBorder(24, 16));
        // Set button dimension
        if (text != null && !text.isEmpty()) {
            final Dimension dim = getMinimumDimension(text);
            setMinimumSize(dim);
            setSize(dim);
        } else {
            setMinimumSize(minimumEmptySize);
            setSize(minimumEmptySize);
        }
    }

    private Dimension getMinimumDimension(String text) {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        FontMetrics metrics = g.getFontMetrics(getFont());
        int textWidth = metrics.stringWidth(text) + (text.length() - 1) * letterSpacing;
        int textHeight = metrics.getHeight();
        g.dispose();
        if (getBorder() != null) {
            Insets insets = getBorder().getBorderInsets(this);
            textWidth += insets.left + insets.right;
            textHeight += insets.top + insets.bottom;
        }
        return new Dimension(textWidth, textHeight);
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        // Update minimum dimension
        if (text != null && !text.isEmpty()) {
            final Dimension dim = getMinimumDimension(text);
            setMinimumSize(dim);
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
        Color bgDarker, bgLighter, bgSide;

        float bgRatio = 0.125f; // 1/8
        final float opacity = 0.8f; // 80%
        if (!isEnabled()) {
            bgDarker = SwingUtils.withAlpha(SwingUtils.blackerColor(bg, bgRatio), opacity);
            bgLighter = SwingUtils.withAlpha(SwingUtils.whiterColor(bg, bgRatio), opacity);
            bg = SwingUtils.withAlpha(bg, opacity);
            bgSide = SwingUtils.withAlpha(SwingUtils.blackerColor(bgDarker, bgRatio), opacity);
        } else if (hover || pressed) {
            bgDarker = bg;
            bg = SwingUtils.whiterColor(bg, bgRatio);
            bgLighter = SwingUtils.whiterColor(bg, bgRatio);
            bgSide = SwingUtils.blackerColor(bgDarker, bgRatio);
        } else {
            bgDarker = SwingUtils.blackerColor(bg, bgRatio);
            bgLighter = SwingUtils.whiterColor(bg, bgRatio);
            bgSide = SwingUtils.blackerColor(bgDarker, bgRatio);
        }
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int cornersRounding = borderRadius * 2;
        final int sideHeight = Math.min(Math.max(2, h / 4), buttonSideHeight);
        final int addedY = pressed ? sideHeight : 0;
        if (!pressed) {
            g2d.setColor(bgSide);
            g2d.fillRoundRect(0, sideHeight, w - 1, h - 1 - sideHeight, cornersRounding, cornersRounding);
        }
        LinearGradientPaint gp = new LinearGradientPaint(0, 0, 0, h,
                new float[]{0f, .4f, .6f, 1f}, new Color[]{bgLighter, bg, bg, bgDarker});
        g2d.setPaint(gp);
        g2d.fillRoundRect(0, addedY, w - 1, h - 1 - sideHeight, cornersRounding, cornersRounding);
        g2d.setColor(getForeground());
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        if (!pressed) {
            g2d.drawRoundRect(0, 0, w - 1, h - 1, cornersRounding, cornersRounding);
        }
        g2d.drawRoundRect(0, addedY, w - 1, h - 1 - sideHeight, cornersRounding, cornersRounding);
        if (getText() != null && !getText().isEmpty()) {
            g2d.setColor(isEnabled() ? getForeground() : SwingUtils.withAlpha(getForeground(), opacity));
            g2d.setPaint(null);
            g2d.setFont(getFont());
            SwingUtils.drawStringWithLetterSpacing(g2d, getText(), w, h - sideHeight, letterSpacing, new Point(0, addedY));
        }
        g2d.dispose();
    }
}
