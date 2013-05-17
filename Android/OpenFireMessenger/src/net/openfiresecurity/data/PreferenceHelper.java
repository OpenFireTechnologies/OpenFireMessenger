/*
 * Copyright (c) 2013. Alexander Martinz @ OpenFire Security
 */

package net.openfiresecurity.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceHelper {

	private static SharedPreferences prefs;
	private static SharedPreferences.Editor editor;

	@SuppressLint("CommitPrefEdits")
	public PreferenceHelper(Context c) {
		PreferenceHelper.prefs = PreferenceManager
				.getDefaultSharedPreferences(c);
		PreferenceHelper.editor = prefs.edit();
	}

	public String getString(String key) {
		return prefs.getString(key, "");
	}

	public int getInt(String key) {
		return prefs.getInt(key, 0);
	}

	public boolean getBoolean(String key) {
		return prefs.getBoolean(key, true);
	}

	public void setString(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}

	public void setInt(String key, int value) {
		editor.putInt(key, value);
		editor.commit();
	}

	public void setBoolean(String key, boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}

}
