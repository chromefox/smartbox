package appspot.adapter;

import java.util.List;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import appspot.smartboxsmu.R;
import appspot.smartboxsmu.parcelable.FindSlotResult;

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
public class SlotResultAdapter extends ArrayAdapter<FindSlotResult> {
	private Context context;
	private List<FindSlotResult> results;
	private static final String DATE_HOUR_FORMAT = "HH:mm";

	public SlotResultAdapter(Context context, int resource, int textViewResourceId,
			List<FindSlotResult> objects) {
		// the super constructor will set the objects and allow us to call
		// getItem(position)
		super(context, resource, textViewResourceId, objects);
		this.context = context;
		this.results = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// get the layout inflater of the context (in this case the
		// viewMemoActivity)
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		//Reuse the viewHolder
		FindSlotResultViewHolder viewHolder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.slot_time_result_list, null);
			viewHolder = new FindSlotResultViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (FindSlotResultViewHolder) convertView.getTag();
		}
		
		FindSlotResult result = getItem(position);
		viewHolder.setValue(result);
		return convertView;
	}

	// Implement ViewHolder for Factory design
	public static class FindSlotResultViewHolder implements ViewHolder {
		public TextView slotTimeTextView;
		public TextView availabilityTextView;
		
		public FindSlotResultViewHolder(View convertView) {
			// Set the elements of layout here
			slotTimeTextView = (TextView) convertView.findViewById(R.id.slot_time_result_text);
			availabilityTextView = (TextView) convertView.findViewById(R.id.slot_time_availability);
		}

		public void setValue(FindSlotResult result) {
			// set value here
			slotTimeTextView.setText(result.getStartTime());
			availabilityTextView.setText(String.valueOf(result.getAvailableCount()));
		}

	}
}
