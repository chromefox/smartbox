package appspot.smartboxsmu;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import appspot.smartboxsmu.network.URL;

public class PushLocationService extends IntentService {
	LocationManager locationManager;
	LocationListener locationListener;
	private Location currentLocation;
	private static final int TWO_MINUTES = 5000;
	private double latitude = 0;
	private double longitude = 0;

	public PushLocationService() {
		super("PushLocationService");
	}

	// Will be called asynchronously be Android
	@Override
	protected void onHandleIntent(Intent intent) {
		// Send your current location to the server
		//Initialize listening to Location Services
		//Acquire a reference to the system Location Manager
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(currentLocation == null) currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		locationSetup();
		
		PushLocationPOSTRequest post = new PushLocationPOSTRequest(this, currentLocation.getLatitude(), currentLocation.getLongitude());
		post.execute(URL.SEND_LOCATION);
	}

	private void locationSetup() {
		// Define a listener that responds to location updates
		locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location
				// provider.
				if (isBetterLocation(location, currentLocation)) {
					currentLocation = location;
					latitude = currentLocation.getLatitude();
					longitude = currentLocation.getLongitude();
				}
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};
	}

	protected boolean isBetterLocation(Location location,
			Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// if it is not network (gps), and isnewer -> always return gps
		// location. Assumed to be much more accurate.
		if (!location.getProvider().equals("network") && isNewer) {
			return true;
		}

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate) {
			return true;
		}
		return false;
	}

}