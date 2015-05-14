/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.mycompany.geotracker.data.MyData;
import com.mycompany.geotracker.model.Location;

import java.util.ArrayList;
/**
 * This activity showing the a list of location base on user inputs date
 */
public class ShowMovementDataActivity extends ActionBarActivity {

    /**
     * Creates the screen where it displays the data of where the user has been.
     * @param savedInstanceState all the saved data on the user's locations
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_movement_data);

        TextView tv = (TextView) findViewById(R.id.movement_datas);
        String listOfLocation = "";

        MyData myData = new MyData(this);
        ArrayList<Location> locList = myData.selectAllLocations();
        myData.close();

        for (Location loc : locList) {
            listOfLocation += loc.toString() + "\n\n";
        }
        tv.setText(" Timestamp       Latitude        Longitude    Heading\n\n" + listOfLocation);
    }

    /**
     * Creates the options menu.
     * @param menu the menu
     * @return true if there is a menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_movement_data, menu);
        return true;
    }

    /**
     * Returns the action that is performed when an item is selected on the menu.
     * @param item the menu item
     * @return the action that is performed when an item is selected on the menu
     */
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
    }
}
