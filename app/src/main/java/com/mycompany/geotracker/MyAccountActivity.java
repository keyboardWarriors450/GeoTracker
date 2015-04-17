package com.mycompany.geotracker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;


public class MyAccountActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        final Button change_password = (Button)findViewById(R.id.change_password);

        change_password.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                toChangePassword();
//     startActivity(new Intent("geoTracker.ChangePassword"));
            }
        });

        final Button log_out = (Button)findViewById(R.id.log_out);
        final Button view_data = (Button)findViewById(R.id.movement_data);

        log_out.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //log out somehow from the user
                toHomeScreen();
            }
        });

        view_data.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                toMovementData();
            }
        });

    }

    public void toHomeScreen() {
        startActivity(new Intent(this, HomeScreen.class));
    }

    public void toChangePassword() {
        startActivity(new Intent(this, ChangePassword.class));
    }

    public void toMovementData() {
        startActivity(new Intent(this, pickDateActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_account, menu);
        return true;
    }

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

        return super.onOptionsItemSelected(item);
    }
}
