
package com.mycompany.geotracker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.mycompany.geotracker.controller.TrackingLocation;
import com.mycompany.geotracker.controller.UserPreferenceActivity;
import com.mycompany.geotracker.service.DataMovementService;

/**
 *
 * Restart service after system reboot
 */
public class LocationBroadcastReceiver extends BroadcastReceiver {

    /**
     * Receives the locations.
     * @param context the context
     * @param intent the intent
     */

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("*****************************Started LocationBroadcastReceiver ******");

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            SharedPreferences sharedPref = context.getSharedPreferences(UserPreferenceActivity.USER_PREF,
                    Context.MODE_PRIVATE);

            if (sharedPref.getBoolean(UserPreferenceActivity.TRACKING_SWITCH, true)) {
                System.out.println("Tracking ON ********** from BroadcastReceiver");
                DataMovementService.scheduleUpdate(context, sharedPref);
            } else {
                System.out.println("Tracking OFF *********** from BroadcastReiver");
                DataMovementService.scheduleUpdate(context, sharedPref);

            }
        }

    }
}