/*
 * Copyright (c) 2013. Alexander Martinz @ OpenFire Security
 */

package net.openfiresecurity.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.openfiresecurity.messenger.MainService;

public class BootupReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, MainService.class));
    }
}
