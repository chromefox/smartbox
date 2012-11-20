package appspot.smartboxsmu.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

public class FindSlotResult implements Parcelable {
	private String date;
	private int availableCount;

	public FindSlotResult() {
	}

	public FindSlotResult(String date, int availableCount) {
		this.availableCount = availableCount;
		this.date = date;
	}

	// Standard implementation
	public FindSlotResult(Parcel in) {
		readFromParcel(in);
	}

	// Method to read from a parcel (deserialization)
	public void readFromParcel(Parcel in) {
		date = in.readString();
		availableCount = in.readInt();
	}

	// Method to write to a parcel (serialization)
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(date);
		out.writeInt(availableCount);
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
