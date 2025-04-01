package com.scrumsquad.taskmaster.lib.swing;

import com.scrumsquad.taskmaster.lib.ResourceLoader;
import com.scrumsquad.taskmaster.lib.SwingUtils;

import java.awt.*;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Rounded3dButtonWithTextAndImage extends Rounded3dButton {

    private BufferedImage image;
    private final String bottomText;

    public Rounded3dButtonWithTextAndImage(String text, String path) {
        super(null);
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }
        bottomText = text;
        image = ResourceLoader.loadImage(path);
        if (image != null && (image.getWidth() == 0 || image.getHeight() == 0)) {
            image = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null && bottomText == null) return;

        Graphics2D g2d = (Graphics2D) g.create();


        Font textFont = new Font("Serif", Font.BOLD, 18);
        g2d.setFont(textFont);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontMetrics metrics = g2d.getFontMetrics();

        int buttonWidth = getWidth();
        int buttonHeight = getHeight();

        int yImage = 16 + (isPressed() ? getSideHeight() : 0);
        int imageHeight = 0;

        if (image != null) {
            // Escalado y centrado de imagen
            int imageMaxHeight = buttonHeight - metrics.getHeight() - 32;
            int imageMaxWidth = buttonWidth - 32;
            int imageW = image.getWidth();
            int imageH = image.getHeight();
            double wRatio = imageMaxWidth * 1.0 / imageW;
            double hRatio = imageMaxHeight * 1.0 / imageH;

            int scaledW, scaledH;
            if (wRatio < hRatio) {
                scaledW = imageMaxWidth;
                scaledH = (int) (imageH * wRatio);
            } else {
                scaledH = imageMaxHeight;
                scaledW = (int) (imageW * hRatio);
            }

            imageHeight = scaledH;
            AffineTransformOp op = SwingUtils.generateAffineTransformOp(image, scaledW, scaledH);
            int xImage = (buttonWidth - scaledW) / 2;

            g2d.drawImage(image, op, xImage, yImage);
        }

        // Caja de texto redondeada
        int paddingX = 10;
        int paddingY = 6;
        int textWidth = metrics.stringWidth(bottomText);
        int textHeight = metrics.getHeight();

        int boxWidth = textWidth + paddingX * 2;
        int boxHeight = textHeight + paddingY * 2;

        int xBox = (buttonWidth - boxWidth) / 2;
        int sideHeight = getSideHeight();
        int addedY = isPressed() ? sideHeight : 0;
        int yBox = buttonHeight - boxHeight - 15 + addedY;

        // Dibujar fondo redondeado
        g2d.setColor(new Color(255, 255, 255, 180)); // blanco semitransparente
        g2d.fillRoundRect(xBox, yBox, boxWidth, boxHeight, 20, 20);

        // Dibujar texto centrado
        g2d.setColor(new Color(30, 30, 30));
        int xText = xBox + paddingX;
        int yText = yBox + paddingY + metrics.getAscent();
        g2d.drawString(bottomText, xText, yText);

        g2d.dispose();
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 280);
    }


}
