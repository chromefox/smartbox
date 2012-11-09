package appspot.smartboxsmu.parcelable;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import appspot.smartboxsmu.model.Group;

import com.google.gson.Gson;

public class User implements Parcelable {
	private String username;
	private String deviceRegId;
	private String name;
	private String email;
	private String mobileNumber;
	private ArrayList<Group> groupList;

	public User() {
		// initialization
		groupList = new ArrayList<Group>();
	};

	// Standard implementation
	public User(Parcel in) {
		readFromParcel(in);
	}

	// Auto-generated method stub
	public int describeContents() {
		return 0;
	}

	// Method to read from a parcel (deserialization)
	public void readFromParcel(Parcel in) {
		name = in.readString();
		email = in.readString();
		mobileNumber = in.readString();
		username = in.readString();
		deviceRegId = in.readString();
		in.readTypedList(groupList, Group.CREATOR);
	}

	// Method to write to a parcel (serialization)
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(name);
		out.writeString(email);
		out.writeString(mobileNumber);
		out.writeString(username);
		out.writeString(deviceRegId);
		out.writeTypedList(groupList);
	}

	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		public User[] newArray(int size) {
			return new User[size];
		}
	};

	// {"encodedKey":"agtzbWFydGJveHNtdXIMCxIFVXNlcnMiAXQM","password":"t","priv":0,"name":"T","email":"t","mobileNumber":"2"}
	public ArrayList<Group> getGroupList() {
		return groupList;
	}

	public void setGroupList(ArrayList<Group> groupList) {
		this.groupList = groupList;
	}
	
	public void setGroupList(JSONArray array) throws JSONException {
		for(int i = 0; i < array.length(); i++) {
			JSONObject obj = (JSONObject) array.get(i);
			Group group = new Gson().fromJson(obj.toString(), Group.class);
			groupList.add(group);
		}
	}

	public void mapObject(String JSONResponse) {
		try {
			JSONObject json = new JSONObject(JSONResponse);
			this.setName(json.getString("name"));
			this.setEmail(json.getString("email"));
			this.setMobileNumber(json.getString("mobileNumber"));
			try {
				this.setDeviceRegId(json.getString("deviceRegId"));
			} catch (Exception e) {
				this.setDeviceRegId("");
			}
			JSONArray array = json.getJSONArray("groupList");
			this.setGroupList(array);
		} catch (Exception e) {
			Log.e("User mapping", "Mapping exception");
		}
	}

	/*
	 * ==========================================================================
	 * === Default Getters and Setters
	 * ============================================
	 * ================================
	 */

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDeviceRegId() {
		return deviceRegId;
	}

	public void setDeviceRegId(String deviceRegId) {
		this.deviceRegId = deviceRegId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
}
