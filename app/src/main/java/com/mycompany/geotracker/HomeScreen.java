package com.mycompany.geotracker;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
                    toRegisterActivity();
                }
        });

        /* Viet's page log in listener*/
        final Button pickDate = (Button)findViewById(R.id.login);

        pickDate.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                Log.i("test", "HomeScreen");
                pickDate.setTextColor(Color.parseColor("#67818a"));
                selectStartEndDate();
            }
        });
         // end log in button listener

        final TextView forgotPass = (TextView)findViewById(R.id.forgot_password);
        forgotPass.setOnClickListener(new TextView.OnClickListener() {

            public void onClick(View v) {
                forgotPass.setTextColor(Color.parseColor("#67818a"));
                toForgotPassword();
            }
        });
    }

    // Responding to Log in button, it should direct to user input for map view
    public void selectStartEndDate() {
       // Log.i("test", "HomeScreen");
        Intent intent = new Intent(this, pickDateActivity.class);
       // EditText editText = (EditText) findViewById(R.id.edit_message);
      //  String message = editText.getText().toString();
     //   intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

     /* Viet's page log in listener end here*/

    public void toForgotPassword() {
        startActivity(new Intent(this, ForgotPassword.class));
    }

    public void toRegisterActivity() {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
