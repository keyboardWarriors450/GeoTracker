/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewMapActivity extends ActionBarActivity implements OnMapReadyCallback {

    //  private LocationLog mLocationLog;
    private GoogleMap mGoogleMap;
    private Location myLocation;

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


    @Override
    public void onMapReady(GoogleMap map) {

        map.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        /*if (mGoogleMap != null) {
            Log.i("Map Activity", "Inside mGoogleMap");
            LatLng latlng = new LatLng(47.2528768,-122.4442906 ); // Tacoma coordinates
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title("Tacoma")
                    .snippet("This is Tacoma location"));

            LatLng latlng1 = new LatLng(47.2530768,-122.4440906 ); // Tacoma coordinates
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(latlng1)
                    .title("Tacoma2")
                    .snippet("This is Tacoma2 location"));

            LatLng latlng2 = new LatLng(47.25387768,-122.4447906 ); // Tacoma coordinates
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(latlng2)
                    .title("Tacoma2")
                    .snippet("This is Tacoma2 location"));
            // Move the camera instantly to tacoma with a zoom of 15.
          //  mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng1, 15));
        }*/


        // initial location manager
       /* LocationManager locationManager = (LocationManager) this.getSystemService(
                Context.LOCATION_SERVICE);
        Location myLocation1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);*/

        for (int i=0; i<PickDateActivity.mLocationList.size(); i++) {
            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(PickDateActivity.mLocationList.get(i).getLatitude()
                            , PickDateActivity.mLocationList.get(i).getLongitude()))
                    .title("My Locations"));
        }
        LatLng firstLatLng = new LatLng(PickDateActivity.mLocationList.get(0).getLatitude(),
                PickDateActivity.mLocationList.get(0).getLongitude());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 13));
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


  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}