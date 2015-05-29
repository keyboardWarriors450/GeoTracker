/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mycompany.geotracker.server.LoginToServer;
import com.mycompany.geotracker.R;
import com.mycompany.geotracker.data.MyData;
import com.mycompany.geotracker.model.User;

import java.util.ArrayList;

/**
 * Created by David on April 2015
 */
public class HomeScreen extends ActionBarActivity {

    private static final String TAG = "HomeScreen";
    private TextView create_user, forgotPass;
    private EditText user_name, password;
    private MyData myData;
    private String userIDStr, passwordStr;

    /**
     * Creates the login screen.
     * @param savedInstanceState the saved information about the user
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "*************************HomeScreen started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        myData = new MyData(this);
        user_name = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);
        create_user = (TextView) findViewById(R.id.create_user);
        forgotPass = (TextView) findViewById(R.id.forgot_password);

        create_user.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_user.setTextColor(Color.parseColor("#67818a"));
                toRegisterActivity();
            }
        });

        forgotPass.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPass.setTextColor(Color.parseColor("#67818a"));
                toForgotPassword();
            }
        });
    }

    private void toForgotPassword() {
        startActivity(new Intent(this, ForgotPassword.class));
    }

    private void toRegisterActivity() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private boolean isEmpty(EditText text) {
        return text.getText().toString().trim().length() == 0;
    }

    /**
     *
     */
    @Override
    protected void onStart() {
        super.onStart();

        final ArrayList<User> allData = myData.selectAllUsers();
        if (allData.size() != 0) {
            userIDStr = allData.get(allData.size()-1).getEmail();
            passwordStr = allData.get(allData.size()-1).getPassword();
        }

    }

    /**
     * When the app is ever restarted, the last user who logged on is saved.
     */
    @Override
    protected void onResume() {
        super.onResume();

        user_name = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);
        user_name.setText(userIDStr);
        password.setText(passwordStr);
    }

    /**
     * Creates the options menu on the top of the screen.
     * @param menu the menu
     * @return true if there is a menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    /**
     * What the item does when it is clicked on.
     * @param item the menu item
     * @return the item's action
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        //takes the user back to the home screen
        if (id == R.id.action_login) {
            userIDStr = user_name.getText().toString();
            passwordStr = password.getText().toString();

            if (isEmpty(user_name) || isEmpty(password)) {
                Toast.makeText(HomeScreen.this, R.string.no_blank, Toast.LENGTH_SHORT).show();
            } else {
                new LoginToServer(HomeScreen.this).execute(userIDStr, passwordStr);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
