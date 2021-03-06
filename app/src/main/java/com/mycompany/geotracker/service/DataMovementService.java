/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker.service;


import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.mycompany.geotracker.controller.MyAccountActivity;
import com.mycompany.geotracker.controller.UserPreferenceActivity;
import com.mycompany.geotracker.data.MyData;
import com.mycompany.geotracker.model.User;
import com.mycompany.geotracker.receiver.GeoBroadcastReceiver;
import com.mycompany.geotracker.server.LocationToServer;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DataMovementService extends IntentService implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static Location myLocation;
    public static LocationListener locationListener;
    public static LocationManager locationManager;
    static int counter = 0;
    private static ConnectivityManager mConnectivityManager;
    private static boolean isConnected;
    private static GoogleApiClient mGoogleApiClient;
    private SharedPreferences sharedPref;
    public DataMovementService() {
        super("DataMovement");
    }

    /**
     * Starts the service.
     * @param intent the intent
     * @param flags the flags
     * @param startId the start id
     * @return start the service
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        System.out.println( "service starting");

        return START_NOT_STICKY;
    }

    /**
     * Handles all of the location information, network connectivity, and the battery.
     * @param intent the intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        sharedPref = this.getSharedPreferences(UserPreferenceActivity.USER_PREF,
                Context.MODE_PRIVATE);
        int status = sharedPref.getInt(UserPreferenceActivity.SAMPLING_STATUS, 0);
        if (status != 0) {
            initial(this);

            NetworkInfo mWifi = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (myLocation != null && mWifi.isConnected() && locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,
                        100000, 40, locationListener);
                myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                Log.i("NETWORK", "PASSIVE_PROVIDER");
            } else if (myLocation != null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        100000, 40, locationListener);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }

                Log.i("NETWORK", "GPS_PROVIDER");
            }

            if (myLocation != null && isConnected) {
                sharedPref = this.getSharedPreferences(UserPreferenceActivity.USER_PREF,
                        Context.MODE_PRIVATE);

                // now we have current location
                String uid = sharedPref.getString(MyAccountActivity.UID, null);
                String latStr = Double.toString(myLocation.getLatitude());
                String lonStr = Double.toString(myLocation.getLongitude());
                String speedStr = Double.toString((double) myLocation.getSpeed());
                String headingStr = Double.toString((double) myLocation.getBearing());
                String timestampStr = Long.toString(System.currentTimeMillis() / 1000);

                int samplingInterval = Integer.parseInt(sharedPref.getString(UserPreferenceActivity.
                        SAMPLING_INTERVAL_POWER_ON, "60"));
                int uploadInterval = sharedPref.getInt(UserPreferenceActivity.UPLOAD_INTERVAL, 60);

                /**
                 * This is the current battery tester. The times are not 100% correct yet, because the service will
                 * continue the upload every so often, more frequently than the sampling of the data.
                 */
                boolean charging_status = sharedPref.getBoolean(UserPreferenceActivity.
                        CHARGING_STATUS, false);
                if (GeoBroadcastReceiver.isConnected || charging_status) {
                    System.out.println("The interval is " + samplingInterval);
                    processLocation(this, samplingInterval, uploadInterval, uid, latStr, lonStr, speedStr,
                            headingStr, timestampStr);
                } else {
                    if (samplingInterval < 300) {
                        samplingInterval = 300;
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(UserPreferenceActivity.SAMPLING_INTERVAL_POWER_OFF,
                                Integer.toString(samplingInterval));
                        editor.commit();
                    }
                    processLocation(this, samplingInterval, 0, uid, latStr, lonStr, speedStr,
                            headingStr, timestampStr);
                }

                System.out.println("Updated current location: " + myLocation.getLatitude() + ", " +
                        myLocation.getLongitude());
            } else {
                if (myLocation == null) {
                } else
                    System.out.println("NetWork is NOT available");
            }
            SystemClock.sleep(5000);
            locationManager.removeUpdates(locationListener);
            locationManager = null;
        }
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(UserPreferenceActivity.SAMPLING_STATUS, 1);
        editor.commit();
        status = sharedPref.getInt(UserPreferenceActivity.SAMPLING_STATUS, 1);
    }

    private void processLocation(Context context, int samplingInterval, int uploadInterval, String uid,
                                 String latStr, String lonStr, String speedStr, String headingStr,
                                 String timestampStr) {
        int interval = uploadInterval / samplingInterval;

        System.out.println("interval " + interval + " sampling " + samplingInterval +
                " upload " + uploadInterval + " timestamp " + timestampStr + " counter " + counter);

        MyData myData = new MyData(context);
        final ArrayList<User> allData = myData.selectAllUsers();
        ArrayList<com.mycompany.geotracker.model.Location> allDataLocation =
                myData.selectAllLocations();

        if (allData.size() != 0) {
            uid = allData.get(allData.size() - 1).getUserID();
        }

        counter++;

        try {
            myData.insertLocation(uid, latStr, lonStr, speedStr, headingStr, Long.parseLong(timestampStr));
            boolean charging_status = sharedPref.getBoolean(UserPreferenceActivity.
                    CHARGING_STATUS, false);
            if (GeoBroadcastReceiver.isConnected || charging_status || MyAccountActivity.pushNow  ) {

                if (counter >= interval || allDataLocation.size() > interval ||MyAccountActivity.pushNow ) {
                    Log.i("LocToServer", "UPloading to Server");
                    allDataLocation = myData.selectAllLocations();
                    for (com.mycompany.geotracker.model.Location loc : allDataLocation) {
                        new LocationToServer().execute(uid, loc.getLat(), loc.getLon(),
                                loc.getSpeed(), loc.getHeading(), Long.toString(loc.getTimestamp()));
                    }

                    myData.deleteAllLocations();

                    counter = 0;
                    MyAccountActivity.pushNow = false;
                    Log.i("LocServer", "Push to server");
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        myData.close();
    }

    /**
     * Stops the service.
     * @param context the context
     */
    public static void stopService(Context context) {

        Intent intentAlarm = new Intent(context, DataMovementService.class);
        PendingIntent pIntent = PendingIntent.getService(context, 0, intentAlarm, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pIntent);
        pIntent.cancel();
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
        locationManager =null;
        ComponentName receiver = new ComponentName(context, GeoBroadcastReceiver.class);

        PackageManager pm1 = context.getPackageManager();
        pm1.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);


        Toast.makeText(context, "Location Service has been Disable", Toast.LENGTH_SHORT).show();
    }

    /**
     * Starts update location service.
     * @param context the context
     * @param sharedPref the shared preferences
     */
    public static void startService(Context context, SharedPreferences sharedPref) {
        boolean isOn = sharedPref.getBoolean(UserPreferenceActivity.TRACKING_SWITCH, true);
        int samplingInterval = Integer.parseInt(sharedPref.getString(UserPreferenceActivity
                .SAMPLING_INTERVAL_POWER_OFF, "300"));

        if (GeoBroadcastReceiver.isConnected) {
            samplingInterval = Integer.parseInt(sharedPref.getString(UserPreferenceActivity
                    .SAMPLING_INTERVAL_POWER_ON, "60"));
        }

        samplingInterval *= 1000;
        Intent intentAlarm = new Intent(context, DataMovementService.class);

        PendingIntent pIntent = PendingIntent.getService(context, 0, intentAlarm, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //set the alarm for particular time

        if (isOn) {
            Log.i("start service", "Sampling Interval " + samplingInterval/1000);
            alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis()
                    , samplingInterval, pIntent);  // samplingInterval

            ComponentName receiver = new ComponentName(context, GeoBroadcastReceiver.class);

            PackageManager pm1 = context.getPackageManager();
            pm1.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);

        } else {
            alarmManager.cancel(pIntent);
            pIntent.cancel();
            Toast.makeText(context, "Tracking Off", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Initializes the network connectivity.
     * @param c the context
     */
    public void initial(Context c) {

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
               System.out.println("on location change" + location.toString());
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
        };

        buildGoogleApiClient();
        mGoogleApiClient.connect();

        locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
        mConnectivityManager = (ConnectivityManager)
                c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        System.out.println("Network connectivity: " + Boolean.toString(isConnected));
    }

    /**
     * Builds the google api client.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Connects to the google api.
     * @param bundle the bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        myLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
