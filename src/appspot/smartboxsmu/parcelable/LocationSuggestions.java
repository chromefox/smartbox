package appspot.smartboxsmu.parcelable;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationSuggestions implements Parcelable {
	private double latitude;
	private double longitude;
	private String address;

	public LocationSuggestions() {
	}

	public LocationSuggestions(double latitude, double longitude, String address) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
	}

	// Standard implementation
	public LocationSuggestions(Parcel in) {
		readFromParcel(in);
	}

	// Method to read from a parcel (deserialization)
	public void readFromParcel(Parcel in) {
		latitude = in.readDouble();
		longitude = in.readDouble();
		address = in.readString();
	}

	// Method to write to a parcel (serialization)
	public void writeToParcel(Parcel out, int flags) {
		out.writeDouble(latitude);
		out.writeDouble(longitude);
		out.writeString(address);
	}

	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public LocationSuggestions createFromParcel(Parcel in) {
			return new LocationSuggestions(in);
		}

		public LocationSuggestions[] newArray(int size) {
			return new LocationSuggestions[size];
		}
	};

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
	
}
