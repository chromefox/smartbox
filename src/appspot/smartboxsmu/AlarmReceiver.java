package appspot.smartboxsmu;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

	private static final String DEBUG_TAG = "AlarmReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
	    Intent service = new Intent(context, PushLocationService.class);
	    context.startService(service);
	}

}