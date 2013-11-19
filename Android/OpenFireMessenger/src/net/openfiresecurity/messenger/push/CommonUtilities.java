/*
 * Copyright (c) 2013. Alexander Martinz @ OpenFire Security
 */

package net.openfiresecurity.messenger.push;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {

    // give your server registration url here
    public static final String SERVER_URL = "http://android.openfire-security.net/messenger/register.php";

    // Google project id
    public static final String SENDER_ID = "YOUR_SENDER_ID";

    /**
     * Tag used on log messages.
     */
    public static final String TAG = "OpenFireMessenger GCM";

    public static final String DISPLAY_MESSAGE_ACTION = "net.openfiresecurity.messenger.DISPLAY_MESSAGE";

    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_EMAIL = "email";
    public static final String EXTRA_TIME = "time";
    public static final String EXTRA_FILE = "isFile";

    /**
     * Notifies UI to display a message.
     * <p/>
     * This method is defined in the common helper because it's used both by the
     * UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message,
                                      String email, String time) {

        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_EMAIL, email);
        intent.putExtra(EXTRA_TIME, time);
        context.sendBroadcast(intent);
    }

}
