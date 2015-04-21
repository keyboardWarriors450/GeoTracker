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
import android.widget.Toast;

/*
 * Created by Alex on April 2015
 */
public class ChangePassword extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        final Button cancel_button = (Button)findViewById(R.id.cancel);
        final Button submit_button = (Button)findViewById(R.id.submit);

        cancel_button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor sharedPreferenceEditor = sharedPreferences.edit();

                TextView old = (TextView)findViewById(R.id.old_password_field);
                TextView new_password = (TextView)findViewById(R.id.new_password_field);
                TextView check_new_password = (TextView)findViewById(R.id.repeated_password_field);
                String entered_old_password = old.getText().toString();
                String entered_new_password = new_password.getText().toString();
                String confirmed_new_password = check_new_password.getText().toString();
                String current_password = sharedPreferences.getString("password", "");


                if (!entered_old_password.equals(current_password)) {
                    Toast.makeText(ChangePassword.this, R.string.wrong_password, Toast.LENGTH_SHORT).show();
                } else if (!entered_new_password.equals(confirmed_new_password)) {
                    Toast.makeText(ChangePassword.this, R.string.no_match, Toast.LENGTH_SHORT).show();
                } else {
                    sharedPreferenceEditor.putString("password", entered_new_password);
                    sharedPreferenceEditor.apply();
                    toMyAccount();
                }
            }
        });

    }

    private void toMyAccount() {
        startActivity(new Intent(this, MyAccountActivity.class));
    }

}
