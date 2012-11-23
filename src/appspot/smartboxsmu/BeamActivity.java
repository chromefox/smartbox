package appspot.smartboxsmu;

import java.nio.charset.Charset;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import appspot.smartboxsmu.helpers.DatabaseHelper;
import appspot.smartboxsmu.network.Names;
import appspot.smartboxsmu.parcelable.Contact;

@SuppressLint({ "NewApi", "NewApi", "NewApi" })
public class BeamActivity extends Activity implements CreateNdefMessageCallback,
        OnNdefPushCompleteCallback {
    NfcAdapter mNfcAdapter;
    TextView mInfoText;
    private static final int MESSAGE_SENT = 1;
    private static final int CONTACT_ADDED = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
   
        mInfoText = (TextView) findViewById(R.id.textView);
        // Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            mInfoText = (TextView) findViewById(R.id.textView);
            mInfoText.setText("NFC is not available on this device.");
        }
        
        // Register callback to set NDEF message
        mNfcAdapter.setNdefPushMessageCallback(this, this);
        // Register callback to listen for message-sent success
        mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
    }


    /**
     * Implementation for the CreateNdefMessageCallback interface
     */
    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String mobile = prefs.getString(Names.USER_MOBILE, "");
		String name = MainApplication.user.getName();
		//Format: 9999999asdfasdfadsf
    	String message = mobile + name;
        NdefMessage msg = new NdefMessage(
                new NdefRecord[] { createMimeRecord(
                        "application/appspot.smartboxsmu", message.getBytes())
         /**
          * The Android Application Record (AAR) is commented out. When a device
          * receives a push with an AAR in it, the application specified in the AAR
          * is guaranteed to run. The AAR overrides the tag dispatch system.
          * You can add it back in to guarantee that this
          * activity starts when receiving a beamed message. For now, this code
          * uses the tag dispatch system.
          */
          //,NdefRecord.createApplicationRecord("com.example.android.beam")
        });
        return msg;
    }
    
    //failing google API method
    /*public static String getProfile(Context context) {
    	Cursor c = context.getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
    	int count = c.getCount();
    	String[] columnNames = c.getColumnNames();
    	String phoneNumber = "";  
    	if (count == 1) {
    	        String columnName = columnNames[12];
    	        String contactId = c.getString(c.getColumnIndex(ContactsContract.Profile._ID));
    	        Cursor phones = context.getContentResolver().query(
    					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
    					null,
    					ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
    							+ " = " + contactId, null, null);
    			phoneNumber = phones
    						.getString(phones
    								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
    			String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
    			phones.close();
    	}
    	c.close();
    	return phoneNumber;
    }*/

    /**
     * Implementation for the OnNdefPushCompleteCallback interface
     */
    @Override
    public void onNdefPushComplete(NfcEvent arg0) {
        // A handler is needed to send messages to the activity when this
        // callback occurs, because it happens from a binder thread
        mHandler.obtainMessage(MESSAGE_SENT).sendToTarget();
    }

    /** This handler receives a message from onNdefPushComplete */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_SENT:
                Toast.makeText(getApplicationContext(), "Message sent!", Toast.LENGTH_LONG).show();
                break;
            case CONTACT_ADDED:
                Toast.makeText(getApplicationContext(), "Contact successfully added!", Toast.LENGTH_LONG).show();
                break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        //display for testing purpose
        mInfoText.setText(new String(msg.getRecords()[0].getPayload()));
        //get the message
        String message = new String(msg.getRecords()[0].getPayload());
        //substring the phone number and display name
        String phoneNumber = message.substring(0, 8);
        String displayName = message.substring(8);
        //get latest contact ID
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = ops.size();
        
        ContentValues values = new ContentValues();
        values.put(Data.RAW_CONTACT_ID, rawContactInsertIndex);
        
        ops.add(ContentProviderOperation.newInsert(
        	    ContactsContract.RawContacts.CONTENT_URI)
        	    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
        	    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
        	    .build()
        	);

        //------------------------------------------------------ Names
        if(displayName != null)
        {           
        	    ops.add(ContentProviderOperation.newInsert(
        	        ContactsContract.Data.CONTENT_URI)              
        	        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
        	        .withValue(ContactsContract.Data.MIMETYPE,
        	            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
        	        .withValue(
        	            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,     
        	            displayName).build()
        	    );
        } 

       	//------------------------------------------------------ Mobile Number                      
       	if(phoneNumber != null)
       	{
        	    ops.add(ContentProviderOperation.
        	        newInsert(ContactsContract.Data.CONTENT_URI)
        	        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
        	        .withValue(ContactsContract.Data.MIMETYPE,
        	        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
        	        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
        	        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, 
        	        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
        	        .build()
        	    );
       	}
       	try {
           //insert to contact
       		getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        }catch (Exception e) {               
           e.printStackTrace();
           Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
       	//send success message
       	mHandler.obtainMessage(CONTACT_ADDED).sendToTarget();
       	//Insert into your DB as well
       	DatabaseHelper db = new DatabaseHelper(this);
       	db.addContact(new Contact(displayName, phoneNumber));
       	finish();
    }

    /**
     * Creates a custom MIME type encapsulated in an NDEF record
     *
     * @param mimeType
     */
    public NdefRecord createMimeRecord(String mimeType, byte[] payload) {
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
        NdefRecord mimeRecord = new NdefRecord(
                NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
        return mimeRecord;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // If NFC is not available, we won't be needing this menu
        if (mNfcAdapter == null) {
            return super.onCreateOptionsMenu(menu);
        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_beam, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent intent = new Intent(Settings.ACTION_NFCSHARING_SETTINGS);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
