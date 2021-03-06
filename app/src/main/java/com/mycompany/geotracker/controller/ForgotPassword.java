/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker.controller;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mycompany.geotracker.R;
import com.mycompany.geotracker.server.RecoverPassword;

/*
 * Created by Alex on April 2015.
 */
public class ForgotPassword extends ActionBarActivity {

    /**
     * Creates the screen where the user goes if they forgot their password.
     * @param savedInstanceState the saved password
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        final Button submit = (Button)findViewById(R.id.submit);
        final Button go_back = (Button)findViewById(R.id.go_back);

        submit.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                TextView text = (TextView)findViewById(R.id.email_address);
                String typed_email = text.getText().toString();
                new RecoverPassword(ForgotPassword.this).execute(typed_email);
            }
        });

        go_back.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

}
