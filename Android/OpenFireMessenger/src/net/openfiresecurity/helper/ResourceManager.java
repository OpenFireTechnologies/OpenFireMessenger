package net.openfiresecurity.helper;

import android.content.Context;
import android.graphics.Typeface;

public class ResourceManager {

    /* FONTS Information */
    private final String FONTS = "fonts/";

    /* Light Fonts */
    private final String L_STEINER_NAME = "l_steiner.ttf";

    /* FONTS Loaded */
    public static Typeface L_STEINER;

    /* Context */
    final Context c;

    public ResourceManager(Context c) {
        this.c = c;

		/* Load Fonts */
        L_STEINER = loadFont(L_STEINER_NAME);

    }

    Typeface loadFont(String fontname) {
        return Typeface.createFromAsset(c.getAssets(), FONTS + fontname);
    }

    public static Typeface getFontByName(String font) {
        Typeface temp = Typeface.DEFAULT;
        if (font.equals("Default")) {
            temp = Typeface.DEFAULT;
        }
        if (font.equals("Monospace")) {
            temp = Typeface.MONOSPACE;
        }
        if (font.equals("Serif")) {
            temp = Typeface.SERIF;
        }
        if (font.equals("Steiner")) {
            temp = L_STEINER;
        }
        return temp;
    }

}
