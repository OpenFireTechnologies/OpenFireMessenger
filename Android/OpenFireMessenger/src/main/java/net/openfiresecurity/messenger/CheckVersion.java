/*
 * Copyright (c) 2013. Alexander Martinz @ OpenFire Security
 */

package net.openfiresecurity.messenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.openfiresecurity.helper.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

class CheckVersion extends AsyncTask<String, Void, String> {

	private final MainView c;

	public CheckVersion(MainView context) {
		c = context;
	}

	@Override
	protected void onPostExecute(String result) {
		c.update(result);
	}

	@Override
	protected String doInBackground(String... params) {

		HttpClient httpClient = new DefaultHttpClient();

		HttpPost httpPost = new HttpPost(Constants.URL + Constants.versionFile);
		try {
			httpPost.setHeader("User-Agent", "OpenFireMessengerAlex");
			HttpResponse httpResponse = httpClient.execute(httpPost);
			return (entityToString(httpResponse.getEntity()));
		} catch (Exception uee) {
			Log.e("Updater", "Error: " + uee.getMessage());
		}
		return "-1";
	}

	String entityToString(HttpEntity entity) {

		InputStream is = null;

		StringBuilder str = null;
		try {
			is = entity.getContent();

			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(is));
			str = new StringBuilder();

			String line;
			while ((line = bufferedReader.readLine()) != null) {
				str.append(line);
			}
		} catch (IOException e) {
			Log.e("Updater", "Error: " + e.getMessage());
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				Log.e("Updater", "Error: " + e.getMessage());
			}
		}
		return str != null ? str.toString() : "";
	}
}
