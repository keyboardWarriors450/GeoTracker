/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker.controller;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mycompany.geotracker.R;

/**
 * Created by Daniel on 4/13/2015.
 *
 * Screen that shows up when the user answers wrong.
 */
public class WrongAnswer extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_retrieval);

        final Button btn_ok = (Button)findViewById(R.id.reg_continue);

        btn_ok.setOnClickListener(new TextView.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}
