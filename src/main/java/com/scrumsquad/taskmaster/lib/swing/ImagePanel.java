package com.scrumsquad.taskmaster.lib.swing;

import com.scrumsquad.taskmaster.lib.ResourceLoader;
import com.scrumsquad.taskmaster.lib.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {

    private BufferedImage image;

    public ImagePanel(String path) {
        setOpaque(false);
        image = ResourceLoader.loadImage(path);
        if (image != null && (image.getWidth() == 0 || image.getHeight() == 0)) {
            image = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            double w = getWidth();
            double h = getHeight();
            int imageW = image.getWidth();
            int imageH = image.getHeight();
            double wRatio = w / imageW;
            double hRatio = h / imageH;
            int imageWidth, imageHeight;
            if (wRatio < hRatio) {
                imageHeight = (int) Math.floor(h);
                imageWidth = (int) Math.floor(hRatio * imageW);
            } else {
                imageHeight = (int) Math.floor(wRatio * imageH);
                imageWidth = (int) Math.floor(w);
            }
            AffineTransformOp op = SwingUtils.generateAffineTransformOp(image, imageWidth, imageHeight);
            int x = (int) Math.floor(w - imageWidth);
            int y = (int) Math.floor(h - imageHeight);
            g2d.drawImage(image, op, x, y);
            g2d.dispose();
        }
    }
}
