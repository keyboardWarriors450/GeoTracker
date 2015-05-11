
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

       // PickDateActivity.scheduleUpdate();
       /* if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            LocationService.setServiceAlarm(context, true);
        }*/

        /*LocationManager locationManager = (LocationManager) context.getSystemService(
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
                0, 0, locationListener);*/
        LocationManager locationManager = (LocationManager) context.getSystemService(
                Context.LOCATION_SERVICE);
        Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        PickDateActivity.mLocationList.add(myLocation);
      //  Toast.makeText(context, "No last location is found ", Toast.LENGTH_LONG).show();
     //   Double lat = 0.0000;
    //    lat = intent.getDoubleExtra (PickDateActivity.LATITUDE, 0.00);
       String lat =  intent.getStringExtra(PickDateActivity.LATITUDE);
        Toast.makeText(context, "lATITUDE: " + myLocation.getLatitude() + "Longitude: " + myLocation.getLongitude()
                , Toast.LENGTH_LONG).show();
       /* if (myLocation != null) {
            String myCoordinates = "" + myLocation.getLatitude() + ", " + myLocation.getLongitude();
            Toast.makeText(context, "Updated my Location: " + myCoordinates, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "No last location is found ", Toast.LENGTH_LONG).show();
        }*/
     /******* DAVID put your code below  ****/
        // now we have currentlocation
        Double latitude = myLocation.getLatitude();
        Double longitude = myLocation.getLongitude();
        Float speed = myLocation.getSpeed();
        Float bearing = myLocation.getBearing();
        Double altitude = myLocation.getAltitude();

    }
}