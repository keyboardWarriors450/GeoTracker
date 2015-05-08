/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mycompany.geotracker.data.MyData;
import com.mycompany.geotracker.model.User;

import java.util.ArrayList;

/*
 * Created by Alex on April 2015.
 */
public class ForgotPassword extends ActionBarActivity {

//    private MyData myData;
//    private boolean userExists;
//    private ArrayList<User> allData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        final Button submit = (Button)findViewById(R.id.submit);
        final Button go_back = (Button)findViewById(R.id.go_back);
//        myData = new MyData(this);

        submit.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
//                allData = myData.selectAllUsers();
//                myData.close();

//                userExists = false;
//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                TextView text = (TextView)findViewById(R.id.email_address);
//                String compared_string = sharedPreferences.getString("email", "");
                String typed_email = text.getText().toString();

//                for (int i = 0; i < allData.size(); i++) {
//                    System.out.println(allData.get(i).getEmail() + " email in the DB");
//                    if (typed_email.equals(allData.get(i).getEmail())) {
//                        userExists = true;
//                    }
//                }


//                System.out.println(typed_email + " This is the typed email");

//                if (userExists) {
//                    toPasswordRetrieval();
                    new RecoverPassword(ForgotPassword.this).execute(typed_email);
//                } else {
//                    /*
//                    This system print should not be here, I am working on making an error in red font
//                    pop up if it is wrong saying that the email isnt in the data base.
//                     */
//                    Toast.makeText(ForgotPassword.this, R.string.no_such_email, Toast.LENGTH_SHORT).show();
//                }
            }
        });

        go_back.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void toPasswordRetrieval() {
        startActivity(new Intent(this, PasswordRetrieval.class));
    }

}
