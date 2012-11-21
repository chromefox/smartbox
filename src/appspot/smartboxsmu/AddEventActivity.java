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
import android.widget.EditText;
import android.widget.TextView;
import appspot.smartboxsmu.helpers.CalendarEventHelper;
import appspot.smartboxsmu.model.Group;
import appspot.smartboxsmu.network.URL;
import appspot.smartboxsmu.parcelable.FindSlotResult;

public class AddEventActivity extends Activity {
	private FindSlotResult result;
	private TextView tv;
	private EditText name;
	private EditText location;
	private Group group;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        setSlotResult(getIntent());
        //set the corresponding view based on the findSlotResult
        setViewContent();
        
        //Find components
        name = (EditText) findViewById(R.id.add_event_name_edit_text);
        location = (EditText) findViewById(R.id.add_event_location_edit_text);
    }
    
    private void setViewContent() {
    	//Set the corresponding date, start time and end time
    	tv = (TextView) findViewById(R.id.add_event_date_text);
    	tv.setText(result.getDateTimeDay().toString());
    	
    	tv = (TextView) findViewById(R.id.add_event_start_time_text);
    	tv.setText(result.getStartTime());
    	
    	tv = (TextView) findViewById(R.id.add_event_end_time_text);
    	tv.setText(result.getEndTime());
    }
    
   public void onClickHandler(View view) {
	   switch(view.getId()) {
	   case R.id.add_event_button:
		   //TODO validation
		   //Send the event to the server
		   //Event is saved to device only in the case where event is successfully created
		   CalendarEventPOSTRequest post = new CalendarEventPOSTRequest(this, result, name.getText().toString(), location.getText().toString(), group);
		   post.execute(URL.ADD_EVENT);
		   break;
	   case R.id.add_event_check_location:
		   //TODO Rishik's code here to set the lat long, too
		   
	   }
   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_event, menu);
        return true;
    }
    
    private void setSlotResult(Intent intent) {
    	Bundle extra = intent.getExtras();
    	if(extra != null) {
    		result = extra.getParcelable("result");
    		group = extra.getParcelable("group");
    	}
    }
  
}
