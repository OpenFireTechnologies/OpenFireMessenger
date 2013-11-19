/*
 * Copyright (c) 2013. Alexander Martinz @ OpenFire Security
 */

package net.openfiresecurity.messenger.push;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {

    private final Context _context;

    public ConnectionDetector(Context context) {
        _context = context;
    }

    /**
     * Checking for all possible internet providers
     */
    public boolean isConnectingToInternet() {

        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] info = connectivity.getAllNetworkInfo();
        if (info != null) {
            for (NetworkInfo element : info) {
                if (element.getState() == NetworkInfo.State.CONNECTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
