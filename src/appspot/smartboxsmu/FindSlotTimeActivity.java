package appspot.smartboxsmu;

import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import appspot.adapter.SlotResultAdapter;
import appspot.smartboxsmu.model.Group;
import appspot.smartboxsmu.network.Util;
import appspot.smartboxsmu.parcelable.FindSlotResult;

public class FindSlotTimeActivity extends Activity {
	public static final int ADD_EVENT = 0;
	private ArrayList<FindSlotResult> resultList;
	private SlotResultAdapter adapter;
	private ListView slotLV;
	private Group group;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_slot_time);
        setResultList(getIntent());
        
        slotLV = (ListView) findViewById(R.id.find_slot_listview);
        adapter = new SlotResultAdapter(this, R.id.add_group_button, R.id.add_group_button, resultList);
        
        slotLV.setAdapter(adapter);
        
        //set behaviour
        setupListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_find_slot_time, menu);
        return true;
    }
    
    private void setResultList(Intent intent) {
    	Bundle extra = intent.getExtras();
    	if(extra != null) {
    		resultList = extra.getParcelableArrayList("slotResults");
    		group = extra.getParcelable("group");
    	}
    }
    
    @SuppressLint("NewApi")
    private void setupListView() {
		slotLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View item,
					int position, long id) {
				//go to the custom create event page to fill up details (with some preset details)
				FindSlotResult result = adapter.getItem(position);
				Intent intent = new Intent(FindSlotTimeActivity.this, AddEventActivity.class);
				intent.putExtra("result", result);
				intent.putExtra("group", group);
				startActivity(intent);
				
//				Intent intent = new Intent(Intent.ACTION_INSERT)
//				        .setData(Events.CONTENT_URI)
//				        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
//				        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
//				        .putExtra(Events.TITLE, "Yoga")
//				        .putExtra(Events.DESCRIPTION, "Group class")
//				        .putExtra(Events.EVENT_LOCATION, "The gym")
//				        .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY)
//				        .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
//				startActivityForResult(intent, ADD_EVENT);
			}
		});
    }
    
	//Handling of the activity result:
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			switch(requestCode) {
			case ADD_EVENT:
				Util.alertToast(this, "Added");	
				break;
			}
		} else {
			Util.alertToast(this, "Cancelled");
		}
	}

    
    public void onClickHandler(View view) {
    	
    }
    
   
    
}
