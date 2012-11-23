package appspot.smartboxsmu;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import appspot.smartboxsmu.helpers.DatabaseHelper;
import appspot.smartboxsmu.network.ContactHelper;
import appspot.smartboxsmu.network.NetworkRequestFactory;
import appspot.smartboxsmu.network.Util;
import appspot.smartboxsmu.parcelable.Contact;
import appspot.smartboxsmu.parcelable.FindSlotResult;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class DistanceCheckGETRequest extends NetworkRequestFactory {
	
	
	public DistanceCheckGETRequest(Context context) {
		super(context, null, true);
	}
	
	@Override
	public HttpUriRequest sendDataToServer(String url)
			throws JSONException, UnsupportedEncodingException {
		HttpGet post = getHttpGet(url);
		
		post.setHeader("Accept", "application/json");
		post.setHeader("Content-type", "application/json");
		return post;
	}

	@Override
	public void parseResponseFromServer(String result) {
		if (statusCode == HttpStatus.SC_OK) {
			try {
			//	JSONObject obj = new JSONObject(result);
				 JSONObject object = (JSONObject) new JSONTokener(result).nextValue();
				    JSONArray array = object.getJSONArray("routes");
				        //Log.d("JSON","array: "+array.toString());
			
				    //Routes is a combination of objects and arrays
				    JSONObject routes = array.getJSONObject(0);
				        //Log.d("JSON","routes: "+routes.toString());
			
				    String summary = routes.getString("summary");
				        //Log.d("JSON","summary: "+summary);
			
				    JSONArray legs = routes.getJSONArray("legs");
				        //Log.d("JSON","legs: "+legs.toString());
			
				    JSONObject steps = legs.getJSONObject(0);
				            //Log.d("JSON","steps: "+steps.toString());
			
				    JSONObject distance = steps.getJSONObject("distance");
				        //Log.d("JSON","distance: "+distance.toString());
				    JSONObject time = steps.getJSONObject("duration");

				            String sDistance = distance.getString("text");
				            String sDuration = time.getString("text");
				            
				    Intent intent = new Intent(context, FindDateResultActivity.class);
					intent.putExtra("distance", sDistance);
					intent.putExtra("duration", sDuration);
					context.startActivity(intent);
			} catch (JSONException e) {
				Log.e("FinDatePOST", e.toString());
			}
			
		} else {
			// Display error messages
			// Expected {error: String} JSON
			JSONObject obj;
			try {
				obj = new JSONObject(result);
				Util.alertToast(context, obj.getString("error"));
			} catch (JSONException e) {
				Log.e("FinDatePOST", e.toString());
			}
		}
	}

	@Override
	public void additionalExceptionHandling() {
		//EMPTY
	}
}