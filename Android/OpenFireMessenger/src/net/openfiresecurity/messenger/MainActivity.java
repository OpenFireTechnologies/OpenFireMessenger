/*
 * Copyright (c) 2013. Alexander Martinz.
 */

package net.openfiresecurity.messenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO Splash Screen
        //setContentView(R.layout.activity_main);
        startActivity(new Intent(MainActivity.this, Menu.class));
        finish();
    }
}