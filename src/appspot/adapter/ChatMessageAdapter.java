package appspot.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import appspot.smartboxsmu.R;
import appspot.smartboxsmu.parcelable.ChatMessage;

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
public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {
	private Context context;
	private List<ChatMessage> chatMessages;

	public ChatMessageAdapter(Context context, int resource, int textViewResourceId,
			List<ChatMessage> objects) {
		// the super constructor will set the objects and allow us to call
		// getItem(position)
		super(context, resource, textViewResourceId, objects);
		this.context = context;
		this.chatMessages = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// get the layout inflater of the context (in this case the
		// viewMemoActivity)
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// Reuse the viewHolder
		ChatMessageViewHolder viewHolder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.chat_list, null);
			viewHolder = new ChatMessageViewHolder(convertView);

			convertView.setTag(viewHolder);
			// get the textView and set the text

		} else {
			viewHolder = (ChatMessageViewHolder) convertView.getTag();
		}
		ChatMessage group = getItem(position);
		viewHolder.setValue(group);
		return convertView;
	}

	// Implement ViewHolder for Factory design
	public static class ChatMessageViewHolder implements ViewHolder {
		public TextView chatText;

		public ChatMessageViewHolder(View convertView) {
			// Set the elements of layout here
			chatText = (TextView) convertView.findViewById(R.id.chat_text);
			// mMemo = (TextView) convertView.findViewById(R.id.memo);
			// ownerName = (TextView)
			// convertView.findViewById(R.id.memo_owner_name);
			// Util.setCustomFont(context, mMemo);
			// Util.setCustomFont(context, ownerName);
		}

		public void setValue(ChatMessage message) {
			// set value here
			chatText.setText(message.getMessage());
			// mMemo.setText(memo.toString());
			// ownerName.setText(memo.getOwnerName());
		}

	}
}
