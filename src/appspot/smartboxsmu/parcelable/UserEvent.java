package appspot.smartboxsmu.parcelable;

import org.joda.time.DateTime;

public class UserEvent {
	private String details;
	private DateTime startDateTime;
	private DateTime endDateTime;
	
	public UserEvent() {
		
	}
	
	public UserEvent(String details, DateTime startDateTime,
			DateTime endDateTime) {
		super();
		this.details = details;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
	}

	public DateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(DateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	public DateTime getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(DateTime endDateTime) {
		this.endDateTime = endDateTime;
	}
}
