package appspot.smartboxsmu.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.Calendar;
import java.util.Properties;

import org.apache.http.HttpStatus;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class Util {
	public static int MAP_ZOOM_LEVEL = 16;
	private final static String FILENAME_PREFIX = "IMG_";
	private static String BM_FILENAME = "temp_bm";

	public static void cacheBitmap(Context context, Bitmap bm) {
		FileOutputStream fos;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		try {
			fos = context.openFileOutput(BM_FILENAME, Context.MODE_PRIVATE);
			fos.write(baos.toByteArray());
			fos.close();
		} catch (IOException e) {

		}
	}

	public static Bitmap getCachedBitmap(Context context) {
		FileInputStream fis;
		Bitmap bm = null;
		try {
			fis = context.openFileInput(BM_FILENAME);
			InputStream input = new BufferedInputStream(fis);
			bm = BitmapFactory.decodeStream(input);
		} catch (FileNotFoundException e) {

		}
		return bm;
	}

	public static void alertbox(Context context, String title, String mymessage) {
		new AlertDialog.Builder(context)
				.setMessage(mymessage)
				.setTitle(title)
				.setCancelable(true)
				.setNeutralButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						}).show();
	}

	public static void alertToast(Context context, String mymessage) {
		Toast.makeText(context, mymessage, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Default behaviour is not to show error.
	 * 
	 * @param context
	 *            the context of the activity in which the method is running
	 * @param exception
	 *            the exception thrown by method/class
	 * @return true if exception exists and handled accordingly; false otherwise
	 */
	public static boolean handleConnectionException(Context context,
			Exception exception) {
		return Util.handleConnectionException(context, exception, false, false);
	}

	/**
	 * Default behaviour is to show error in Toast instead of the blocking
	 * Dialog Box.
	 * 
	 * @param context
	 *            the context of the activity in which the method is running
	 * @param exception
	 *            the exception thrown by method/class
	 * @param showError
	 *            specify whether or not to show error. Default to false if
	 *            omitted. Default to Toast mode if true.
	 * @return true if exception exists and handled accordingly; false otherwise
	 */
	public static boolean handleConnectionException(Context context,
			Exception exception, boolean showError) {
		return Util.handleConnectionException(context, exception, showError,
				true);
	}

	/**
	 * This will handle the common ConnectionActivity and display the Error
	 * messages accordingly.
	 * 
	 * @param context
	 *            the context of the activity in which the method is running
	 * @param exception
	 *            the exception thrown by method/class
	 * @param showError
	 *            whether or not to show Error
	 * @param toast
	 *            Display error in non-blocking Toast or the blocking Dialog
	 *            Box.
	 * @return true if exception exists and handled accordingly; false otherwise
	 */
	public static boolean handleConnectionException(Context context,
			Exception exception, boolean showError, boolean toast) {
		if (exception != null) {
			if (exception instanceof ConnectTimeoutException) {
				if (showError) {
					if (toast) {
						Util.alertToast(context,
								DialogStrings.SERVER_BUSY_MSG.getDescription());
					} else {
						Util.alertbox(context, DialogStrings.SERVER_BUSY_TITLE
								.getDescription(),
								DialogStrings.SERVER_BUSY_MSG.getDescription());
					}
				}
				return true;
			} else if (exception instanceof SocketTimeoutException) {
				if (showError) {
					if (toast) {
						Util.alertToast(context,
								DialogStrings.SERVER_BUSY_MSG.getDescription());
					} else {
						Util.alertbox(context, DialogStrings.SERVER_BUSY_TITLE
								.getDescription(),
								DialogStrings.SERVER_BUSY_MSG.getDescription());
					}
					return true;
				}
			} else if (exception instanceof NoNetworkException) {
				if (showError) {
					if (toast) {
						Util.alertToast(context,
								DialogStrings.NO_NETWORK_AVAILABILITY_MSG
										.getDescription());
					} else {
						Util.alertbox(context,
								DialogStrings.NO_NETWORK_AVAILABILITY_TITLE
										.getDescription(),
								DialogStrings.NO_NETWORK_AVAILABILITY_MSG
										.getDescription());
					}
					return true;
				}
			} else {
				if (showError) {
					// This exception is usually called when connection is
					// forcefully closed (when user suddenly lose connection)
					if (toast) {
						Util.alertToast(context,
								DialogStrings.SOMETHING_WRONG_MSG
										.getDescription());
					} else {
						Util.alertbox(context,
								DialogStrings.SOMETHING_WRONG_TITLE
										.getDescription(),
								DialogStrings.SOMETHING_WRONG_MSG
										.getDescription());
					}
					return true;
				}
			}
		}
		return false;
	}

	private static String getProperty(Context context, String propertyName) {
		Resources resources = context.getResources();
		AssetManager assetManager = resources.getAssets();
		String value = "";
		// Read from the /assets directory
		try {
			InputStream inputStream = assetManager.open("property.properties");
			Properties properties = new Properties();
			properties.load(inputStream);
			value = properties.getProperty(propertyName);
		} catch (IOException e) {

		}
		return value;
	}

	/**
	 * Utility method to check for internet connection (Wireless/3G) before
	 * sending any request Handle displaying the no network error/warning to the
	 * user as well.
	 * 
	 * @param appContext
	 *            the application context of the activity
	 * @return true if the network is available; false if otherwise
	 */
	public static boolean isNetworkAvailable(Context appContext) {
		ConnectivityManager cm = (ConnectivityManager) appContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}

	/**
	 * Check whether external storage is currently available and/or writeable
	 * 
	 * @return Boolean array. Boolean[0] returns read availability and [1]
	 *         returns write access for External Storage.
	 */
	public static boolean[] getStorageAccessibility() {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// Something else is wrong. It may be one of many other states, but
			// all we need
			// to know is we can neither read nor write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}

		return new boolean[] { mExternalStorageAvailable,
				mExternalStorageWriteable };
	}

	public static boolean isStorageWriteable() {
		return getStorageAccessibility()[1];
	}

	public static void saveImageToSDCard(byte[] byteArray, Context context) {
		if (Util.isStorageWriteable()) {
			// Get the Pictures dir
			File path = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

			final Calendar calendar = Calendar.getInstance();
			String filename = FILENAME_PREFIX + calendar.get(Calendar.YEAR)
					+ calendar.get(Calendar.MONTH)
					+ calendar.get(Calendar.DAY_OF_WEEK);
			File file = new File(path, filename + ".jpg");

			// Save the pictures: filename -> IMG_CURRENTDATE_CURRENTTIME
			try {
				// Make sure the Pictures directory exists.
				path.mkdirs();

				// Writing stream to file from byteArray
				OutputStream os = new FileOutputStream(file);
				BufferedOutputStream br = new BufferedOutputStream(os);
				br.write(byteArray);
				br.close();

				// Tell the media scanner about the new file so that it is
				// immediately available to the user.
				MediaScannerConnection.scanFile(context,
						new String[] { file.toString() }, null,
						new MediaScannerConnection.OnScanCompletedListener() {

							public void onScanCompleted(String path, Uri uri) {
								Log.i("ExternalStorage", "Scanned " + path
										+ ":");
								Log.i("ExternalStorage", "-> uri=" + uri);
							}
						});

			} catch (IOException e) {
				// Unable to create file, likely because external storage is
				// not currently mounted.
				Log.w("ExternalStorage", "Error writing " + file, e);
			}
			// TODO: Potential problem with older version
		} else {
			// No writeable storage. Display Toast message
			Toast.makeText(context,
					DialogStrings.NO_WRITEABLE_STORAGE.getDescription(),
					Toast.LENGTH_SHORT);
		}
	}

	public static boolean tryParseOKStatusCode(String result) {
		try {
			return Integer.parseInt(result) == HttpStatus.SC_OK;
		} catch (Exception e) {
			
		}
		return false;
	}

}
