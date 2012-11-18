package appspot.smartboxsmu;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import appspot.smartboxsmu.model.Group;
import appspot.smartboxsmu.network.NetworkRequestFactory;
import appspot.smartboxsmu.network.Util;

import com.google.gson.Gson;

public class GroupPOSTRequest extends NetworkRequestFactory {
	public final static byte CREATE_GROUP = 0;
	public final static byte UPDATE_GROUP = 1;
	public final static byte DELETE_GROUP = 2;
	public final static byte GET_ALL_GROUPS = 3;
	
	private byte mode;
	private Group group;
	public GroupPOSTRequest(Context context, Group group, byte mode) {
		super(context, null, true);
		this.group = group;
		this.mode = mode;
	}
	
	@Override
	public HttpUriRequest sendDataToServer(String url)
			throws JSONException, UnsupportedEncodingException {
		HttpUriRequest req = null;
		switch(mode) {
		case CREATE_GROUP:
			req = handleCreateGroup(url);
			break;
		case UPDATE_GROUP:
			
			break;
		case DELETE_GROUP:
			
			break;
		case GET_ALL_GROUPS:
			//Send user email (unique identifier) and get all user's group
			req = handleGetAllGroup(url);
			break;
		}
	
		return req;
	}
	
	public HttpPost handleCreateGroup(String url) throws UnsupportedEncodingException {
		String json = new Gson().toJson(group);
		StringEntity se = new StringEntity(json);
		//setting httpPost Params
		HttpPost post = getHttpPost(url);
		post.setEntity(se);
		post.setHeader("Accept", "application/json");
		post.setHeader("Content-type", "application/json");
		return post;
	}
	
	public HttpPost handleGetAllGroup(String url) throws UnsupportedEncodingException {
		String json = new Gson().toJson(MainApplication.user.getEmail());
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
			switch(mode) {
			case CREATE_GROUP:
				Util.alertToast(context, "Group Created");
				//Append the Group to the groupAdapter and refresh the listview
				
				//Redirect to Group List Page
				break;
			case UPDATE_GROUP:
				
				break;
			case DELETE_GROUP:
				
				break;
			case GET_ALL_GROUPS:
				//Parse all group
				
				//Add to Array
				
				//Refresh adapter
				break;
			}
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