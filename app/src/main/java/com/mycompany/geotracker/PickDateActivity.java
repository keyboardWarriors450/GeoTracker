/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 *
 * This file is take the user input for start and end date and showing a possible location list
 * or point on the next view map screen.
 */
package com.mycompany.geotracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * this class will take user input; start date and end date to show location
 */
public class PickDateActivity extends ActionBarActivity {

    public final static String START_DATE = "start date";
    private Button mStartButton;
    private Button mStopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_date);

        final Button viewMap = (Button)findViewById(R.id.viewMap);

        viewMap.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                Log.i("test", "HomeScreen");
                viewMap.setTextColor(Color.parseColor("#67818a"));
                toViewMap();
            }
        });

        // service button
        final Button startService = (Button)findViewById(R.id.start_service);

        viewMap.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                Log.i("test", "start service");
                viewMap.setTextColor(Color.parseColor("#67818a"));
                toViewMap();
            }
        });

        mStartButton = (Button) findViewById(R.id.start_service);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the service
                //Intent i = new Intent(v.getContext(), RSSService.class);
                //startService(i);

                //  LocationService.setServiceAlarm(v.getContext(), true);
                scheduleUpdate();

                // this will enable Alarm ON
                ComponentName receiver = new ComponentName(v.getContext(), LocationBroadcastReceiver.class);
                PackageManager pm = v.getContext().getPackageManager();

                pm.setComponentEnabledSetting(receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
            }
        });

        mStopButton = (Button) findViewById(R.id.end_service);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  LocationService.setServiceAlarm(v.getContext(), false);

                // this will enable Alarm OFF
                ComponentName receiver = new ComponentName(v.getContext(), LocationBroadcastReceiver.class);
                PackageManager pm = v.getContext().getPackageManager();

                pm.setComponentEnabledSetting(receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
            }
        });
    }

    public void scheduleUpdate() {
        // Context context = V.getContext();
        // create an Intent and set the class which will execute when Alarm triggers, here we have
        // given AlarmReciever in the Intent, the onRecieve() method of this class will execute when
        // alarm triggers
        Intent intentAlarm = new Intent(this, LocationBroadcastReceiver.class);

        // create the object
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //set the alarm for particular time
        //   alarmManager.set(AlarmManager.RTC_WAKEUP,time, PendingIntent.getBroadcast(this,1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

        // repeat every 5 seconds
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis()
                , 10000, PendingIntent.getBroadcast(this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

        Toast.makeText(this, "Location Update every 10 seconds", Toast.LENGTH_LONG).show();

    }

    /**
     * This method will collect start date and end date then link to the sho map page
     */
    public void toViewMap() {
        // this indicate which page you want to link to
        Intent intent = new Intent(this, ViewMapActivity.class);
        // reserve space for extra information here if need
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pick_date, menu);
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
    }

/*    public void viewMap(View view) {
        Intent intent = new Intent(this, ViewMapActivity.class);
        EditText editText = (EditText) findViewById(R.id.startDate);
      //  EditText editText2 = (EditText) findViewById(R.id.endDate);
        String message = editText.getText().toString();
        intent.putExtra(START_DATE, message);
        startActivity(intent);
    }*/
}
