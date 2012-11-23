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
import appspot.smartboxsmu.network.CustomProgressDialog;
import appspot.smartboxsmu.network.NetworkRequestFactory;
import appspot.smartboxsmu.network.Util;
import appspot.smartboxsmu.parcelable.FindSlotResult;
import appspot.smartboxsmu.parcelable.LocationSuggestions;
import appspot.smartboxsmu.parcelable.UserEvent;

import com.google.gson.Gson;

public class CalendarEventPOSTRequest extends NetworkRequestFactory {
	private FindSlotResult slotResult;
	private String name;
	private String location;
	private LocationSuggestions loc;
	private Group group;

	public CalendarEventPOSTRequest(Context context, FindSlotResult result,
			String name, String location, Group group, LocationSuggestions loc) {
		super(context);
		dialog = CustomProgressDialog.show(context, "", "Adding events to your friends");
		super.setCustomProgressDialog(dialog);
		this.slotResult = result;
		this.name = name;
		this.location = location;
		this.group = group;
		this.loc = loc;
	}

	@Override
	public HttpUriRequest sendDataToServer(String url) throws JSONException,
			UnsupportedEncodingException {
		String userEvent = new Gson().toJson(UserEvent.instantiate(slotResult,
				name));
		JSONObject obj = new JSONObject(userEvent);
		JSONObject json = new JSONObject();
		json.put("event", obj);
		json.put("userKey", MainApplication.user.getEncodedKey());
		json.put("groupKey", group.getEncodedKey());
		
		double latitude;
		double longitude;
		if(loc != null) {
			latitude = loc.getLatitude();
			longitude = loc.getLongitude();
		} else {
			latitude = 0;
			longitude = 0;
		}
		
		json.put("latitude", latitude);
		json.put("longitude", longitude);
		json.put("location", loc.getAddress());

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
			// Save the event to the device
			CalendarEventHelper
					.insertEvent(slotResult, name, location, context);

			//Bring up the group intent, deleting the other result activities
			Intent intent = new Intent(context, GroupPageActivity.class);
			// set intent so it does not start a new activity
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.putExtra("group", group);
			context.startActivity(intent);
			
			//Toast message
			Util.alertToast(context, "Event has been saved to your Calendar");
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