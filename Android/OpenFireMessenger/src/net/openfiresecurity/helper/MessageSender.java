/*
 * Copyright (c) 2013. Alexander Martinz @ OpenFire Security
 */

package net.openfiresecurity.helper;

import android.os.AsyncTask;
import android.util.Log;

import net.openfiresecurity.helper.CustomMultiPartEntity.ProgressListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MessageSender extends AsyncTask<String, Integer, String> {

    @SuppressWarnings("deprecation")
    @Override
    protected String doInBackground(String... params) {

        String url = Constants.MSGURL + Constants.EXCHANGER;
        String from = params[0];
        String to = params[1];
        String content = params[2];
        String hash = params[3];

        HttpClient httpClient = new DefaultHttpClient();

        HttpContext httpContext = new BasicHttpContext();

        HttpPost httpPost = new HttpPost(url);
        try {

            CustomMultiPartEntity multipartContent = new CustomMultiPartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE,
                    new ProgressListener() {
                        @Override
                        public void transferred(long num) {
                        }
                    });

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm",
                    Locale.getDefault());
            String currentTime = sdf.format(new Date());

            multipartContent.addPart("from", new StringBody(from));
            multipartContent.addPart("to", new StringBody(to));
            multipartContent.addPart("content",
                    new StringBody(URLEncoder.encode(content)));
            multipartContent.addPart("hash", new StringBody(hash));
            multipartContent.addPart("time",
                    new StringBody(URLEncoder.encode(currentTime)));

            httpPost.setEntity(multipartContent);
            try {
                HttpResponse httpResponse = httpClient.execute(httpPost,
                        httpContext);
                return (entityToString(httpResponse.getEntity()));
            } catch (Exception exc) {
                return (exc.getMessage());
            }
        } catch (Exception excp) {
            return (excp.getMessage());
        }
    }

    /**
     * Converts HttpEntities into readable text.
     *
     * @param entity Entity, which should get converted to a String.
     */

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
            Log.e(Constants.TAG, e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e(Constants.TAG, e.getMessage());
            }
        }
        return str.toString();
    }
}
