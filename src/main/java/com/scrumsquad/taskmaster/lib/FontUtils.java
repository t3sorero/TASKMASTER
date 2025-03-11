package com.scrumsquad.taskmaster.lib;

import java.awt.*;

public class FontUtils {
    private FontUtils() {
    }

    public static final Font defaultFont = SwingUtils.getDefaultFont();
    public static final Font latoFont = ResourceLoader.loadFont("/fonts/lato/Lato-Regular.ttf");

    public static final Font default12 = defaultFont.deriveFont(12f);
    public static final Font default14 = defaultFont.deriveFont(14f);

    public static final Font lato12 = latoFont.deriveFont(12f);
    public static final Font lato14 = latoFont.deriveFont(14f);
    public static final Font lato16 = latoFont.deriveFont(16f);
    public static final Font lato20 = latoFont.deriveFont(20f);



}
