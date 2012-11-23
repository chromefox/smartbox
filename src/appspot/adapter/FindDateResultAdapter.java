package appspot.adapter;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import appspot.smartboxsmu.R;

/**
 * The usage of ViewHolder is recommended by Android developers. Its principle
 * optimization is one of avoiding the usage of findViewById (which consumes a
 * lot of processing power) The basic code follows this logic: create a new
 * viewholder for each empty convertview, set the viewholder as the tag of the
 * convertview. Notice viewholder now holds Reference to the children Views of
 * the inflated layout (one that you need to constantly manipulate). The next
 * thing that follows is just to reuse the ViewHolder and manipulate the
 * contained Views
 * 
 * @author Ronny
 * 
 */
public class FindDateResultAdapter extends ArrayAdapter<DateTime> {
	private Context context;
	private List<DateTime> dates;
	public static final String DATE_DAY_FORMAT = "E, d MMM YYYY";
	private static final String DATE_HOUR_FORMAT = "HH:mm";

	public FindDateResultAdapter(Context context, int resource, int textViewResourceId,
			List<DateTime> objects) {
		// the super constructor will set the objects and allow us to call
		// getItem(position)
		super(context, resource, textViewResourceId, objects);
		this.context = context;
		this.dates = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// get the layout inflater of the context (in this case the
		// viewMemoActivity)
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		//Reuse the viewHolder
		DateTimeViewHolder viewHolder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.date_result_list, null);
			viewHolder = new DateTimeViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (DateTimeViewHolder) convertView.getTag();
		}
		
		DateTime date = getItem(position);
		viewHolder.setValue(date);
		return convertView;
	}

	// Implement ViewHolder for Factory design
	public static class DateTimeViewHolder implements ViewHolder {
		public TextView dateTextView;

		public DateTimeViewHolder(View convertView) {
			// Set the elements of layout here
			dateTextView = (TextView) convertView.findViewById(R.id.date_result_text);
		}

		public void setValue(DateTime date) {
			// set value here
			DateTimeFormatter fmt2 = DateTimeFormat.forPattern(DATE_DAY_FORMAT);
			String formattedDate = fmt2.print(date);
			dateTextView.setText(formattedDate);
		}

	}
}
