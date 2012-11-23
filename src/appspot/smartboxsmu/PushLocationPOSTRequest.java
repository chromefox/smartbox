package appspot.smartboxsmu;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import appspot.smartboxsmu.network.Names;
import appspot.smartboxsmu.network.NetworkRequestFactory;
import appspot.smartboxsmu.network.Util;

import com.google.android.gcm.GCMRegistrar;

public class PushLocationPOSTRequest extends NetworkRequestFactory {
	private double latitude;
	private double longitude;
	
	public PushLocationPOSTRequest(Context context,double latitude, double longitude) {
		super(context, null, true);
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	@Override
	public HttpUriRequest sendDataToServer(String url)
			throws JSONException, UnsupportedEncodingException {
		HttpPost post = getHttpPost(url);
		JSONObject obj = new JSONObject();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String key = prefs.getString(Names.USER_KEY, "");
		
		obj.put("userKey", key);
		obj.put("latitude", latitude);
		obj.put("longitude", longitude);
		
		StringEntity se = new StringEntity(obj.toString());
		post.setEntity(se);
		post.setHeader("Accept", "application/json");
		post.setHeader("Content-type", "application/json");
		return post;
	}

	@Override
	public void parseResponseFromServer(String result) {
		if (statusCode == HttpStatus.SC_OK) {
			//
			
			
			
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