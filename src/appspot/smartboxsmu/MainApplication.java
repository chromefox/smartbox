package appspot.smartboxsmu;

import android.app.Application;
import appspot.smartboxsmu.parcelable.User;

//Serve as the global state manager and startup codes (GCM)
public class MainApplication extends Application {
	private static String TAG = "MainApplication";
	
	public static User user;
	@Override
	public void onCreate() {
		
	}
}
