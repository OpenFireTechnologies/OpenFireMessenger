package net.openfiresecurity.helper;

import android.content.Context;
import android.graphics.Typeface;

public class ResourceManager {

	/* FONTS Information */
	private final String FONTS = "fonts/";

	/* Bold Fonts */
	private final String B_COMIC_NAME = "b_comic.ttf";

	/* Light Fonts */
	private final String L_LATO_NAME = "l_lato.ttf";
	private final String L_MOONBEAM_NAME = "l_moonbeam.ttf";
	private final String L_RALEWAY_NAME = "l_raleway.ttf";
	private final String L_SKETCHFLOW_NAME = "b_sketchflow.ttf";
	private final String L_STEINER_NAME = "l_steiner.ttf";

	/* FONTS Loaded */
	public static Typeface B_COMIC;
	public static Typeface L_LATO;
	public static Typeface L_MOONBEAM;
	public static Typeface L_RALEWAY;
	public static Typeface L_SKETCHFLOW;
	public static Typeface L_STEINER;

	/* Context */
	final Context c;

	public ResourceManager(Context c) {
		this.c = c;

		/* Load Fonts */
		B_COMIC = loadFont(B_COMIC_NAME);
		L_SKETCHFLOW = loadFont(L_SKETCHFLOW_NAME);
		L_LATO = loadFont(L_LATO_NAME);
		L_MOONBEAM = loadFont(L_MOONBEAM_NAME);
		L_RALEWAY = loadFont(L_RALEWAY_NAME);
		L_STEINER = loadFont(L_STEINER_NAME);

	}

	Typeface loadFont(String fontname) {
		return Typeface.createFromAsset(c.getAssets(), FONTS + fontname);
	}

	public static Typeface getFontByName(String font) {
		Typeface temp = L_RALEWAY;
		if (font.equals("Default")) {
			temp = Typeface.DEFAULT;
		}
		if (font.equals("Monospace")) {
			temp = Typeface.MONOSPACE;
		}
		if (font.equals("Serif")) {
			temp = Typeface.SERIF;
		}
		if (font.equals("ComicSans")) {
			temp = B_COMIC;
		}
		if (font.equals("SketchFlow")) {
			temp = L_SKETCHFLOW;
		}
		if (font.equals("Lato")) {
			temp = L_LATO;
		}
		if (font.equals("Moonbeam")) {
			temp = L_MOONBEAM;
		}
		if (font.equals("Raleway")) {
			temp = L_RALEWAY;
		}
		if (font.equals("Steiner")) {
			temp = L_STEINER;
		}
		return temp;
	}

}
