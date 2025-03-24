package com.scrumsquad.taskmaster.lib;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

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
     *
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
     *
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

    public static void drawStringWithLetterSpacing(Graphics2D g2d, String text, int width, int height, int letterSpacing) {
        drawStringWithLetterSpacing(g2d, text, width, height, letterSpacing, new Point());
    }

    public static void drawStringWithLetterSpacing(Graphics2D g2d, String text, int width, int height, int letterSpacing, Point point) {
        if (text == null || text.trim().isEmpty()) return;
        if (width <= 0 || height <= 0) return;
        if (point.x < 0 || point.x >= width) return;
        if (point.y < 0 || point.y >= height) return;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontMetrics metrics = g2d.getFontMetrics();
        int spacing = Math.max(0, letterSpacing);
        int textWidth = metrics.stringWidth(text) + spacing * (text.length() - 1);
        int numLines = (textWidth / width) + 1;
        int textHeight = metrics.getHeight();
        int minWidth = textWidth / numLines;
        boolean tooMuchHeight = false;
        if (numLines * textHeight > height) {
            tooMuchHeight = true;
            numLines = height / textHeight;
            minWidth = width;
        }
        final String[] words = text.split(" ");
        java.util.List<String> lines = new ArrayList<>();
        java.util.List<Integer> linesWidth = new ArrayList<>();
        String line = "";
        for (int i = 0; i < words.length && lines.size() < numLines; i++) {
            String word = words[i];
            final String lineWithWord = line.isEmpty() ? word : line + ' ' + word;
            int lineWithWordWidth = metrics.stringWidth(lineWithWord) + spacing * (lineWithWord.length() - 1);
            if (lineWithWordWidth < minWidth) {
                line = lineWithWord;
            } else {
                if (lineWithWordWidth < width) {
                    line = lineWithWord;
                }
                if (tooMuchHeight && lines.size() == numLines - 1) {
                    line = line + ' ';
                    int lineLength = line.length();
                    String lineWithDots = line + "...";
                    int lineWithDotsWidth = metrics.stringWidth(lineWithDots) + spacing * (lineWithDots.length() - 1);
                    while (lineWithDotsWidth > width) {
                        lineLength -= 1;
                        lineWithDots = line.substring(0, lineLength) + "...";
                        lineWithDotsWidth = metrics.stringWidth(lineWithDots) + spacing * (lineWithDots.length() - 1);
                    }
                    line = lineWithDots;
                }
                lines.add(line);
                linesWidth.add(metrics.stringWidth(line) + spacing * (line.length() - 1));
                if (lineWithWordWidth < width) {
                    line = "";
                } else {
                    line = word;
                }
            }
        }
        if (lines.size() < numLines) {
            lines.add(line);
            linesWidth.add(metrics.stringWidth(line) + spacing * (line.length() - 1));
        }
        int imageHeight = Math.min(height, lines.size() * textHeight);
        boolean done = false;
        int linesPainted = 0;
        int y = (height - imageHeight) / 2;
        final int maxHeight = y + imageHeight;
        int textAscent = metrics.getAscent();
        for (; linesPainted < lines.size() && !done; linesPainted++) {
            String l = lines.get(linesPainted);
            int x = (width - linesWidth.get(linesPainted)) / 2 + Math.max(point.x, 0);
            if (y - textHeight > maxHeight) {
                done = true;
            } else if (spacing == 0) {
                // Draw string
                g2d.drawString(l, x, y + Math.max(point.y, 0) + textAscent);
            } else {
                // Draw each character with spacing
                for (char c : text.toCharArray()) {
                    g2d.drawString(String.valueOf(c), x, y + Math.max(point.y, 0) + textAscent);
                    x += metrics.charWidth(c) + spacing; // Move to the next character position
                }
            }
            y += textHeight;
        }
    }

    public static GridBagConstraints horizontalConstraints() {
        GridBagConstraints horizontalConstraints = new GridBagConstraints();
        horizontalConstraints.gridx = GridBagConstraints.RELATIVE;
        horizontalConstraints.gridy = 0;
        horizontalConstraints.fill = GridBagConstraints.VERTICAL;
        return horizontalConstraints;
    }

    public static GridBagConstraints verticalConstraints() {
        GridBagConstraints verticalConstraints = new GridBagConstraints();
        verticalConstraints.gridx = 0;
        verticalConstraints.gridy = GridBagConstraints.RELATIVE;
        verticalConstraints.fill = GridBagConstraints.HORIZONTAL;
        return verticalConstraints;
    }
}
