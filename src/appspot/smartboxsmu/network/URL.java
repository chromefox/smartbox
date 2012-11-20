package appspot.smartboxsmu.network;

//Define all the routes that are used by Android device
/**
 * Define all the routes to the Rails server to keep the codes cleaner and free
 * from hard-coded strings
 * 
 * @author Ronny
 * 
 */
public class URL {
//	private static final String DOMAIN = "http://smartboxsmu.appspot.com";
	private static final String DOMAIN = "http://192.168.1.179:8888";
//	private static final String DOMAIN = "http://10.124.7.172:8888";
	public static final String SIGN_IN = DOMAIN + "/logIn.do";
	public static final String REGISTER_DEVICE = DOMAIN + "/registerDevice";
	public static final String SEND_MESSAGE = DOMAIN + "/sendAll";
	public static final String UNREGISTER_DEVICE = DOMAIN + "/unregisterDevice";
	public static final String REGISTER = DOMAIN + "/register.do";
	public static final String CONTACT_CHECK = DOMAIN + "/contactCheck";
	public static final String ADD_GROUP = DOMAIN + "/addGroup";
	public static final String GET_GROUP = DOMAIN + "/getGroup";
	public static final String SEND_EVENT = DOMAIN + "/sendEvent";
	public static final String FIND_SLOT = DOMAIN + "/findSlot";
}