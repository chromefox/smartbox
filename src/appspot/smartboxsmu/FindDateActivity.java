package appspot.smartboxsmu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import appspot.adapter.ChatMessageAdapter;
import appspot.smartboxsmu.model.Group;
import appspot.smartboxsmu.network.URL;
import appspot.smartboxsmu.parcelable.ChatMessage;

public class FindDateActivity extends Activity {
	private DatePicker datePicker;
	private DatePicker datePickerStart;
	private Spinner spinner;
	private int year;
	private int month;
	private int day;
	private Group group;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_date);
		setSpinner();
		setDate();
		setGroup(getIntent());
	}

	private void setGroup(Intent intent) {
		Bundle extra = intent.getExtras();
		if (extra != null) {
			group = extra.getParcelable("group");
		}
	}

	private void setSpinner() {
		spinner = (Spinner) findViewById(R.id.spinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.duration, android.R.layout.simple_spinner_item);

		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
	}

	// set the default end search date to 7 days from the current date
	private void setDate() {
		datePicker = (DatePicker) findViewById(R.id.datePicker1);
		Calendar cal = Calendar.getInstance();
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
		day = cal.get(Calendar.DAY_OF_MONTH);
		day += 7;
		int maxDay = cal.getMaximum(Calendar.DAY_OF_MONTH);
		if (day > maxDay) {
			day = day - maxDay;
			month += 1;
		}
		datePicker.updateDate(year, month, day);

		// Get today's date
		int year1 = cal.get(Calendar.YEAR);
		int month1 = cal.get(Calendar.MONTH);
		int day1 = cal.get(Calendar.DAY_OF_MONTH);

		// Update the start datepickern to today's date
		datePickerStart = (DatePicker) findViewById(R.id.datePicker2);
		datePickerStart.updateDate(year1, month1, day1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO put a proper menu resource item here
		getMenuInflater().inflate(R.menu.activity_find_date, menu);
		return true;
	}

	public void onClickHandler(View view) {
		Intent intent;
		switch (view.getId()) {
		case R.id.find_slot_button:
			// Send the duration and the date range to the server
			int duration = Integer.parseInt(String.valueOf(spinner
					.getSelectedItem().toString().charAt(0)));
			int day = datePicker.getDayOfMonth();
			int month = datePicker.getMonth();
			int year = datePicker.getYear();

			int startDay = datePickerStart.getDayOfMonth();
			int startMonth = datePickerStart.getMonth();
			int startYear = datePickerStart.getYear();

			long startDate = new Date(startYear - 1900, startMonth, startDay)
					.getTime();
			long endDate = new Date(year - 1900, month, day).getTime();

			FindDatePOSTRequest post = new FindDatePOSTRequest(this, duration,
					startDate, endDate, group);
			post.execute(URL.FIND_SLOT);
			break;
		}
	}
}
