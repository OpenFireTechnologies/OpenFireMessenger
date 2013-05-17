/*
 * Copyright (c) 2013. Alexander Martinz @ OpenFire Security
 */

package net.openfiresecurity.messenger;

import java.net.URLDecoder;
import java.util.List;
import java.util.Vector;

import net.openfiresecurity.data.PreferenceHelper;
import net.openfiresecurity.data.SQLiteContacts;
import net.openfiresecurity.data.SQLiteContactsDataSource;
import net.openfiresecurity.data.SQLiteMessagesDataSource;
import net.openfiresecurity.fragments.Chat;
import net.openfiresecurity.fragments.Contacts;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;

public class MainService extends Service {

	/* MainView + Fragments */
	public static MainView main;
	public static Chat chat;
	public static Contacts contacts;
	private static List<Fragment> fragments;

	/* Database */
	public static SQLiteMessagesDataSource messagesDB;
	public static SQLiteContactsDataSource contactsDB;

	/* */
	public static boolean isActive = false;
	public static PreferenceHelper prefs;
	public static Resources res;
	public static Vibrator sVibrator;

	private static InputMethodManager inputMethodManager;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		sVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		prefs = new PreferenceHelper(this);
		res = getResources();
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		/* Database */
		setupDatabases();

		/* Fragments */
		fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(this, Contacts.class.getName()));
		fragments.add(Fragment.instantiate(this, Chat.class.getName()));
		contacts = (Contacts) fragments.get(0);
		chat = (Chat) fragments.get(1);

		return START_STICKY;
	}

	public static void hideSoftKeyboard(Activity activity) {

		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), 0);
	}

	/**
	 * Opens the Databases
	 */
	private void setupDatabases() {
		contactsDB = new SQLiteContactsDataSource(MainService.this);
		contactsDB.open();
		messagesDB = new SQLiteMessagesDataSource(MainService.this);
		messagesDB.open();
	}

	public static void createContact(String email, String name) {
		contactsDB.createContact(email, name);
		contacts.refreshList();
	}

	@SuppressWarnings("deprecation")
	public static void createMessage(String content, String email,
			String source, String time) {
		messagesDB.createMessage(URLDecoder.decode(content), email, source,
				time);
		chat.refreshList();
	}

	public static List<SQLiteContacts> getAllContacts() {
		return contactsDB.getAllContacts();
	}

	public static void deleteContact(int id) {
		contactsDB.deleteContact(id);
		messagesDB.deleteChat(id);
		contacts.refreshList();
	}

	public static void deleteChat(String email) {
		messagesDB.deleteChat(email);
		chat.refreshList();
	}

	public static List<Fragment> getFragments() {
		return MainService.fragments;
	}

	public static String filter(String content) {
		String filtered = content;
		filtered = filtered.replace("+", "%2b");
		filtered = filtered.replace("&", "%26");
		filtered = filtered.replace("%", "%25");
		return filtered;
	}
}
