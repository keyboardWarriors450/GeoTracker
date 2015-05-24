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
import android.util.Log;
import android.widget.Toast;

import com.mycompany.geotracker.controller.TrackingLocation;
import com.mycompany.geotracker.controller.UserPreferenceActivity;
import com.mycompany.geotracker.data.MyData;
import com.mycompany.geotracker.model.User;
import com.mycompany.geotracker.receiver.LocationBroadcastReceiver;
import com.mycompany.geotracker.server.LocationToServer;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DataMovementService extends IntentService {
    static int counter = 0;
    public static boolean wifiOn = false;
    private Location myLocation;
    private static final String TAG = "DataMovementService";
    private static final String LISTENER = "Location Listener";
    public static LocationListener locationListener;
    public static LocationManager locationManager;
    public DataMovementService() {
        super("DataMovement");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        //    Log.i(TAG, "service starting");

        return START_STICKY;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // System.out.println("*************************DataMovementService.onHandleIntent started");

        //boolean wifi = intent.getExtras().getBoolean("wifi");

//        LocationManager locationManager = (LocationManager) this.getSystemService(
//                Context.LOCATION_SERVICE);

        //LocationManager locationManager = TrackingLocation.get(this).getLocationMan();
      /*  locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
         //       Log.i(">>>>> TEST SERVICES", location.toString());
                //  mLocationLog.addLocation(location);
                //    myLocation = location;
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        locationManager = (LocationManager) this.getSystemService(
                Context.LOCATION_SERVICE);
        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0, 0, locationListener);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } //else if (!wifi) {
           // myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        //}

        if (myLocation != null) {
            *//*******....  *****//*
            long timestamp = System.currentTimeMillis() / 1000;

            // now we have current location
            String uid = "";
            String latStr = Double.toString(myLocation.getLatitude());
            String lonStr = Double.toString(myLocation.getLongitude());
            String speedStr = Double.toString((double) myLocation.getSpeed());
            String headingStr = Double.toString((double) myLocation.getBearing());
            String timestampStr = Long.toString(System.currentTimeMillis() / 1000);

            *//** optional values **//*
            //  double altitude = myLocation.getAltitude();

            MyData myData = new MyData(this);
            final ArrayList<User> allData = myData.selectAllUsers();
            final ArrayList<com.mycompany.geotracker.model.Location> allDataTemp =
                    myData.selectAllLocationsTemp();

            if (allData.size() != 0) {
                uid = allData.get(allData.size() - 1).getUserID();
            }

            try {
                if (allDataTemp.size() != 0) {
                    myData.deleteAllLocationsTemp();
                }
                myData.insertLocationTemp(uid, latStr, lonStr, speedStr, headingStr, timestamp);
            } catch (Exception e) {
                e.printStackTrace();
            }

            myData.close();

            SharedPreferences sharedPref = this.getSharedPreferences(UserPreferenceActivity.USER_PREF,
                    Context.MODE_PRIVATE);
            int interval_track = Integer.parseInt(sharedPref.getString(UserPreferenceActivity.
                    TRACKING_INTERVAL, "60"));
            int interval_upload = sharedPref.getInt(UserPreferenceActivity.UPLOAD_INTERVAL, 60);
            int interval = interval_upload / interval_track;
        //    System.out.println("****************interval " + interval + " tracking " + interval_track +
       //         " uploading " + interval_upload);
            counter++;
            if (counter == interval) {
                new LocationToServer().execute(uid, latStr, lonStr, speedStr, headingStr,
                        timestampStr);
                counter = 0;
            }
        }*/
    }

    /**
     * Creates an Intent and sets the class which will execute when the alarm triggers.
     *
     */
   /* public static void scheduleUpdate(Context context, SharedPreferences sharedPref) {
     //   System.out.println("**********************DataMovementService.scheduleUpdate started*******");

        boolean isOn = sharedPref.getBoolean(UserPreferenceActivity.TRACKING_SWITCH, true);
        int trackingInterval = Integer.parseInt(sharedPref.getString(UserPreferenceActivity
                .TRACKING_INTERVAL, "60"));



        trackingInterval *= 1000;
        Intent intentAlarm = new Intent(context, LocationBroadcastReceiver.class);
        //  intentAlarm.putExtra("wifi", wifiOn);

        // getService(..) for service only
        PendingIntent pIntent = PendingIntent.getService(context, 0, intentAlarm, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //set the alarm for particular time
       // alarmManager.set(AlarmManager.RTC_WAKEUP,time, PendingIntent.getBroadcast(context,1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

        if (isOn) {



            alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis()
                    , 5000, pIntent);  // trackingInterval
            ComponentName receiver = new ComponentName(context, LocationBroadcastReceiver.class);
            PackageManager pm = context.getPackageManager();
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);

            Toast.makeText(context, "Location Update every 5 seconds", Toast.LENGTH_SHORT).show();
        }
    }*/

    public static void stopService(Context context) {

        Intent intentAlarm = new Intent(context, DataMovementService.class);
        //  intentAlarm.putExtra("wifi", wifiOn);
        PendingIntent pIntent = PendingIntent.getService(context, 0, intentAlarm, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        //       System.out.println("Tracking off");
        alarmManager.cancel(pIntent);
        pIntent.cancel();

        ComponentName receiver = new ComponentName(context, LocationBroadcastReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        /*LocationBroadcastReceiver.locationListener = new LocationListener() {
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

        LocationBroadcastReceiver.locationManager = (LocationManager) context.getSystemService(
                Context.LOCATION_SERVICE);
        // Register the listener with the Location Manager to receive location updates
        LocationBroadcastReceiver.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0, 0, LocationBroadcastReceiver.locationListener);
      //   if(LocationBroadcastReceiver.locationManager != null) {
             LocationBroadcastReceiver.locationManager.removeUpdates(LocationBroadcastReceiver.locationListener);
      //   }
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }*/


        Toast.makeText(context, "Location Service has been Disable", Toast.LENGTH_SHORT).show();


    }

    /** start update location service*/
    public static void startService(Context context, SharedPreferences sharedPref ) {
        //   System.out.println("**********************DataMovementService.scheduleUpdate started*******");

        boolean isOn = sharedPref.getBoolean(UserPreferenceActivity.TRACKING_SWITCH, true);
        int trackingInterval = Integer.parseInt(sharedPref.getString(UserPreferenceActivity
                .TRACKING_INTERVAL, "60"));

        trackingInterval *= 1000;
        Intent intentAlarm = new Intent(context, LocationBroadcastReceiver.class);
        //  intentAlarm.putExtra("wifi", wifiOn);

        //   intentAlarm.putExtra(LISTENER, listener);

        // PendingIntent pIntent = PendingIntent.getService(context, 0, intentAlarm, 0);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 1, intentAlarm, 0); //PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //set the alarm for particular time
        // alarmManager.set(AlarmManager.RTC_WAKEUP,time, PendingIntent.getBroadcast(context,1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

        if (isOn) {
            alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis()
                    , 5000, pIntent);  // trackingInterval
            ComponentName receiver = new ComponentName(context, LocationBroadcastReceiver.class);
            PackageManager pm = context.getPackageManager();
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
            //Toast.makeText(context, "Location Update every 5 seconds", Toast.LENGTH_SHORT).show();
        } else {
            //       System.out.println("Tracking off");
            alarmManager.cancel(pIntent);
            pIntent.cancel();
        }
    }

}
