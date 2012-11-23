package appspot.smartboxsmu;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import appspot.smartboxsmu.network.CustomProgressDialog;
import appspot.smartboxsmu.network.Names;
import appspot.smartboxsmu.network.NetworkRequestFactory;
import appspot.smartboxsmu.network.URL;
import appspot.smartboxsmu.network.Util;
import appspot.smartboxsmu.parcelable.User;

import com.google.android.gcm.GCMRegistrar;

public class LoginActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GCMRegistrar.unregister(this);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	@SuppressLint("NewApi")
	public void onClickHandler(View view) {
		Intent intent;
		switch (view.getId()) {
		case R.id.login_button:
			// Make a call to the server to authenticate user

			String email = ((EditText) LoginActivity.this
					.findViewById(R.id.login_email)).getText().toString();
			String password = ((EditText) LoginActivity.this
					.findViewById(R.id.login_password)).getText().toString();
			
			if (email.isEmpty() || password.isEmpty()) {
				Util.alertToast(this, "Fields cannot be empty");
			} else {
				LoginPOSTRequest post = new LoginPOSTRequest(this, email,
						password);
				post.execute(URL.SIGN_IN);
			}
			break;
		case R.id.register_button:
			// Go to register page
			intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
			break;

			// Testing the location manager
			// latitude = currentLocation.getLatitude();
			// longitude = currentLocation.getLongitude();
			//
			// Util.alertToast(this, String.valueOf(latitude) +
			// String.valueOf(longitude));
			//
			// if
			// (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
			// && !locationManager
			// .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			// // if no location service is enabled: Display alertbox to go to
			// // settings page
			// AlertBoxes.locationAlertbox(this);
			// }
			//
		}
	}

	/*
	 * ==========================================================================
	 * ======= NESTED CLASSES
	 * ====================================================
	 * =============================
	 */

	public static class AlertBoxes {
		public static void locationAlertbox(final Context context) {
			new AlertDialog.Builder(context)
					.setMessage(
							"Lovebyte needs at least one location service to be enabled for the feature to be enabled")
					.setTitle("No Location Service enabled")
					.setCancelable(true)
					.setNegativeButton(android.R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							})
					.setNeutralButton(R.string.enable,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// Go to the setting page
									Intent i = new Intent(
											android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
									context.startActivity(i);
								}
							}).show();
		}

	}

}
