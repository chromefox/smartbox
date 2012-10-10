package appspot.smartboxsmu.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Parcelable {
	private String name;
	private String mobileNumber;
	private int id;
	private boolean checked = false;

	public Contact() {
	};

	public Contact(String name, String mobileNumber) {
		this.name = name;
		this.mobileNumber = mobileNumber;
	}

	public Contact(int id, String name, String mobileNumber) {
		this.id = id;
		this.name = name;
		this.mobileNumber = mobileNumber;
	}

	// Standard implementation
	public Contact(Parcel in) {
		readFromParcel(in);
	}

	// Auto-generated method stub
	public int describeContents() {
		return 0;
	}

	// Method to read from a parcel (deserialization)
	public void readFromParcel(Parcel in) {
		name = in.readString();
		mobileNumber = in.readString();
	}

	// Method to write to a parcel (serialization)
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(name);
		out.writeString(mobileNumber);
	}

	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Contact createFromParcel(Parcel in) {
			return new Contact(in);
		}

		public Contact[] newArray(int size) {
			return new Contact[size];
		}
	};

	// public void mapObject(String JSONResponse) {
	// try {
	// JSONObject json = new JSONObject(JSONResponse);
	// this.setName(json.getString("name"));
	// this.setEmail(json.getString("email"));
	// this.setMobileNumber(json.getString("mobileNumber"));
	// this.setEncodedKey(json.getString("encodedKey"));
	//
	// try {
	// this.setDeviceRegId(json.getString("deviceRegId"));
	// } catch (Exception e) {
	// this.setDeviceRegId("");
	// }
	// } catch (Exception e) {
	// Log.e("User mapping", "Mapping exception");
	// }
	// }

	/*
	 * ==========================================================================
	 * Default Getters and Setters ============================================
	 */

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void toggleChecked() {
		checked = !checked;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

}
