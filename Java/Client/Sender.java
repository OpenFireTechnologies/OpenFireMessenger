import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Sender extends Thread {
	// Variables, giving values via constructor
	String urlParameters, urlRequest;
	boolean urlPost;
	GUI parentClass;

	/**
	 * Constructor of the Sender Class.
	 * 
	 * @URL The URL, to where the Request should get send
	 * @isPost TRUE for POST, FALSE for GET
	 * @parameters Parameters of the Request
	 * @c The GUI
	 */
	public Sender(String URL, boolean isPost, String parameters, GUI c) {
		urlParameters = parameters;
		urlRequest = URL;
		urlPost = isPost;
		parentClass = c;
	}

	@Override
	public void run() {
		try {
			URL url = new URL(urlRequest);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod((urlPost ? "POST" : "GET"));
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(urlParameters.getBytes().length));
			connection.setUseCaches(false);

			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			String line = "";
			StringBuilder sb = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
			wr.close();
			connection.disconnect();
			parentClass.getResult(sb.toString());
		} catch (Exception exc) {
			// TODO Show Error to Debug Console
		}
	}
}
