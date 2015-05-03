/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mycompany.geotracker.data.MyData;
import com.mycompany.geotracker.model.User;

import java.util.ArrayList;

/**
 * Created by David on April 2015
 */
public class HomeScreen extends ActionBarActivity {

    private TextView create_user, forgotPass;
    private EditText user_name, password;
    private MyData myData;
    private String userIDStr, passwordStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        myData = new MyData(this);
        user_name = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);
        create_user = (TextView) findViewById(R.id.create_user);
        forgotPass = (TextView) findViewById(R.id.forgot_password);
        Button login = (Button) findViewById(R.id.login);

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

        login.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<User> allData = myData.selectAll();
                userIDStr = user_name.getText().toString();
                passwordStr = password.getText().toString();

                if (isEmpty(user_name) || isEmpty(password)) {
                    Toast.makeText(HomeScreen.this, R.string.no_blank, Toast.LENGTH_SHORT).show();
                } else {
                    new LoginToServer(HomeScreen.this).execute(userIDStr, passwordStr);
                }
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

    @Override
    protected void onStart() {
        super.onStart();

        myData = new MyData(this);
        final ArrayList<User> allData = myData.selectAll();
        if (allData.size() != 0) {
            userIDStr = allData.get(allData.size()-1).getEmail();
            passwordStr = allData.get(allData.size()-1).getPassword();
        }
//        myData.deleteAll();
    }

    @Override
    protected void onResume() {
        super.onResume();

        user_name = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);
        user_name.setText(userIDStr);
        password.setText(passwordStr);
    }
}
