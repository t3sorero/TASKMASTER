package com.scrumsquad.taskmaster.lib.swing;

import com.scrumsquad.taskmaster.lib.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.net.URL;

public class TransparentButton extends JButton {

    private Image image;
    private int imageWidth;
    private int imageHeight;

    public TransparentButton(String text, int width, int height) {
        setText(text);
        setPreferredSize(new Dimension(width, height));
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
    }

    public TransparentButton(ImageIcon icon, int width, int height) {
        this.image = icon.getImage();
        this.imageWidth = icon.getIconWidth();
        this.imageHeight = icon.getIconHeight();
        setPreferredSize(new Dimension(width, height));
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            int w = (int) getPreferredSize().getWidth();
            int h = (int) getPreferredSize().getHeight();
            g2d.drawImage(image, (w - imageWidth) / 2, (h - imageHeight) / 2, null);
            g2d.dispose();
        }

    }

    public static TransparentButton withImage(String path) {
        return withImage(path, 0, 0);
    }

    public static TransparentButton withImage(String path, int width, int height) {
        BufferedImage image = ResourceLoader.loadImage(path);
        if (image == null) return new TransparentButton("", width, height);
        ImageIcon icon = new ImageIcon(image);
        int w = Math.max(icon.getIconWidth(), width);
        int h = Math.max(icon.getIconHeight(), height);
        return new TransparentButton(icon, w, h);
    }
}
