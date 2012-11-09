package appspot.smartboxsmu;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import appspot.adapter.ContactAdapter.ContactViewHolder;
import appspot.adapter.ContactAdapter;
import appspot.adapter.GroupAdapter;
import appspot.smartboxsmu.model.Group;
import appspot.smartboxsmu.network.URL;
import appspot.smartboxsmu.parcelable.Contact;

public class HomeActivity extends Activity {
	private ListView groupListView;
	private GroupAdapter adapter;
	private ArrayList<Group> group;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
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
						//Go to group detail page, passing in the group object in the intent
						Intent intent = new Intent(HomeActivity.this, GroupPageActivity.class);
						intent.putExtra("group", group);
						HomeActivity.this.startActivity(intent);
					}
				});
		
		//put into adapter
		adapter = new GroupAdapter(this, R.id.add_group_contact_list,
				R.id.add_group_contact_list, group);
		//set adapter
		groupListView.setAdapter(adapter);
		
		//Request Data and populate group array
//		GroupPOSTRequest post = new GroupPOSTRequest(this, null, GroupPOSTRequest.GET_ALL_GROUPS);
//		post.execute(URL.GET_GROUP);
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
			// Go to demo page for now
			intent = new Intent(this, DemoActivity.class);
			startActivity(intent);
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
