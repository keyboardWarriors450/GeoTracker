
package com.mycompany.geotracker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import com.mycompany.geotracker.data.MyData;
import com.mycompany.geotracker.model.User;
import com.mycompany.geotracker.server.LocationToServer;
import com.mycompany.geotracker.service.DataMovementService;

import java.util.ArrayList;

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

           /* Intent serviceLauncher = new Intent(context, DataMovementService.class);
            context.startService(serviceLauncher);
            Log.v("TEST", "Service loaded at start");*/
            // Set the alarm here.
            DataMovementService.scheduleUpdate(context, true);
        }

    }
}