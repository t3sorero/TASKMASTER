package com.scrumsquad.taskmaster.lib;

import java.awt.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {
    private CommonUtils() {
    }

    public static int getJavaMainVersion() {
        String version = System.getProperty("java.version");
        if (version.startsWith("1.")) {
            return Integer.parseInt(version.substring(2, 3)); // Java 8 and earlier
        }
        return Integer.parseInt(version.split("\\.")[0]); // Java 9+
    }

    /**
     * Calcula el color que debe tener el texto (blanco o negro) cuando tiene
     * un fondo de color c
     * @param c
     * @return
     */
    public static Color calculateTextColor(Color c) {
        float[] hsl = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        if (hsl[2] < .6) return Color.white;
        return Color.black;
    }

    /**
     * Calcula el color que debe tener el texto (blanco o negro) cuando tiene
     * un fondo de color c
     * @param c
     * @return
     */
    public static Color calculateTextColor(Color c, Color white, Color black) {
        float[] hsl = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        if (hsl[2] < .6) return white;
        return black;
    }

    private static final String emailRegex =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public static boolean isEmail(String str) {
        if (str == null) return false;
        return Pattern.compile(emailRegex).matcher(str).matches();
    }

}
