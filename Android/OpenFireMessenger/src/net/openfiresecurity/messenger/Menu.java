/*
 * Copyright (c) 2013. Alexander Martinz.
 */

package net.openfiresecurity.messenger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.openfiresecurity.helper.Constants;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Alex on 20.05.13.
 */
public class Menu extends Activity implements View.OnClickListener {

    private Button bExit, bUpdate, bDebug, bRegister, bLogin;
    private EditText etUserMenu, etPassMenu;
    private DownloadManager mgr;
    private Request req;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //Download Manager
        mgr = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        //Buttons
        bExit = (Button) findViewById(R.id.bExit);
        bUpdate = (Button) findViewById(R.id.bUpdate);
        bDebug = (Button) findViewById(R.id.bDebug);
        bRegister = (Button) findViewById(R.id.bMenuSignUp);
        bLogin = (Button) findViewById(R.id.bMenuLogin);
        //Button OnClickListeners
        bExit.setOnClickListener(this);
        bUpdate.setOnClickListener(this);
        bDebug.setOnClickListener(this);
        bRegister.setOnClickListener(this);
        bLogin.setOnClickListener(this);
        //Edit Texts
        etUserMenu = (EditText)findViewById(R.id.etUserNameLogin);
        etPassMenu = (EditText)findViewById(R.id.etPasswordLogin);

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
            case R.id.bDebug:
                startActivity(new Intent(Menu.this, DEBUG.class));
                break;
            case R.id.bMenuSignUp:
                //TODO Handle SignUp
                break;
            case R.id.bMenuLogin:
                //TODO Handle Login
                break;
        }
    }

    public void update(final String result) {
        try {
            @NotNull String version = getVersionNumber();
            if (Integer.parseInt(version) < Integer.parseInt(result)) {
                @NotNull AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Update Available!").setMessage("A new Update is available!\nUpdate now?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        @NotNull String appname = Constants.fileName + result + ".apk";
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
                    public void onClick(@NotNull DialogInterface dialogInterface, int i) {
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