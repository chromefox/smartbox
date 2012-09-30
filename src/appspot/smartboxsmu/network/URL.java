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
	private static final String DOMAIN = "http://smartboxsmu.appspot.com";
	public static final String SIGN_IN = DOMAIN + "/register.do";
	public static final String REGISTER = DOMAIN + "/logIn.do";
}
