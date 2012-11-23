package appspot.smartboxsmu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import appspot.smartboxsmu.model.Group;
import appspot.smartboxsmu.network.URL;
import appspot.smartboxsmu.parcelable.FindSlotResult;
import appspot.smartboxsmu.parcelable.LocationSuggestions;

public class AddEventActivity extends Activity {
	private FindSlotResult result;
	private TextView tv;
	private EditText name;
	private EditText location;
	private Group group;
	private LocationSuggestions chosenSuggestion;
	
	public void setChosenSuggestion(LocationSuggestions suggestion) {
		this.chosenSuggestion = suggestion;
	}
	
	public void setPlaceName(String placeName) {
		location.setText(placeName);
	}
	
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
		   //MUST check whether chosenSuggestion is null or not, create new object and pass it 
		   
		   //TODO if have time, editchange => nullify the chosenSuggestion obj
		   CalendarEventPOSTRequest post = new CalendarEventPOSTRequest(this, result, name.getText().toString(), location.getText().toString(), group, chosenSuggestion);
		   post.execute(URL.ADD_EVENT);
		   break;
	   case R.id.add_event_check_location:
		   LocationCheckGETRequest get = new LocationCheckGETRequest(this);
		   get.execute(URL.AUTOCOMPLETE + location.getText().toString().replaceAll(" ", "+"));
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
