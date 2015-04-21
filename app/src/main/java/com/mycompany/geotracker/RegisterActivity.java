/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/*
 * Created by David on April 2015
 */
public class RegisterActivity extends ActionBarActivity {
    private String email, password, confirmed_password, answer;
    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
                preferenceSettings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                preferenceEditor = preferenceSettings.edit();

                EditText reg_email = (EditText)findViewById(R.id.reg_email);
                email = reg_email.getText().toString();
                EditText reg_pass = (EditText)findViewById(R.id.reg_password);
                password = reg_pass.getText().toString();
                EditText conf_pass = (EditText)findViewById(R.id.conf_password);
                confirmed_password = conf_pass.getText().toString();
                String question = (String) spin_securityQuest.getSelectedItem();
                EditText sec_answer = (EditText)findViewById(R.id.sec_answer);
                answer = sec_answer.getText().toString();
                CheckBox check = (CheckBox) findViewById(R.id.accept_checkBox);

                /*
                Need this to test to see if the password is the same as the confirmed.
                Wasn't sure what you wanted to put here. For now I made it so that it just
                accepts it no matter what.
                 */
//                if(!password.equals(confirmed_password)) {
//                    //passwords do not match
//                } else {
//
//                }

                preferenceEditor.putString("email", email);
                preferenceEditor.putString("password", password);
                preferenceEditor.putString("question", question);
                preferenceEditor.putString("answer", answer);
                preferenceEditor.commit();

                if (!password.equals(confirmed_password)) {
                    Toast.makeText(RegisterActivity.this, R.string.no_match, Toast.
                            LENGTH_SHORT).show();
                } else
                if (isEmpty(reg_email) || isEmpty(reg_pass) || isEmpty(conf_pass) ||
                        isEmpty(sec_answer))
                    Toast.makeText(RegisterActivity.this, R.string.no_blank, Toast.
                            LENGTH_SHORT).show();
                else if (!check.isChecked())
                    Toast.makeText(RegisterActivity.this, R.string.no_check,
                            Toast.LENGTH_SHORT).show();
                    else toMyAccountActivity();
            }
        });
    }

    private void toMyAccountActivity() {
        startActivity(new Intent(this, MyAccountActivity.class));
    }

    private boolean isEmpty(EditText text) {
        return text.getText().toString().trim().length() == 0;
    }

}
