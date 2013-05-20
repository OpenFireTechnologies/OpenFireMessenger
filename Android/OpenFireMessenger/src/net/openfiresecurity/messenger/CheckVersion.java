/*
 * Copyright (c) 2013. Alexander Martinz.
 */

package net.openfiresecurity.messenger;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import net.openfiresecurity.helper.Constants;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class CheckVersion extends AsyncTask<String, Void, String> {

    private final ProgressDialog dialog;
    private final Menu c;

    public CheckVersion(Menu context) {
        c = context;
        dialog = new ProgressDialog(c);
    }

    @Override
    protected void onPreExecute() {
        dialog.setTitle("Checking!");
        dialog.setMessage("Checking for available Updates!");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        dialog.dismiss();
        c.update(result);
    }

    @NotNull
    @Override
    protected String doInBackground(String... params) {
        @NotNull HttpClient httpClient = new DefaultHttpClient();
        @NotNull HttpPost httpPost = new HttpPost(Constants.URL + Constants.versionFile);
        try {
            httpPost.setHeader("User-Agent", "ImageUploaderAlex");
            HttpResponse httpResponse = httpClient.execute(httpPost);
            return (entityToString(httpResponse.getEntity()));
        } catch (Exception uee) {
        }
        return "-1";
    }

    @NotNull
    String entityToString(@NotNull HttpEntity entity) {
        @Nullable InputStream is = null;
        @Nullable StringBuilder str = null;
        try {
            is = entity.getContent();
            @NotNull BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(is));
            str = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                str.append(line);
            }
        } catch (IOException e) {
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
        }
        return str != null ? str.toString() : "";
    }
}
