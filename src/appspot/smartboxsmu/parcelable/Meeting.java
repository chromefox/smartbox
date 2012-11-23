package appspot.smartboxsmu.parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class Meeting implements Parcelable {
	private String details;
	private Date startDate;
	private Date endDate;
	private double latitude;
	private double longitude;
	private String location;
	
	private final static String SHOWN_FORMAT = "MMM dd,yyyy HH:mm";
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	// Standard implementation
	public Meeting(Parcel in) {
		readFromParcel(in);
	}

	// Method to read from a parcel (deserialization)
	public void readFromParcel(Parcel in) {
		details = in.readString();
		startDate = new Date(in.readLong());
		endDate = new Date(in.readLong());
		latitude = in.readDouble();
		longitude = in.readDouble();
		location = in.readString();
	}

	// Method to write to a parcel (serialization)
	public void writeToParcel(Parcel out, int flags) {
		if(details != null) {
			out.writeString(details);
		} else {
			out.writeString("");
		}
		if(startDate != null) {
			out.writeLong(startDate.getTime());
		}
		
		if(endDate != null) {
			out.writeLong(endDate.getTime());
		}
		
		out.writeDouble(latitude);
		out.writeDouble(longitude);
		out.writeString(location);
	}

	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Meeting createFromParcel(Parcel in) {
			return new Meeting(in);
		}

		public Meeting[] newArray(int size) {
			return new Meeting[size];
		}
	};

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

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

	public Meeting() {

	}
	
	public String getStartDateFormatted() {
		SimpleDateFormat format = new SimpleDateFormat(SHOWN_FORMAT);
		return format.format(startDate);
	}
	
	public String getEndDateFormatted() {
		SimpleDateFormat format = new SimpleDateFormat(SHOWN_FORMAT);
		return format.format(endDate);
	}

	public Meeting(String details, Date startDate, Date endDate) {
		super();
		this.details = details;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public long getStartDateMillis() {
		return startDate.getTime();
	}

	public long getEndDateMillis() {
		return endDate.getTime();
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public static Meeting instantiate(FindSlotResult result, String name) {
		return new Meeting(name, result.getStartDateObject(),
				result.getEndDateObject());
	}

}
