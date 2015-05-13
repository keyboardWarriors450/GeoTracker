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
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mycompany.geotracker.data.MyData;
import com.mycompany.geotracker.model.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * this class will take user input; start date and end date to show location
 */
public class PickDateActivity extends ActionBarActivity {

    public final static String START_DATE = "start date";
    public final static String END_DATE = "end date";
    private long start;
    private long end;
    public final static String LATITUDE = "Latitude";
    private Button mStartButton;
    private Button mStopButton;
    private Location myLocation;
    public static List<Location> mLocationList;
    private Context context = PickDateActivity.this;
    private EditText startTxt;
    private EditText endTxt;
    private DateFormat dfm;
    private String startStr;
    private String endStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_date);

        mLocationList = new ArrayList<>();

        // movement data button
        final Button showData = (Button)findViewById(R.id.show_location);

        showData.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {

                showData.setTextColor(Color.parseColor("#67818a"));
                /** get user id**/
                MyData myData = new MyData(PickDateActivity.this);
                final ArrayList<User> allData = myData.selectAllUsers();
                String uid = "";
                if (allData.size() != 0) {
                    uid = allData.get(allData.size()-1).getUserID();
                }
                myData.close();
                /* get user input and convert to unix Time Stamp */
                // start date
                startTxt = (EditText)findViewById(R.id.startDate);
                // end date
                endTxt = (EditText)findViewById(R.id.endDate);

                String startDate = startTxt.getText().toString();
                String endDate = endTxt.getText().toString();
                if (isValidDates(startDate,endDate) ) {

                    dfm = new SimpleDateFormat("MM-dd-yy");

                    try {
                        start = dfm.parse(startDate).getTime() / 1000; // start date Unix time
                        startStr = Long.toString(start);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    try {
                        end = dfm.parse(endDate).getTime() / 1000; // end date Unix Time
                        endStr = Long.toString(end);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    new MovementDataFromServer(context).execute(uid, startStr, endStr, " ");  //
                    // pause and wait for 5 seconds for download list of location is done
                    // SystemClock.sleep(5000);
                    //  toListPoints();  // this bring to list of point toListPoints()
                } else {
                    Toast.makeText(context, "Invalid inputs or Missing end/start date", Toast.LENGTH_LONG).show();
                }
            }
        });

        // view map from user input location
        final Button viewMap = (Button)findViewById(R.id.viewMap);
        viewMap.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {

                /** get user id**/
                MyData myData = new MyData(PickDateActivity.this);
                final ArrayList<User> allData = myData.selectAllUsers();

                String uid = "";
                if (allData.size() != 0) {
                    uid = allData.get(allData.size()-1).getUserID();
                }
                myData.close();
                /* get user input and convert to unix Time Stamp */
                // start date
                startTxt = (EditText)findViewById(R.id.startDate);
                // end date
                endTxt = (EditText)findViewById(R.id.endDate);

                String startDate = startTxt.getText().toString();
                String endDate = endTxt.getText().toString();
                if (isValidDates(startDate,endDate) ) {

                    dfm = new SimpleDateFormat("MM-dd-yy");

                    try {
                        start = dfm.parse(startDate).getTime() / 1000; // start date Unix time
                        startStr = Long.toString(start);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    try {
                        end = dfm.parse(endDate).getTime() / 1000; // end date Unix Time
                        endStr = Long.toString(end);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    new MovementDataFromServer(context).execute(uid, startStr, endStr, "map");  //
                    // pause and wait for 5 seconds for download list of location is done
                  //  SystemClock.sleep(5000);
                  //  toViewMap();
                }  else {
                    Toast.makeText(context, "Invalid inputs or Missing end/start date", Toast.LENGTH_LONG).show();
                }
            }
        });

        // service button
        final Button startService = (Button)findViewById(R.id.start_service);

        mStartButton = (Button) findViewById(R.id.start_service);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    Toast.makeText(context, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
                    //  LocationService.setServiceAlarm(v.getContext(), true);
                    scheduleUpdate();

                    // this will enable Alarm ON
                    ComponentName receiver = new ComponentName(v.getContext(), LocationBroadcastReceiver.class);
                    PackageManager pm = v.getContext().getPackageManager();

                    pm.setComponentEnabledSetting(receiver,
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                    Toast.makeText(context, "Location Service has been Enable", Toast.LENGTH_SHORT).show();

                }else {
                    promptUserTurnGPSon(); // ask user to turn gps on
                }

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
                Toast.makeText(context, "Location Service has been Disable", Toast.LENGTH_SHORT).show();
            }
        });


        /*********************/
        LocationManager locationManager = (LocationManager) this.getSystemService(
                Context.LOCATION_SERVICE);

        // Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Log.i("LOCATION SERVICES", location.toString());
                //  mLocationLog.addLocation(location);
                myLocation = location;
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0, 0, locationListener);
        /********************/
    }

    public void scheduleUpdate() {

        // Context context = V.getContext();
        // create an Intent and set the class which will execute when Alarm triggers, here we have
        // given AlarmReciever in the Intent, the onRecieve() method of this class will execute when
        // alarm triggers
        Intent intentAlarm = new Intent(this, LocationBroadcastReceiver.class);
      //  Double lat1 = myLocation.getLatitude();
        String lat = " Latitude: ";
        if (myLocation != null) {
            lat += myLocation.getLatitude();
        }
        intentAlarm.putExtra(LATITUDE,  lat);
        // create the object
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //set the alarm for particular time
        //   alarmManager.set(AlarmManager.RTC_WAKEUP,time, PendingIntent.getBroadcast(this,1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

        // repeat every 5 seconds
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis()
                , 5000, PendingIntent.getBroadcast(this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

        Toast.makeText(this, "Location Update every 5 seconds", Toast.LENGTH_SHORT).show();

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

    /**
     * validate user inputs
     * @param startD startDAte
     * @param endD endDAte
     * @return
     */
    public boolean isValidDates(String startD, String endD) {
        if(startD == null || endD == null || startD.length() != 8 || endD.length() != 8 )
            return false;

        if (startD.charAt(2) == '-' && startD.charAt(5) == '-'
                && endD.charAt(2) == '-' && endD.charAt(5) == '-') {

            DateFormat dFormat = new SimpleDateFormat("MM-dd-yy");
            long starDatetLong = 0;
            long endDateLong = 0;

            try {
                starDatetLong = dFormat.parse(startD).getTime() / 1000; // start date Unix time
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                endDateLong = dFormat.parse(endD).getTime() / 1000; // start date Unix time
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (starDatetLong < endDateLong) return true; else return false;


        } else return false;

    }


/*    public void toListPoints() {
        // this indicate which page you want to link to
        Intent intent = new Intent(this, ShowMovementDataActivity.class);
        // reserve space for extra information here if need
       startActivity(intent);
    }*/

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
            toHomeScreen();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toHomeScreen() {
        startActivity(new Intent(this, HomeScreen.class));
    }

/*    public void viewMap(View view) {
        Intent intent = new Intent(this, ViewMapActivity.class);
        EditText editText = (EditText) findViewById(R.id.startDate);
      //  EditText editText2 = (EditText) findViewById(R.id.endDate);
        String message = editText.getText().toString();
        intent.putExtra(START_DATE, message);
        startActivity(intent);
    }*/


    /**
     * Prompt user to turn GPS on
     */
    private void promptUserTurnGPSon(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Your GPS is NOT enable")
                .setCancelable(false)
                .setPositiveButton("turn on GPS here",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
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

