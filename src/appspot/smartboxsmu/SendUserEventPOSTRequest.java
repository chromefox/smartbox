package appspot.smartboxsmu;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;
import appspot.smartboxsmu.helpers.DatabaseHelper;
import appspot.smartboxsmu.network.NetworkRequestFactory;
import appspot.smartboxsmu.network.Util;
import appspot.smartboxsmu.parcelable.Contact;
import appspot.smartboxsmu.parcelable.UserEvent;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SendUserEventPOSTRequest extends NetworkRequestFactory {
	public SendUserEventPOSTRequest(Context context) {
		super(context, null, true);
	}

	@Override
	public HttpUriRequest sendDataToServer(String url) throws JSONException,
			UnsupportedEncodingException {
		List<UserEvent> eventList = readCalendar(context);
		// parse contact into jsonArray
		String eventArrayJson = new Gson().toJson(eventList);
		JSONArray array = new JSONArray(eventArrayJson);
		JSONObject json = new JSONObject();
		json.put("events", array);
		json.put("userKey", MainApplication.user.getEncodedKey());
		
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
			// Construct the contact list from JSON
			Type collectionType = new TypeToken<ArrayList<Contact>>() {
			}.getType();
			List<Contact> deviceContactList = new Gson().fromJson(result,
					collectionType);
			// Save contacts to SQLLite
			DatabaseHelper db = new DatabaseHelper(context);
			// Drop table first and rewrite them
			db.deleteContactTable();
			for (Contact contact : deviceContactList) {
				db.addContact(contact);
			}
			Util.alertToast(context, "Contacts Saved");
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

	public ArrayList<UserEvent> readCalendar(Context context) {
		ArrayList<UserEvent> userEvents = new ArrayList<UserEvent>();

		try {
			ContentResolver contentResolver = context.getContentResolver();

			// Fetch a list of all calendars synced with the device, their
			// display names and whether the
			// user has them selected for display.
			final Cursor cursor = contentResolver.query(
					Uri.parse("content://com.android.calendar/calendars"),
					(new String[] { "_id", "displayName", "selected" }), null,
					null, null);
			// For a full list of available columns see
			// http://tinyurl.com/yfbg76w
			HashSet<String> calendarIds = new HashSet<String>();

			while (cursor.moveToNext()) {

				final String _id = cursor.getString(0);
				final String displayName = cursor.getString(1);
				final Boolean selected = !cursor.getString(2).equals("0");

				System.out.println("Id: " + _id + " Display Name: "
						+ displayName + " Selected: " + selected);
				calendarIds.add(_id);
			}

			// For each calendar, display all the events from the previous week
			// to the end of next week.
			for (String id : calendarIds) {
				Uri.Builder builder = Uri.parse(
						"content://com.android.calendar/instances/when")
						.buildUpon();
				long now = new Date().getTime();
				ContentUris.appendId(builder, now - DateUtils.WEEK_IN_MILLIS);
				ContentUris.appendId(builder, now + DateUtils.WEEK_IN_MILLIS);

				Cursor eventCursor = contentResolver.query(builder.build(),
						new String[] { "title", "begin", "end", "allDay" },
						"Calendars._id=" + id, null,
						"startDay ASC, startMinute ASC");
				// For a full list of available columns see
				// http://tinyurl.com/yfbg76w

				while (eventCursor.moveToNext()) {
					final String title = eventCursor.getString(0);
					final DateTime begin = new DateTime(new Date(
							eventCursor.getLong(1)));
					final DateTime end = new DateTime(new Date(
							eventCursor.getLong(2)));

					// Create a user event and add it to arraylist
					userEvents.add(new UserEvent(title, begin, end));

					// final Boolean allDay =
					// !eventCursor.getString(3).equals("0");
				}
			}
		} catch (Exception ex) {
			System.err.println("Exception :" + ex.getMessage());
		}

		return userEvents;
	}
}