
package com.mycompany.geotracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import com.mycompany.geotracker.data.MyData;
import com.mycompany.geotracker.model.User;

import java.util.ArrayList;

/**
 *
 * It receive the intent from alarm manager service and do the location upload service
 */
public class LocationBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

       /* if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            LocationService.setServiceAlarm(context, true);
        }*/


        LocationManager locationManager = (LocationManager) context.getSystemService(
                Context.LOCATION_SERVICE);
        Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        //PickDateActivity.mLocationList.add(myLocation);
      //  Toast.makeText(context, "No last location is found ", Toast.LENGTH_LONG).show();
     //   Double lat = 0.0000;
    //    lat = intent.getDoubleExtra (PickDateActivity.LATITUDE, 0.00);
     //  String lat =  intent.getStringExtra(PickDateActivity.LATITUDE);

       /* if (myLocation != null) {
            String myCoordinates = "" + myLocation.getLatitude() + ", " + myLocation.getLongitude();
            Toast.makeText(context, "Updated my Location: " + myCoordinates, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "No last location is found ", Toast.LENGTH_LONG).show();
        }*/
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

            MyData myData = new MyData(context);
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

            new LocationToServer(context).execute(uid, latStr, lonStr, speedStr, headingStr, timestampStr);
            Toast.makeText(context, "Uploaded current location: " + myLocation.getLatitude() + ", " +
                    myLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        }
    }
}