package appspot.smartboxsmu;

import static appspot.gcm.example.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static appspot.gcm.example.CommonUtilities.EXTRA_MESSAGE;
import static appspot.gcm.example.CommonUtilities.SENDER_ID;
import static appspot.gcm.example.CommonUtilities.SERVER_URL;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import appspot.adapter.ChatMessageAdapter;
import appspot.adapter.GroupAdapter;
import appspot.smartboxsmu.model.Group;
import appspot.smartboxsmu.network.URL;
import appspot.smartboxsmu.network.Util;
import appspot.smartboxsmu.parcelable.ChatMessage;

import com.google.android.gcm.GCMRegistrar;

public class ChatActivity extends Activity {
	private ListView chatList;
	private ChatMessageAdapter chatAdapter;
	private Group group;
	private ArrayList<ChatMessage> messages;
	AsyncTask<Void, Void, Void> mRegisterTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
		chatList = (ListView) findViewById(R.id.chatList);
		
		setGroup(getIntent());
		// Register broadcast receivers that will handle the messages
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_chat, menu);
        return true;
    }
    
    private void setGroup(Intent intent) {
    	Bundle extra = intent.getExtras();
    	if(extra != null) {
    		group = extra.getParcelable("group");
    		messages = group.getMessages();
    		if(messages == null) messages = new ArrayList<ChatMessage>();
    		// put into adapter 
    		chatAdapter = new ChatMessageAdapter(this, R.id.add_group_contact_list,
    				R.id.add_group_contact_list, messages);
    		// set adapter
    		chatList.setAdapter(chatAdapter);
    	}
    }
    
    public void onClickHandler(View view) {
		switch(view.getId()) {
		case R.id.sendChatButton:
			//test sending message to own devices
			String chatMsg = ((EditText) findViewById(R.id.chatEditText))
					.getText().toString();

			if (chatMsg.isEmpty()) {
				Util.alertToast(this, "Please type a message");
			} else {
				//Empty chat
				((EditText) findViewById(R.id.chatEditText)).setText("");
				SendMessagePOSTRequest post = new SendMessagePOSTRequest(this,
						group, chatMsg);
				post.execute(URL.SEND_MESSAGE);
			}
			break;
		}
	}
    
    @Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		unregisterReceiver(mHandleMessageReceiver);
//		GCMRegistrar.onDestroy(this);
		super.onDestroy();
	}


	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			//On receive new message - Add new Message to the adapter and to the listview
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			//Add the new message to the arraylist attached to the adapter and notify changes
			ChatMessage msg = new ChatMessage(newMessage);
			messages.add(msg);
			group.getMessages().add(msg);
			chatAdapter.notifyDataSetChanged();
		}
	};
}
