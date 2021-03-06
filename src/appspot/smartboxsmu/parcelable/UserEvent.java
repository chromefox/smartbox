package appspot.smartboxsmu.parcelable;

import java.util.Date;

public class UserEvent {
	private String details;
	private Date startDate;
	private Date endDate;

	public UserEvent() {

	}

	public UserEvent(String details, Date startDate, Date endDate) {
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

	public static UserEvent instantiate(FindSlotResult result, String name) {
		return new UserEvent(name, result.getStartDateObject(),
				result.getEndDateObject());
	}

}
