package com.mycompany.geotracker;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class pickDateActivity extends ActionBarActivity {
    public final static String START_DATE = "start date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_date);
        final Button viewMap = (Button)findViewById(R.id.viewMap);

        viewMap.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                Log.i("test", "HomeScreen");
                viewMap.setTextColor(Color.parseColor("#67818a"));
                toViewMap();
            }
        });

    }

    public void toViewMap() {
        // this indicate which page you want to link to
        Intent intent = new Intent(this, ViewMapActivity.class);
        // reserve space for extra information here if need
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pick_date, menu);
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

/*    public void viewMap(View view) {
        Intent intent = new Intent(this, ViewMapActivity.class);
        EditText editText = (EditText) findViewById(R.id.startDate);
      //  EditText editText2 = (EditText) findViewById(R.id.endDate);
        String message = editText.getText().toString();
        intent.putExtra(START_DATE, message);
        startActivity(intent);
    }*/
}
