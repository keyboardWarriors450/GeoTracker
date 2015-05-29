/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker.controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mycompany.geotracker.receiver.BatteryBroadcastReceiver;
import com.mycompany.geotracker.receiver.LocationBroadcastReceiver;
import com.mycompany.geotracker.R;
import com.mycompany.geotracker.data.MyData;
import com.mycompany.geotracker.model.User;
import com.mycompany.geotracker.service.DataMovementService;

import java.util.ArrayList;

/*
 * Created by Alex on April 2015
 */
public class MyAccountActivity extends ActionBarActivity {

    public static final String UID = "user_id";
    private static final String TAG = "MyAccountActivity";
    private MyData myData;
    private String user;
    private String uid;
    private LocationListener locationListener;
    private Context that = this;
    private ConnectivityManager mConnectivityManager;

    /**
     * Creates the page with all the buttons and shows the user name.
     * @param savedInstanceState all the saved information about the user
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "***********************MyAccountActivity started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        //**************** Check if tracking is on or off ************************
        SharedPreferences sharedPref = this.getSharedPreferences(UserPreferenceActivity.USER_PREF,
                Context.MODE_PRIVATE);

        if (sharedPref.getBoolean(UserPreferenceActivity.TRACKING_SWITCH, true)) {
            Log.i(TAG, "Tracking ON");

            //**************** Turn tracking on***************************
            // TrackingLocation.get(that).startLocationUpdates();
            // DataMovementService.scheduleUpdate(that, sharedPref);

            DataMovementService.startService(that, sharedPref);

        } else {
            Log.i(TAG, "Tracking OFF");
        }

        myData = new MyData(this);
        final ArrayList<User> allData = myData.selectAllUsers();
        user = allData.get(allData.size() - 1).getEmail();
        uid = allData.get(allData.size() - 1).getUserID();
        myData.close();

        SharedPreferences sharePref = getSharedPreferences(UserPreferenceActivity.USER_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(UID, uid);
        editor.commit();

        final TextView username = (TextView) findViewById(R.id.username_display);
        int end = 0;

        for (int i = 0; i < user.length(); i++) {
            if (user.charAt(i) == '@') {
                end = i;
            }
        }

        String new_user = user.substring(0, end);

        username.setTextColor(Color.WHITE);
        username.setText("Welcome " + new_user);
//        final Button change_password = (Button)findViewById(R.id.change_password);

//        change_password.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View v) {
//                //set the preferences to the user that is currently logged in.
//                new RecoverPassword(MyAccountActivity.this).execute(user);
//            }
//        });

        final Button view_data = (Button) findViewById(R.id.movement_data);

        view_data.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //check the movement for that particular user
                toMovementData();
            }
        });

        Button preference = (Button) findViewById(R.id.preference);
        preference.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MyAccountActivity.this, UserPreferenceActivity.class);
                startActivity(i);
            }
        });

    }

    /**
     * Creates the options menu on the top of the screen.
     * @param menu the menu
     * @return true if there is a menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_account, menu);
        return true;
    }

    /**
     * What the item does when it is clicked on.
     * @param item the menu item
     * @return the item's action
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

        //takes the user back to the home screen
        if (id == R.id.action_logout) {
            //Log the user out.
            Toast.makeText(that, "Logout successful", Toast.LENGTH_SHORT).show();
            Toast.makeText(that, "Service has been Disabled", Toast.LENGTH_SHORT).show();
      //      TrackingLocation.get(that).stopLocationUpdates();

            DataMovementService.stopService(that);

            ComponentName receiver1 = new ComponentName(this.getApplicationContext(), LocationBroadcastReceiver.class);
            ComponentName receiver2 = new ComponentName(this.getApplicationContext(), BatteryBroadcastReceiver.class);

            PackageManager pm1 = this.getApplicationContext().getPackageManager();
            PackageManager pm2 = this.getApplicationContext().getPackageManager();

            pm1.setComponentEnabledSetting(receiver1,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);

            pm2.setComponentEnabledSetting(receiver2,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);

            finish();
            toHomeScreen();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toHomeScreen() {
        startActivity(new Intent(this, HomeScreen.class));
    }

    private void toMovementData() {
        startActivity(new Intent(this, ViewLocations.class));
    }

}
