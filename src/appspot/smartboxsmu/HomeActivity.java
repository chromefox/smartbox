package appspot.smartboxsmu;

import static appspot.gcm.example.CommonUtilities.SENDER_ID;
import static appspot.gcm.example.CommonUtilities.SERVER_URL;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import appspot.adapter.GroupAdapter;
import appspot.smartboxsmu.model.Group;
import appspot.smartboxsmu.network.URL;

import com.google.android.gcm.GCMRegistrar;

public class HomeActivity extends Activity {
	private ListView groupListView;
	private GroupAdapter adapter;
	private ArrayList<Group> group;

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

		// Request Data and populate group array
		// GroupPOSTRequest post = new GroupPOSTRequest(this, null,
		// GroupPOSTRequest.GET_ALL_GROUPS);
		// post.execute(URL.GET_GROUP);
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
		case R.id.test:
			// Test sending synced events here
			SendUserEventPOSTRequest post1 = new SendUserEventPOSTRequest(this);
			post1.execute(URL.SEND_EVENT);
			break;
		case R.id.home_create_group:
			intent = new Intent(this, AddGroupActivity.class);
			startActivity(intent);
			break;
		case R.id.home_send_contact:
			// Send a contact Sync
			ContactCheckPOSTRequest post = new ContactCheckPOSTRequest(this);
			post.execute(URL.CONTACT_CHECK);
			break;
		}
	}
}
