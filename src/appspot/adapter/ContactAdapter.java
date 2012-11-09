package appspot.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import appspot.smartboxsmu.R;
import appspot.smartboxsmu.parcelable.Contact;

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
public class ContactAdapter extends ArrayAdapter<Contact> {
	private Context context;
	private List<Contact> contacts;

	public ContactAdapter(Context context, int resource,
			int textViewResourceId, List<Contact> objects) {
		// the super constructor will set the objects and allow us to call
		// getItem(position)
		super(context, resource, textViewResourceId, objects);
		this.context = context;
		this.contacts = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// get the layout inflater of the context (in this case the
		// viewMemoActivity)
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// Reuse the viewHolder
		ContactViewHolder viewHolder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.contact_list, null);
			viewHolder = new ContactViewHolder(convertView);

			convertView.setTag(viewHolder);
			// get the textView and set the text

		} else {
			viewHolder = (ContactViewHolder) convertView.getTag();
		}
		Contact contact = getItem(position);
		viewHolder.setValue(contact);
		return convertView;
	}
	
	public List<Contact> getCheckedContacts() {
		ArrayList<Contact> list = new ArrayList<Contact>();
		for(Contact contact : contacts) {
			if(contact.isChecked()) list.add(contact);
		}
		return list;
	}

	// Implement ViewHolder for Factory design
	public static class ContactViewHolder implements ViewHolder {
		// define elements inside each list
		public CheckedTextView contactName;

		public ContactViewHolder(View convertView) {
			// Set the elements of layout here
			contactName = (CheckedTextView) convertView
					.findViewById(R.id.contact_name);
		}

		public void setValue(Contact contact) {
			// set value here
			contactName.setText(contact.getName());
		}
		
		public void setChecked(boolean check) {
			contactName.setChecked(check);
		}
	
	}
}
