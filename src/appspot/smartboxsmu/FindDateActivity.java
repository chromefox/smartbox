package appspot.smartboxsmu;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

public class FindDateActivity extends Activity {
	private DatePicker datePicker;
	private Spinner spinner;
	private int year;
	private int month;
	private int day;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_date);
        setSpinner();
        setDate();
    }
    
    private void setSpinner(){
    	 spinner = (Spinner) findViewById(R.id.spinner);
         // Create an ArrayAdapter using the string array and a default spinner layout
         ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
         R.array.duration, android.R.layout.simple_spinner_item);
         
         // Specify the layout to use when the list of choices appears
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         // Apply the adapter to the spinner
         spinner.setAdapter(adapter);
    }
    
    //set the default end search date to 7 days from the current date
    private void setDate(){
    	datePicker = (DatePicker) findViewById(R.id.datePicker1);
        Calendar cal=Calendar.getInstance();
        year=cal.get(Calendar.YEAR);
        month=cal.get(Calendar.MONTH);
        day=cal.get(Calendar.DAY_OF_MONTH);
        day += 7;
        int maxDay = cal.getMaximum(Calendar.DAY_OF_MONTH);
        if(day>maxDay){
        	day = day-maxDay;
        	month +=1;
        }
        datePicker.updateDate(year, month, day);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	//TODO put a proper menu resource item here
        getMenuInflater().inflate(R.menu.activity_find_date, menu);
        return true;
    }
    
    public void onClickHandler(View view) {
    	Intent intent;
    	
    	switch(view.getId()) {
    	case R.id.find_slot_button:
    		//Send the duration and the date range to the server
    		
    		
    		
    		break;
    	}
    }
}
