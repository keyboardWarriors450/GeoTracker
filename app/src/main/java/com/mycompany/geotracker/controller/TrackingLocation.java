/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker.controller;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.mycompany.geotracker.receiver.LocationBroadcastReceiver;

/**
 * Created by David on May 2015
 */
public class TrackingLocation {
    private static final String TAG = "TrackingLocation";

    public static final String ACTION_LOCATION = "com.mycompany.geotracker.ACTION_LOCATION";

    private static TrackingLocation sTrackingLocation;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Context context;
    private boolean wifiOn = false;

    private TrackingLocation(Context context) {
        locationManager = (LocationManager) context.getSystemService(
                context.LOCATION_SERVICE);
        this.context = context;
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            System.out.println("****************GPS enabled");
            //         promptUserTurnGPSon();
        } else {
            System.out.println("************GPS not enabled");
        }
    }

    public static TrackingLocation get(Context c) {
        if (sTrackingLocation == null) {
            sTrackingLocation = new TrackingLocation(c.getApplicationContext());
        }
        return sTrackingLocation;
    }

    public void startLocationUpdates() {
//    } else if (wifiOn) {
//        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,
//                0, 0, locationListener);
//    } else {
//        promptUserTurnGPSon(); // ask user to turn gps on
//    }
        String provider = LocationManager.GPS_PROVIDER;

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Log.i("LOCATION SERVICES", location.toString());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
    }

    public void stopLocationUpdates() {
        locationManager.removeUpdates(locationListener);
    }

    public boolean isLocationListenerOff() {
        return locationListener == null;
    }

    public LocationManager getLocationMan() {
        return locationManager;
    }



//    /**
//     * Prompt user to turn GPS on
//     */
//    private void promptUserTurnGPSon(){
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//        alertDialogBuilder.setMessage("Your GPS is NOT enabled")
//                .setCancelable(false)
//                .setNeutralButton("Use wifi",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int id) {
//                                wifiOn = true;
//            //                    mStartButton.performClick();
//                            }
//                        })
//                .setPositiveButton("Turn on GPS",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                Intent callGPSSettingIntent = new Intent(
//                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//       //                         startActivity(callGPSSettingIntent);
//                            }
//                        });
//        alertDialogBuilder.setNegativeButton("Cancel",
//                new DialogInterface.OnClickListener(){
//                    public void onClick(DialogInterface dialog, int id){
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alert = alertDialogBuilder.create();
//        alert.show();
//    }


   /* @Override
    public void onDestroy() {
        ComponentName receiver = new ComponentName(this.getApplicationContext(), LocationBroadcastReceiver.class);
        PackageManager pm = this.getApplicationContext().getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        finish();
        DataMovementService.scheduleUpdate(context, false);
        locationManager.removeUpdates(locationListener);
        toHomeScreen();
        super.onDestroy();
    }*/

}
