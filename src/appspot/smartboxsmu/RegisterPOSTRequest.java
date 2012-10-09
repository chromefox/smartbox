package appspot.smartboxsmu;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;

import android.content.Context;
import android.util.Log;
import appspot.gcm.example.CommonUtilities;
import appspot.smartboxsmu.network.NetworkRequestFactory;
import appspot.smartboxsmu.network.Util;

public class RegisterPOSTRequest extends NetworkRequestFactory {
	
	public RegisterPOSTRequest(Context context) {
		super(context, null, true);
	}
	
	@Override
	public HttpUriRequest sendDataToServer(String url)
			throws JSONException, UnsupportedEncodingException {
		HttpPost post = getHttpPost(url);
		JSONObject obj = new JSONObject();
		obj.put("encodedKey", MainApplication.user.getEncodedKey());
		obj.put("deviceRegId", MainApplication.user.getDeviceRegId());
		obj.put("email", MainApplication.user.getEmail());
		
		StringEntity se = new StringEntity(obj.toString());
		post.setEntity(se);
		post.setHeader("Accept", "application/json");
		post.setHeader("Content-type", "application/json");
		return post;
	}

	@Override
	public void parseResponseFromServer(String result) {
		if (statusCode == HttpStatus.SC_OK) {
			GCMRegistrar.setRegisteredOnServer(context, true);
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