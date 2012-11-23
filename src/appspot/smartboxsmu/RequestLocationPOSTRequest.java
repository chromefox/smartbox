package appspot.smartboxsmu;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.sax.StartElementListener;
import android.util.Log;
import appspot.smartboxsmu.helpers.CalendarEventHelper;
import appspot.smartboxsmu.model.Group;
import appspot.smartboxsmu.network.NetworkRequestFactory;
import appspot.smartboxsmu.network.Util;
import appspot.smartboxsmu.parcelable.FindSlotResult;
import appspot.smartboxsmu.parcelable.LocationSuggestions;
import appspot.smartboxsmu.parcelable.UserEvent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RequestLocationPOSTRequest extends NetworkRequestFactory {
	private Group group;

	public RequestLocationPOSTRequest(Context context, Group group) {
		super(context);
		this.group = group;
	}

	@Override
	public HttpUriRequest sendDataToServer(String url) throws JSONException,
			UnsupportedEncodingException {
		JSONObject json = new JSONObject();
		json.put("userKey", MainApplication.user.getEncodedKey());
		json.put("groupKey", group.getEncodedKey());

		StringEntity se = new StringEntity(json.toString());

		// setting httpPost Params
		HttpPost post = getHttpPost(url);
		post.setEntity(se);
		post.setHeader("Accept", "application/json");
		post.setHeader("Content-type", "application/json");
		return post;
	}

	@Override
	public void parseResponseFromServer(String result) {
		if (statusCode == HttpStatus.SC_OK) {
			Gson gson = new GsonBuilder().create();
			Group group = gson.fromJson(result, Group.class);
			// set new group data and refresh listview

			if (group != null) {
				GroupPageActivity act = (GroupPageActivity) context;
				act.locationCallback(group);
			} else {
				Util.alertToast(context, "error");
			}
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