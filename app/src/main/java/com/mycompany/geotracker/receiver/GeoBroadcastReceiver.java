
package com.mycompany.geotracker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;

import com.mycompany.geotracker.controller.UserPreferenceActivity;

import com.mycompany.geotracker.service.DataMovementService;


/**
 *
 * Restart service after system reboot
 */
public class GeoBroadcastReceiver extends BroadcastReceiver {

    public static boolean isConnected;

    /**
     * Receives the locations.
     * @param context the context
     * @param intent the intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("*******************Started GeoBroadcastReceiver ******");

        SharedPreferences sharedPref = context.getSharedPreferences(UserPreferenceActivity.USER_PREF,
                Context.MODE_PRIVATE);

        if (intent.getAction() != null) {
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

                if (sharedPref.getBoolean(UserPreferenceActivity.TRACKING_SWITCH, true)) {
                    System.out.println("Tracking ON ********** from BroadcastReceiver");
                    DataMovementService.startService(context, sharedPref);
                } else {
                    System.out.println("Tracking OFF *********** from BroadcastReiver");
                    DataMovementService.stopService(context);

                }
            }


            if (intent.getAction().equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
                isConnected = false;
                System.out.println("--------POWER IS *NOT* CONNECTED---------------->");
                DataMovementService.startService(context, sharedPref);
            } else if (intent.getAction().equals("android.intent.action.ACTION_POWER_CONNECTED")) {
                isConnected = true;
                System.out.println("--------POWER IS CONNECTED---------------->");
                DataMovementService.startService(context, sharedPref);
            }
        }
    }
}
