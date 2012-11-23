package appspot.smartboxsmu;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import appspot.smartboxsmu.model.Group;
import appspot.smartboxsmu.network.NetworkRequestFactory;
import appspot.smartboxsmu.network.Util;
import appspot.smartboxsmu.parcelable.LocationSuggestions;

public class LocationCheckGETRequest extends NetworkRequestFactory {
	
	private ArrayList<LocationSuggestions> locations;
	
	public LocationCheckGETRequest(Context context) {
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
				 JSONObject object = (JSONObject) new JSONTokener(result).nextValue();
				    JSONArray array = object.getJSONArray("results");
				locations = new ArrayList<LocationSuggestions>();
				for (int i = 0; i < array.length(); i++) {
					double longitude = array.getJSONObject(i)
							.getJSONObject("geometry")
							.getJSONObject("location").getDouble("lng");
					double latitude = array.getJSONObject(i)
							.getJSONObject("geometry")
							.getJSONObject("location").getDouble("lat");
					String address = array.getJSONObject(i).getString(
							"formatted_address");
					locations.add(new LocationSuggestions(latitude, longitude,
							address));
				}
				            
				// ArrayList<LocationSuggestions> location=get1.getLocations();
				 String[] locationNames=new String[locations.size()];
		           for(int i=0;i<locations.size();i++){
		        	   LocationSuggestions loc=locations.get(i);
		        	   locationNames[i]=loc.getAddress();
		           }
		           
				 AlertDialog.Builder builder = new AlertDialog.Builder(context);
				 builder.setTitle("Locations");

				 ListView modeList = new ListView(context);
				 ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, locationNames);
				 modeList.setAdapter(modeAdapter);
				 
				 builder.setView(modeList);
				 final Dialog dialog = builder.create();

				 dialog.show();
				
				 //set the onclick so that the lat/long and location can be automatically filled in
				// Setting up onItemClickHandler
				modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapter, View item,
							int position, long id) {
						String place = (String) adapter.getAdapter().getItem(
								position);
						// set the place here
						for (LocationSuggestions suggest : locations) {
							if (suggest.getAddress().equals(place)) {
								AddEventActivity act = (AddEventActivity) context;
								act.setChosenSuggestion(suggest);
								act.setPlaceName(suggest.getAddress());
								dialog.cancel();
								break;
							}
						}
					}
				});

				
				 /*
				 //  Dialog dialog = new Dialog(context);
		           dialog.setContentView(R.layout.pop_up_table);
		           dialog.setTitle("Location");
		           dialog.setCancelable(true);
		           String[] locationNames=new String[locations.size()];
		           for(int i=0;i<locations.size();i++){
		        	   LocationSuggestions loc=locations.get(i);
		        	   locationNames[i]=loc.getAddress();
		           }
		           ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,locationNames); 
		         //  setListAdapter(R.id.listExample);
					 modeList.setAdapter(adapter);

		          
		           dialog.show();*/
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
	public ArrayList<LocationSuggestions> getLocations(){
		return locations;
	}
	@Override
	public void additionalExceptionHandling() {
		//EMPTY
	}
}