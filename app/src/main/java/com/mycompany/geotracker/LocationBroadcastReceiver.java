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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class LocationBroadcastReceiver extends BroadcastReceiver {

    private Location myLocation;

    @Override
    public void onReceive(Context context, Intent intent) {
       /* if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            LocationService.setServiceAlarm(context, true);
        }*/

        LocationManager locationManager = (LocationManager) context.getSystemService(
                Context.LOCATION_SERVICE);

      // Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Log.i("LOCATION SERVICES", location.toString());
              //  mLocationLog.addLocation(location);
                myLocation = location;
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0, 0, locationListener);

        Toast.makeText(context, "No last location is found ", Toast.LENGTH_LONG).show();


        if (myLocation != null) {
            String myCoordinates = "" + myLocation.getLatitude() + ", " + myLocation.getLongitude();
            Toast.makeText(context, "Updated my Location: " + myCoordinates, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "No last location is found ", Toast.LENGTH_LONG).show();
        }
        // David put tasks here for uploading the current location to server
    }
}