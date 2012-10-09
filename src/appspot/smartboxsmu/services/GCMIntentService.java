package appspot.smartboxsmu.services;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import appspot.gcm.example.CommonUtilities;
import appspot.smartboxsmu.HomeActivity;
import appspot.smartboxsmu.LoginActivity;
import appspot.smartboxsmu.MainApplication;
import appspot.smartboxsmu.R;
import appspot.smartboxsmu.LoginActivity.POSTRequest;
import appspot.smartboxsmu.network.CustomProgressDialog;
import appspot.smartboxsmu.network.NetworkRequestFactory;
import appspot.smartboxsmu.network.URL;
import appspot.smartboxsmu.network.Util;
import appspot.smartboxsmu.parcelable.User;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	/*
	 * onRegistered(Context context, String regId): Called after a registration
	 * intent is received, passes the registration ID assigned by GCM to that
	 * device/application pair as parameter. Typically, you should send the
	 * regid to your server so it can use it to send messages to this device.
	 * 
	 * onUnregistered(Context context, String regId): Called after the device
	 * has been unregistered from GCM. Typically, you should send the regid to
	 * the server so it unregisters the device.
	 * 
	 * onMessage(Context context, Intent intent): Called when your server sends
	 * a message to GCM, and GCM delivers it to the device. If the message has a
	 * payload, its contents are available as extras in the intent.
	 * 
	 * onError(Context context, String errorId): Called when the device tries to
	 * register or unregister, but GCM returned an error. Typically, there is
	 * nothing to be done other than evaluating the error (returned by errorId)
	 * and trying to fix the problem.
	 * 
	 * onRecoverableError(Context context, String errorId): Called when the
	 * device tries to register or unregister, but the GCM servers are
	 * unavailable. The GCM library will retry the operation using exponential
	 * backup, unless this method is overridden and returns false. This method
	 * is optional and should be overridden only if you want to display the
	 * message to the user or cancel the retry attempts.
	 */
	private static String TAG = "GCMIntentService";
	
	@Override
	protected void onError(Context context, String errorId) {
		Log.e(TAG, "Intent Error");
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		//TODO: Add to the chat list or show notification
		
		
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		//send regId that is tied to the device back to the server for this particular user
		MainApplication.user.setDeviceRegId(regId);
		POSTRequest post = new POSTRequest(this);
		post.execute(URL.REGISTER_DEVICE);
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		// TODO unregister device or make the state inactive.

	}
	
	public class POSTRequest extends NetworkRequestFactory {
		
		public POSTRequest(Context context) {
			super(context, null, true);
		}
		
		@Override
		public HttpUriRequest sendDataToServer(String url)
				throws JSONException, UnsupportedEncodingException {
			HttpPost post = getHttpPost(url);
			JSONObject obj = new JSONObject();
			obj.put("encodedKey", MainApplication.user.getEncodedKey());
			obj.put("deviceRegId", MainApplication.user.getDeviceRegId());

			StringEntity se = new StringEntity(obj.toString());
			post.setEntity(se);
			post.setHeader("Accept", "application/json");
			post.setHeader("Content-type", "application/json");
			return post;
		}

		@Override
		public void parseResponseFromServer(String result) {
			if (statusCode == HttpStatus.SC_OK) {
				CommonUtilities.displayMessage(context, "success");
			} else {
				//Display error messages
				// Expected {error: String} JSON
				JSONObject obj;
				try {
					obj = new JSONObject(result);
					Util.alertToast(context, obj.getString("error"));
				} catch (JSONException e) {
					Log.e("Util", "JSON Parsing Error");
				}
			}
		}

		@Override
		public void additionalExceptionHandling() {
			//EMPTY
		}
	}
	

}
