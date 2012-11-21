package appspot.smartboxsmu.parcelable;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.os.Parcel;
import android.os.Parcelable;

public class FindSlotResult implements Parcelable {
	private static final String DATE_FORMAT = "E, d MMM YYYY, HH:mm";
	private static final String DATE_DAY_FORMAT = "E, d MMM YYYY";
	private static final String DATE_HOUR_FORMAT = "HH:mm";
	private String date;
	private String endDate;
	private int availableCount;

	public FindSlotResult() {
	}

	public FindSlotResult(String date, String endDate, int availableCount) {
		this.availableCount = availableCount;
		this.date = date;
		this.endDate = endDate;
	}

	// Standard implementation
	public FindSlotResult(Parcel in) {
		readFromParcel(in);
	}

	// Method to read from a parcel (deserialization)
	public void readFromParcel(Parcel in) {
		date = in.readString();
		availableCount = in.readInt();
		endDate = in.readString();
	}

	// Method to write to a parcel (serialization)
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(date);
		out.writeInt(availableCount);
		out.writeString(endDate);
	}

	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public FindSlotResult createFromParcel(Parcel in) {
			return new FindSlotResult(in);
		}

		public FindSlotResult[] newArray(int size) {
			return new FindSlotResult[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	//Need to transform the full date and time to only day for comparison purpose
	public DateTime getDateTimeDay() {
		DateTimeFormatter fmt = DateTimeFormat.forPattern(DATE_FORMAT);
		DateTimeFormatter fmt2 = DateTimeFormat.forPattern(DATE_DAY_FORMAT);
		DateTime originalDate = fmt.parseDateTime(date);
		return fmt2.parseDateTime(fmt2.print(originalDate));
	}
	
	public long getStartDateMillis() {
		DateTimeFormatter fmt = DateTimeFormat.forPattern(DATE_FORMAT);
		DateTime originalDate = fmt.parseDateTime(date);
		return originalDate.getMillis();
	}
	
	public long getEndDateMillis() {
		DateTimeFormatter fmt = DateTimeFormat.forPattern(DATE_FORMAT);
		DateTime originalDate = fmt.parseDateTime(endDate);
		return originalDate.getMillis();
	}
	
	public String getStartTime() {
		DateTimeFormatter fmt = DateTimeFormat.forPattern(DATE_FORMAT);
		DateTimeFormatter fmt2 = DateTimeFormat.forPattern(DATE_HOUR_FORMAT);
		DateTime originalDate = fmt.parseDateTime(date);
		return fmt2.print(originalDate);
	}
	
	public String getEndTime() {
		DateTimeFormatter fmt = DateTimeFormat.forPattern(DATE_FORMAT);
		DateTimeFormatter fmt2 = DateTimeFormat.forPattern(DATE_HOUR_FORMAT);
		DateTime originalDate = fmt.parseDateTime(endDate);
		return fmt2.print(originalDate);
	}
	
	public Date getStartDateObject() {
		return new Date(getStartDateMillis());
	}
	
	public Date getEndDateObject() {
		return new Date(getEndDateMillis());
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getAvailableCount() {
		return availableCount;
	}

	public void setAvailableCount(int availableCount) {
		this.availableCount = availableCount;
	}
	
	
	
	
}
