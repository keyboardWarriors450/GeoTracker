package com.mycompany.geotracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Daniel on 4/13/2015.
 */
public class PasswordEmailSent extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_email_sent);

        final Button btn_ok = (Button)findViewById(R.id.button4);

        btn_ok.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                toHomeScreen();
            }
        });

        /*final Button btn_ok = (Button)findViewById(R.id.reg_continue);

        btn_ok.setOnClickListener(new TextView.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });*/
    }
    public void toHomeScreen() {
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }
}
