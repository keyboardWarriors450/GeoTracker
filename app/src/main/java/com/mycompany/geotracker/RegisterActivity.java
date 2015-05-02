/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mycompany.geotracker.data.MyData;

/*
 * Created by David on April 2015
 */
public class RegisterActivity extends ActionBarActivity {

    private MyData myData;
    private String email, password, confirmed_password, question, answer;

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
                EditText reg_pass = (EditText) findViewById(R.id.reg_password);
                EditText conf_pass = (EditText) findViewById(R.id.conf_password);
                EditText sec_answer = (EditText) findViewById(R.id.sec_answer);
                CheckBox check = (CheckBox) findViewById(R.id.accept_checkBox);

                email = reg_email.getText().toString();
                password = reg_pass.getText().toString();
                confirmed_password = conf_pass.getText().toString();
                question = (String) spin_securityQuest.getSelectedItem();
                answer = sec_answer.getText().toString();

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
                else if (password.length() < 6)
                    Toast.makeText(RegisterActivity.this, R.string.short_password,
                            Toast.LENGTH_SHORT).show();
                else {
                    try {
                        myData.insert(email, password, question, answer);
                        myData.close();
                    } catch (Exception e) {
                        Toast.makeText(v.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new AddNewUserToServer(RegisterActivity.this).execute(email, password, question, answer);
                }
            }
        });
    }

    private boolean isEmpty(EditText text) {
        return text.getText().toString().trim().length() == 0;
    }


}

