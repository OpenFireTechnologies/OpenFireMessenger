/*
 * Copyright (c) 2013. Alexander Martinz @ OpenFire Security
 */

package net.openfiresecurity.data;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.preference.PreferenceFragment;

import net.openfiresecurity.messenger.R;

public class Preferences extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .add(new PrefFragment(), "PrefFragment").commit();
    }

    public static class PrefFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs_inner);
        }
    }
}
