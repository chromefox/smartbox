package appspot.smartboxsmu.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatMessage implements Parcelable {
	private String message;

	public ChatMessage() {
	}

	public ChatMessage(String message) {
		this.message = message;
	}

	// Standard implementation
	public ChatMessage(Parcel in) {
		readFromParcel(in);
	}

	// Method to read from a parcel (deserialization)
	public void readFromParcel(Parcel in) {
		message = in.readString();
	}

	// Method to write to a parcel (serialization)
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(message);
	}

	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public ChatMessage createFromParcel(Parcel in) {
			return new ChatMessage(in);
		}

		public ChatMessage[] newArray(int size) {
			return new ChatMessage[size];
		}
	};

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}
