/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mycompany.geotracker.data.MyData;
import com.mycompany.geotracker.model.User;

import java.util.ArrayList;

/*
 * Created by Alex on April 2015.
 */
public class MyAccountActivity extends ActionBarActivity {

    private MyData myData;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        myData = new MyData(this);
        final ArrayList<User> allData = myData.selectAllUsers();
        user = allData.get(allData.size()-1).getEmail();

        final TextView username = (TextView) findViewById(R.id.username_display);
        int end = 0;

        for(int i = 0; i < user.length(); i++) {
            if (user.charAt(i) == '@') {
                end = i;
            }
        }

        String new_user = user.substring(0, end);

        username.setTextColor(Color.WHITE);
        username.setText("Welcome " + new_user);
        final Button change_password = (Button)findViewById(R.id.change_password);

        change_password.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //set the preferences to the user that is currently logged in.
                new RecoverPassword(MyAccountActivity.this).execute(user);
            }
        });

        final Button log_out = (Button)findViewById(R.id.log_out);
        final Button view_data = (Button)findViewById(R.id.movement_data);

        log_out.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //log out of the user somehow.
                toHomeScreen();
            }
        });

        view_data.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //check the movement for that particular user
                toMovementData();
            }
        });

    }

    private void toHomeScreen() {
        startActivity(new Intent(this, HomeScreen.class));
    }

    private void toChangePassword() {
        startActivity(new Intent(this, ChangePassword.class));
    }

    private void toMovementData() {
        startActivity(new Intent(this, PickDateActivity.class));
    }

}
