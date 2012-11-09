package appspot.smartboxsmu;

import android.app.Application;
import appspot.adapter.GroupAdapter;
import appspot.smartboxsmu.parcelable.User;

//Serve as the global state manager and startup codes (GCM)
public class MainApplication extends Application {
	private static String TAG = "MainApplication";
	
	public GroupAdapter groupAdapter;
	
	public static User user;
	@Override
	public void onCreate() {
		
	}
}
