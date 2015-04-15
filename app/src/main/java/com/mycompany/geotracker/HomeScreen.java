package com.mycompany.geotracker;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class HomeScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        final TextView create_user = (TextView)findViewById(R.id.create_user);

        create_user.setOnClickListener(new TextView.OnClickListener() {
            public void onClick(View v) {
                create_user.setTextColor(Color.parseColor("#67818a"));
                startActivity(new Intent("geoTracker.RegisterActivity"));
            }
        });

        /*create_user.setOnClickListener(new TextView.OnClickListener() {
            public void onClick(View v) {
                create_user.setTextColor(Color.parseColor("#67818a"));
                startActivity(new Intent("geoTracker.PasswordRetrieval"));
            }
        });*/
    }
}
