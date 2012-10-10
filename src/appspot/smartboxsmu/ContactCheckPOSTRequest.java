package appspot.smartboxsmu;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import appspot.smartboxsmu.helpers.DatabaseHelper;
import appspot.smartboxsmu.network.ContactHelper;
import appspot.smartboxsmu.network.NetworkRequestFactory;
import appspot.smartboxsmu.network.Util;
import appspot.smartboxsmu.parcelable.Contact;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ContactCheckPOSTRequest extends NetworkRequestFactory {
	public ContactCheckPOSTRequest(Context context) {
		super(context, null, true);
	}
	
	@Override
	public HttpUriRequest sendDataToServer(String url)
			throws JSONException, UnsupportedEncodingException {
		List<Contact> contactList = ContactHelper.getAllContact(context);
		//parse contact into jsonArray
		String json = new Gson().toJson(contactList);
		StringEntity se = new StringEntity(json);
		
		//setting httpPost Params
		HttpPost post = getHttpPost(url);
		post.setEntity(se);
		post.setHeader("Accept", "application/json");
		post.setHeader("Content-type", "application/json");
		return post;
	}

	@Override
	public void parseResponseFromServer(String result) {
		if (statusCode == HttpStatus.SC_OK) {
			//Construct the contact list from JSON
			Type collectionType = new TypeToken<ArrayList<Contact>>() {}.getType();
			List<Contact> deviceContactList = new Gson().fromJson(result, collectionType);
			//Save contacts to SQLLite
			DatabaseHelper db = new DatabaseHelper(context);
			for(Contact contact: deviceContactList) {
				db.addContact(contact);
			}
			Util.alertToast(context, "Contacts Saved");
		} else {
			//Display error messages
			// Expected {error: String} JSON
			JSONObject obj;
			try {
				obj = new JSONObject(result);
				Util.alertToast(context, obj.getString("error"));
			} catch (JSONException e) {
				Log.e("Util", "JSON Parsing Error");
			}
		}
	}

	@Override
	public void additionalExceptionHandling() {
		//EMPTY
	}
}