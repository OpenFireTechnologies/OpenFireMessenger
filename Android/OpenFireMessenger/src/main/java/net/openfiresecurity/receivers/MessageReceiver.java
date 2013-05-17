/*
 * Copyright (c) 2013. Alexander Martinz @ OpenFire Security
 */

package net.openfiresecurity.receivers;

import static net.openfiresecurity.messenger.push.CommonUtilities.EXTRA_EMAIL;
import static net.openfiresecurity.messenger.push.CommonUtilities.EXTRA_MESSAGE;
import static net.openfiresecurity.messenger.push.CommonUtilities.EXTRA_TIME;
import net.openfiresecurity.helper.WakeLocker;
import net.openfiresecurity.messenger.MainService;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MessageReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		boolean found = false;

		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (MainService.class.getName().equals(
					service.service.getClassName())) {
				found = true;
				break;
			}
		}

		if (!found) {
			context.startService(new Intent(context, MainService.class));
		}
		String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
		String email = intent.getExtras().getString(EXTRA_EMAIL);
		String time = intent.getExtras().getString(EXTRA_TIME);
		// boolean isfile = intent.getExtras().getBoolean(EXTRA_FILE);

		boolean wakelock = MainService.prefs.getBoolean("notificationWakelock");
		if (wakelock) {
			WakeLocker.acquire(context.getApplicationContext());
		}
		MainService.createMessage(newMessage, email, "0", time);
		if (wakelock) {
			WakeLocker.release();
		}
	}
}