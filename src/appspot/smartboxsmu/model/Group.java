package appspot.smartboxsmu.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import appspot.smartboxsmu.parcelable.Contact;
import appspot.smartboxsmu.parcelable.User;

public class Group implements Parcelable {
	private Long id;
	private String groupName;
	private String className;
	private int sectionNumber;
	private ArrayList<Contact> contacts;
	private String userEmail;
	private ArrayList<String> memberNames;
	
	public Group() {
		// initialization
		memberNames = new ArrayList<String>();
	};
	
	public Group(String groupName, String className, int sectionNumber) {
		super();
		this.groupName = groupName;
		this.className = className;
		this.sectionNumber = sectionNumber;
	}

	public ArrayList<String> getMemberNames() {
		return memberNames;
	}

	public void setMemberNames(ArrayList<String> memberNames) {
		this.memberNames = memberNames;
	}

	public Group(String groupName, List<Contact> list, String userKey) {
		super();
		this.userEmail = userKey;
		this.groupName = groupName;
		this.contacts = (ArrayList<Contact>) list;
	}

	public Group(Parcel in) {
		readFromParcel(in);
	}

	// Method to read from a parcel (deserialization)
	public void readFromParcel(Parcel in) {
		groupName = in.readString();
		className = in.readString();
		sectionNumber = in.readInt();
		userEmail = in.readString();
		if(memberNames == null) memberNames = new ArrayList<String>();
		in.readStringList(memberNames);
	}

	// Method to write to a parcel (serialization)
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(groupName);
		out.writeString(className);
		out.writeInt(sectionNumber);
		out.writeString(userEmail);
		out.writeStringList(memberNames);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Group createFromParcel(Parcel in) {
			return new Group(in);
		}

		public Group[] newArray(int size) {
			return new Group[size];
		}
	};

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public ArrayList<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(ArrayList<Contact> contacts) {
		this.contacts = contacts;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getSectionNumber() {
		return sectionNumber;
	}

	public void setSectionNumber(int sectionNumber) {
		this.sectionNumber = sectionNumber;
	}

}
