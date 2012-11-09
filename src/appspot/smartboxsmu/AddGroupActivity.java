package appspot.smartboxsmu;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import appspot.adapter.ContactAdapter;
import appspot.adapter.ContactAdapter.ContactViewHolder;
import appspot.smartboxsmu.helpers.DatabaseHelper;
import appspot.smartboxsmu.model.Group;
import appspot.smartboxsmu.network.URL;
import appspot.smartboxsmu.parcelable.Contact;

@TargetApi(11)
public class AddGroupActivity extends Activity {
	private ListView contactListView;
	private ContactAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_group);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// getting components
		contactListView = (ListView) findViewById(R.id.add_group_contact_list);

		// setting item click listeners
		contactListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View item,
							int position, long id) {
						Contact contact = adapter.getItem(position);
						contact.toggleChecked();
						ContactViewHolder viewHolder = (ContactViewHolder) item
								.getTag();
						viewHolder.setChecked(contact.isChecked());
					}
				});
		
		DatabaseHelper db = new DatabaseHelper(this);
		// get contacts from sqllite
		ArrayList<Contact> contactList = (ArrayList<Contact>) db.getAllContacts();
		// put into adapter
		adapter = new ContactAdapter(this, R.id.add_group_contact_list,R.id.add_group_contact_list, contactList);
		// set adapter
		contactListView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_add_group, menu);
		return true;
	}
	
	public void onClickHandler(View view) {
		switch(view.getId()) {
		case R.id.add_group_button:
			//Get group name
			String groupName = ((EditText)findViewById(R.id.add_group_name)).getText().toString();
			//Get contacts and create Group Object
			Group group = new Group(groupName, adapter.getCheckedContacts(), MainApplication.user.getEmail());
			//Send to server to create group (with userId/email)
			GroupPOSTRequest post = new GroupPOSTRequest(this, group, GroupPOSTRequest.CREATE_GROUP);
			post.execute(URL.ADD_GROUP);
		break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
