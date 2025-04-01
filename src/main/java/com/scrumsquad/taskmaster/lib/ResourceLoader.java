package com.scrumsquad.taskmaster.lib;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceLoader {
    private ResourceLoader() {
    }

    private static final Map<String, Font> fonts = new ConcurrentHashMap<>();
    private static final Map<String, BufferedImage> images = new ConcurrentHashMap<>();
    private static final Map<String, ImageIcon> imageIcons = new ConcurrentHashMap<>();

    public static BufferedImage loadImage(String path) {
        return images.computeIfAbsent(path, p -> {
            if (p == null) return null;
            final URL url = ResourceLoader.class.getResource(p);
            if (url == null) {
                System.err.println("File \"" + p + "\" not found");
                return null;
            }
            try {
                return ImageIO.read(url);
            } catch (Exception ex) {
                System.err.println("File \"" + p + "\" failed while being read");
                ex.printStackTrace();
                return null;
            }
        });
    }

    public static ImageIcon loadImageIcon(String path) {
        return imageIcons.computeIfAbsent(path, p -> {
            if (p == null) return null;
            final URL url = ResourceLoader.class.getResource(p);
            if (url == null) {
                System.err.println("File \"" + p + "\" not found");
                return null;
            }
            try {
                return new ImageIcon(url);
            } catch (Exception ex) {
                System.err.println("File \"" + p + "\" failed while being read");
                ex.printStackTrace();
                return null;
            }
        });
    }

    public static Font loadFont(String path) {
        return fonts.computeIfAbsent(path, p -> {
            if (p == null) return null;
            try (InputStream stream = ResourceLoader.class.getResourceAsStream(p)) {
                if (stream == null) {
                    System.err.println("File \"" + p + "\" not found");
                    return null;
                }
                return Font.createFont(Font.TRUETYPE_FONT, stream);
            } catch (Exception ex) {
                System.err.println("File \"" + p + "\" failed while being read");
                ex.printStackTrace();
                return null;
            }
        });
    }
}
