
package com.mycompany.geotracker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mycompany.geotracker.controller.TrackingLocation;
import com.mycompany.geotracker.controller.UserPreferenceActivity;
import com.mycompany.geotracker.data.MyData;
import com.mycompany.geotracker.model.User;
import com.mycompany.geotracker.server.LocationToServer;
import com.mycompany.geotracker.service.DataMovementService;

import java.util.ArrayList;

/**
 *
 * Restart service after system reboot
 */
public class LocationBroadcastReceiver extends BroadcastReceiver {

    public static LocationListener locationListener;
    public static LocationManager locationManager;
    public static Location myLocation;
    static int counter = 0;
    private static final String TAG = "NetWork availability";
    private ConnectivityManager mConnectivityManager;

    /**
     * Receives the locations.
     * @param context the context
     * @param intent the intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("*****************************Started LocationBroadcastReceiver ******");
        //Toast.makeText(context, ": ", Toast.LENGTH_SHORT).show();
        /*if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            SharedPreferences sharedPref = context.getSharedPreferences(UserPreferenceActivity.USER_PREF,
                    Context.MODE_PRIVATE);

            if (sharedPref.getBoolean(UserPreferenceActivity.TRACKING_SWITCH, true)) {
                System.out.println("Tracking ON ********** from BroadcastReceiver");
                DataMovementService.startService(context, sharedPref);
            } else {
                System.out.println("Tracking OFF *********** from BroadcastReiver");
                DataMovementService.stopService(context);

            }
         }*/

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                // Log.i("LOCATION SERVICES", location.toString());
                //  mLocationLog.addLocation(location);
                //    myLocation = location;
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        locationManager = (LocationManager) context.getSystemService(
                Context.LOCATION_SERVICE);
        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0, 0, locationListener);

        mConnectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        Log.i(TAG, "Network connectivity: " + Boolean.toString(isConnected));
        NetworkInfo mWifi = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else if (mWifi.isConnected()) {
             myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }

        if (myLocation != null && isConnected) {
            /*******....  *****/
            long timestamp = System.currentTimeMillis() / 1000;

            // now we have current location
            String uid = "";
            String latStr = Double.toString(myLocation.getLatitude());
            String lonStr = Double.toString(myLocation.getLongitude());
            String speedStr = Double.toString((double) myLocation.getSpeed());
            String headingStr = Double.toString((double) myLocation.getBearing());
            String timestampStr = Long.toString(System.currentTimeMillis() / 1000);

            SharedPreferences sharedPref = context.getSharedPreferences(UserPreferenceActivity.USER_PREF,
                    Context.MODE_PRIVATE);
            int interval_track = Integer.parseInt(sharedPref.getString(UserPreferenceActivity.
                    TRACKING_INTERVAL, "60"));
            int interval_upload = sharedPref.getInt(UserPreferenceActivity.UPLOAD_INTERVAL, 60);
            int interval = interval_upload / interval_track;

            System.out.println("****************interval " + interval + " tracking " + interval_track +
                    " uploading " + interval_upload + " timestamp " + timestampStr + " counter " + counter);

            MyData myData = new MyData(context);
            final ArrayList<User> allData = myData.selectAllUsers();
            ArrayList<com.mycompany.geotracker.model.Location> allDataLocation =
                    myData.selectAllLocations();

            if (allData.size() != 0) {
                uid = allData.get(allData.size() - 1).getUserID();
            }

            counter++;

            try {
                if (allDataLocation.size() != 0 && counter == 1) {
                    myData.deleteAllLocations();
                }
                myData.insertLocation(uid, latStr, lonStr, speedStr, headingStr, timestamp);

                if (counter >= interval) {
                    allDataLocation = myData.selectAllLocations();
                    for (com.mycompany.geotracker.model.Location loc : allDataLocation) {
                        new LocationToServer().execute(uid, loc.getLat(), loc.getLon(),
                                loc.getSpeed(), loc.getHeading(), Long.toString(loc.getTimestamp()));
                    }
                    counter = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            myData.close();

            Toast.makeText(context, "Updated current location: " + myLocation.getLatitude() + ", " +
                    myLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        } else {
            if (myLocation == null) {
                Toast.makeText(context, "NO last location is found", Toast.LENGTH_SHORT).show();
            } else
            Toast.makeText(context, "NetWork is NOT available", Toast.LENGTH_SHORT).show();
        }
    }

}
