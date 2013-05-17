/*
 * Copyright (c) 2013. Alexander Martinz @ OpenFire Security
 */

package net.openfiresecurity.messenger;

import static net.openfiresecurity.messenger.push.CommonUtilities.SENDER_ID;
import static net.openfiresecurity.messenger.push.CommonUtilities.displayMessage;

import java.net.URLDecoder;

import net.openfiresecurity.messenger.push.ServerUtilities;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	public GCMIntentService() {
		super(SENDER_ID);
	}

	/**
	 * Method called on device registered
	 */
	@Override
	protected void onRegistered(Context context, String registrationId) {
		ServerUtilities.register(context, MainView.nameOwn, MainView.emailOwn,
				registrationId);
	}

	/**
	 * Method called on device un registred
	 */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		ServerUtilities.unregister(context, registrationId);
	}

	/**
	 * Method called on Receiving a new message
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void onMessage(Context context, Intent intent) {
		try {
			String title = intent.getExtras().getString("title");
			String time = URLDecoder.decode(intent.getExtras()
					.getString("time"));
			String content = URLDecoder.decode(intent.getExtras().getString(
					"content"));

			displayMessage(context, content, title, time);
			if (!MainService.isActive) {
				generateNotification(context, content, title);
			}
		} catch (Exception exc) {
			Log.e("GCM", "Error: " + exc.getMessage());
		}

		// Play default notification sound
		if (MainService.prefs.getBoolean("notificationSound")) {
			try {
				Uri notification = RingtoneManager
						.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
				Ringtone r = RingtoneManager.getRingtone(
						getApplicationContext(), notification);
				r.play();
			} catch (Exception e) {
				Log.e("GCM", "Notification Error: " + e.getMessage());
			}
		}

		// Vibrate if vibrate is enabled
		if (MainService.prefs.getBoolean("notificationVibrate")) {
			MainService.sVibrator.vibrate(200);
		}
	}

	/**
	 * Method called on receiving a deleted message
	 */
	@Override
	protected void onDeletedMessages(Context context, int total) {
	}

	/**
	 * Method called on Error
	 */
	@Override
	public void onError(Context context, String errorId) {
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	@SuppressWarnings("deprecation")
	private static void generateNotification(Context context, String message,
			String title) {
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = new Notification(icon, message, when);

		Intent notificationIntent = new Intent(context, MainView.class);
		notificationIntent.putExtra("contact", title);
		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, title, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// Sound
		// notification.sound = Uri.parse("android.resource://" +
		// context.getPackageName() + "your_sound_file_name.mp3");

		notificationManager.notify(0, notification);

	}
}
