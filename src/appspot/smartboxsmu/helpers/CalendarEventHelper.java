package appspot.smartboxsmu.helpers;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import appspot.smartboxsmu.parcelable.FindSlotResult;
import appspot.smartboxsmu.parcelable.UserEvent;

public class CalendarEventHelper {
	@SuppressLint("NewApi")
	public static void insertEvent(FindSlotResult result, String name,
			String location, Context context) {
		ArrayList<Long> calIds = getCalendarID(context);
		long startMillis = result.getStartDateMillis();
		long endMillis = result.getEndDateMillis();

		// insert to all your synced calendarl.. D
		for (Long id : calIds) {
			ContentResolver cr = context.getContentResolver();
			ContentValues values = new ContentValues();
			values.put(Events.DTSTART, startMillis);
			values.put(Events.DTEND, endMillis);
			values.put(Events.TITLE, name);
			values.put(Events.CALENDAR_ID, id);
			values.put(Events.EVENT_TIMEZONE, "UTC+8h");
			Uri uri = cr.insert(Events.CONTENT_URI, values);
		}

		// get the event ID that is the last element in the Uri
		// long eventID = Long.parseLong(uri.getLastPathSegment());
		// ... do something with event ID
		//
		//
	}
	
	@SuppressLint("NewApi")
	public static void insertEvent(UserEvent result, Context context) {
		ArrayList<Long> calIds = getCalendarID(context);
		long startMillis = result.getStartDateMillis();
		long endMillis = result.getEndDateMillis();

		// insert to all your synced calendarl.. D
		for (Long id : calIds) {
			ContentResolver cr = context.getContentResolver();
			ContentValues values = new ContentValues();
			values.put(Events.DTSTART, startMillis);
			values.put(Events.DTEND, endMillis);
			values.put(Events.TITLE, result.getDetails());
			values.put(Events.CALENDAR_ID, id);
			values.put(Events.EVENT_TIMEZONE, "UTC+8h");
			Uri uri = cr.insert(Events.CONTENT_URI, values);
		}

		// get the event ID that is the last element in the Uri
		// long eventID = Long.parseLong(uri.getLastPathSegment());
		// ... do something with event ID
		//
		//
	}

	// Get all the calendars that are synced.
	@SuppressLint("NewApi")
	public static ArrayList<Long> getCalendarID(Context context) {
		// Run query
		Cursor cur = null;
		ArrayList<Long> calIDs = new ArrayList<Long>();
		ContentResolver cr = context.getContentResolver();
		Uri uri = Calendars.CONTENT_URI;
		String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND ("
				+ Calendars.ACCOUNT_TYPE + " = ?) AND ("
				+ Calendars.OWNER_ACCOUNT + " = ?))";
		// Submit the query and get a Cursor object back.
		cur = cr.query(uri, EVENT_PROJECTION, null, null, null);
		// Use the cursor to step through the returned records
		while (cur.moveToNext()) {
			long id;
			String displayName = null;
			String accountName = null;
			String ownerName = null;
			int sync = 0;

			// Get the field values
			id = cur.getLong(PROJECTION_ID_INDEX);
			displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
			accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
			ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
			sync = cur.getInt(PROJECTION_SYNC_EVENTS_INDEX);
			if (sync == 1)
				calIDs.add(Long.valueOf(id));
		}

		return calIDs;
	}

	// Projection array. Creating indices for this array instead of doing
	// dynamic lookups improves performance.
	public static final String[] EVENT_PROJECTION = new String[] {
			Calendars._ID, // 0
			Calendars.ACCOUNT_NAME, // 1
			Calendars.CALENDAR_DISPLAY_NAME, // 2
			Calendars.OWNER_ACCOUNT, // 3
			Calendars.SYNC_EVENTS // 4
	};

	// The indices for the projection array above.
	private static final int PROJECTION_ID_INDEX = 0;
	private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
	private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
	private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
	private static final int PROJECTION_SYNC_EVENTS_INDEX = 4;
}
