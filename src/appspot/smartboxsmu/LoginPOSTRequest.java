package appspot.smartboxsmu;

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
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import appspot.smartboxsmu.network.CustomProgressDialog;
import appspot.smartboxsmu.network.Names;
import appspot.smartboxsmu.network.NetworkRequestFactory;
import appspot.smartboxsmu.network.Util;
import appspot.smartboxsmu.parcelable.User;

public class LoginPOSTRequest extends NetworkRequestFactory {

	private String email;
	private String password;

	public LoginPOSTRequest(Context context, String email, String password) {
		super(context, null, true);
		dialog = CustomProgressDialog.show(context, "", "Signing in");
		super.setCustomProgressDialog(dialog);
		this.email = email;
		this.password = password;
	}

	@Override
	public HttpUriRequest sendDataToServer(String url) throws JSONException,
			UnsupportedEncodingException {
		HttpPost post = getHttpPost(url);
		JSONObject obj = new JSONObject();
		obj.put("email", email);
		obj.put("password", password);

		StringEntity se = new StringEntity(obj.toString());
		post.setEntity(se);
		post.setHeader("Accept", "application/json");
		post.setHeader("Content-type", "application/json");

		return post;
	}

	@Override
	public void parseResponseFromServer(String result) {
		if (statusCode == HttpStatus.SC_OK) {
			// Store User Data returned from the server on Main Application
			// global variable
			User user = new User();
			user.mapObject(result);
			MainApplication.user = user;

			// Persist userKey here
			SharedPreferences.Editor prefs = PreferenceManager
					.getDefaultSharedPreferences(context).edit();

			prefs.putString(Names.USER_KEY, user.getEncodedKey());
			prefs.putString(Names.USER_MOBILE, user.getMobileNumber());

			prefs.commit();

			// Switch to other activity
			Activity act = (Activity) context;
			Intent intent = new Intent(act, HomeActivity.class);
			intent.putExtra("user", user);
			act.startActivity(intent);
			// Destroy current Activity
			act.finish();
		} else {
			// Display error messages
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
		// EMPTY
	}

}