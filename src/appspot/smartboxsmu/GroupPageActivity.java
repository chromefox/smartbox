package appspot.smartboxsmu;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import appspot.smartboxsmu.model.Group;
import appspot.smartboxsmu.network.URL;
import appspot.smartboxsmu.parcelable.Meeting;

public class GroupPageActivity extends Activity {
	private Group group;
	private boolean reschedule = false;
	private ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_page);
		// Get the group parcel from the activity
		setGroup(getIntent());
		
		setViewContent();
		
		setupLV();
	}

	private void setGroup(Intent intent) {
		Bundle extra = intent.getExtras();
		if (extra != null) {
			group = extra.getParcelable("group");
			// Set the view content depending on the group
			setViewContent(group);
		}
	}
	
	public void locationCallback(Group group) {
		this.group = group;
		
		setupLV();		
	}
	
	private void setupLV() {
		lv = (ListView) findViewById(R.id.group_page_member_listview);

		ArrayList<String> array = new ArrayList<String>();
		for (int i = 0; i < group.getMembers().size(); i++) {
			String name = group.getMembers().get(i);
			String duration = group.getUserDistances().get(i);

			if (duration.isEmpty())
				duration = "N/A";
			array.add(name + " is reaching in " + duration);
		}
		String[] locationNames = new String[array.size()];
		for (int i = 0; i < array.size(); i++) {
			locationNames[i] = array.get(i);
		}

		// Create adapter
		ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				locationNames);
		lv.setAdapter(modeAdapter);
		modeAdapter.notifyDataSetChanged();
		// create
	}
	
	private void setViewContent() {
		Meeting meeting = group.getMeeting();
		// if meeting is now null or after now (have not passed)
		if (meeting != null && meeting.getStartDate()!= null && meeting.getStartDate().after(new Date())) {
			TextView tv;

			tv = (TextView) findViewById(R.id.group_page_meeting_name_text);
			tv.setText(meeting.getDetails());

			tv = (TextView) findViewById(R.id.group_page_meeting_location_text);
			tv.setText(meeting.getLocation());

			tv = (TextView) findViewById(R.id.group_page_meeting_start_text);
			tv.setText(meeting.getStartDateFormatted());

			tv = (TextView) findViewById(R.id.group_page_meeting_end_text);
			tv.setText(meeting.getEndDateFormatted());
			
			ImageButton button = (ImageButton) findViewById(R.id.group_find_meeting_button);
			button.setImageResource(R.drawable.picture3);
			
			reschedule = true;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	public void onClickHandler(View view) {
		Intent intent;
		switch (view.getId()) {
		case R.id.group_find_meeting_button:
			// Go to the Find Date Activity
			intent = new Intent(this, FindDateActivity.class);
			intent.putExtra("group", group);
			intent.putExtra("reschedule", reschedule);
			startActivity(intent);
			break;
		case R.id.group_chat_button:
			// Go to the Group Chat Page
			intent = new Intent(this, ChatActivity.class);
			intent.putExtra("group", group);
			startActivity(intent);
			break;
		case R.id.group_page_refresh_time_button:
			RequestLocationPOSTRequest post = new RequestLocationPOSTRequest(this, group);
			post.execute(URL.REQUEST_LOCATION);
			break;
    	}
    }

	private void setViewContent(Group group) {
		TextView tv;
		StringBuilder sb = new StringBuilder();
		tv = (TextView) findViewById(R.id.group_page_group_name);
		tv.setText(group.getGroupName());

		tv = (TextView) findViewById(R.id.group_page_member_name);
		for (int i = 0; i < group.getMemberNames().size(); i++) {
			sb.append(group.getMemberNames().get(i));
			// if it is the last array, do not append comma
			if (i + 1 != group.getMemberNames().size()) {
				sb.append(", ");
			}
		}
		tv.setText(sb.toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_group_page, menu);
		return true;
	}
}
