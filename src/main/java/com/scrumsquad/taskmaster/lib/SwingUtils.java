package com.scrumsquad.taskmaster.lib;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SwingUtils {
    private SwingUtils() {
    }

    public static Border emptyBorder(int padding) {
        return BorderFactory.createEmptyBorder(padding, padding, padding, padding);
    }

    public static Border emptyBorder(int horizontalPadding, int verticalPadding) {
        return BorderFactory.createEmptyBorder(verticalPadding, horizontalPadding, verticalPadding, horizontalPadding);
    }

    public static Border emptyBorderTop(int padding) {
        return BorderFactory.createEmptyBorder(padding, 0, 0, 0);
    }

    public static Border emptyBorderLeft(int padding) {
        return BorderFactory.createEmptyBorder(0, padding, 0, 0);
    }

    public static Border emptyBorderRight(int padding) {
        return BorderFactory.createEmptyBorder(0, 0, 0, padding);
    }

    public static Border emptyBorderBottom(int padding) {
        return BorderFactory.createEmptyBorder(0, 0, padding, 0);
    }

    public static Font getDefaultFont() {
        return javax.swing.UIManager.getDefaults().getFont("Label.font");
    }

    /**
     * Devuelve el color con un tono mas negro.
     * @param c
     * @param ratio [0, 1] cantidad a incrementar
     * @return
     */
    public static Color blackerColor(Color c, float ratio) {
        double r = 1 - Math.max(Math.min(ratio, 1.0), 0.0);
        return new Color(
                Math.max((int) Math.floor(c.getRed() * r), 0),
                Math.max((int) Math.floor(c.getGreen() * r), 0),
                Math.max((int) Math.floor(c.getBlue() * r), 0));
    }

    /**
     * Devuelve el color con un tono mas blanco.
     * @param c
     * @param ratio [0, 1] cantidad a incrementar
     * @return
     */
    public static Color whiterColor(Color c, float ratio) {
        double r = 1 + Math.max(Math.min(ratio, 1.0), 0.0);
        return new Color(
                Math.min((int) Math.floor(c.getRed() * r), 255),
                Math.min((int) Math.floor(c.getGreen() * r), 255),
                Math.min((int) Math.floor(c.getBlue() * r), 255));
    }

    public static Color withAlpha(Color c, float opacity) {
        float op = Math.max(Math.min(opacity, 1f), 0f);
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), Math.max(Math.round(op * 0xFF) - 1, 0));
    }


    public static AffineTransformOp generateAffineTransformOp(BufferedImage image, int width, int height) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        double scaleX = (double) width / imageWidth;
        double scaleY = (double) height / imageHeight;
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        return new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);
    }

    public static void drawStringWithLetterSpacing(Graphics2D g2d, Font font, String text, int width, int height, int letterSpacing) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (letterSpacing <= 0) {
            FontMetrics metrics = g2d.getFontMetrics();
            int textWidth = metrics.stringWidth(text);
            int textHeight = metrics.getHeight();
            int textAscent = metrics.getAscent();

            // Compute center position
            int x = (width - textWidth) / 2;
            int y = (height - textHeight) / 2 + textAscent;

            g2d.drawString(text, x, y);
        }
        FontMetrics fm = g2d.getFontMetrics(font);

        // Calculate total text width with letter spacing
        int textWidth = 0;
        for (char c : text.toCharArray()) {
            textWidth += fm.charWidth(c) + letterSpacing;
        }
        textWidth -= letterSpacing; // Remove extra spacing after the last character

        int x = (width - textWidth) / 2; // Center horizontally
        int y = (height + fm.getAscent() - fm.getDescent()) / 2; // Center vertically

        // Draw each character with spacing
        for (char c : text.toCharArray()) {
            g2d.drawString(String.valueOf(c), x, y);
            x += fm.charWidth(c) + letterSpacing; // Move to the next character position
        }
    }
}
