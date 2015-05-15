/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mycompany.geotracker.data.MyData;
import com.mycompany.geotracker.model.User;

import java.util.ArrayList;

/*
 * Created by Alex on April 2015.
 */
public class MyAccountActivity extends ActionBarActivity {

    private MyData myData;
    private String user;

    /**
     * Creates the page with all the buttons and shows the user name.
     * @param savedInstanceState all the saved information about the user
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        SharedPreferences sharedPref = this.getSharedPreferences(UserPreferenceActivity.PREF_NAME,
                Context.MODE_PRIVATE);

        if (sharedPref.getBoolean(UserPreferenceActivity.TRACKING_SWITCH, true)) {
            System.out.println("TRUE");
        }

        myData = new MyData(this);
        final ArrayList<User> allData = myData.selectAllUsers();
        user = allData.get(allData.size()-1).getEmail();
        myData.close();

        final TextView username = (TextView) findViewById(R.id.username_display);
        int end = 0;

        for(int i = 0; i < user.length(); i++) {
            if (user.charAt(i) == '@') {
                end = i;
            }
        }

        String new_user = user.substring(0, end);

        username.setTextColor(Color.WHITE);
        username.setText("Welcome " + new_user);
        final Button change_password = (Button)findViewById(R.id.change_password);

        change_password.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //set the preferences to the user that is currently logged in.
                new RecoverPassword(MyAccountActivity.this).execute(user);
            }
        });

        final Button view_data = (Button)findViewById(R.id.movement_data);

        view_data.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //check the movement for that particular user
                toMovementData();
            }
        });

        Button preference = (Button) findViewById(R.id.preference);
        preference.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MyAccountActivity.this, UserPreferenceActivity.class));
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
            ComponentName receiver = new ComponentName(this.getApplicationContext(), LocationBroadcastReceiver.class);
            PackageManager pm = this.getApplicationContext().getPackageManager();

            pm.setComponentEnabledSetting(receiver,
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
        startActivity(new Intent(this, PickDateActivity.class));
    }

}
