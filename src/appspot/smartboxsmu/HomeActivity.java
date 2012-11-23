package appspot.smartboxsmu;

import static appspot.gcm.example.CommonUtilities.SENDER_ID;
import static appspot.gcm.example.CommonUtilities.SERVER_URL;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import appspot.adapter.GroupAdapter;
import appspot.smartboxsmu.model.Group;
import appspot.smartboxsmu.network.URL;

import com.google.android.gcm.GCMRegistrar;

public class HomeActivity extends Activity {
	private ListView groupListView;
	private GroupAdapter adapter;
	private ArrayList<Group> group;
	private TextView noGroupTV;

	
	@Override
	protected void onStart() {
		super.onStart();
		if(group != null && group.size() == 0) {
			noGroupTV.setVisibility(View.VISIBLE);
		} else {
			noGroupTV.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		registerDevice();
		// Getting variables
		groupListView = (ListView) findViewById(R.id.home_group_list);
		if (MainApplication.user.getGroupList() != null) {
			group = MainApplication.user.getGroupList();
		} else {
			group = new ArrayList<Group>();
		}
		
		noGroupTV = (TextView) findViewById(R.id.home_page_no_group_text);

		// Setting up onItemClickHandler
		groupListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View item,
							int position, long id) {
						Group group = adapter.getItem(position);
						// Go to group detail page, passing in the group object
						// in the intent
						Intent intent = new Intent(HomeActivity.this,
								GroupPageActivity.class);
						intent.putExtra("group", group);
						HomeActivity.this.startActivity(intent);
					}
				});

		// put into adapter
		adapter = new GroupAdapter(this, R.id.add_group_contact_list,
				R.id.add_group_contact_list, group);
		// set adapter
		groupListView.setAdapter(adapter);
		
		//Set adapter on global class
		((MainApplication)getApplication()).setGroupAdapter(adapter);
		
		// Request Data and populate group array
		// GroupPOSTRequest post = new GroupPOSTRequest(this, null,
		// GroupPOSTRequest.GET_ALL_GROUPS);
		// post.execute(URL.GET_GROUP);

		// Set recurring alarm
		setRecurringAlarm();
		
		//Sending data to the servers
		ContactCheckPOSTRequest post = new ContactCheckPOSTRequest(this);
		post.execute(URL.CONTACT_CHECK);
		
		SendUserEventPOSTRequest post1 = new SendUserEventPOSTRequest(this);
		post1.execute(URL.SEND_EVENT);
	}

	private void setRecurringAlarm() {
		Intent downloader = new Intent(this, AlarmReceiver.class);

		Calendar cal = Calendar.getInstance();
		// Start 30 seconds after boot completed
		cal.add(Calendar.SECOND, 5);

		PendingIntent recurringDownload = PendingIntent.getBroadcast(this, 0,
				downloader, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarms = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				cal.getTimeInMillis(), AlarmManager.INTERVAL_HALF_HOUR,
				recurringDownload);
	}

	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException(getString(R.string.error_config,
					name));
		}
	}
	
	

	public void registerDevice() {
		checkNotNull(SERVER_URL, "SERVER_URL");
		checkNotNull(SENDER_ID, "SENDER_ID");

		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);
		// Make sure the manifest was properly set - comment out this line
		GCMRegistrar.checkManifest(this);

		final String regId = GCMRegistrar.getRegistrationId(this);

		if (regId.equals("")) {
			// Automatically registers application on startup.
			GCMRegistrar.register(this, SENDER_ID);
		} else {
			// Device is already registered on GCM, check server.
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration. Do nothing
			} else {
				registerOnGAE(regId);
			}
		}

		// Extra Check here. Make sure the server has the latest deviceregid
		if (MainApplication.user.getDeviceRegId() == null
				|| !MainApplication.user.getDeviceRegId().equals(regId)) {
			registerOnGAE(regId);
		}
	}

	public void registerOnGAE(String regId) {
		// Attempt to register the devId on the GAE server
		MainApplication.user.setDeviceRegId(regId);
		RegisterPOSTRequest post = new RegisterPOSTRequest(this);
		post.execute(URL.REGISTER_DEVICE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_home, menu);
		return true;
	}

	public void onClickHandler(View view) {
		Intent intent;
		switch (view.getId()) {
			// Test sending synced events here
//			SendUserEventPOSTRequest post1 = new SendUserEventPOSTRequest(this);
//			post1.execute(URL.SEND_EVENT);
//			PushLocationPOSTRequest post2 = new PushLocationPOSTRequest(this, 1.29686, 103.85220);
//			post2.execute(URL.SEND_LOCATION);
		case R.id.home_create_group:
			intent = new Intent(this, AddGroupActivity.class);
			startActivity(intent);
			break;
		}
	}
}
