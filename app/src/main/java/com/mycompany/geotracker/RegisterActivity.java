/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mycompany.geotracker.data.MyData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by David on April 2015
 */
public class RegisterActivity extends ActionBarActivity {

    // Progress Dialog
    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();

    private MyData myData;
    private String email, password, confirmed_password, question, answer;
    private static String url_new_user = "http://450.atwebpages.com/adduser.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        myData = new MyData(this);
        Button btn_cancel = (Button)findViewById(R.id.reg_cancel);
        Button btn_continue = (Button) findViewById(R.id.reg_continue);
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

        btn_continue.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                EditText reg_email = (EditText) findViewById(R.id.reg_email);
                email = reg_email.getText().toString();
                EditText reg_pass = (EditText) findViewById(R.id.reg_password);
                password = reg_pass.getText().toString();
                EditText conf_pass = (EditText) findViewById(R.id.conf_password);
                confirmed_password = conf_pass.getText().toString();
                question = (String) spin_securityQuest.getSelectedItem();
                EditText sec_answer = (EditText) findViewById(R.id.sec_answer);
                answer = sec_answer.getText().toString();
                CheckBox check = (CheckBox) findViewById(R.id.accept_checkBox);

                /*
                Need this to test to see if the password is the same as the confirmed.
                Wasn't sure what you wanted to put here. For now I made it so that it just
                accepts it no matter what.
                 */

                if (!password.equals(confirmed_password)) {
                    Toast.makeText(RegisterActivity.this, R.string.no_match, Toast.
                            LENGTH_SHORT).show();
                } else if (isEmpty(reg_email) || isEmpty(reg_pass) || isEmpty(conf_pass) ||
                        isEmpty(sec_answer))
                    Toast.makeText(RegisterActivity.this, R.string.no_blank, Toast.
                            LENGTH_SHORT).show();
                else if (!check.isChecked())
                    Toast.makeText(RegisterActivity.this, R.string.no_check,
                            Toast.LENGTH_SHORT).show();
                else {
                    try {
                        myData.insert(email, password, question, answer);
                        myData.close();
                    } catch (Exception e) {
                        Toast.makeText(v.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    toMyAccountActivity();
                }
            }
        });
    }

    private void toMyAccountActivity() {
        startActivity(new Intent(this, MyAccountActivity.class));
    }

    private boolean isEmpty(EditText text) {
        return text.getText().toString().trim().length() == 0;
    }

    /**
     * Background Async Task to Create new product
     * */
    class CreateNewProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Registering New User..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("question", question));
            params.add(new BasicNameValuePair("answer", answer));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_new_user,
                    "POST", params);

            // check log cat from response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created a user
                    Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create user
                    Log.d("failed to create user", json.toString());

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}

