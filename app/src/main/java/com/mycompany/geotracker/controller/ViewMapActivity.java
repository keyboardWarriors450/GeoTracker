/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker.controller;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.PolylineOptions;
import com.mycompany.geotracker.R;
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

        for (int i = 0; i < locList.size(); i++) {
            latitude = Double.parseDouble(locList.get(i).getLat());
            longitude = Double.parseDouble(locList.get(i).getLon());
            LatLng currentCoordinate = new LatLng(latitude, longitude);
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(currentCoordinate)
                    .title("My Locations"));
            if (i < locList.size() - 1) {
                double nextLatitude = Double.parseDouble(locList.get(i + 1).getLat());
                double nextLongitude = Double.parseDouble(locList.get(i + 1).getLon());
                LatLng nextCoordinate = new LatLng(nextLatitude, nextLongitude);
                mGoogleMap.addPolyline(new PolylineOptions()
                        .add(currentCoordinate, nextCoordinate)
                        .width(5)
                        .color(Color.BLUE));
            }
        }

        LatLng lastLatLng = new LatLng(latitude, longitude);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, 12));

    }
}