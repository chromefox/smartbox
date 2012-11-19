package appspot.smartboxsmu;



import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import appspot.gcm.example.CommonUtilities;
import appspot.smartboxsmu.LoginActivity.POSTRequest;
import appspot.smartboxsmu.network.URL;
import appspot.smartboxsmu.network.Util;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	/*
	 * onRegistered(Context context, String regId): Called after a registration
	 * intent is received, passes the registration ID assigned by GCM to that
	 * device/application pair as parameter. Typically, you should send the
	 * regid to your server so it can use it to send messages to this device.
	 * 
	 * onUnregistered(Context context, String regId): Called after the device
	 * has been unregistered from GCM. Typically, you should send the regid to
	 * the server so it unregisters the device.
	 * 
	 * onMessage(Context context, Intent intent): Called when your server sends
	 * a message to GCM, and GCM delivers it to the device. If the message has a
	 * payload, its contents are available as extras in the intent.
	 * 
	 * onError(Context context, String errorId): Called when the device tries to
	 * register or unregister, but GCM returned an error. Typically, there is
	 * nothing to be done other than evaluating the error (returned by errorId)
	 * and trying to fix the problem.
	 * 
	 * onRecoverableError(Context context, String errorId): Called when the
	 * device tries to register or unregister, but the GCM servers are
	 * unavailable. The GCM library will retry the operation using exponential
	 * backup, unless this method is overridden and returns false. This method
	 * is optional and should be overridden only if you want to display the
	 * message to the user or cancel the retry attempts.
	 */
	private static String TAG = "GCMIntentService";
	
	@Override
	protected void onError(Context context, String errorId) {
		Log.e(TAG, "Intent Error");
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		//This is we parse the message and payload sent by GCM
		String message = intent.getStringExtra("msg");
		CommonUtilities.displayMessage(context, message);
		// notifies user
		generateNotification(context, message);
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		//send regId that is tied to the device back to the server for this particular user
		MainApplication.user.setDeviceRegId(regId);
		RegisterPOSTRequest post = new RegisterPOSTRequest(context);
		post.execute(URL.REGISTER_DEVICE);
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		// TODO unregister device or make the state inactive.

	}
	
	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	private static void generateNotification(Context context, String message) {
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, message, when);
		String title = context.getString(R.string.app_name);
		Intent notificationIntent = new Intent(context, DemoActivity.class);
		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, title, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(0, notification);
	}
	

}
