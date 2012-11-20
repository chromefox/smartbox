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
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Instances;
import android.text.format.DateUtils;
import android.util.Log;
import appspot.smartboxsmu.model.Group;
import appspot.smartboxsmu.network.NetworkRequestFactory;
import appspot.smartboxsmu.network.Util;
import appspot.smartboxsmu.parcelable.FindSlotResult;
import appspot.smartboxsmu.parcelable.UserEvent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class FindDatePOSTRequest extends NetworkRequestFactory {
	private int duration;
	private long endDate;
	private long startDate;
	private Group group;
	
	public FindDatePOSTRequest(Context context, int duration, long startDate, long endDate, Group group) {
		super(context, null, true);
		this.duration = duration;
		this.endDate = endDate;
		this.group = group;
		this.startDate = startDate;
	}

	@Override
	public HttpUriRequest sendDataToServer(String url) throws JSONException,
			UnsupportedEncodingException {
		JSONObject json = new JSONObject();
		json.put("groupKey", group.getEncodedKey());
		json.put("duration", duration);
		json.put("endDate", endDate);
		json.put("startDate", startDate);
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
			try {
				JSONObject obj = new JSONObject(result);
				Gson gson = new GsonBuilder().create();
				Type collectionType = new TypeToken<ArrayList<FindSlotResult>>() {}.getType();
				List<FindSlotResult> findSlotResults = gson.fromJson(obj.getJSONArray("dates").toString(), collectionType);
				
				//Display inside a listview or something
				int b = 0;
				
			} catch (JSONException e) {
				Log.e("FinDatePOST", e.toString());
			}
			
			
			
			//Display the toast
			Util.alertToast(context, "Calendar Synced");
		} else {
			// Display error messages
			// Expected {error: String} JSON
			JSONObject obj;
			try {
				obj = new JSONObject(result);
				Util.alertToast(context, obj.getString("error"));
			} catch (JSONException e) {
				Log.e("FinDatePOST", e.toString());
			}
		}
	}

	@Override
	public void additionalExceptionHandling() {
		// EMPTY
	}

	public ArrayList<UserEvent> readCalendar2(Context context) {
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
				ContentUris.appendId(builder, now);
				ContentUris.appendId(builder, now + DateUtils.WEEK_IN_MILLIS);

				Cursor eventCursor = contentResolver.query(builder.build(),
						new String[] { "title", "begin", "end", "allDay" },
						"Calendars._id=" + id, null,
						"startDay ASC, startMinute ASC");
				// For a full list of available columns see
				// http://tinyurl.com/yfbg76w

				while (eventCursor.moveToNext()) {
					final String title = eventCursor.getString(0);
					final Date begin = new Date(
							eventCursor.getLong(1));
					final Date end = new Date(
							eventCursor.getLong(2));

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
	
	public static final String[] INSTANCE_PROJECTION = new String[] {
	    Instances.EVENT_ID,      // 0
	    Instances.BEGIN,         // 1
	    Instances.END, // 2
	    Instances.TITLE    //3      
	    
	  };
	  
	// The indices for the projection array above.
	private static final int PROJECTION_ID_INDEX = 0;
	private static final int PROJECTION_BEGIN_INDEX = 1;
	private static final int PROJECTION_END_INDEX = 2;
	private static final int PROJECTION_TITLE_INDEX = 3;
	
	@SuppressLint("NewApi")
	public ArrayList<UserEvent> readCalendar(Context context) {
		ArrayList<UserEvent> userEvents = new ArrayList<UserEvent>();
		Cursor cur = null;
		DateTime now = DateTime.now();
		try {
			//Get all the calendar events with the respective columns
			cur = context.getContentResolver()
		            .query(Uri.parse("content://com.android.calendar/events"),
		                    new String[] { "calendar_id", "title", "description",
		                            "dtstart", "dtend", "eventLocation" }, null,
		                    null, null);
		    cur.moveToFirst();
		    //fetching calendars name
		    String CNames[] = new String[cur.getCount()];

		    for (int i = 0; i < CNames.length; i++) {
		        String title = cur.getString(1);
		        long beginVal = Long.parseLong(cur.getString(3));
		        long endVal = Long.parseLong(cur.getString(4));		        
//		        String detai
//		        descriptions.add(cursor.getString(2));
		        CNames[i] = cur.getString(1);
		        DateTime beginDate = new DateTime(beginVal);
		        //Only includes events that is after the current date, not interested in past data		        
		        if(beginDate.isAfter(now)) {
		        	userEvents.add(new UserEvent(title, new Date(beginVal), new Date(endVal)));	
		        }
		        cur.moveToNext();
		    }

//			// The ID of the recurring event whose instances you are searching
//			// for in the Instances table
////			String selection = Instances.EVENT_ID + " = ?";
////			String[] selectionArgs = new String[] {"207"};
//

//
//			// Submit the query
//			cur =  cr.query(builder.build(), 
//			    INSTANCE_PROJECTION, 
//			    null, 
//			    null, 
//			    null);
//			   
//			while (cur.moveToNext()) {
//			    String title = null;
//			    long eventID = 0;
//			    long beginVal = 0;
//			    long endVal = 0;
//			    
//			    // Get the field values
//			    eventID = cur.getLong(PROJECTION_ID_INDEX);
//			    beginVal = cur.getLong(PROJECTION_BEGIN_INDEX);
//			    endVal = cur.getLong(PROJECTION_END_INDEX);
//			    title = cur.getString(PROJECTION_TITLE_INDEX);
//			    
//				// Create a user event and add it to arraylist
//				userEvents.add(new UserEvent(title, new Date(beginVal), new Date(endVal)));
//			}			
		} catch (Exception e) {
			Log.e("SendUserEventPOST", e.toString());
			Log.e("SendUserEventPOST", e.getStackTrace().toString());
		} finally {
			if(cur != null) cur.close();
		}
		return userEvents;
	}
	
}