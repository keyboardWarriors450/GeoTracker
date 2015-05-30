
package com.mycompany.geotracker.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mycompany.geotracker.controller.MyAccountActivity;
import com.mycompany.geotracker.controller.UserPreferenceActivity;
import com.mycompany.geotracker.data.MyData;
import com.mycompany.geotracker.model.User;
import com.mycompany.geotracker.server.LocationToServer;
import com.mycompany.geotracker.service.DataMovementService;

import java.util.ArrayList;

/**
 *
 * Restart service after system reboot
 */
public class GeoBroadcastReceiver extends android.content.BroadcastReceiver {

    public static boolean isConnected;

    /**
     * Receives the locations.
     * @param context the context
     * @param intent the intent
     */
    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("*******************Started LocationBroadcastReceiver ******");

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            SharedPreferences sharedPref = context.getSharedPreferences(UserPreferenceActivity.USER_PREF,
                    Context.MODE_PRIVATE);

            if (sharedPref.getBoolean(UserPreferenceActivity.TRACKING_SWITCH, true)) {
                System.out.println("Tracking ON ********** from GeoBroadcastReceiver");
                DataMovementService.startService(context, sharedPref);
            } else {
                System.out.println("Tracking OFF *********** from BroadcastReiver");
                DataMovementService.stopService(context);

            }
        }

        if (intent.getAction().equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
            isConnected = false;
        } else if (intent.getAction().equals("android.intent.action.ACTION_POWER_CONNECTED")) {
            isConnected = true;
        }
    }
}
