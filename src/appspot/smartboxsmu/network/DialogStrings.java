package appspot.smartboxsmu.network;

/**
 * This enum class is used to encapsulate strings that the app used to display error dialogs etc.
 * Try not to hard-code String messages in the source code and use this class or extend/create another Enum class
 * in order to deal with more specific scenarios/better organizations or abstractions.
 * @author Ronny
 */
public enum DialogStrings {
	NO_WRITEABLE_STORAGE("No writeable SD card detected"),
	DELETE_PROGRESS_DIALOG("Deleting item..."),
	SAVING_AS_MOMENT_DIALOG("Saving to timeline..."),
	LOGIN_FAIL_TITLE("Invalid Login"),
	LOGIN_FAIL_MSG("Wrong username and password combination"),
	LOGIN_PROGRESS_DIALOG("Verifying credential. Please wait..."),
	INVITE_PROGRESS_DIALOG("lnviting..."),
	PICTURE_GET_PROGRESS_DIALOG("Loading..."),
	PICTURE_POST_PROGRESS_DIALOG("Uploading..."),
	UPDATE_ACCOUNT_PROGRESS_DIALOG("Updating..."),
	MEMO_GET_PROGRESS_DIALOG("Loading memos"),
	MEMO_POST_OR_UPDATE_PROGRESS_DIALOG("Saving memo"),
	DATE_GET_PROGRESS_DIALOG("Loading dates"),
	DATE_POST_OR_UPDATE_PROGRESS_DIALOG("Saving date"),
	SERVER_BUSY_TITLE("Server Unreachable"),
	SERVER_BUSY_MSG("Server is unreachable. Please try again."),
	NO_NETWORK_AVAILABILITY_TITLE("No network detected"),
	NO_NETWORK_AVAILABILITY_MSG("Lovebyte needs either Wifi or 3G to connect to the server."),
	SOCKET_CONNECTION_ERROR_TITLE("Socket Error"),
	SOCKET_CONNECTION_ERROR_MSG("Connection is interrupted. Please resend request"),
	SOMETHING_WRONG_TITLE("Error"),
	SOMETHING_WRONG_MSG("Oops. Something went wrong.");

	private final String description;

	private DialogStrings(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
