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
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.mycompany.geotracker.controller.TrackingLocation;
import com.mycompany.geotracker.controller.UserPreferenceActivity;
import com.mycompany.geotracker.data.MyData;
import com.mycompany.geotracker.model.User;
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

    public DataMovementService() {
        super("DataMovement");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i(TAG, "service starting");

        return START_STICKY;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println("*************************DataMovementService.onHandleIntent started");

        //boolean wifi = intent.getExtras().getBoolean("wifi");

//        LocationManager locationManager = (LocationManager) this.getSystemService(
//                Context.LOCATION_SERVICE);

        LocationManager locationManager = TrackingLocation.get(this).getLocationMan();

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        } //else if (!wifi) {
           // myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //}

        if (myLocation != null) {
            /*******....  *****/
            long timestamp = System.currentTimeMillis() / 1000;

            // now we have current location
            String uid = "";
            String latStr = Double.toString(myLocation.getLatitude());
            String lonStr = Double.toString(myLocation.getLongitude());
            String speedStr = Double.toString((double) myLocation.getSpeed());
            String headingStr = Double.toString((double) myLocation.getBearing());
            String timestampStr = Long.toString(System.currentTimeMillis() / 1000);

            /** optional values **/
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
            System.out.println("****************interval " + interval + " tracking " + interval_track +
                " uploading " + interval_upload);
            counter++;
            if (counter == interval) {
                new LocationToServer().execute(uid, latStr, lonStr, speedStr, headingStr,
                        timestampStr);
                counter = 0;
            }
        }
    }

    /**
     * Creates an Intent and sets the class which will execute when the alarm triggers.
     *
     */
    public static void scheduleUpdate(Context context, SharedPreferences sharedPref) {
        System.out.println("**********************DataMovementService.scheduleUpdate started*******");

        boolean isOn = sharedPref.getBoolean(UserPreferenceActivity.TRACKING_SWITCH, true);
        int trackingInterval = Integer.parseInt(sharedPref.getString(UserPreferenceActivity
                .TRACKING_INTERVAL, "60"));

        trackingInterval *= 1000;
        Intent intentAlarm = new Intent(context, DataMovementService.class);
      //  intentAlarm.putExtra("wifi", wifiOn);
        PendingIntent pIntent = PendingIntent.getService(context, 0, intentAlarm, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //set the alarm for particular time
       // alarmManager.set(AlarmManager.RTC_WAKEUP,time, PendingIntent.getBroadcast(context,1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

        if (isOn) {
            alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis()
                    , trackingInterval, pIntent);
            //Toast.makeText(context, "Location Update every 5 seconds", Toast.LENGTH_SHORT).show();
        } else {
            System.out.println("Tracking off");
            alarmManager.cancel(pIntent);
            pIntent.cancel();
        }
    }

    public static void scheduleUpdateLogout(Context context) {

        Intent intentAlarm = new Intent(context, DataMovementService.class);
        //  intentAlarm.putExtra("wifi", wifiOn);
        PendingIntent pIntent = PendingIntent.getService(context, 0, intentAlarm, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(pIntent);
        pIntent.cancel();
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
