/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.mycompany.geotracker.data.MyData;

import java.util.ArrayList;

/**
 * This show locations by user's input date range
 */
public class ViewMapActivity extends ActionBarActivity implements OnMapReadyCallback {

    //  private LocationLog mLocationLog;
    private GoogleMap mGoogleMap;
    private com.mycompany.geotracker.model.Location myLocation;

    /**
     * Creates the map screen.
     * @param savedInstanceState the saved map
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);

        //   mLocationLog = (LocationLog) getIntent().getParcelableExtra("locations");

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapView);
        mGoogleMap = mapFragment.getMap();
        mapFragment.getMapAsync(this);
    }

    /**
     * Displays the google map.
     * @param map the map
     */
    @Override
    public void onMapReady(GoogleMap map) {
        MyData myData = new MyData(this);
        ArrayList<com.mycompany.geotracker.model.Location> locList = myData.selectAllLocations();
        myData.close();

        map.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        double latitude = 0;
        double longitude = 0;

        for (int i = 0; i <  locList.size(); i++) {
           latitude = Double.parseDouble(locList.get(i).getLat());
           longitude = Double.parseDouble(locList.get(i).getLon());
           mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title("My Locations"));
        }

        LatLng lastLatLng = new LatLng(latitude,longitude);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, 12));

        // get user current location list
       /* for (int i=0; i<PickDateActivity.mLocationList.size(); i++) {
            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(PickDateActivity.mLocationList.get(i).getLatitude()
                            , PickDateActivity.mLocationList.get(i).getLongitude()))
                    .title("My Locations"));
        }
        LatLng firstLatLng = new LatLng(PickDateActivity.mLocationList.get(0).getLatitude(),
                PickDateActivity.mLocationList.get(0).getLongitude());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 15));*/
        // end user location list



        // initial location manager
       /* LocationManager locationManager = (LocationManager) this.getSystemService(
                Context.LOCATION_SERVICE);
        Location myLocation1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);*/
        // exacting longitude and latitude from my current location
      //  LatLng myLatlng = new LatLng(myLocation1.getLatitude(), myLocation1.getLongitude());
       /* if (myLatlng != null) {

            Log.i("Map Activity", "Inside mGoogleMap my current location");

            mGoogleMap.addMarker(new MarkerOptions()
                    .position(myLatlng)
                    .title("My New Location")
                    .snippet("This is my current location"));

            // Move the camera instantly to my current location with a zoom of 15.
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatlng, 15));
        }*/
        // Seattle coordinates - 47.6097, -122.3331
/*
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener ll = new myLocationListener();

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);

        if (myLocation != null) {

            LatLng myLatLong = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(myLatLong)
                    .title("Update Location")
                    .snippet("This is update location"));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLong, 15));
        }*/

      /*  if (mLocationLog != null) {

            List<Location> locations = mLocationLog.getLocationList();
            Location location = locations.get(0);
            LatLng firstLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            for (int i=0; i<locations.size(); i++) {
                Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(locations.get(i).getLatitude()
                                , locations.get(i).getLongitude()))
                        .title("My Locations"));
            }
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 15));
        }*/

    }

    /*class myLocationListener implements LocationListener {


        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                double pLong = location.getLongitude();
                double pLat = location.getLatitude();
             //   Toast.makeText(this, "Location Update every 10 seconds", Toast.LENGTH_LONG).show();
                //  textLat.setText(Double.toString(pLat));
                //   textLong.setText(Double.toString(pLong));
                myLocation = location;
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }*/



}