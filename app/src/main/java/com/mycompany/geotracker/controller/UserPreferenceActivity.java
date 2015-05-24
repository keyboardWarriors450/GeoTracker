/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker.controller;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mycompany.geotracker.receiver.LocationBroadcastReceiver;
import com.mycompany.geotracker.R;
import com.mycompany.geotracker.service.DataMovementService;


public class UserPreferenceActivity extends ActionBarActivity {

    public static final String USER_PREF = "UserPref";
    public static final String TRACKING_SWITCH = "tracking_switch";
    public static final String TRACKING_INTERVAL = "tracking_interval";
    public static final String UPLOAD_INTERVAL = "upload_interval";
    private TextView trackingInterval;
    private int uploadInterval;
    private Context that = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_preference);

        SharedPreferences sharedPref = this.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);

        trackingInterval = (TextView) findViewById(R.id.curr_tracking_time_field);
        final Switch tracking = (Switch) findViewById(R.id.tracking_switch);
        Button btnTrackingInterval = (Button) findViewById(R.id.btn_tracking_interval_time);
        RadioGroup myRadioGroup = (RadioGroup) findViewById(R.id.myRadioGroup);

        tracking.setChecked(sharedPref.getBoolean(TRACKING_SWITCH, true));
        trackingInterval.setText(sharedPref.getString(TRACKING_INTERVAL, "60"));
        uploadInterval = sharedPref.getInt(UPLOAD_INTERVAL, 60);

        tracking.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPref = that.getSharedPreferences(USER_PREF,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean
                        (TRACKING_SWITCH, tracking.isChecked());
                editor.commit();

                if (isChecked) {
                    Toast.makeText(that, "Location Service has been Enabled", Toast.LENGTH_SHORT).show();
                    //   TrackingLocation.get(that).startLocationUpdates();
                    //  DataMovementService.scheduleUpdate(that, sharedPref);
                    DataMovementService.startService(that, sharedPref);

                } else {
                    // Toast.makeText(that, "Location Service has been Disabled", Toast.LENGTH_SHORT).show();
                    // TrackingLocation.get(that).stopLocationUpdates();
                    DataMovementService.stopService(that);
                    //  stopService(new Intent(that, DataMovementService.class));
                }
            }
        });

        btnTrackingInterval.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectTrackingInterval();
            }
        });

        myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btn_one_min) {
                    setUploadIntervalTime(60);
                } else if (checkedId == R.id.btn_one_hr) {
                    setUploadIntervalTime(3600);
                } else if (checkedId == R.id.btn_12_hr) {
                    setUploadIntervalTime(43200);
                } else if (checkedId == R.id.btn_24_hr) {
                    setUploadIntervalTime(86400);
                }

                SharedPreferences sharedPref = that.getSharedPreferences(USER_PREF,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit(); editor.putInt
                        (UPLOAD_INTERVAL, uploadInterval);
                editor.commit();
                System.out.println(uploadInterval);
            }
        });
        RadioButton radio_one_min = (RadioButton) findViewById(R.id.btn_one_min);
        RadioButton radio_one_hr = (RadioButton) findViewById(R.id.btn_one_hr);
        RadioButton radio_12_hr = (RadioButton) findViewById(R.id.btn_12_hr);
        RadioButton radio_24_hr = (RadioButton) findViewById(R.id.btn_24_hr);

        if (uploadInterval == 60) {
            radio_one_min.setChecked(true);
        } else if (uploadInterval == 3600) {
            radio_one_hr.setChecked(true);
        } else if (uploadInterval == 43200) {
            radio_12_hr.setChecked(true);
        } else {
            radio_24_hr.setChecked(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_preference, menu);
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

        //takes the user back to the home screen
        if (id == R.id.action_logout) {
            //Log the user out.
            Toast.makeText(that, "Logout successful", Toast.LENGTH_SHORT).show();
            Toast.makeText(that, "Service has been Disabled", Toast.LENGTH_SHORT).show();
            TrackingLocation.get(that).stopLocationUpdates();

            DataMovementService.stopService(that);

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

    private void setUploadIntervalTime(int sec) {
        uploadInterval = sec;
    }

    private void selectTrackingInterval(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Time");
        builder.setMessage("Enter new tracking time interval (10 - 300 sec)");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setFilters(new InputFilter[] { new InputFilter.LengthFilter(3) });
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!input.getText().toString().equals("") &&
                        Integer.parseInt(input.getText().toString()) >= 10 &&
                        Integer.parseInt(input.getText().toString()) <+ 300) {
                    trackingInterval.setText(input.getText());
                    SharedPreferences sharedPref = that.getSharedPreferences(USER_PREF,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit(); editor.putString
                            (TRACKING_INTERVAL, trackingInterval.getText().toString());
                    editor.commit();
                } else {
                    selectTrackingInterval();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void toHomeScreen() {
        startActivity(new Intent(this, HomeScreen.class));
    }
}
