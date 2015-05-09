/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;

public class LocationBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       /* if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            LocationService.setServiceAlarm(context, true);
        }*/

        LocationManager locationManager = (LocationManager) context.getSystemService(
                Context.LOCATION_SERVICE);
        android.location.Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        String myCoordinates = "" +  myLocation.getLatitude() + ", " + myLocation.getLongitude();
        Toast.makeText(context, "Updated my Location: " + myCoordinates, Toast.LENGTH_LONG).show();

        // David put tasks here for uploading the current location to server
    }
}