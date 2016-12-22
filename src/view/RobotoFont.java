/*
 * File: RobotoFont.java
 * Author: Fredrik Johansson
 * Date: 2016-12-20
 */
package view;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Flyweight pattern
 */
public class RobotoFont {

    private static Font masterFont;
    private static Map<Float, Font> cached = new HashMap<>();
    private static boolean loaded = false;

    public static Font getFont(float size) {
        if (!loaded) {
            load();
        }
        Font font = cached.get(size);
        if (font == null) {
            font = masterFont.deriveFont(size);
            cached.put(size, font);
        }
        return font;
    }

    private static void load() {
        InputStream is = RobotoFont.class.getResourceAsStream("/fonts/Roboto-Regular.ttf");
        try {
            masterFont = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (IOException | FontFormatException e) {
            masterFont = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        }
    }
}
