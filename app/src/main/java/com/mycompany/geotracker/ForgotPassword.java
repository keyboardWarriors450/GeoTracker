/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
 * Created by Alex on April 2015.
 */
public class ForgotPassword extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        final Button submit = (Button)findViewById(R.id.submit);
        final Button go_back = (Button)findViewById(R.id.go_back);

        submit.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                TextView text = (TextView)findViewById(R.id.email_address);
                String compared_string = sharedPreferences.getString("email", "");
                String typed_email = text.getText().toString();

                if (typed_email.equals(compared_string)) {
                    toPasswordRetrieval();
                } else {
                    /*
                    This system print should not be here, I am working on making an error in red font
                    pop up if it is wrong saying that the email isnt in the data base.
                     */
                    System.out.println("wrong");
                }
            }
        });

        go_back.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void toPasswordRetrieval() {
        startActivity(new Intent(this, PasswordRetrieval.class));
    }

}
