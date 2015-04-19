/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.mycompany.geotracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Daniel on 4/13/2015.
 */
public class PasswordRetrieval extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_retrieval);

        final Button btn_cancel = (Button)findViewById(R.id.button2);
        final Button btn_ok = (Button)findViewById(R.id.ok);

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
    public void toPassWordEmailSent() {
        Intent intent = new Intent(this, PasswordEmailSent.class);
        startActivity(intent);
    }

}
