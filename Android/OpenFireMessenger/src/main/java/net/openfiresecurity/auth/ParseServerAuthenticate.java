/*
 * Copyright (c) 2013. Alexander Martinz @ OpenFire Security
 */

package net.openfiresecurity.auth;

import java.io.IOException;
import java.io.Serializable;

import net.openfiresecurity.helper.Constants;
import net.openfiresecurity.helper.CustomMultiPartEntity;
import net.openfiresecurity.helper.CustomMultiPartEntity.ProgressListener;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

public class ParseServerAuthenticate implements ServerAuthenticate {
	@Override
	public String userSignUp(String name, String email, String pass)
			throws Exception {

		String url = Constants.MSGURL + Constants.USER + Constants.SIGNUP;

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);

		CustomMultiPartEntity multipartContent = new CustomMultiPartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE, new ProgressListener() {
					@Override
					public void transferred(long num) {
					}
				});

		multipartContent.addPart("username", new StringBody(name));
		multipartContent.addPart("password", new StringBody(pass));
		multipartContent.addPart("email", new StringBody(email));

		httpPost.setEntity(multipartContent);

		String authtoken = null;
		int statuscode;
		try {
			HttpResponse response = httpClient.execute(httpPost);
			String responseString = EntityUtils.toString(response.getEntity());

			if (response.getStatusLine().getStatusCode() != 200) {
				ParseComError error = new Gson().fromJson(responseString,
						ParseComError.class);
				throw new Exception(error.statuscode + " - " + error.error);
			}

			User createdUser = new Gson().fromJson(responseString, User.class);
			authtoken = createdUser.hash;
			statuscode = createdUser.statuscode;
			if (statuscode != 0) {
				ParseComError error = new Gson().fromJson(responseString,
						ParseComError.class);
				throw new Exception(error.statuscode + " - " + error.error);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return authtoken;
	}

	@Override
	public String userSignIn(String user, String email, String pass)
			throws Exception {

		DefaultHttpClient httpClient = new DefaultHttpClient();
		String url = Constants.MSGURL + Constants.USER + Constants.LOGIN;

		HttpPost httpPost = new HttpPost(url);

		CustomMultiPartEntity multipartContent = new CustomMultiPartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE, new ProgressListener() {
					@Override
					public void transferred(long num) {
					}
				});

		multipartContent.addPart("username", new StringBody(user));
		multipartContent.addPart("password", new StringBody(pass));
		multipartContent.addPart("email", new StringBody(email));

		httpPost.setEntity(multipartContent);

		String authtoken = null;
		int statuscode;
		try {
			HttpResponse response = httpClient.execute(httpPost);

			String responseString = EntityUtils.toString(response.getEntity());
			if (response.getStatusLine().getStatusCode() != 200) {
				ParseComError error = new Gson().fromJson(responseString,
						ParseComError.class);
				throw new Exception(error.statuscode + " - " + error.error);
			}

			User loggedUser = new Gson().fromJson(responseString, User.class);
			authtoken = loggedUser.hash;
			statuscode = loggedUser.statuscode;
			if (statuscode != 0) {
				ParseComError error = new Gson().fromJson(responseString,
						ParseComError.class);
				throw new Exception(error.statuscode + " - " + error.error);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return authtoken;
	}

	private class ParseComError implements Serializable {

		private static final long serialVersionUID = 1L;
		int statuscode;
		String error;
	}

	private class User implements Serializable {

		private static final long serialVersionUID = 10L;
		private int statuscode;
		public String hash;

		@SuppressWarnings("unused")
		public int getStatuscode() {
			return statuscode;
		}

		@SuppressWarnings("unused")
		public void setStatuscode(int statuscode) {
			this.statuscode = statuscode;
		}

		@SuppressWarnings("unused")
		public String getHash() {
			return hash;
		}

		@SuppressWarnings("unused")
		public void setHash(String hash) {
			this.hash = hash;
		}
	}
}
