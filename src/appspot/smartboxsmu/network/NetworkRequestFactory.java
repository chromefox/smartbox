package appspot.smartboxsmu.network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public abstract class NetworkRequestFactory extends AsyncTask<String, Void, String> {
	protected Context context;
	protected CustomProgressDialog dialog;
	protected Exception exception = null;
	protected boolean showError;
	protected boolean toast;
	protected int statusCode = 0;

	public abstract HttpUriRequest sendDataToServer(String url) throws JSONException, UnsupportedEncodingException;
	public abstract void parseResponseFromServer(String result);
	
	/**
	 * This is where you can implement behaviour to handle network exception (showing error page with refresh button, etc) because
	 * parseResponseFromServer will not be called.
	 */
	public abstract void additionalExceptionHandling();

	public NetworkRequestFactory(Context context) {
		this.context = context;
	}

	/**
	 * Use this constructor when you are showing a progress dialog. It will ensure that the progress dialog will be closed regardless of any error which 
	 * might be encountered. 
	 * This constructor defaults to not showing any network error encountered. Specify argument showError (boolean) otherwise.
	 * @param context The activity from which the AsyncTask is called
	 * @param dialog. The shown CustomProgressDialog to be closed
	 */
	public NetworkRequestFactory(Context context, CustomProgressDialog dialog) {
		this(context,dialog,false,true);
	}
	
	/**
	 * Use this constructor when you are showing a progress dialog. It will ensure that the progress dialog will be closed regardless of any error which 
	 * might be encountered. 
	 * @param context The activity from which the AsyncTask is called
	 * @param dialog. The shown CustomProgressDialog to be closed
	 * @param showError specify whether or not to show error. If true, will show error in Toast Mode.
	 */
	public NetworkRequestFactory(Context context, CustomProgressDialog dialog, boolean showError) {
		this(context,dialog,showError,true);
	}
	
	/**
	 * Use this constructor when you are showing a progress dialog. It will ensure that the progress dialog will be closed regardless of any error which 
	 * might be encountered. Toast mode is the default mode used.
	 * @param context The activity from which the AsyncTask is called
	 * @param dialog. The shown CustomProgressDialog to be closed
	 * @param showError specify whether or not to show error
	 * @param toast If true, non-blocking Toast Mode will be used. Otherwise, Dialog Box will be used to display error to user instead.
	 */
	public NetworkRequestFactory(Context context, CustomProgressDialog dialog, boolean showError, boolean toast) {
		this.context = context;
		this.dialog = dialog;
		this.showError = showError;
		this.toast = toast;
	}

	@Override
	protected final String doInBackground(String... urls) {
		String response = "";
		DefaultHttpClient client = new DefaultHttpClient();
		if(Util.isNetworkAvailable(context)) {
			for (String url : urls) {
				try {
					HttpUriRequest req = sendDataToServer(url);

					HttpResponse execute = client.execute(req);
					InputStream content = execute.getEntity().getContent();
					BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
					String s = "";
					while ((s = buffer.readLine()) != null) {
						response += s;
					}
					
					if(execute.getStatusLine() != null) statusCode = execute.getStatusLine().getStatusCode();
				} catch (ConnectTimeoutException e) {
					Log.e(this.getClass().toString(), "ConnectTimeoutException: " + e.toString());
					exception = e;
				} catch (SocketTimeoutException e) {
					Log.e(this.getClass().toString(), "SocketTimeoutException: " + e.toString());
					exception = e;
				} catch (Exception e) {
					Log.e(this.getClass().toString(), "Other exceptions: " + e);
					exception = e;
				}
			}
		} else {
			exception = new NoNetworkException();
		}
		return response;
	}

	protected HttpPost getHttpPost(String url) {
		HttpPost post = new HttpPost(url);
		HTTPBuilder.setParameter(post);
		return post;
	}
	
	protected HttpGet getHttpGet(String url) {
		HttpGet get = new HttpGet(url);
		HTTPBuilder.setParameter(get);
		return get;
	}

	protected HttpPut getHttpPut(String url) {
		HttpPut put = new HttpPut(url);
		HTTPBuilder.setParameter(put);
		return put;
	}
	
	public void setCustomProgressDialog(CustomProgressDialog dialog) {
		this.dialog = dialog;
	}

	@Override
	protected final void onPostExecute(String result) {
		boolean error = false;
		if(dialog != null) dialog.dismiss();
		if(Util.handleConnectionException(context, exception, showError, toast))  error = true;
		
		if(error) {
			additionalExceptionHandling();
			return;
		}

		parseResponseFromServer(result);
	}
}