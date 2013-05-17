/*
 * Copyright (c) 2013. Alexander Martinz @ OpenFire Security
 */

package net.openfiresecurity.messenger;

import net.openfiresecurity.auth.AuthenticatorActivity;
import net.openfiresecurity.helper.ResourceManager;

import org.holoeverywhere.app.Activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Splash extends Activity {

	// Accounts
	public Account[] accounts;

	// Request Code for Login
	private final int REQ = 1111;

	// Screen Elements
	private TextView tvSplashProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		tvSplashProgress = (TextView) findViewById(R.id.tvSplashProgress);
		tvSplashProgress.setText("Loading Resources...");

		// Load All
		new ResourceManager(this);

		tvSplashProgress.setTypeface(ResourceManager.L_STEINER);

		tvSplashProgress.setText("Starting Service...");

		startService(new Intent(Splash.this, MainService.class));

		tvSplashProgress.setText("Looking for Accounts...");

		accounts = AccountManager.get(getBaseContext()).getAccountsByType(
				"net.openfiresecurity.messenger");
		if (accounts.length > 0) {
			if (AccountManager.get(Splash.this)
					.getUserData(accounts[0], "hash").isEmpty()) {
				tvSplashProgress.setText("Waiting for Login...");
				startActivityForResult(new Intent(Splash.this,
						AuthenticatorActivity.class), REQ);
			}

			tvSplashProgress.setText("Starting Chat...");

			Intent i = new Intent(Splash.this, MainView.class);
			startActivity(i);
			finish();
		} else {
			tvSplashProgress.setText("Waiting for Login...");
			startActivityForResult(new Intent(Splash.this,
					AuthenticatorActivity.class), REQ);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if ((requestCode == REQ) && (resultCode == RESULT_OK)) {
			accounts = AccountManager.get(getBaseContext()).getAccountsByType(
					"net.openfiresecurity.messenger");
			tvSplashProgress.setText("Starting Chat...");
			Intent i = new Intent(Splash.this, MainView.class);
			startActivity(i);
		}
		finish();
	}
}