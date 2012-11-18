package appspot.smartboxsmu;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import appspot.gcm.example.CommonUtilities;
import appspot.smartboxsmu.model.Group;
import appspot.smartboxsmu.network.NetworkRequestFactory;
import appspot.smartboxsmu.network.Util;
import appspot.smartboxsmu.parcelable.ChatMessage;

import com.google.android.gcm.GCMRegistrar;

public class SendMessagePOSTRequest extends NetworkRequestFactory {
	private Group group;
	private String msg;
	
	public SendMessagePOSTRequest(Context context, Group group, String message) {
		super(context, null, true);
		this.group = group;
		msg = message;
	}

	@Override
	public HttpUriRequest sendDataToServer(String url) throws JSONException,
			UnsupportedEncodingException {
		HttpPost post = getHttpPost(url);
		JSONObject obj = new JSONObject();
		obj.put("groupKey", group.getEncodedKey());
		obj.put("userKey", MainApplication.user.getEncodedKey());
		obj.put("message", msg);

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