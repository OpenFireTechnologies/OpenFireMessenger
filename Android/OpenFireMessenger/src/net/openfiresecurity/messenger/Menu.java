/*
 * Copyright (c) 2013. Alexander Martinz.
 */

package net.openfiresecurity.messenger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.*;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import net.openfiresecurity.helper.Constants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Alex on 20.05.13.
 */
public class Menu extends Activity implements View.OnClickListener {

    private Button bExit, bUpdate;
    private DownloadManager mgr;
    private Request req;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mgr = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        bExit = (Button) findViewById(R.id.bExit);
        bUpdate = (Button) findViewById(R.id.bUpdate);
        bExit.setOnClickListener(this);
        bUpdate.setOnClickListener(this);

        @NotNull BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, @NotNull Intent intent) {
                @Nullable String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    @NotNull Intent i = new Intent();
                    i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
                    startActivity(i);
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void onClick(@NotNull View view) {
        switch (view.getId()) {
            case R.id.bExit:
                System.exit(0);
                break;
            case R.id.bUpdate:
                new CheckVersion(Menu.this).execute();
                break;
        }
    }

    public void update(final String result) {
        try {
            @NotNull String version = getVersionNumber();
            if (Integer.parseInt(version) < Integer.parseInt(result)) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Update Available!").setMessage("A new Update is available!\nUpdate now?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String appname = Constants.fileName + result + ".apk";
                        req = new Request(Uri.parse(Constants.urls + appname));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                        }
                        req.setDescription("Updating!");
                        req.setTitle(appname);
                        req.setMimeType("application/vnd.android.package-archive");
                        req.setDestinationInExternalPublicDir(
                                Environment.DIRECTORY_DOWNLOADS, appname);

                        mgr.enqueue(req);
                        makeToast("Downloading!");
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                dialog.show();

            } else {
                makeToast("No new Update available!");
            }
        } catch (Exception exc) {
            Log.d("MESSENGER", exc.getLocalizedMessage());
            makeToast("Couldnt contact Update Server!");
        }
    }

    void makeToast(String msg) {
        Toast.makeText(Menu.this, msg, Toast.LENGTH_LONG).show();
    }

    @NotNull
    private String getVersionNumber() {
        int version = -1;
        try {
            version = getPackageManager().getPackageInfo("net.openfiresecurity.messenger", 0).versionCode;
        } catch (Exception e) {
        }
        return ("" + version);
    }
}