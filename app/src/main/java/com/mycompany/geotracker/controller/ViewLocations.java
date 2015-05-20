/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 *
 * This file is take the user input for start and end date and showing a possible location list
 * or point on the next view map screen.
 */
package com.mycompany.geotracker.controller;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.mycompany.geotracker.receiver.LocationBroadcastReceiver;
import com.mycompany.geotracker.server.MovementDataFromServer;
import com.mycompany.geotracker.R;
import com.mycompany.geotracker.data.MyData;
import com.mycompany.geotracker.model.User;
import com.mycompany.geotracker.service.DataMovementService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * This class will take user input; start date and end date to show location and data
 */
public class ViewLocations extends ActionBarActivity {

    public final static String DATE_TYPE = "DATE TYPE";
    private static long start;
    private static long end;
    private Button mStartButton;
    private Button mStopButton;
    private Context context = ViewLocations.this;
    private static String startStr;
    private static String endStr;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private boolean wifiOn = false;
    private static Button viewMap;
    private static Button showData;
    private static TextView startDatePicker;
    private static TextView endDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_locations);
        start = 0; end = 0; // initial start end unix time
        locationManager = (LocationManager) this.getSystemService(
                Context.LOCATION_SERVICE);

        // movement data button
        showData = (Button)findViewById(R.id.show_location);
        showData.setEnabled(false);
        showData.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {

                /** get user id**/
                MyData myData = new MyData(ViewLocations.this);
                final ArrayList<User> allData = myData.selectAllUsers();

                String uid = "";
                if (allData.size() != 0) {
                    uid = allData.get(allData.size()-1).getUserID();
                }
                myData.close();

                new MovementDataFromServer(context).execute(uid, startStr, endStr, " ");

            }
        });

        // view map from user input location
        viewMap = (Button)findViewById(R.id.viewMap);
        viewMap.setEnabled(false);
        viewMap.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {

                /** get user id**/
                MyData myData = new MyData(ViewLocations.this);
                final ArrayList<User> allData = myData.selectAllUsers();

                String uid = "";
                if (allData.size() != 0) {
                    uid = allData.get(allData.size()-1).getUserID();
                }
                myData.close();

                new MovementDataFromServer(context).execute(uid, startStr, endStr, "map");

            }
        });

        // service button

        mStartButton = (Button) findViewById(R.id.start_service);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(context, "Tracking is enabled in your devise", Toast.LENGTH_SHORT).show();

                    DataMovementService.scheduleUpdate(v.getContext(), true);

                    // this will enable Alarm ON
                    ComponentName receiver = new ComponentName(v.getContext(), LocationBroadcastReceiver.class);
                    PackageManager pm = v.getContext().getPackageManager();

                    pm.setComponentEnabledSetting(receiver,
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);

                    // Register the listener with the Location Manager to receive location updates
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                0, 0, locationListener);

                    /********************/
                    Toast.makeText(context, "Location Service has been Enable", Toast.LENGTH_SHORT).show();

                } else if (wifiOn) {
                    locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,
                            0, 0, locationListener);
                } else {
                    promptUserTurnGPSon(); // ask user to turn gps on
                }

            }
        });

        mStopButton = (Button) findViewById(R.id.end_service);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // this will enable Alarm OFF
                DataMovementService.scheduleUpdate(context, false);

                ComponentName receiver = new ComponentName(v.getContext(), LocationBroadcastReceiver.class);
                PackageManager pm = v.getContext().getPackageManager();

                pm.setComponentEnabledSetting(receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
                locationManager.removeUpdates(locationListener);
                wifiOn = false;
                Toast.makeText(context, "Location Service has been Disable", Toast.LENGTH_SHORT).show();
            }
        });


        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
            //    Log.i("LOCATION SERVICES", location.toString());
                //  mLocationLog.addLocation(location);
             //   myLocation = location;
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Select a date from a DatePickerDialog
        startDatePicker = (TextView) findViewById(R.id.start_date_text);
        startDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerFragment newFragment = new DatePickerFragment();
                Bundle bundle = new Bundle();
                bundle.putString(DATE_TYPE, "start");
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "datePiker");
            }
        });

        endDatePicker = (TextView) findViewById(R.id.end_date_text);
        endDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerFragment newFragment = new DatePickerFragment();
                Bundle bundle = new Bundle();
                bundle.putString(DATE_TYPE, " ");
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "datePiker");
            }
        });
    }

    // Date Picker
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

      //  private String type;


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Bundle bundle = new Bundle();
            bundle = getArguments();
            String type = bundle.getString(DATE_TYPE);
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            String dateFormat = "";
            // convert day and month to 2 numbers format
            if (month < 10 && day < 10) {
                dateFormat += "0" + (month+1) + "/0" + day + "/" + year;
            } else if (month < 10) {
                dateFormat += "0" + (month+1) + "/" + day + "/" + year;
            } else if (day < 10) {
                dateFormat += (month + 1) + "/0" + day + "/" + year;
            }else { dateFormat += (month + 1) + "/" + day + "/" + year;}

            String startD = "";
            String endD = "";


            if (type.equals("start")) {
                startDatePicker.setText(" " + dateFormat + " ");
                startD = dateFormat;
                try {
                    start = df.parse(startD).getTime() / 1000; // start date Unix time
                    startStr = Long.toString(start);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } else {
                endDatePicker.setText(" " + dateFormat + " ");
                endD = dateFormat;
                try {
                    end = df.parse(endD).getTime() / 1000; // end date Unix Time
                    if (end > start) { viewMap.setEnabled(true); showData.setEnabled(true);}

                    else { Toast.makeText(view.getContext(), "End Date must greater than Start Date "
                            , Toast.LENGTH_SHORT).show();}
                    endStr = Long.toString(end);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(view.getContext(), "You selected " + dateFormat , Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * Creates the options menu on the top of the screen.
     * @param menu the menu
     * @return true if there is a menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pick_date, menu);
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
            ComponentName receiver = new ComponentName(this.getApplicationContext(), LocationBroadcastReceiver.class);
            PackageManager pm = this.getApplicationContext().getPackageManager();
            pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
            finish();
            DataMovementService.scheduleUpdate(context, false);
            locationManager.removeUpdates(locationListener);
            toHomeScreen();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        ComponentName receiver = new ComponentName(this.getApplicationContext(), LocationBroadcastReceiver.class);
        PackageManager pm = this.getApplicationContext().getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        finish();
        DataMovementService.scheduleUpdate(context, false);
        locationManager.removeUpdates(locationListener);
        toHomeScreen();
        super.onDestroy();
    }

    private void toHomeScreen() {
        startActivity(new Intent(this, HomeScreen.class));
    }

    /**
     * Prompt user to turn GPS on
     */
    private void promptUserTurnGPSon(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Your GPS is NOT enabled")
                .setCancelable(false)
                .setNeutralButton("Use wifi",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                wifiOn = true;
                                mStartButton.performClick();
                            }
                        })
                .setPositiveButton("Turn on GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

}

