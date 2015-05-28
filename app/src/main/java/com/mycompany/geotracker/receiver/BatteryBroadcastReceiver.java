/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Alex on 5/27/2015.
 */
public class BatteryBroadcastReceiver extends BroadcastReceiver {
    public static boolean isConnected;


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
            isConnected = false;
        } else if (intent.getAction().equals("android.intent.action.ACTION_POWER_CONNECTED")) {
            isConnected = true;
        }

    }
}
