package appspot.smartboxsmu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import appspot.smartboxsmu.model.Group;

public class GroupPageActivity extends Activity {
	private Group group;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_page);
		// Get the group parcel from the activity
		setGroup(getIntent());
	}

	private void setGroup(Intent intent) {
		Bundle extra = intent.getExtras();
		if (extra != null) {
			group = extra.getParcelable("group");
			// Set the view content depending on the group
			setViewContent(group);
		}
	}

	public void onClickHandler(View view) {
		Intent intent;
		switch (view.getId()) {
		case R.id.group_find_meeting_button:
			// Go to the Find Date Activity
			intent = new Intent(this, FindDateActivity.class);
			intent.putExtra("group", group);
			startActivity(intent);
			break;
		case R.id.group_chat_button:
			// Go to the Group Chat Page
			intent = new Intent(this, ChatActivity.class);
			intent.putExtra("group", group);
			startActivity(intent);
			break;
		case R.id.group_task_button:
    		
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
