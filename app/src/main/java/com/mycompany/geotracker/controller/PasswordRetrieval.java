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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.mycompany.geotracker.R;
import com.mycompany.geotracker.controller.PasswordEmailSent;

/**
 * Created by Daniel on 4/13/2015.
 *
 * Asks user for the answer to the security question.
 */
public class PasswordRetrieval extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_retrieval);

        final Button btn_cancel = (Button)findViewById(R.id.button2);
        final Button btn_ok = (Button)findViewById(R.id.ok);

        final Spinner spin_securityQuest = (Spinner) findViewById(R.id.reg_security_quest);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.arr_securityQuest, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_securityQuest.setAdapter(adapter);

        btn_cancel.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

  /*      btn_ok.setOnClickListener(new Button.OnClickListener() {
            public void setOnClickListener(View v) {
                toPassWordEmailSent();
            }
        });*/

        btn_ok.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                btn_ok.setTextColor(Color.parseColor("#67818a"));
                toPassWordEmailSent();
            }
        });

    }

    /**
     * When user enters the correct answer to the security question, the password
     * is sent to the user's email.
     */
    public void toPassWordEmailSent() {
        Intent intent = new Intent(this, PasswordEmailSent.class);
        startActivity(intent);
    }

}
